package client;

import bean.Util;
import common.RpcProtocol;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;


public class RpcApp {
    private static Logger log = LoggerFactory.getLogger(RpcApp.class);

    public static void main(String[] args) {
        CommandLine cmd = Util.getCommandLine(args);
        String h = cmd.getOptionValue("h");
        String p = cmd.getOptionValue("p");

        String host = h != null ? h : Util.getServerRemotePort();
        int port = p != null ? Integer.parseInt(p) : Util.getServerBindPort();

        RpcProtocol rpc = new RpcProtocolStub(host, port);
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
