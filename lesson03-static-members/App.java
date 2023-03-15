public class App {
    public static void main(String[] args) {
        var s1 = new Student(1, "Ze Manel");
        var s2 = new Student(2, "Ana Maria");
        var s3 = new Student(7, "Ze Gato");
        System.out.println(Student.count());

        var p1 = new Person(11, "Maria Rosa");
        var p2 = new Person(80, "Manel");
        // TPC: How to call count() in Person class from Java ???
        // System.out.println(Person.count());
        System.out.println(Person.Companion.count());
        
        

    }
    public static int test(Student s) {
        return s.getName().hashCode() + s.getNr() + Student.count();
    }
}
