package pt.isel;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;

public abstract class AbstractGetter implements Getter{
    private final Formatter formatter;
    protected final Printer out;

    public AbstractGetter(Printer out, AnnotatedElement member) {
        this.out = out;
        //
        // Check for Format annotation
        //
        final var anot = member.getAnnotation(Format.class);
        try {
            formatter = anot == null
                    ? null
                    : anot.formatter().getConstructor().newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    protected void format(Object v) {
        if(formatter != null) {
            v = formatter.format(v);
        }
        out.print(v);
    }

}
