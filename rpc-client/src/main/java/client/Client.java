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
    private boolean stopped;

    private BlockingQueue<Call> callQueue;

    private String host;

    private int port;

    public Client(String host, int port) {
        this.host =host;
        this.port = port;
        callQueue = new LinkedBlockingDeque<>();
        stopped = false;
    }

    public void addCall(Call call) {
        callQueue.offer(call);
    }

    public Response call() {
        Call call;
        try {
            call = callQueue.take();
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host, port));
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            call.writeExternal(oos);
            Response response = new Response();
            response.readExternal(new ObjectInputStream(socket.getInputStream()));
            return response;
        } catch (InterruptedException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new Response(ERROR, "", e.getMessage());
        }
    }
}
