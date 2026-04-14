package com.github.mawen12.easeagent.core.utils;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Objects;

@EaseAgentClassLoader
public class NetUtils {

    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            String host = e.getMessage();
            if (host != null) {
                int colon = host.indexOf(':');
                if (colon > 0) {
                    return host.substring(0, colon);
                }
            }
        }
        return "UnknownHost";
    }

    public static String getHostIpV4() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface net = networkInterfaces.nextElement();
                if (net.isLoopback()) {
                    continue;
                }

                if (net.getName().startsWith("en") || net.getName().startsWith("eth")) {
                    String ip = ipaddress(net);
                    if (!Objects.equals(ip, "")) {
                        return ip;
                    }
                }
            }
        } catch (Exception e) {

        }
        return "UnknownIP";
    }

    private static String ipaddress(NetworkInterface net) {
        Enumeration<InetAddress> inetAddresses = net.getInetAddresses();
        while (inetAddresses.hasMoreElements()) {
            InetAddress a = inetAddresses.nextElement();
            if (a instanceof Inet4Address) {
                if (!a.isMulticastAddress() && !a.isLoopbackAddress()) {
                    return a.getHostAddress();
                }
            }
        }
        return null;
    }
}
