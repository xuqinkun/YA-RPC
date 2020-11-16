package client;

import bean.Response;
import common.Calculator;
import common.Call;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;

public class CalculatorStub implements Calculator {
    private ClientProxy proxy;

    private String host;

    private int port;

    public CalculatorStub(String host, int port) {
        this.host = host;
        this.port = port;
        proxy = ClientProxy.getInstance();
    }

    @Override
    public Number sum(Number x, Number y) {
        try {
            Method method = getClass().getMethod("sum", Number.class, Number.class);
            Call call = new Call(this.getClass(), method);
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host, port));
            call.setSocket(socket);
            call.addParams(x, y);
            proxy.addCall(call);
            Response response = new Response();
            response.readExternal(call.getInputStream());
            return (Number) response.getResult();
        } catch (NoSuchMethodException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
