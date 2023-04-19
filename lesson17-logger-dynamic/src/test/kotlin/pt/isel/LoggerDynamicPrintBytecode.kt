package pt.isel

import org.junit.jupiter.api.Test
import org.objectweb.asm.ClassReader
import org.objectweb.asm.util.TraceClassVisitor
import java.io.PrintWriter

class LoggerDynamicPrintBytecode {
    fun printBytecodes(bytes: ByteArray) {
        val reader = ClassReader(bytes)
        val tcv = TraceClassVisitor(PrintWriter(System.out))
        reader.accept(tcv, 0)
    }

    @Test fun `Print bytecode for LoggerDynamicStudent for Properties`() {
        val maker = LoggerDynamic.buildLoggerDynamicForProperties(Student::class.java)
        printBytecodes(maker.finishBytes())
    }
}