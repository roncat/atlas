package br.com.aexo.atlas.infraestructure.util;

import java.net.URLConnection;

public class HttpCall {

	public static String getJSON(String url) {
		try {
			URLConnection conn = new java.net.URL(url).openConnection();
			conn.setRequestProperty("content-type", "application/json");
			conn.setRequestProperty("accept", "application/json");
			return org.apache.commons.io.IOUtils.toString(conn.getInputStream());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String get(String url) {
		try {
			URLConnection conn = new java.net.URL(url).openConnection();
			return org.apache.commons.io.IOUtils.toString(conn.getInputStream());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


}
