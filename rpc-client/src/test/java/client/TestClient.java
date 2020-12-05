package client;

import bean.Util;
import common.RpcProtocol;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import server.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

public class TestClient {
    private static String host = "localhost";
    private static int port = 8686;

    private static Server server;

    private static RpcProtocol rpc;

    private static ExecutorService service = Executors.newCachedThreadPool();

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

    static class RandomTask implements Callable<Boolean> {
        Random random = new Random();

        RpcProtocol rpc;

        public RandomTask(RpcProtocol rpc) {
            this.rpc = rpc;
        }

        @Override
        public Boolean call() throws Exception {
            int a = random.nextInt(), b = random.nextInt();
            if (a % 2 == 0)
                return this.rpc.sum(a, b).equals(a + b);
            else {
                String uuid = Util.getUUID();
                return this.rpc.upperCase(uuid).equals(uuid.toUpperCase());
            }
        }
    }

    @Test
    public void testAtLeastOnce() {
        int n = 1000;
        List<Callable<Boolean>> taskList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Callable<Boolean> task = new RandomTask(rpc);
            service.submit(task);
            taskList.add(task);
        }
        boolean flag = true;
        for (Callable<Boolean> task : taskList) {
            try {
                flag &= task.call();
            } catch (Exception e) {
                flag = false;
                break;
            }
        }
        assertTrue(flag);
    }

    @Test
    public void testMultiClients() {
        int n = 10;
        List<Callable<Boolean>> taskList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            RpcProtocolStub client = new RpcProtocolStub(host, port);
            RandomTask task = new RandomTask(client);
            service.submit(task);
            taskList.add(task);
        }
        boolean flag = true;
        for (Callable<Boolean> task : taskList) {
            try {
                flag &= task.call();
            } catch (Exception e) {
                flag = false;
                break;
            }
        }
        assertTrue(flag);
    }

    @AfterClass
    public static void destroy() {
        server.stop();
    }
}
