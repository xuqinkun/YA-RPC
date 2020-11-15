package rpc.bean;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Message implements Externalizable {
    private String messageID;
    private OperationType opt;
    private List<OperationData> params;

    public Message() {
    }

    public Message(String messageID, OperationType opt) {
        this.messageID = messageID;
        this.opt = opt;
        params = new ArrayList<>();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        Util.writeString(out, messageID);
        Util.writeString(out, opt.toString());
        out.writeInt(params.size());
        for (OperationData data :
                params) {
            try {
                Util.writeString(out, data.getClassName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            out.writeObject(data.getData());
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.messageID = Util.readString(in);
        this.opt = OperationType.valueOf(Util.readString(in));
        int n = in.readInt();
        List<OperationData> params = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String clazzName = Util.readString(in);
            Object obj = in.readObject();
            params.add(new OperationData(clazzName, obj));
        }
        this.params = params;
    }

    public String getMessageID() {
        return messageID;
    }

    public OperationType getOpt() {
        return opt;
    }

    public List<OperationData> getParams() {
        return params;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public void setOpt(OperationType opt) {
        this.opt = opt;
    }

    public void setParams(OperationData... args) {
        this.params = new ArrayList<>(Arrays.asList(args));
    }
}
