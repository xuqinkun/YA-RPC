package client;

import bean.Response;
import bean.Util;
import common.RpcProtocol;
import common.Call;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.Server;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static bean.Status.OK;

public class RpcProtocolStub implements RpcProtocol {
    private static Logger log = LoggerFactory.getLogger(RpcProtocolStub.class);

    public static final String HOST = "127.0.0.1";

    private Client client;

    public RpcProtocolStub() {
        client = new Client.ClientFactory(HOST, Server.port).build();
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
            log.error(e.getMessage());
            return null;
        }
        if (response.getStatus() != OK) {
            log.error("Status: {}, Error:{}", response.getStatus(), response.getResult());
            return null;
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
            log.error(e.getMessage());
            return null;
        }
        if (response.getStatus() != OK) {
            log.error("Status: {} Error:{}", response.getStatus(), response.getResult());
        }
        return (String) response.getResult();
    }
}
