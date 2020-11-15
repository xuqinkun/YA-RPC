package rpc.bean;

public class OperationData {
    private String className;
    private Object data;

    public <T> OperationData(String className, Object data) {
        this.className = className;
        this.data = data;
    }

    public String getClassName() throws ClassNotFoundException {
        return className;
    }

    public Object getData() {
        return data;
    }
}
