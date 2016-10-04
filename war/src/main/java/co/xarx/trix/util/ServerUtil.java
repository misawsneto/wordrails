package co.xarx.trix.util;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

public class ServerUtil {
    public static String getServerStatus(){
        StringBuilder status = new StringBuilder();
        Runtime runtime = Runtime.getRuntime();

        status.append("\nstatus: OK\n");
        status.append("hostname: " + getHostname() + "\n");
        status.append("time: " + new Date().toString() + "\n");
        status.append("JVM mem use: " + runtime.totalMemory() / 1048576 + "MB\n");
        status.append("JVM mem available: " + runtime.freeMemory() / 1048576 + "MB\n");

        return status.toString();
    }

    private static String getHostname(){
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "unknown";
        }
    }
}
