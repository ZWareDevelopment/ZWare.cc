package dev.zihasz.client.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostNameToIP implements Util {

	public static InetAddress resolve(String hostname) {
		InetAddress address;
		try {
			address = InetAddress.getByName(hostname);
		} catch (UnknownHostException e) {
			throw new IllegalStateException("Error resolving " + hostname + ", " + e.getMessage());
		}
		return (address);
	}

}
