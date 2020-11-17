package client;

import common.RpcProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RpcApp {
    private static Logger log = LoggerFactory.getLogger(RpcApp.class);

    public static void main(String[] args) {
        RpcProtocol rpc = new RpcProtocolStub();
        Number sum = rpc.sum(1.0f, 2.0f);
        log.info("Result={}", sum.toString());
        sum = rpc.sum(4, 2);
        log.info("Result={}", sum.toString());
        String ret = rpc.upperCase("abscd");
        log.info(ret);
        System.exit(0);
    }
}
