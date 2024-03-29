/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package pt.isel

import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class LoggerTest {
    @Test fun `log fields of Student`() {
        val out = PrinterBuffer();
        val logger = Logger(out, MemberKind.FIELD)
        val s = Student(131324, "Ze Manel")
        logger.log(s)
        assertEquals("Student: nr = 131324, name = Ze Manel, ", out.buffer())
    }
    @Test fun `log properties of Point`() {
        val out = PrinterBuffer();
        val logger = Logger(out, MemberKind.PROPERTY)
        val p = Point(5, 7)
        logger.log(p)
        assertEquals("Point: MODULE = 8.602325267042627, ", out.buffer())
    }
    @Test fun `log fields of Person with Formatter`() {
        val out = PrinterBuffer();
        val logger = Logger(out, MemberKind.FIELD)
        val p = Person("Maria Papoila", "Rua Rosa", LocalDate.of(1997, 3, 27))
        logger.log(p)
        assertEquals("Person: name = Maria Papoila, address = RUA ROSA, birth = 1997-W13-4, ", out.buffer())
    }

}
