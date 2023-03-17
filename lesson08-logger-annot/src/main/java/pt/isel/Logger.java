package pt.isel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Logger {
    private final Printer out;
    private final MemberKind kind;

    public Logger() {
        this(PrinterConsole.INSTANCE, MemberKind.FIELD);
    }

    public Logger(Printer out, MemberKind kind) {
        this.out = out;
        this.kind = kind;
    }

    public void log(Object target) {
        out.print(target.getClass().getSimpleName());
        out.print(": ");
        try {
            switch (kind) {
                case FIELD -> logFields(target);
                case PROPERTY -> logProperties(target);
            }

        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void logFields(Object target) throws IllegalAccessException {
        for(Field f : target.getClass().getDeclaredFields()) {
            // Enables the use of inaccessible fields
            // The metadata is unchanged.
            f.setAccessible(true);
            out.print(f.getName());
            out.print(" = ");
            out.print(f.get(target));
            out.print(", ");
        }
    }
    private void logProperties(Object target) throws IllegalAccessException, InvocationTargetException {
        for(Method m : target.getClass().getDeclaredMethods()) {
            if(m.getParameterCount() == 0
                && m.getReturnType() != void.class
                && m.getName().startsWith("get")) {
                // Enables the use of inaccessible methods
                // The metadata is unchanged.
                m.setAccessible(true);
                out.print(m.getName().substring(3));
                out.print(" = ");
                out.print(m.invoke(target));
                out.print(", ");

            }
        }
    }


}
