package bean;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import static bean.Util.readString;
import static bean.Util.writeString;

public class Response implements Externalizable {
    private Status status;
    private String callID;
    private Object result;

    public Response() {
    }

    public Response(Status status,String callID, Object result) {
        this.status = status;
        this.callID = callID;
        this.result = result;
    }

    public String getCallID() {
        return callID;
    }

    public void setCallID(String callID) {
        this.callID = callID;
    }

    public Object getResult() {
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
        writeString(out, status.toString());
        writeString(out, callID);
        out.writeObject(result);
    }

    /**
     * Deserialize response
     * @param in
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        status = Status.valueOf(readString(in));
        callID = readString(in);
        result = in.readObject();
    }

    @Override
    public String toString() {
        return "Response{" +
                "status=" + status +
                ", callID='" + callID + '\'' +
                ", result=" + result +
                '}';
    }
}
