package client;

import bean.Response;
import common.RpcProtocol;
import common.Call;

import static bean.Status.OK;

public class RpcProtocolStub implements RpcProtocol {
    private Client client;

    public RpcProtocolStub(Client client) {
        this.client = client;
    }

    @Override
    public Number sum(Number x, Number y) {
        Call call = new Call(getClass().getSimpleName(), "sum");
        call.addTypes(Number.class, Number.class);
        call.addParams(x, y);
        client.addCall(call);
        Response response = client.call();
        if (response.getStatus() != OK) {
            throw new RuntimeException();
        }
        return (Number) response.getResult();
    }

    @Override
    public String upperCase(String s) {
        Call call = new Call(getClass().getSimpleName(), "upperCase");
        call.addTypes(String.class);
        call.addParams(s);
        client.addCall(call);
        Response response = client.call();
        if (response.getStatus() != OK) {
            throw new RuntimeException();
        }
        return (String) response.getResult();
    }
}
