package rpc.client;

import rpc.bean.Message;
import rpc.bean.OperationData;
import rpc.bean.Response;
import rpc.core.Server;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Logger;

import static rpc.bean.OperationType.SUM;

public class Client {
    private static Logger log = Logger.getLogger(Server.class.toString());

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("127.0.0.1", 1111));
        Message message = new Message();
        message.setMessageID("1");
        message.setOpt(SUM);
        OperationData opt1 = new OperationData(Integer.class.getName(), 1);
        OperationData opt2 = new OperationData(Integer.class.getName(), 2);
        message.setParams(opt1, opt2);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        message.writeExternal(oos);
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        Response res = new Response();
        res.readExternal(ois);
        log.info(res.toString());
    }
}
