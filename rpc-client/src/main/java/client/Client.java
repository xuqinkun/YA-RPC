package client;

import bean.Response;
import common.Call;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static bean.Status.ERROR;

public class Client {
    private BlockingQueue<Call> callQueue;

    private String host;

    private int port;

    public Client(String host, int port) {
        this.host =host;
        this.port = port;
        callQueue = new LinkedBlockingDeque<>();
    }

    public void addCall(Call call) {
        callQueue.offer(call);
    }

    public Response call() {
        Call call;
        Response response = new Response();
        try {
            call = callQueue.take();
            response.setCallID(call.getCallID());
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host, port));
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            call.writeExternal(oos);
            response.readExternal(new ObjectInputStream(socket.getInputStream()));
        } catch (InterruptedException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
            response.setStatus(ERROR);
            response.setResult(e.getMessage());
        }
        return response;
    }
}
