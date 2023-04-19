package pt.isel;

public class LoggerBaselineStudent extends AbstractLogger {

    protected LoggerBaselineStudent(Printer out) {
        super(out);
    }

    public void log(Object target) {
        Student st = (Student) target;
        out.print("Student: ");
        // Prop nr
        out.print("nr = ");
        out.print(st.getNr());
        out.print(", ");
        // Prop name
        out.print("name = ");
        out.print(st.getName());
        out.print(", ");
    }
}
