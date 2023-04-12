/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package pt.isel

import kotlin.system.measureNanoTime

fun main() {
    val emptyPrinter = object : Printer {
        override fun print(msg: Any?) {
            /* Do nothing */
        }
    }
    val logger = Logger(emptyPrinter, MemberKind.FIELD)
    val baseline = LoggerBaselineStudent(emptyPrinter)
    val s = Student(91237, "Ze Manel")
    for(i in 1..10)
        measureNanoTime {
            logger.log(s)
        }.let {
            println()
            println("Logger reflect log() took $it ns")
        }
    for(i in 1..10)
        measureNanoTime {
            baseline.log(s)
        }.let {
            println()
            println("Baseline NO reflect log() took $it ns")
        }
}
