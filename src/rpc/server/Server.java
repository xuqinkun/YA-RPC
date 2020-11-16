package rpc.server;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import rpc.bean.Message;
import rpc.bean.OperationData;
import rpc.bean.OperationType;
import rpc.bean.Response;
import rpc.common.CalculatorImpl;
import rpc.common.Call;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import static rpc.bean.OperationType.SUM;

public class Server {
    private static Logger log = LoggerFactory.getLogger(Server.class);
    public static final int port = 1111;

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress("0.0.0.0", port));
        Socket socket;
        while ((socket = server.accept()) != null) {
            log.debug("Accept {}", socket.getRemoteSocketAddress());
            Call call = new Call();
            call.setSocket(socket);
            Response response = call.getResponse();
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            response.writeExternal(oos);
            oos.flush();
        }
    }
}
