package com.SeleniumFramework.commons.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

public class ResponseHelper {
	static BufferedReader in = null;

	/**
	 * @param urlConnObj
	 * @return
	 * @throws IOException
	 * 
	 */
	public static String readHeaderInfo(URLConnection urlConnObj) throws IOException {
		readHeaderStatusInfo(urlConnObj); 
		String serviceCallStatus = null;
		try {
			serviceCallStatus = urlConnObj.getHeaderField(0);
			System.out.println(urlConnObj.getContent());
			return serviceCallStatus;
		} catch (Exception e) {
			System.out.println("readHeaderInfo Exception" + e.getMessage());
			return serviceCallStatus;
		} 
	}

	/**
	 * @param urlConnObj
	 * @return
	 * @throws IOException
	 */
	public static HashMap<String,String> readHeaderStatusInfo(URLConnection urlConnObj) throws IOException {

		//TODO: Change to a generic method to retrieve hashmap for specific set of named headers
		HashMap<String, String> statusMap = new HashMap<String, String>();
		statusMap.put("status_code", urlConnObj.getHeaderField(0));
		statusMap.put("app_error_msg", urlConnObj.getHeaderField("app_error_msg"));
		statusMap.put("app_error_svc_msg", urlConnObj.getHeaderField("app_error_svc_msg"));
		statusMap.put("app_error_code", urlConnObj.getHeaderField("app_error_code"));
		return statusMap;
	}

	public static String postResponseObject(HttpURLConnection httpURLConnection,JSONObject jsonObject) throws IOException {
		StringBuilder response = new StringBuilder(); 
		OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
		wr.write(jsonObject.toString());
		wr.flush();
		
		if(httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK){
		} else {
			BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
			String line = null;  
			while ((line = br.readLine()) != null) {  
				response.append(line + "\n");  
			}
		}
		httpURLConnection.disconnect();
		return response.toString();
	}


}