package rpc.client;

import rpc.common.Call;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class ClientProxy implements Runnable {
    private boolean stopped;

    private BlockingQueue<Call> callQueue;

    private String host;

    private int port;

    private static ClientProxy INSTANCE = new ClientProxy();

    public ClientProxy() {
        callQueue = new LinkedBlockingDeque<>();
        stopped = false;
        new Thread(this).start();
    }

    public static ClientProxy getInstance() {
        return INSTANCE;
    }

    @Override
    public void run() {
        try {
            Call call;
            while (!stopped) {
                call = callQueue.take();
                call.request();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addCall(Call call) {
        callQueue.offer(call);
    }
}
