package client;

import common.RpcProtocol;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import server.Server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestClient {
    private static String host = "localhost";
    private static int port = 8686;
    static Server server;
    static RpcProtocol rpc;

    @BeforeClass
    public static void init() {
        server = new Server(host, port);
        new Thread(server).start();
        rpc = new RpcProtocolStub(host, port);
    }

    @Test
    public void testSum() {
        assertEquals(rpc.sum(1, 2), 1 + 2);
        assertEquals(rpc.sum(-1, 2), -1 + 2);
        assertEquals(rpc.sum(Integer.MAX_VALUE, 2), Integer.MAX_VALUE + 2);
    }

    @Test
    public void testUppercase() {
        String str1 = "hello world!";
        assertEquals(rpc.upperCase(str1), str1.toUpperCase());
        assertEquals(rpc.upperCase(""), "");
        assertEquals(rpc.upperCase(" "), " ");
        assertNull(rpc.upperCase(null));
    }

    @AfterClass
    public static void destroy() {
        server.stop();
    }
}
