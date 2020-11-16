package client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class CalculatorHandler implements InvocationHandler {
    private Object objOriginal;

    public CalculatorHandler(Object objOriginal) {
        this.objOriginal = objOriginal;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(this.objOriginal, args);
    }
}
