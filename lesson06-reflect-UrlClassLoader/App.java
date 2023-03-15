import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.lang.System.lineSeparator;

public class App {
    static final String javapoetPath = "https://repo1.maven.org/maven2/com/squareup/javapoet/1.13.0/javapoet-1.13.0.jar";

    public static void main(String[] args) throws IOException {
        ClassesFromUrl
            .listClasses(new URL(javapoetPath))
            .forEach(c -> printMembers(System.out, c));
    }

    public static void printMembers(Appendable out, Class<?> clazz) {
        try {
            out.append(clazz.getName());
            out.append(lineSeparator());
            printFields(out, clazz);
            printMethods(out, clazz);
        } catch (IOException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }
    public static void printFields(Appendable out, Class<?> clazz) throws IOException {
        for (Field f : clazz.getDeclaredFields()) {
            out.append(format("   field: %s %s", f.getType(), f.getName()));
            out.append(lineSeparator());
        }
    }
    public static void printMethods(Appendable out, Class<?> clazz) throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        for (Executable m : concat(Executable.class, clazz.getMethods(), clazz.getDeclaredConstructors())) {
            out.append(format("   method: %s(", m.getName()));
            out.append(Arrays
                .stream(m.getParameters())
                .map(p -> p.getType().getSimpleName())
                .collect(Collectors.joining(",")));
            out.append(")");
            if((m instanceof Method) 
                && m.getParameterCount() == 0 
                && ((Method)m).getReturnType() != void.class) {
                    printCallParameterlessMethod(out, clazz, (Method) m);
            }
            out.append(lineSeparator());
        }
    }
    private static void printCallParameterlessMethod(Appendable out, Class<?> clazz, Method m) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
        if(Modifier.isStatic(m.getModifiers())) {
            Object res = m.invoke(null);
            out.append(" => " + res.toString());    
        } else {
            Object obj = clazz.getDeclaredConstructor().newInstance();
            Object res = m.invoke(obj);
            out.append(" => " + res.toString());    
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] concat(Class<T> eClazz, T[] a, T[] b) {
        return Stream
            .concat(Arrays.stream(a), Arrays.stream(b))
            .toArray(size -> (T[]) Array.newInstance(eClazz, size));
    }
}

