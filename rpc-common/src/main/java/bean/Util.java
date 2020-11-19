package bean;

import org.apache.commons.cli.*;

import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.UUID;

public class Util {
    private static final Properties properties = new Properties();

    private static final String SERVER_BIND_HOST_KEY = "server.bind.host";

    private static final String SERVER_BIND_PORT_KEY = "server.bind.port";

    private static final String SERVER_REMOTE_HOST_KEY = "server.remote.host";

    private static final String IP_PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.)" +
            "{3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
    public static final String DEFAULT_SERVER_BIND_IP = "0.0.0.0";
    public static final String DEFAULT_SERVER_BIND_PORT = "8888";
    public static final String DEFAULT_REMOTE_SERVER_IP = "127.0.0.1";

    {
        try {
            URL url = Util.class.getClassLoader().getResource("configuration.properties");
            if (url != null)
                properties.load(url.openStream());
        } catch (IOException e) {

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
        return properties.getProperty(SERVER_BIND_HOST_KEY, DEFAULT_SERVER_BIND_IP);
    }

    public static int getServerBindPort() {
        String port = properties.getProperty(SERVER_BIND_PORT_KEY, DEFAULT_SERVER_BIND_PORT);
        return Integer.parseInt(port);
    }

    public static String getServerRemotePort() {
        return properties.getProperty(SERVER_REMOTE_HOST_KEY, DEFAULT_REMOTE_SERVER_IP);
    }

    public static boolean validateHost(final String ip) {
        return ip.matches(IP_PATTERN);
    }

    public static boolean validatePort(String p) {
        if (p != null && !p.matches("\\d+")) {
            return false;
        }
        int port = Integer.parseInt(p);
        if (port <= 1024 || port > 65535) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(validatePort("1"));
    }

    public static void load(File file) {
        try {
            properties.load(new FileInputStream(file));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static CommandLine getCommandLine(String[] args) {
        Options options = new Options();

        Option argHost = new Option("h", "host", true, "server remote ip");
        argHost.setRequired(false);
        options.addOption(argHost);

        Option argPort = new Option("p", "port", true, "server bind port");
        argPort.setRequired(false);
        argPort.setType(Integer.class);
        options.addOption(argPort);

        Option argConfigure = new Option("c", "configure", true, "configuration path");
        argConfigure.setRequired(false);
        options.addOption(argConfigure);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            formatter.printHelp("utility-name", options);
            System.exit(1);
        }

        String configurePath = cmd.getOptionValue("c");
        String host = cmd.getOptionValue("h");

        if (configurePath != null) {
            File file = new File(configurePath);
            if (!file.exists()) {
                System.out.println("Configuration file " + configurePath + " does not exist!");
                System.exit(1);
            }
            load(file);
        }

        if (host != null && !validateHost(host)) {
            System.out.println("Bad IP format:" + host);
            System.exit(1);
        }
        return cmd;
    }
}
