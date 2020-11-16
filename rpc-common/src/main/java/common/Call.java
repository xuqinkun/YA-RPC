package common;

import bean.Response;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static bean.Status.ERROR;
import static bean.Status.OK;
import static bean.Util.readString;
import static bean.Util.writeString;

public class Call implements Externalizable {
    private Class<?> clazz;

    private Method method;

    private List<Object> params;

    private Socket socket;

    public Call() {
    }

    public Call(Class<?> clazz, Method method) {
        this.clazz = clazz;
        this.method = method;
        params = new ArrayList<>();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        writeString(out, clazz.getSimpleName());
        writeString(out, method.getName());
        int cnt = method.getParameterCount();
        out.writeInt(cnt);
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < cnt; i++) {
            writeString(out, parameters[i].getType().getName());
            out.writeObject(params.get(i));
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        String classSimpleName = readString(in);
        String methodName = readString(in);
        int cnt = in.readInt();
        Class<?>[] types = new Class[cnt];
        List<Object> args = new ArrayList<>();
        for (int i = 0; i < cnt; i++) {
            String paramClassName = readString(in);
            types[i] = Class.forName(paramClassName);
            args.add(in.readObject());
        }

        buildCall(classSimpleName, methodName, types, args);
    }

    private void buildCall(String classSimpleName, String methodName, Class<?>[] types, List<Object> args)
            throws ClassNotFoundException {
        String packageName = getClass().getPackage().getName();
        String classFullName = String.format("%s.%s", packageName, classSimpleName);
        clazz = Class.forName(classFullName);
        params = args;
        try {
            method = clazz.getMethod(methodName, types);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public ObjectInputStream getInputStream() throws IOException {
        return new ObjectInputStream(socket.getInputStream());
    }

    public Response getResponse() {
        Object ret = null;
        try {
            readExternal(getInputStream());
            Object obj = clazz.newInstance();
            ret = method.invoke(obj, params.get(0), params.get(1));
        } catch (IOException | ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return new Response(ERROR, "", e.getMessage());
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return new Response(OK, "", ret);
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void addParams(Object... args) {
        params.addAll(Arrays.asList(args));
    }

    public void request() throws IOException {
        writeExternal(new ObjectOutputStream(socket.getOutputStream()));
    }
}
