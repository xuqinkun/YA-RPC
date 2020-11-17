package common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static bean.Util.readString;
import static bean.Util.writeString;

public class Call implements Externalizable {
    private String callID;

    private String className;

    private String methodName;

    private List<Object> params;

    private List<String> types;

    public Call() {
    }

    public Call(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
        this.params = new ArrayList<>();
        this.types = new ArrayList<>();
    }

    public String getCallID() {
        return callID;
    }

    public void setCallID(String callID) {
        this.callID = callID;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public List<Object> getParams() {
        return params;
    }

    public List<String> getTypes() {
        return types;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        writeString(out, callID);
        writeString(out, className);
        writeString(out, methodName);
        int cnt = params.size();
        out.writeInt(cnt);
        for (int i = 0; i < cnt; i++) {
            Object param = params.get(i);
            writeString(out, types.get(i));
            out.writeObject(param);
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        callID = readString(in);
        className = readString(in);
        methodName = readString(in);
        int cnt = in.readInt();
        List<Object> args = new ArrayList<>();
        types = new ArrayList<>();
        for (int i = 0; i < cnt; i++) {
            types.add(readString(in));
            args.add(in.readObject());
        }
        params = args;
    }

    public void addParams(Object... args) {
        params.addAll(Arrays.asList(args));
    }

    public void addTypes(Class<?>... types) {
        for (Class<?> type : types) {
            this.types.add(type.getName());
        }
    }

    @Override
    public String toString() {
        return "Call{" +
                "callID='" + callID + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", params=" + params +
                ", types=" + types +
                '}';
    }
}
