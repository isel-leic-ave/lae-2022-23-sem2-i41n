public class Student {
    final int nr;
    final String name;

    private static int studentsCount;
    
    public static int count() {
        // Cannot use this in static context !!!!
        // System.out.println(this.name);
        return Student.studentsCount;
    }

    public Student(int nr, String name) {
        this.nr = nr;
        this.name = name;
        studentsCount++;
    }
}