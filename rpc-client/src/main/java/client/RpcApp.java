package client;

import common.RpcProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RpcApp {
    private static Logger log = LoggerFactory.getLogger(RpcApp.class);

    public static void main(String[] args) {
        RpcProtocol rpc = new RpcProtocolStub();
        Number sum = rpc.sum(1.0f, 0);
        if (sum != null)
            log.info("Result={}", sum.toString());
        sum = rpc.sum(Integer.MAX_VALUE, 1);
        if (sum != null)
            log.info("Result={}", sum.toString());
        String ret = rpc.upperCase("abscd");
        if (sum != null)
            log.info(ret);
    }
}
