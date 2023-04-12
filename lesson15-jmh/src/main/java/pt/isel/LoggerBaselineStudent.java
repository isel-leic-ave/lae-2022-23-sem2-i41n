package pt.isel;

public class LoggerBaselineStudent {
    private final Printer printer;

    public LoggerBaselineStudent(Printer printer) {
        this.printer = printer;
    }
    public void log(Object target) {
        Student st = (Student) target;
        printer.print("Student: nr = ");
        printer.print(st.getNr());
        printer.print(", name = ");
        printer.print(st.getName());
        printer.print(", ");
    }
}
