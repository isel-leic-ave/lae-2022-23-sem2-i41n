public class App {
    public static void main(String[] args) {
        int n = 5;
        String s = "Ola";
        Object o = s; // No checkcast => aload_2; astore_3;
        Object nr = n; // boxing => Integer.valueOf(int): Integer
        Integer a = n; // boxing => Integer.valueOf(int): Integer
        Integer b = 57;
        Integer res = a + b;
    }
    public static void foo(Object o) {
        // String msg = o; // incompatible types: Object cannot be converted to String
        String msg = (String) o; // aload_0; checkcast; astore_1;
    }
}