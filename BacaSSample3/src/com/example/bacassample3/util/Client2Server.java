package com.example.bacassample3.util;

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

public class Client2Server {
	final private String className = "Client2Server";
	
	// 싱글톤 패턴 적용
	private static Client2Server client2Server;
	
	private Client2Server() {}
	
	public static Client2Server getInstance() {
		if(client2Server == null) {
			client2Server = new Client2Server();
		}
		return client2Server;
	}
	
	// 사용자 정보 등록
	public String registUserDeviceID(String parameter) {
		String html = "";
		
		String query = "http://" + CommonUtilities.SERVER_URL + "/mobile/user_regist_deviceid?" + parameter;
		
		Log.i( className + " @ query", query);
		
		String tagName = "";
		
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(query);
		ResponseHandler<String> handler = new BasicResponseHandler();

		try {
			String response;
			response = client.execute(httpGet, handler);
			
			Log.i(className + " @ response", response);

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
		return html;
	}
	
	// 사용자 정보 등록
	public String registUserAll(String parameter) {
		String html = "";
		
		String query = "http://" + CommonUtilities.SERVER_URL + "/mobile/user_regist_all?" + parameter;
		
		Log.i( className + " @ query", query);
		
		String tagName = "";
		
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(query);
		ResponseHandler<String> handler = new BasicResponseHandler();

		try {
			String response;
			response = client.execute(httpGet, handler);
			
			Log.i(className + " @ response", response);

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
		return html;
	}

	// 랭킹정보 저장 : 사용자의 사용 기록 저장
	public String saveRanking(String parameter) {
		String html = "";
		
		String query = "http://" + CommonUtilities.SERVER_URL + "/mobile/rank?" + parameter;
		
		Log.i( className + " @ query", query);
		
		String tagName = "";
		
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(query);
		ResponseHandler<String> handler = new BasicResponseHandler();

		try {
			String response;
			response = client.execute(httpGet, handler);
			
			Log.i(className + " @ response", response);

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
		return html;
	}
	
	// 사용자 정보 가져오기
	public String getUserinfo(String parameter) {
		String html = "";
		
		String query = "http://" + CommonUtilities.SERVER_URL + "/mobile/get_user_info?" + parameter;
		
		Log.i( className + " @ query", query);
		
		String tagName = "";
		
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(query);
		ResponseHandler<String> handler = new BasicResponseHandler();

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
		return html;
	}
	
	// 지역 정보 가져오기
	public String getLocationinfo(String parameter) {
		String html = "";
		
		String query = "http://" + CommonUtilities.SERVER_URL + "/mobile/get_location?" + parameter;
		
		Log.i( className + " @ query", query);
		
		String tagName = "";
		
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(query);
		ResponseHandler<String> handler = new BasicResponseHandler();

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
		return html;
	}

}
