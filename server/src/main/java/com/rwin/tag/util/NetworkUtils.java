package com.rwin.tag.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetworkUtils {

    public static String getPublicIpAddress() {
        InetAddress candidate = InetAddress.getLoopbackAddress();
        try {
            Enumeration<NetworkInterface> e = NetworkInterface
                    .getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface n = (NetworkInterface) e.nextElement();
                Enumeration<InetAddress> ee = n.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress i = (InetAddress) ee.nextElement();
                    candidate = getPreferred(i, candidate);
                }
            }
        } catch (SocketException e1) {

        }

        return candidate.getHostName();
    }

    private static InetAddress getPreferred(InetAddress i, InetAddress candidate) {
        if (candidate == null)
            return i;
        if (i == null)
            return candidate;
        if (candidate.isLoopbackAddress())
            return i;
        if (i.isLoopbackAddress())
            return candidate;
        if (candidate.isLinkLocalAddress())
            return i;
        if (i.isLinkLocalAddress())
            return candidate;

        return candidate;
    }
}
