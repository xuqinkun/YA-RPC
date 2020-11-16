package rpc.client;

import rpc.server.Server;

import java.util.logging.Logger;

public class Client {
    private static Logger log = Logger.getLogger(Server.class.toString());

    public static void main(String[] args) {
        CalculatorStub calc = new CalculatorStub("127.0.0.1", Server.port);
        Number sum = calc.sum(1.0f, 2.0f);
        log.info(sum.toString());
    }
}
