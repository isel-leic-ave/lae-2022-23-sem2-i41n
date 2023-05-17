package pt.isel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;
import static java.util.Arrays.asList;

public class App {

    public static void main(String[] args) {
        List<Object> serializables = asList("isel", "super", "ola", 7);
    }

    static void testGenerics() {
        // 1. Expressiveness - Type of variable labels express the type of its items
        List<String> labels = asList("isel", "super", "ola");
        // error: incompatible types: int cannot be converted to String
        // labels.add(7); // 2. Type Safety
        String last = labels.get(labels.size()-1);
        out.println(last.length());
    }

    static void lifeBeforeGenerics() {
        // 1. Lack of Expressiveness - Type of variable labels DOES NOT express the type of its items
        List labels = new ArrayList(asList("isel", "super", "ola"));
        labels.add(7); // 2. NO Type Safety
        // .ClassCastException: class java.lang.Integer cannot be cast to class java.lang.String
        String last = (String) labels.get(labels.size()-1);
        out.println(last.length());
    }
}

