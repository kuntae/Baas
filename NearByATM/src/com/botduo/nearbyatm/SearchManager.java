package com.botduo.nearbyatm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 장소 검색을 관리하는 클래스.
 * 구글 맵 api와는 연관성 없이 작성되었다.
 * @author ricale
 *
 */
public class SearchManager {
	///////////
	//////////
	/////////   enum
	
	/**
	 * 검색될 장소들의 타입을 의미하는 구분자
	 */
	public static enum TYPES {
		/** 은행 혹은 ATM을 의미 */
		ATM,
		/** 편의점을 의미 */
		CS
	} // end TYPES
	
	///////////
	//////////
	/////////   constant
	
	/** 장소 검색 요청을 위한 URL 정보 문자열 상수 */
	private static final String URL_BASIC = "https://maps.googleapis.com/maps/api/place/search/json?"
			                                + "key=AIzaSyBA94vpgV5sobtFoYjo8mJw47SGZ6xb29g&language=ko&sensor=true";
	/** 장소 상세 정보 요청을 위한 URL 정보 물자열 상수 */
	private static final String URL_BASIC_DETAIL = "https://maps.googleapis.com/maps/api/place/details/json?"
			                                     + "key=AIzaSyBA94vpgV5sobtFoYjo8mJw47SGZ6xb29g&language=ko&sensor=false&reference=";
	/** 장소 검색 요청을 위한 URL 정보 문자열 상수 */
	private static final String URL_TOKEN = "&";
	
	/** 장소 검색 요청을 위한 URL 정보 문자열 상수 */
	private static final String URL_KEY_LOCATION = "location=";
	/** 장소 검색 요청을 위한 URL 정보 문자열 상수 */
	private static final String URL_KEY_RADIUS = "radius=";
	/** 장소 검색 요청을 위한 URL 정보 문자열 상수 */
	private static final String URL_KEY_TYPES = "types=";
	/** 장소 검색 요청을 위한 URL 정보 문자열 상수 */
	private static final String URL_KEY_NAME = "name=";
	/** 장소 검색 요청을 위한 URL 정보 문자열 상수 */
	private static final String URL_KEY_PAGE = "pagetoken=";
	
	/** 장소 검색 요청을 위한 URL 정보 문자열 상수 */
	private static final String URL_VALUE_TYPES_ATM = "bank|atm";
	/** 장소 검색 요청을 위한 URL 정보 문자열 상수 */
	private static final String URL_VALUE_TYPES_CS = "convenience_store";
	
	/** 장소 검색 결과를 확인하기 위해 필요한, JSON 객체의 키값 문자열 상수 */
	private static final String JSON_KEY_RESULTSTATUS = "status";
	/** 장소 검색 결과를 확인하기 위해 필요한, JSON 객체의 키값 문자열 상수 */
	private static final String JSON_KEY_RESULTS = "results";
	/** 장소 검색 결과를 확인하기 위해 필요한, JSON 객체의 키값 문자열 상수 */
	private static final String JSON_KEY_TOKEN = "next_page_token";
	/** 장소 세부 정보를 확인하기 위해 필요한, JSON 객체의 키값 문자열 상수 */
	private static final String JSON_KEY_RESULT = "result";
	/** 장소 검색 결과를 확인하기 위해 필요한 JSON 객체의 키값 문자열 상수 */
	private static final String JSON_VALUE_RESULTSTATUS_OK = "OK";
	/** 장소 검색 결과를 확인하기 위해 필요한 JSON 객체의 키값 문자열 상수 */
	private static final String JSON_VALUE_RESULTSTATUS_NO_RESULT = "ZERO_RESULTS";
	
	/**
	 * 장소 검색 결과를 리스너에게 보내기 위한 핸들러 객체
	 */
	private SendResultHandler mHandler;
	
	///////////
	//////////
	/////////   constructor

	/**
	 * 생성자
	 * @param listener 장소 검색 완료 이벤트를 받을 이벤트 리스너 객체
	 */
	public SearchManager(OnSearchingPlacesCompleteListener listener) {
		mHandler = new SendResultHandler(listener);
	} // end SearchManager
	
	///////////
	//////////
	/////////   methods
	
