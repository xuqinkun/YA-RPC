package client;

import common.RpcProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.Server;


public class RpcApp {
    public static final String HOST = "127.0.0.1";
    private static Logger log = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        Client client = new Client(HOST, Server.port);
        RpcProtocol rpc = new RpcProtocolStub(client);

        Number sum = rpc.sum(1.0f, 2.0f);
        log.info("Result={}", sum.toString());
        sum = rpc.sum(4, 2);
        log.info("Result={}", sum.toString());
        String ret = rpc.upperCase("abscd");
        log.info(ret);
    }
}
