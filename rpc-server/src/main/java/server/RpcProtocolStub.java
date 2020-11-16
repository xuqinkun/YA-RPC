package server;

import common.RpcProtocol;

public class RpcProtocolStub implements RpcProtocol {

    public RpcProtocolStub() {
    }

    @Override
    public Number sum(Number x, Number y) {
        return RpcProtocolImpl.getInstance().sum(x, y);
    }

    @Override
    public String upperCase(String s) {
        return RpcProtocolImpl.getInstance().upperCase(s);
    }
}
