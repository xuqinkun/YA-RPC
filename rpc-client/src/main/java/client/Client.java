package client;

import bean.Response;
import common.Call;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

import static bean.Status.ERROR;

public class Client {
    private static Logger log = LoggerFactory.getLogger(Client.class);

    private static final int RETRY_TIMES = 3;

    private static Client INSTANCE;

    private String host;

    private int port;

    private boolean stopped;

    private Map<String, FutureTask<Response>> resMap;

    private Client(String host, int port) {
        this.host = host;
        this.port = port;
        stopped = false;
        resMap = new HashMap<>();
    }

    public void addCall(Call call) {
        CallTask task = new CallTask(call);
        FutureTask<Response> future = new FutureTask<>(task);
        resMap.put(call.getCallID(), future);
    }

    public Response call(String callID) throws InterruptedException, ExecutionException, TimeoutException {
        if (!resMap.containsKey(callID)) {
            return null;
        }
        FutureTask<Response> future = resMap.get(callID);
        new Thread(future).start();
        resMap.remove(callID);
        return future.get();
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
        private Socket socket;

        public CallTask(Call call) {
            this.call = call;
            socket = new Socket();
        }

        @Override
        public Response call() {
            Response response = new Response();
            response.setCallID(call.getCallID());
            int retry = 1;
            try {
                doRequest(response);
            } catch (IOException | ClassNotFoundException e) {
                while (retry <= RETRY_TIMES) {
                    log.error("Request failed. Retry {}", retry++);
                    try {
                        doRequest(response);
                        break;
                    } catch (IOException | ClassNotFoundException e2) {
                        log.error(e2.getMessage());
                        response.setStatus(ERROR);
                        response.setResult(e.getMessage());
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                    }
                }
            }
            if (socket != null && socket.isConnected()) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return response;
        }

        private void doRequest(Response response) throws IOException, ClassNotFoundException {
            socket.connect(new InetSocketAddress(host, port));
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            call.writeExternal(oos);
            response.readExternal(new ObjectInputStream(socket.getInputStream()));
        }
    }
}
