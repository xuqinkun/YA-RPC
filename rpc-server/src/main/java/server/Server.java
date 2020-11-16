package server;

import bean.Response;
import common.Call;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import static bean.Status.ERROR;
import static bean.Status.OK;

public class Server {
    private static Logger log = LoggerFactory.getLogger(Server.class);
    public static final int port = 1111;

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress("0.0.0.0", port));
        Socket socket;
        while ((socket = server.accept()) != null) {
            log.info("Accept {}", socket.getRemoteSocketAddress());
            Call call = new Call();
            Response response;
            try {
                call.readExternal(new ObjectInputStream(socket.getInputStream()));
                response = parseRequest(call);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                response.writeExternal(oos);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Response parseRequest(Call call)  {
        String className = call.getClassName();
        String methodName = call.getMethodName();
        List<String> typeNames = call.getTypes();
        List<Object> params = call.getParams();
        int size = params.size();
        Class<?> []types = new Class[size];
        Object[] args = new Object[size];


        String packageName = Server.class.getPackage().getName();
        String classFullName = packageName + "." + className;
        Response response;
        try {
            for (int i = 0; i < size; i++) {
                types[i] = Class.forName(typeNames.get(i));
                args[i] = params.get(i);
            }
            Class<?> clazz = Class.forName(classFullName);
            Object obj = clazz.newInstance();
            Method method = clazz.getMethod(methodName, types);
            Object ret = method.invoke(obj, args);
            response = new Response(OK, "", ret);
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            response = new Response(ERROR, "", e.getMessage());
        }
        return response;
    }
}
