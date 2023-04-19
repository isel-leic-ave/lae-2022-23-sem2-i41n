package pt.isel;

import org.cojen.maker.ClassMaker;
import org.cojen.maker.MethodMaker;
import org.cojen.maker.Variable;
import pt.isel.AbstractLogger;
import pt.isel.MemberKind;
import pt.isel.Printer;
import pt.isel.PrinterConsole;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class LoggerDynamic {
    public static AbstractLogger logger(Class<?> domain) {
        return logger(MemberKind.PROPERTY, PrinterConsole.INSTANCE, domain);
    }
    public static AbstractLogger logger(MemberKind kind, Printer out, Class<?> domain) {
        final var loggerMaker = switch (kind) {
            case PROPERTY -> buildLoggerDynamicForProperties(domain);
            case FIELD, METHOD -> throw new UnsupportedOperationException();
        };
        try {
            return (AbstractLogger) loggerMaker
                    .finish()
                    .getConstructor(Printer.class)
                    .newInstance(out);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * public class LoggerBaselineStudent extends AbstractLogger {
     *
     *     protected LoggerBaselineStudent(Printer out) {
     *         super(out);
     *     }
     *
     *     public void log(Object target) {
     *         Student st = (Student) target;
     *         out.print("Student: ");
     *         // Prop nr
     *         out.print("nr = ");
     *         out.print(st.getNr());
     *         out.print(", ");
     *         // Prop name
     *         out.print("name = ");
     *         out.print(st.getName());
     *         out.print(", ");
     *     }
     * }
     */
    public static ClassMaker buildLoggerDynamicForProperties(Class<?> domain) {
        ClassMaker maker = ClassMaker
                .beginExternal("pt.isel.LoggerDynamic" + domain.getSimpleName())
                .extend(AbstractLogger.class)
                .public_();
        /**
         * Constructor
         */
        MethodMaker ctor = maker.addConstructor(Printer.class).public_();
        ctor.invokeSuperConstructor(ctor.param(0));
        /**
         * Method log(Object target)
         */
        MethodMaker logMaker = maker.addMethod(void.class, "log", Object.class).public_().override();
        Variable st = logMaker
                .param(0)
                .cast(domain); // Student st = (Student) target;
        logMaker
                .field("out")  // out.print("Student: ");
                .invoke(
                        "print",
                        domain.getSimpleName() + ": ");
        /**
         * For each property generate:
         *         out.print("nr = ");
         *         int propValue = st.getNr();
         *         out.print(propValue);
         *         out.print(", ");
         */
        Arrays
                .stream(domain.getDeclaredMethods())
                .filter(m -> m.getParameterCount() == 0 && m.getName().startsWith("get"))
                .forEach(m -> {
                    String propName = m.getName().substring(3).toLowerCase();
                    logMaker.field("out").invoke(
                                    "print",
                                    propName + " = ");
                    Variable propValue = st.invoke(m.getName()); // int propValue = st.getNr();
                    logMaker.field("out").invoke(
                            "print",
                            propValue);
                    logMaker.field("out").invoke(
                            "print",
                            ", ");
                });
        /**
         * End
         */
        return maker;
    }
}
