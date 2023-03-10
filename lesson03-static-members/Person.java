public final class Person {
    public static final class Companion {

        private Companion() {
        
        }
        public final int count() {
            return Person.studentsCount;
        }
    } 


    public static final Companion Companion = new Companion();
    private final int id;
    private final String name;
    private static int studentsCount;

    public Person(int id, String name) {
        this.id = id;
        this.name = name;
        studentsCount++;
    }

    public final int getId() {
        return this.id;
    }

    public final String getName() {
        return this.name;
    }

}

final class PersonKt {
    public static final void print(Person p) {
            System.out.println("" + p.getId() + " " + p.getName());
    }
}