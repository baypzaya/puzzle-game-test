package com.txyjssr.share;

public class Utils {

	public static String converteIp(int i) {
		String ipAdress = (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "."
				+ ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
		return ipAdress;
	}

}
