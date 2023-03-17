package pt.isel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GetterProperty implements Getter{
    private final Printer out;
    private final Method m;
    private final String label;

    public GetterProperty(Printer out, Method m) {
        this.out = out;
        this.m = m;
        final var a = m.getAnnotation(AltName.class);
        this.label = a != null ? a.label() : m.getName().substring(3);
        // Enables the use of inaccessible fields
        // The metadata is unchanged.
        m.setAccessible(true);
    }


    @Override
    public void getAndPrint(Object target) {
        try {
            out.print(label);
            out.print(" = ");
            out.print(m.invoke(target));
            out.print(", ");
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
