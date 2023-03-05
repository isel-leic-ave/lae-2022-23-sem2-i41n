fun main() {
    val s = Student(87364, "Ze Manel")
    s.name = "Ze Gato" // invokevirtual Student::setName(String)
    val n = s.name     // invokevirtual Student::getName()
    println(n)          
    s.print()          // invokevirtual Student::println()
}