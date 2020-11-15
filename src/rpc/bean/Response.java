package rpc.bean;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class Response implements Externalizable {
    private String messageID;
    private String result;

    public Response() {
    }

    public Response(String messageID, String result) {
        this.messageID = messageID;
        this.result = result;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    /**
     * Serialize response
     * @param out
     * @throws IOException
     */
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        Util.writeString(out, messageID);
        Util.writeString(out, result);
    }

    /**
     * Deserialize response
     * @param in
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        messageID = Util.readString(in);
        result = Util.readString(in);
    }

    @Override
    public String toString() {
        return "Response{" +
                "messageID='" + messageID + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
