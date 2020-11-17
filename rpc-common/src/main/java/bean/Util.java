package bean;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.URL;
import java.util.Properties;
import java.util.UUID;

public class Util {
    private static final Properties properties = new Properties();

    private static final String SERVER_BIND_HOST_KEY = "server.bind.host";

    private static final String SERVER_BIND_PORT_KEY = "server.bind.port";

    private static final String SERVER_REMOTE_HOST_KEY = "server.remote.host";

    {
        try {
            URL url = Util.class.getClassLoader().getResource("configuration.properties");
            if (url != null)
                properties.load(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeString(ObjectOutput out, String str) throws IOException {
        if (str == null || str.length() == 0) {
            out.writeInt(0);
            return;
        }
        byte[] data = str.getBytes();
        out.writeInt(data.length);
        out.write(data);
    }

    public static String readString(ObjectInput in) throws IOException {
        int len = in.readInt();
        if (len == 0) {
            return null;
        }
        byte[] buffer = new byte[len];
        in.read(buffer);
        return new String(buffer);
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static String getServerBindHost() {
        return properties.getProperty(SERVER_BIND_HOST_KEY, "0.0.0.0");
    }

    public static int getServerBindPort() {
        String port = properties.getProperty(SERVER_BIND_PORT_KEY, "8888");
        return Integer.parseInt(port);
    }

    public static String getServerRemotePort() {
        return properties.getProperty(SERVER_REMOTE_HOST_KEY, "127.0.0.1");
    }
}
