package pt.isel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Logger {
    private final Printer out;
    private final MemberKind kind;
    private static final Map<Class<?>, List<? extends Getter>> getters = new HashMap<>();

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
        List<? extends Getter> gs = switch (kind) {
            case FIELD -> loadFieldGetters(target);
            case PROPERTY -> loadPropertyGetters(target);
            case METHOD -> throw new UnsupportedOperationException();
        };
        gs.forEach(g -> g.getAndPrint(target));
   }

    private List<? extends Getter> loadFieldGetters(Object target) {
        return getters.computeIfAbsent(target.getClass(), k ->
                Arrays.stream(target.getClass()
                    .getDeclaredFields())
                    .filter(f -> !f.isAnnotationPresent(NonLog.class))
                    .map(f -> new GetterField(out, f))
                    .collect(toList()));
    }
    private List<? extends Getter> loadPropertyGetters(Object target) {
        return getters.computeIfAbsent(target.getClass(), k ->
                Arrays.stream(target.getClass()
                    .getDeclaredMethods())
                    .filter(m -> m.getParameterCount() == 0
                            && m.getReturnType() != void.class
                            && m.getName().startsWith("get")
                            && !m.isAnnotationPresent(NonLog.class)
                    )
                    .map(m -> new GetterProperty(out, m))
                    .collect(toList()));
    }

}
