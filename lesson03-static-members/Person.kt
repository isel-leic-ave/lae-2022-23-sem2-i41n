class Person(val id: Int, val name: String) {
    companion object {
        private var studentsCount: Int = 0
        fun count() : Int {
            return studentsCount
        }
    }
    init {
        Person.studentsCount++
    }
}

fun Person.print() {
    println("$id $name");
}