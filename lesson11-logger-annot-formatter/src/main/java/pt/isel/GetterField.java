package pt.isel;

import java.lang.reflect.Field;

public class GetterField extends AbstractGetter {
    private final Field f;
    private final String label;

    public GetterField(Printer out, Field f) {
        super(out, f);
        this.f = f;
        final var a = f.getAnnotation(AltName.class);
        this.label = a != null ? a.label() : f.getName();
        // Enables the use of inaccessible fields
        // The metadata is unchanged.
        f.setAccessible(true);
    }


    @Override
    public void getAndPrint(Object target) {
        try {
            out.print(label);
            out.print(" = ");
            format(f.get(target));
            out.print(", ");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