	/**
	 * 특정 지역의 특정 장소들을 검색한다.
	 * @param types     검색한 장소들의 유형. 현재 ATM(은행및현금인출기)과 CS(편의점)를 지원한다.
	 * @param latitude  검색의 기준이 되는 위치(경도)
	 * @param longitude 검색의 기준이 되는 위치(위도)
	 * @param radius    검색 반경(미터)
	 * @param name      검색할 상호명, null이라면 types에 맞는 모든 장소를 검색한다.
	 * @param isAdditional 추가적인 요청인가 아닌가. 추가적인 요청일 시에는 
	 */
	public void requestNearbyPlaces(TYPES types, double latitude, double longitude, int radius, String name) {
		String address = makeAddress(types, latitude, longitude, radius, name);		
		new Thread(new NetworkRunnable().setAddress(address)).start();
	} // end getPlacesPositions
	
	public void requestAdditionalNearbyPlaces(TYPES types, double latitude, double longitude, int radius, String name) {
		String address = makeAddress(types, latitude, longitude, radius, name);		
		new Thread(new NetworkRunnable().setAddressForAdditionalSearch(address)).start();
	}
	
	/**
	 * 특정 장소의 상세 정보를 요청한다.
	 * @param reference 특정 장소의 상세 정보를 얻기 위한 참조 문자열 값
	 */
	public void requestPlaceDetail(String reference) {
		new Thread(new NetworkRunnable().setReference(reference)).start();
	} // end requestPlaceDetail
	
	private String makeAddress(TYPES types, double latitude, double longitude, int radius, String name) {
		String address = URL_BASIC + URL_TOKEN + URL_KEY_LOCATION + latitude + "," + longitude
				+ URL_TOKEN + URL_KEY_RADIUS + radius + URL_TOKEN + URL_KEY_TYPES;

		switch(types) {
		case ATM:
			address += URL_VALUE_TYPES_ATM;
			break;
		case CS:
			address += URL_VALUE_TYPES_CS;
		} // end switch

		if(name != null && !name.equals("")) {
			try {
				name = URLEncoder.encode(name, "UTF-8");
			} catch(UnsupportedEncodingException e) {
				e.printStackTrace();
			} // end try-catch

			address += URL_TOKEN + URL_KEY_NAME + '"' + name + '"';
		} // end if
		
		Log.i("URL", address);
		
		Log.i("KEYWORD", name);
		
		return address;
	}
	
	///////////
	//////////
	/////////   interface
	
	/**
	 * 장소 검색이 끝났을 때를 위한 이벤트 리스너 인터페이스
	 *
	 */
	public interface OnSearchingPlacesCompleteListener {
		/**
		 * 장소 검색이 끝났을 때 호출되는 콜백 메서드
		 * @param datas 검색된 장소 정보 (검색시 정렬된 자료들의 상위 20개의 자료)
		 */
		public void onCompleteSearchingPlaces(Place[] datas);
		
		/**
		 * 추가적인 장소 검색이 끝났을 때 호출되는 콜백 메서드
		 * @param data 검색된 장소 정보 (검색 시 정렬된 자료들의 상위 21번째부터 40번째까지의 자료)
		 */
		public void onCompleteAdditionalSearchingPlaces(Place[] datas);
		
		/**
		 * 장소 상세 정보를 찾았을 때 호출되는 콜백 메서드
		 * @param detail 장소 상세 정보
		 */
		public void onCompleteFindOutDetail(Place.PlaceDetailData detail);
		
		/**
		 * 장소 검색에 실패했을 때의 콜백 메서드
		 * 현재는 인터넷 연결이 되지 않는 경우에만 호출된다.
		 */
		public void onFailSearching();
	} // SearchPlacesCompleteListener
	
	///////////
	//////////
	/////////   inner class
	
	/**
	 * 장소 검색을 실행하기 위한 러너블 클래스 (http 연결과 관련된 작업은 별도의 스레드에서 하지 않으면 예외를 던진다.)
	 * @author  ricale
	 */
	private class NetworkRunnable implements Runnable {
		/** http 연결 시 연결 시도 최대 시간 */
		private static final int CONN_TIMEOUT = 10000;
		
		/** http 요청할 주소. 장소 검색 시에만 값을 넣는다. */
		private String address;
		/** http 요청할 주소의 일부분. 장소 세부 정보 요청 시에만 값을 넣는다. */
		private String reference;
		/** 이 요청이 추가적인 요청인가 새로운 요청인가 */
		private boolean isAdditional;
		
