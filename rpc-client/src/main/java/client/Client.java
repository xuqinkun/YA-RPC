package client;

import bean.Response;
import common.Call;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

import static bean.Status.ERROR;

public class Client implements Runnable {

    private static Client INSTANCE;

    private String host;

    private int port;

    private boolean stopped;

    private BlockingQueue<FutureTask<Response>> taskQueue;

    private Map<String, Future<Response>> resMap;

    private static final ExecutorService executor = Executors.newCachedThreadPool();

    private Client(String host, int port) {
        this.host = host;
        this.port = port;
        stopped = false;
        taskQueue = new LinkedBlockingDeque<>();
        resMap = new HashMap<>();
    }

    public void addCall(Call call) {
        CallTask task = new CallTask(call);
        FutureTask<Response> future = new FutureTask<>(task);
        taskQueue.offer(future);
        resMap.put(call.getCallID(), future);
    }

    @Override
    public void run() {
        while (!stopped) {
            try {
                FutureTask<Response> future;
                if ((future = taskQueue.poll(10, TimeUnit.MILLISECONDS)) != null) {
                    executor.submit(future);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Response call(String callID) throws InterruptedException, ExecutionException, TimeoutException {
        if (!resMap.containsKey(callID)) {
            return null;
        }
        return resMap.get(callID).get(5, TimeUnit.SECONDS);
    }

    public void stop() {
        executor.shutdown();
        stopped = true;
    }

    static class ClientFactory {

        public ClientFactory(String host, int port) {
            if (INSTANCE == null) {
                INSTANCE = new Client(host, port);
            }
        }

        public Client build() {
            return INSTANCE;
        }
    }

    class CallTask implements Callable<Response> {
        private Call call;

        public CallTask(Call call) {
            this.call = call;
        }

        @Override
        public Response call() {
            Response response = new Response();
            try {
                response.setCallID(call.getCallID());
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(host, port));
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                call.writeExternal(oos);
                response.readExternal(new ObjectInputStream(socket.getInputStream()));
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                response.setStatus(ERROR);
                response.setResult(e.getMessage());
            }
            return response;
        }
    }
}
