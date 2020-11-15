package rpc.core;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import rpc.bean.Message;
import rpc.bean.OperationData;
import rpc.bean.OperationType;
import rpc.bean.Response;

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

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        int port = 1111;

        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress("0.0.0.0", port));
        Socket socket;
        while ((socket = server.accept()) != null) {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            log.debug("Accept {}", socket.getRemoteSocketAddress());
            Message msg = new Message();
            msg.readExternal(ois);
            Response response = parseMessage(msg);
            response.setMessageID(msg.getMessageID());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            response.writeExternal(oos);
            oos.flush();
        }
    }

    private static Response parseMessage(Message msg) throws ClassNotFoundException {
        OperationType opt = msg.getOpt();
        List<OperationData> params = msg.getParams();
        Response response = new Response();
        StringBuilder builder = new StringBuilder();
        builder.append(opt);
        builder.append("(");
        if (opt == SUM) {
            if (params.size() != 2) {
                return response;
            }
            OperationData opt1 = params.get(0);
            OperationData opt2 = params.get(1);
            builder.append(opt1.getData());
            builder.append(", ");
            builder.append(opt2.getData());
            builder.append(") ");
            String className = opt1.getClassName();
            if (!className.equals(opt2.getClassName())) {
                throw new IllegalArgumentException("The data type of two params is not identical!");
            }
            if (className.equals(Integer.class.getName())) {
                Integer sum = Calculator.sum((Integer) opt1.getData(), (Integer) opt2.getData());
                builder.append("= ");
                builder.append(sum);
            } else if (className.equals(Float.class.getName())) {
                Calculator.sum((Float) opt1.getData(), (Float) opt2.getData());
            } else {
                throw new IllegalArgumentException("Type " + opt1.getClass() + " is not supported by this method");
            }
        }
        response.setResult(builder.toString());
        return response;
    }
}