		/**
		 * 장소 검색을 요청할 주소를 설정한다.
		 * @param addr  장소 검색 요청을 위한 주소. 별도의 형식 검사가 없으므로 입력하는 사람이 확실하게 형식 검사를 해야 한다.
		 * @return  객체 자기 자신. 연결된 메서드 호출을 위해서이며 필요 없다면 무시해도 상관 없다.
		 */
		public NetworkRunnable setAddress(String addr) {
			address = addr;
			reference = null;
			isAdditional = false;
			return this;
		} // end NetworkThread
		
		/**
		 * 장소 상세 정보를 요청할 주소를 설정한다.
		 * @param ref 상세 정보 요청을 위한 참조 문자열.
		 * @return 객체 자기 자신. 연결된 메서드 호출을 위해서이며 필요 없다면 무시해도 상관 없다.
		 */
		public NetworkRunnable setReference(String ref) {
			reference = ref;
			address = null;
			return this;
		} // end setReference
		
		public NetworkRunnable setAddressForAdditionalSearch(String addr) {
			address = addr;
			reference = null;
			isAdditional = true;
			return this;
		}
		
		/**
		 * 스레드 메서드.
		 * 장소를 검색한 뒤 결과를 SendResultHandler 정적 클래스에 전달한다.
		 */
		public void run() {
			if(address != null) {
				sendPlacesSearchResult();
			} else if(reference != null) {
				sendPlaceDetailResult();
			} // end if
		} // end run
		
		/**
		 * http요청을 통해 정보를 받아온다.
		 * @param url http요청할 url
		 * @return 요청 결과 반환된 문자열
		 */
		private String getURLRequestResult(String url) {
			StringBuilder json = new StringBuilder();
			
			try {
				URL u = new URL(url);
				HttpURLConnection connection = (HttpURLConnection)u.openConnection();
				
				if(connection != null) {
					connection.setConnectTimeout(CONN_TIMEOUT);
					connection.setUseCaches(false);
					
					if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
						BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				
						String line;
						while((line = br.readLine()) != null) {
							json.append(line);
						} // end while
						br.close();
					} // end if
					connection.disconnect();
					return json.toString();
				} // end if
			} catch (IOException e) {
				e.printStackTrace();
			} // end try-catch
			
			return null; // 왜 이 문장은 return json.toString(); 이 실행될 때도 거쳐가는가. 실제로는 return json.toString(); 이 실행되는 것 같은데, 디버거의 버그인가.
		} // end getURLRequestResult
		
		/**
		 * http요청으로 얻은 문자열로부터 장소 검색 결과를 얻어낸다.
		 * 장소 검색 결과가 20개를 초과하면 sendAdditionalPlacesSearchResult()를 호출해 최대 20개를 추가로 검색한다.
		 * 인터넷 연결이 원활하지 않으면 해당 메시지를 전달한다.
		 */
		private void sendPlacesSearchResult() {
			try {
				String result = getURLRequestResult(address);
				if(result == null) {
					
					Message msg = new Message();
					msg.what = SendResultHandler.WHAT_FAIL_CONNECT;
					mHandler.sendMessage(msg);
					return;
				} // end if
				
				JSONObject object = new JSONObject(result);
				
				
				if(object.getString(JSON_KEY_RESULTSTATUS).equals(JSON_VALUE_RESULTSTATUS_OK)) {
					JSONArray array = object.getJSONArray(JSON_KEY_RESULTS);
					
					Place[] data = new Place[array.length()];
					for(int i = 0; i < array.length(); i++) {
						data[i] = new Place(array.getJSONObject(i));
					} // end for
					
					Message msg = new Message();
					msg.what = isAdditional ? SendResultHandler.WHAT_ADDITIONAL : SendResultHandler.WHAT_PLACES;
					msg.obj = data;
					mHandler.sendMessage(msg);
					
					if(!object.isNull(JSON_KEY_TOKEN)) {
						sendAdditionalPlacesSearchResult(object.getString(JSON_KEY_TOKEN));
					} // end if
					
				} else if(object.getString(JSON_KEY_RESULTSTATUS).equals(JSON_VALUE_RESULTSTATUS_NO_RESULT)) {
					Message msg = new Message();
					msg.what = SendResultHandler.WHAT_PLACES;
					msg.obj = new Place[0];
					mHandler.sendMessage(msg);
				} // end if
				
			} catch (JSONException e) {
				e.printStackTrace();
			} // end try-catch
		} // end sendPlacesSearchResult
		
