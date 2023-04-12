package pt.isel;


import org.openjdk.jmh.annotations.Benchmark;

public class LoggerBenchmark {
    static final Printer emptyPrinter = new Printer() {
        @Override
        public void print(Object msg) {
            // Do nothing
        }
    };
    static final Student s = new Student(78163, "Ze Manel");
    static final Logger loggerReflect = new Logger(emptyPrinter, MemberKind.FIELD);
    static final LoggerBaselineStudent loggerBaseline = new LoggerBaselineStudent(emptyPrinter);

    @Benchmark
    public void logReflectStudent() {
        loggerReflect.log(s);
    }

    @Benchmark
    public void logBaselineStudent() {
        loggerBaseline.log(s);
    }
}
