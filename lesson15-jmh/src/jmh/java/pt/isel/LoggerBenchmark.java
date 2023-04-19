package pt.isel;


import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class LoggerBenchmark {
    Printer emptyPrinter;
    static final Student s = new Student(78163, "Ze Manel");
    AbstractLogger logger;
    @Param({"baseline", "reflect", "dynamic"}) String approach;

    @Setup
    public void setup(Blackhole bh) {
        emptyPrinter = new Printer() {
            @Override
            public void print(Object msg) {
                // Do nothing
                bh.consume(msg);
            }
        };
        logger = switch(approach) {
            case "reflect" -> new Logger(emptyPrinter, MemberKind.FIELD);
            case "baseline" -> new LoggerBaselineStudent(emptyPrinter);
            case "dynamic" -> LoggerDynamic.logger(
                    MemberKind.PROPERTY,
                    emptyPrinter,
                    Student.class);
            default -> throw new UnsupportedOperationException();
        };

    }

    @Benchmark
    public void logStudent() {
        logger.log(s);
    }

}
