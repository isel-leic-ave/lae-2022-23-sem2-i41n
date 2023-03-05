final class Student {
    private final int nr; // <=> val
    private String name;  // <=> var
    public Student(int nr, String name) {
        this.nr = nr;
        this.name = name;
    }
    public final int getNr() {
        return nr;
    }
    public final String getName() {
        return name;
    }
    public final void setName(String name) {
        this.name = name;
    }
    public final int getNameLength() {
        return name.length();
    }
    public final void print() {
        System.out.println(
            String.format("%d %s",
            nr,
            name)
        );
    }
}

/*
class MasterStudent extends Student {
    public MasterStudent(int nr, String name) {
        super(nr, name);
    }
}
*/