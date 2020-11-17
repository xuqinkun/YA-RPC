package server;

import bean.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {
    private static Logger log = LoggerFactory.getLogger(Server.class);

    private String host;

    private int port;

    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public Server(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            ServerSocket server = new ServerSocket();
            server.bind(new InetSocketAddress(host, port));
            log.debug("Server bind to {}:{} success", host, port);
            Socket socket;
            while ((socket = server.accept()) != null) {
                log.debug("Accept {}", socket.getRemoteSocketAddress());
                Worker worker = new Worker(socket);
                executor.submit(worker);
            }
        } catch (IOException e) {
            log.error("{}", e.getMessage());
        }
    }

    public static void main(String[] args) {
        Server server = new Server(Util.getServerBindHost(), Util.getServerBindPort());
        Thread t = new Thread(server);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
