package server;

import common.Calculator;

public class CalculatorImpl implements Calculator {
    private static Calculator INSTANCE;

    public static Calculator getInstance() {
        return new CalculatorImpl();
    }

    @Override
    public Number sum(Number x, Number y) {
        if (x == null || y == null) {
            return null;
        }
        if (x instanceof Double) {
            return x.doubleValue() + y.doubleValue();
        } else if (x instanceof Integer) {
            return x.intValue() + y.intValue();
        } else if (x instanceof Float) {
            return x.floatValue() + y.floatValue();
        } else {
            throw new IllegalArgumentException("Type " + x.getClass() + " is not supported by this method");
        }
    }
}
