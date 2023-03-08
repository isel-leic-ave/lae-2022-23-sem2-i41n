fun main(){
    var p1 = Point(5, 7)
    p1.print()   
    makeStudentAndPrint("Ze Manel") 
} 

fun makeStudentAndPrint(name: String) {
    val s = Student(231, name)
    s.print()
}