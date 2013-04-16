package com.example.httpconnection5;

import java.io.IOException;
import java.io.StringReader;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class HttpClass {
	
	private String className = "HttpClass";
	
	public String getHTML(String query) {
		String html = "";
		
		Log.i( className + " @ query", query);
		String tagName = "";
		
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(query);
		ResponseHandler<String> handler = new BasicResponseHandler();
		
//		try {
//			html = client.execute(httpGet, handler);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return html;
		
		
		try {
			String response;
			response = client.execute(httpGet, handler);
			
			Log.i("response", response);

			XmlPullParser xpp = XmlPullParserFactory.newInstance()
					.newPullParser();
			xpp.setInput(new StringReader(response));

			int eventType = xpp.getEventType(), i = 0;
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.END_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					tagName = xpp.getName();
					break;
				case XmlPullParser.TEXT:
					if (tagName.equals("content")) {
						html = xpp.getText();
					} 
					break;
				case XmlPullParser.END_TAG:
					if (tagName.equals("content")) {
						eventType = xpp.next();
					}
					break;
				}
				eventType = xpp.next();
			}
			Log.i("html", html);
			return html;

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
