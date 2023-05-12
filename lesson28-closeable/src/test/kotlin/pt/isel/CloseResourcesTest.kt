package pt.isel

import org.junit.jupiter.api.Test
import java.io.Closeable
import java.io.File
import java.io.FileOutputStream
import java.io.Writer
import java.lang.ref.Cleaner
import java.nio.channels.OverlappingFileLockException
import java.nio.file.Files
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

private const val OUT = "out.txt"

class CloseResourcesTest {

    @Test
    fun `After closing OutputStream lock is released` (){
        var fout: FileOutputStream? = null
        try {
            fout = FileOutputStream(OUT).also { it.channel.lock() }
            /**
             * While first lock is open we cannot acquire another lock
             * on same file.
             */
            assertFailsWith<OverlappingFileLockException> {
                FileOutputStream(OUT).channel.lock()
            }
        } finally {
            fout?.close()
        }
        /**
         * After closing fout the lock is released and we can acquire a new lock
         */
        FileOutputStream(OUT).channel.lock()
    }
    @Test
    fun `After using OutputStream lock is released` (){
        FileOutputStream(OUT).use {
            it.channel.lock()
            /**
             * While first lock is open we cannot acquire another lock
             * on same file.
             */
            assertFailsWith<OverlappingFileLockException> {
                FileOutputStream(OUT).channel.lock()
            }
        }
        /**
         * After closing fout the lock is released and we can acquire a new lock
         */
        FileOutputStream(OUT).channel.lock()
    }

    @Test
    fun `After GC run and clean OutputStream lock` (){
        FileOutputStream(OUT).also { it.channel.lock() }
        /**
         * While first lock is open we cannot acquire another lock
         * on same file.
         */
        assertFailsWith<OverlappingFileLockException> {
            FileOutputStream(OUT).channel.lock()
        }
        /**
         * We forgot to call fout.close() and maybe the GC
         * and the Cleaner process may release the lock.
         */
        tryGc()
        /**
         * Even after GC ran and clean fout the lock stays acquired.
         */
        assertFailsWith<OverlappingFileLockException> {
            FileOutputStream(OUT).channel.lock()
        }
    }

    @Test fun `Using Writer inside use will autoflush`() {
        FileOutputStream(OUT)
            .also { fout ->
                fout.channel.lock()
                fout.writer().use { writer ->
                    writer.write("ola isel")
                }
            }
        assertEquals("ola isel", File(OUT).readText())

        FileOutputStream(OUT)
            .also { it.channel.lock() }
    }

    @Test fun `Using Writer and forget close()`() {
        FileOutputStream(OUT)
            .also { it.channel.lock() }
            .writer()
            .write("ola isel")
        /**
         * Writer has not been either closed or flushed
         */
        assertEquals("", File(OUT).readText())
    }

    @Test fun `Using ExclusiveWriter inside use will autoflush`() {
        ExclusiveWriter(OUT).use {
            it.write("ola isel")
        }

        assertEquals("ola isel", File(OUT).readText())

        FileOutputStream(OUT)
            .also { it.channel.lock() }
    }

    fun writeAndForget() {
        ExclusiveWriter(OUT).also {
            it.write("ola isel")
        }
    }

    @Test fun `Using ExclusiveWriter and forget close and use`() {
        writeAndForget()
        tryGc()
        assertEquals("ola isel", File(OUT).readText())
        FileOutputStream(OUT)
            .also { it.channel.lock() }
    }
}

class ExclusiveWriter(path: String) : Closeable {
    companion object {
        val cleaner = Cleaner.create()
    }

    private val writer: Writer = FileOutputStream(path)
        .also { it.channel.lock() }
        .writer()

    init {
        cleaner.register(this, ExclusiveWriterCleaner(writer))
    }

    fun write(text: String) = writer.write(text)

    override fun close() {
        writer.close()
    }
}

class ExclusiveWriterCleaner(val writer: Writer) : Runnable {
    override fun run() {
        println("Running Cleaner...")
        writer.close()
    }
}

/**
 * The garbage collector runs in parallel, and it may take
 * some time after invoking System.gc() before the garbage
 * collector determines the object is no longer referenced.
 *
 * ALERT: DO NOT use this in your code!!!!
 */
private fun tryGc() {
    for (i in 10 downTo 1) {
        System.gc()
        Thread.sleep(10)
    }
}