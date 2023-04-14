package pt.isel;

import org.cojen.maker.ClassMaker;
import org.cojen.maker.FieldMaker;
import org.cojen.maker.MethodMaker;
import org.cojen.maker.Variable;

import java.io.FileOutputStream;

public class App {
    public static void main(String[] args) throws Exception {
        ClassMaker classMaker = buildDynamicType();
        classMaker.finishTo(new FileOutputStream("DynamicType.class"));
        /*
        Class<?> dynamicTypeClass = classMaker.finish();
        dynamicTypeClass
                .getMethod("run")
                .invoke(null);
         */

        ClassMaker complexMaker = buildComplexType();
        Class<?> complextType = complexMaker.finish();
        Object target = complextType.getConstructor(int.class).newInstance(9);
        /**
         * Via reflect do the equivalent to: target.mul(3);
         * TPC:
         * Replace the reflect invocation by interface call.
         * To that end the ComplextType must implement an interface with a mul(int) method.
         * Then you may cast target to that interface and call mul.
         */
        var res = complextType.getMethod("mul", int.class).invoke(target, 3);
        System.out.println(res);
    }

    /**
     * Generate a dynamic type equivalent to:
     * <p>
     * class DynamicType {
     *   public static void run() {
     *     System.out.println("hello, world");
     *   }
     * }
     *
     * @return
     */
    public static ClassMaker buildDynamicType() {
        ClassMaker cm = ClassMaker.begin().public_();

        // public static void run()...
        cm
            .addMethod(null, "run").public_().static_()
            .var(System.class)
            .field("out")
            .invoke("println", "hello, world");

        return cm;
    }

    /**
     * public final class MyDynamic {
     *  final int nr;
     *  public MyDynamic(int nr) {
     *    this.nr = nr;
     *  }
     *  public int mul(int other) {
     *    return this.nr * other;
     *  }
     * }
     */
    public static ClassMaker buildComplexType() {
        ClassMaker classMaker = ClassMaker.begin().public_().final_();
        /**
         * Field nr
         */
        FieldMaker nrMaker = classMaker.addField(int.class, "nr").final_().private_();
        /**
         * Constructor
         */
        MethodMaker ctorMaker = classMaker.addConstructor(int.class).public_();
        ctorMaker.invokeSuperConstructor();
        ctorMaker
                .field(nrMaker.name()).set(ctorMaker.param(0));
        /**
         * Method run
         */
        MethodMaker mulMaker = classMaker
                .addMethod(int.class, "mul", int.class)
                .public_().final_();
        Variable res = mulMaker.field(nrMaker.name()).mul(mulMaker.param(0));
        mulMaker.return_(res);

        return classMaker;
    }
}


