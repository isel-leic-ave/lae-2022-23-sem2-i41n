package pt.isel

import java.lang.ClassCastException
import kotlin.reflect.KClass

fun main() {
    val l: List<*> = listOf("isel", "super", "ola", 7)
    // val labels: List<String> = l.uselessCast()
    // val labels: List<String> = l.checkedCast(String::class)
    val labels: List<String> = l.checkedCast()
    println(labels[0].length)
    println(labels[1].length)
    println(labels[3].length)
}

/**
 * E and R are erased and there is no check
 * about the items of the receiver list.
 */
private fun <E, R> List<E>.uselessCast(): List<R> {
    return this as List<R>
}

private fun <R : Any> List<*>.checkedCast(klass: KClass<R>): List<R> {
    this.forEach {
        if(it != null)
            if(!klass.java.isAssignableFrom(it::class.java))
                throw ClassCastException("${it::class.simpleName} not compatible with ${klass.simpleName}")
    }
    return this as List<R>
}
private inline fun <reified R : Any> List<*>.checkedCast(): List<R> {
    return this.checkedCast(R::class)
}
