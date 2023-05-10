package pt.isel

import convert
import org.junit.jupiter.api.Test
import where
import java.io.File
import kotlin.test.assertEquals

class QueriesTest {
    private val lae2022uri = ClassLoader
        .getSystemClassLoader()
        .getResource("lae2022.txt")
        .toURI()

    @Test
    fun `Load and print students from file`() {
        File(lae2022uri)
            .readLines()
            .map { it.toStudent() }
            .forEach { println(it) }
    }

    @Test
    fun `Collection extensions test map filter first`() {
        val name = File(lae2022uri)
            .readLines()
            .map { println("map 1 $it"); it.toStudent() }
            .filter {  println("filter 2 $it");it.nr > 47000 }
            .map {  println("map 3 $it");it.name.split(" ").last() }
            .filter {  println("filter 4$it");it.startsWith("A") }
            .first()
        assertEquals("Almeida", name)
    }

    @Test
    fun `Collection count iterations`() {
        var count = 0
        File(lae2022uri)
            .readLines()
            .map { count++; it.toStudent()}
            .filter { count++; it.nr > 47000 }
            .map { count++; it.name.split(" ").last() }
            .filter { count++; it.startsWith("A") }
        assertEquals(356, count)
    }

    @Test
    fun `Eager Queries count iterations`() {
        var count = 0
        val name = File(lae2022uri)
            .readLines()
            .convert { count++; it.toStudent() }
            .where { count++; it.nr > 47000 }
            .convert { count++; it.name.split(" ").last() }
            .where { count++; it.startsWith("A") }
            .iterator().next()
        assertEquals(356, count)
        assertEquals("Almeida", name)
    }

    @Test
    fun `Lazy Queries count iterations`() {
        var count = 0
        val name = File(lae2022uri)
            .readLines()
            .asSequence()
            .convert { count++; it.toStudent() }
            .where { count++; it.nr > 47000 }
            .convert { count++; it.name.split(" ").last() }
            .where { count++; it.startsWith("A") }
            .iterator().next()
        // assertEquals(146, count)
        assertEquals(22, count)
        assertEquals("Almeida", name)
    }

    @Test
    fun `Sequence extensions test map filter first`() {
        var count = 0
        val name = File(lae2022uri)
            .readLines()
            .asSequence()
            .map { count++; it.toStudent()}
            .filter { count++; it.nr > 47000 }
            .map { count++; it.name.split(" ").last() }
            .filter { count++; it.startsWith("A") }
            .first()
        assertEquals(22, count)
        assertEquals("Almeida", name)
    }

    @Test
    fun `Sequence count iterations`() {
        var count = 0
        File(lae2022uri)
            .readLines()
            .asSequence()
            .map { count++; it.toStudent()}
            .filter { count++; it.nr > 47000 }
            .map { count++; it.name.split(" ").last() }
            .filter { count++; it.startsWith("A") }
        assertEquals(0, count)
    }

}