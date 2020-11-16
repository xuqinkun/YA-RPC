package server;

import common.RpcProtocol;

public class RpcProtocolImpl implements RpcProtocol {
    private static RpcProtocol INSTANCE = new RpcProtocolImpl();

    private RpcProtocolImpl() {}

    public static RpcProtocol getInstance() {
        return INSTANCE;
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

    @Override
    public String upperCase(String s) {
        if (s == null)
            return null;
        return s.toUpperCase();
    }
}
