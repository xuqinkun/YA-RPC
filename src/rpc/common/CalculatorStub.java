package rpc.common;

public class CalculatorStub implements Calculator {

    Calculator calculator;

    public CalculatorStub() {
        calculator = CalculatorImpl.getInstance();
    }

    @Override
    public Number sum(Number x, Number y) {
        return calculator.sum(x, y);
    }
}
