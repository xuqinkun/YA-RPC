package server;

import bean.Response;
import common.Call;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.List;

import static bean.Status.ERROR;
import static bean.Status.OK;

public class Worker implements Runnable {
    private static Logger log = LoggerFactory.getLogger(Worker.class);

    private Socket socket;

    public Worker(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        Call call = new Call();
        Response response;
        try {
            call.readExternal(new ObjectInputStream(socket.getInputStream()));
            response = parseRequest(call);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            log.debug("Call[{}]", call);
            response.writeExternal(oos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Response parseRequest(Call call) {
        String className = call.getClassName();
        String methodName = call.getMethodName();
        List<String> typeNames = call.getTypes();
        List<Object> params = call.getParams();
        int size = params.size();
        Class<?>[] types = new Class[size];
        Object[] args = new Object[size];
        String packageName = Server.class.getPackage().getName();
        String classFullName = packageName + "." + className;
        Response response = new Response();
        response.setCallID(call.getCallID());
        try {
            for (int i = 0; i < size; i++) {
                types[i] = Class.forName(typeNames.get(i));
                args[i] = params.get(i);
            }
            Class<?> clazz = Class.forName(classFullName);
            Object obj = clazz.newInstance();
            Method method = clazz.getMethod(methodName, types);
            Object ret = method.invoke(obj, args);
            response.setStatus(OK);
            response.setResult(ret);
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response(ERROR, call.getCallID(), e.getMessage());
            response.setStatus(ERROR);
            response.setResult(e.getMessage());
        }
        return response;
    }

}
