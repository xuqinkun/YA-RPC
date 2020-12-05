package server;

import bean.Util;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
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

    private boolean stop;

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
            while (!stop && (socket = server.accept()) != null) {
                log.debug("Accept {}", socket.getRemoteSocketAddress());
                Worker worker = new Worker(socket);
                executor.submit(worker);
            }
        } catch (IOException e) {
            log.error("{}", e.getMessage());
        }
    }

    public void stop() {
        stop = true;
        Thread.currentThread().interrupt();
    }

    public static void main(String[] args) {
        CommandLine cmd = Util.getCommandLine(args);

        String h = cmd.getOptionValue("h");
        String p = cmd.getOptionValue("p");

        String host = h != null ? h : Util.getServerBindHost();
        int port = p != null ? Integer.parseInt(p) : Util.getServerBindPort();

        Server server = new Server(host, port);
        Thread t = new Thread(server);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
