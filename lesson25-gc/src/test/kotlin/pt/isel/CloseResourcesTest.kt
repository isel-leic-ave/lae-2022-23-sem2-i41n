package pt.isel

import java.io.File
import java.io.FileOutputStream
import java.nio.channels.OverlappingFileLockException
import java.nio.file.FileSystemException
import java.nio.file.Files
import kotlin.concurrent.thread
import kotlin.io.path.Path
import kotlin.io.path.deleteIfExists
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

private const val OUT = "out.txt"

class CloseResourcesTest {

    @Test
    fun `test succeed to delete file after finalization run`() {
        /*
         * FileOutputStream keeps a handle to the native resource.
         */
        FileOutputStream(OUT).channel.lock()
        /*
         * We cannot delete corresponding File out.txt until the handle is released!
         */
        thread {
            assertFailsWith<OverlappingFileLockException> {
                FileOutputStream(OUT).channel.lock()
            }
        }.join()
        /*
         * Running GC will put FileOutputStream object in the Finalization queue.
         * Running finalization will release the handle hold by FileOutputStream.
         * ALERT: DO NOT use this in your code!!!!
         */
        // System.gc()
        // System.runFinalization()
        thread {
            FileOutputStream(OUT).channel.lock()
        }.join()
        /*
         * Once the handle is release we can safely delete out.txt file.
         */
        assertTrue(Path(OUT).deleteIfExists())
    }

    @Test
    fun `test succeed to delete file after use block`() {
        /*
         * FileOutputStream keeps a handle to the native resource.
         */
        FileOutputStream(OUT).use {
            it.channel.lock()
        }
        /*
         * We cannot delete corresponding File out.txt until the handle is released!
         */
        FileOutputStream(OUT).channel.lock()
        /*
         * Once the handle is release we can safely delete out.txt file.
         */
        assertTrue(Path(OUT).deleteIfExists())
    }

    @Test
    fun `test succeed to delete file after close on finally`() {
        /*
         * FileOutputStream keeps a handle to the native resource.
         */
        val fo = FileOutputStream(OUT).channel.lock()
        try {
            /*
             * We cannot delete corresponding File out.txt until the handle is released!
             */
            assertFailsWith<OverlappingFileLockException> {
                FileOutputStream(OUT).channel.lock()
            }
        } finally {
            fo.close()
        }
        FileOutputStream(OUT).use {
            it.channel.lock()
            it.write("Ole Ole".toByteArray())
        }
        /*
         * Once the handle is release we can safely delete out.txt file.
         */
        // assertTrue(Path(OUT).deleteIfExists())
    }
}