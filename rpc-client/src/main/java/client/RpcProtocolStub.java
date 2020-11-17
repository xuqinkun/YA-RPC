package client;

import bean.Response;
import bean.Util;
import common.RpcProtocol;
import common.Call;
import server.Server;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static bean.Status.OK;

public class RpcProtocolStub implements RpcProtocol {
    public static final String HOST = "127.0.0.1";

    private Client client;

    public RpcProtocolStub() {
        client = new Client.ClientFactory(HOST, Server.port).build();
        new Thread(client).start();
    }

    @Override
    public Number sum(Number x, Number y) {
        Call call = new Call(getClass().getSimpleName(), "sum");
        call.setCallID(Util.getUUID());
        call.addTypes(Number.class, Number.class);
        call.addParams(x, y);
        client.addCall(call);
        Response response;
        try {
            response = client.call(call.getCallID());
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        if (response.getStatus() != OK) {
            throw new RuntimeException(response.getResult().toString());
        }
        return (Number) response.getResult();
    }

    @Override
    public String upperCase(String s) {
        Call call = new Call(getClass().getSimpleName(), "upperCase");
        call.setCallID(Util.getUUID());
        call.addTypes(String.class);
        call.addParams(s);
        client.addCall(call);
        Response response;
        try {
            response = client.call(call.getCallID());
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        if (response.getStatus() != OK) {
            throw new RuntimeException(response.getResult().toString());
        }
        return (String) response.getResult();
    }
}