		/**
		 * sendPlacesSearchResult()에서 검색이 완료되지 않았다면 (검색 결과 항목이 20개가 넘는다면)
		 * 추가적인 검색을 수행한다. 최대 20개까지만 수행한다.
		 * @param pagetoken 추가 검색을 위한 참조 문자열.
		 */
		private void sendAdditionalPlacesSearchResult(String pagetoken) {
			try {
				String result = getURLRequestResult(address + URL_TOKEN + URL_KEY_PAGE + pagetoken);
				if(result == null) {
					return;
				} // end if
				
				JSONObject object = new JSONObject(result);
				if(object.getString(JSON_KEY_RESULTSTATUS).equals(JSON_VALUE_RESULTSTATUS_OK)) {
					JSONArray array = object.getJSONArray(JSON_KEY_RESULTS);
					
					Place[] data = new Place[array.length()];
					for(int i = 0; i < array.length(); i++) {
						data[i] = new Place(array.getJSONObject(i));
					} // end for
					
					Message msg = new Message();
					msg.what = SendResultHandler.WHAT_ADDITIONAL;
					msg.obj = data;
					mHandler.sendMessage(msg);
				} // end if
				
			} catch (JSONException e) {
				e.printStackTrace();
			} // end try-catch
		} // end sendAdditionalPlacesSearchResult
		
		/**
		 * http요청으로 얻은 문자열로부터 장소 세부 정보를 얻어낸다.
		 */
		private void sendPlaceDetailResult() {
			try {
				String result = getURLRequestResult(URL_BASIC_DETAIL + reference);
				if(result == null) {
					return;
				} // end if
				
				JSONObject object = new JSONObject(result);
				if(object.getString(JSON_KEY_RESULTSTATUS).equals(JSON_VALUE_RESULTSTATUS_OK)) {
					
					Message msg = new Message();
					msg.what = SendResultHandler.WHAT_DETAIL;
					msg.obj = new Place.PlaceDetailData(object.getJSONObject(JSON_KEY_RESULT));
					mHandler.sendMessage(msg);
				} // end if
				
			} catch (JSONException e) {
				e.printStackTrace();
			} // end try-catch
		} // end getPlaceDetailResult
	} // end NetworkRunnable
	
	
	
	/**
	 * 검색 결과 자료를 (SearchManager 클래스 생성 당시 입력 받아놓은) 장소 검색 완료 이벤트 리스너 객체에게 넘긴다.
	 * @author ricale
	 */
	private static class SendResultHandler extends Handler {
		/** 핸들러가 받는 메시지의 종류를 구분하기 위한 상수. enum으로 만들면 Message로 주고 받기 번거롭기에 int로 선언한다. */
		public static final int WHAT_PLACES = 0;
		/** 핸들러가 받는 메시지의 종류를 구분하기 위한 상수. enum으로 만들면 Message로 주고 받기 번거롭기에 int로 선언한다. */
		public static final int WHAT_ADDITIONAL = 1;
		/** 핸들러가 받는 메시지의 종류를 구분하기 위한 상수. enum으로 만들면 Message로 주고 받기 번거롭기에 int로 선언한다. */
		public static final int WHAT_DETAIL = 2;
		/** 핸들러가 받는 메시지의 종류를 구분하기 위한 상수. enum으로 만들면 Message로 주고 받기 번거롭기에 int로 선언한다. */
		public static final int WHAT_FAIL_CONNECT = 3;
		
		/** 검색 결과를 받을 리스너 */
		private OnSearchingPlacesCompleteListener listener; 
		
		/**
		 * 생성자
		 * @param l 리스너 객체
		 */
		public SendResultHandler(OnSearchingPlacesCompleteListener l) {
			listener = l;
		} // end SearchManager
		
		/**
		 * 핸들러메시지 메서드.
		 * 검색 결과 데이터가 저장된 것이 있다면 데이터를 리스너에 전달한다.
		 */
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case WHAT_PLACES:
				listener.onCompleteSearchingPlaces((Place[])msg.obj);
				break;
			case WHAT_ADDITIONAL:
				listener.onCompleteAdditionalSearchingPlaces((Place[])msg.obj);
				break;
			case WHAT_DETAIL:
				listener.onCompleteFindOutDetail((Place.PlaceDetailData)msg.obj);
				break;
			case WHAT_FAIL_CONNECT:
				listener.onFailSearching();
				break;
			} // end switch
		} // end handleMessage
	} // end SendResultHandler
} // end SearchManager