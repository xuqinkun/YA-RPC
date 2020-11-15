package rpc.core;

public class Calculator {

    public static <T extends Number> T sum(T x, T y) {
        if (x == null || y == null) {
            return null;
        }

        if (x instanceof Double) {
            return (T) new Double(x.doubleValue() + y.doubleValue());
        } else if (x instanceof Integer) {
            return (T)new Integer(x.intValue() + y.intValue());
        } else {
            throw new IllegalArgumentException("Type " + x.getClass() + " is not supported by this method");
        }
    }


    public static void main(String[] args) {
        System.out.println(sum(1.0, 2.0));
        System.out.println(sum(1, 2));
    }
}
