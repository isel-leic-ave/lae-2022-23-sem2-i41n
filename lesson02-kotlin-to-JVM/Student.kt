class Student(val nr: Int, var name: String?) {
    val nameLength get() = name?.length ?: 0
    fun print() {
        println("$nr $name")
    }
}

class Point(val x: Int, val y: Int)

fun foo() {}