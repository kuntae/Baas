package com.botduo.nearbyatm;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * 장소의 정보를 저장하기 위한 데이터 클래스
 * @author ricale
 * @see com.botduo.nearbyatm.SearchManager
 * @see com.botduo.nearbyatm.MainActivity
 *
 */
public class Place {
	///////////
	//////////
	/////////   constant
	
	/** JSONObject에서 자료를 파싱해올 때 필요한 문자열 상수  */
	private static final String JSON_KEY_LOCATIONOBJECT = "geometry";
	/** JSONObject에서 자료를 파싱해올 때 필요한 문자열 상수  */
	private static final String JSON_KEY_LOCATIONOBJECT_2 = "location";
	/** JSONObject에서 자료를 파싱해올 때 필요한 문자열 상수  */
	private static final String JSON_KEY_LATITUDE = "lat";
	/** JSONObject에서 자료를 파싱해올 때 필요한 문자열 상수  */
	private static final String JSON_KEY_LONGITUDE = "lng";
	/** JSONObject에서 자료를 파싱해올 때 필요한 문자열 상수  */
	private static final String JSON_KEY_NAME = "name";
	/** JSONObject에서 자료를 파싱해올 때 필요한 문자열 상수  */
	private static final String JSON_KEY_REFERENCE = "reference";
	
	///////////
	//////////
	/////////   variable
	
	/** 장소의 위치 */
	private LatLng mPosition;
	/** 장소의 이름 */
	private String mName;
	/** 장소에 대한 상세 정보 요청 시 필요한 레퍼런스 */
	private String mReference;
	/** 장소 세부 정보가 들어있는 객체 */
	private PlaceDetailData mDetails;
	
	/** 지도에서 이 장소를 가리킬 마커 */
	private Marker mMarker;
	
	///////////
	//////////
	/////////   constructor
	
	/**
	 * 생성자.
	 * json 객체로부터 장소의 정보(위치, 이름, 레퍼런스)를 저장한다.
	 * @param json 장소 정보가 담긴 JSONObject
	 */
	public Place(JSONObject json) {
		try {
			JSONObject location = json.getJSONObject(JSON_KEY_LOCATIONOBJECT).getJSONObject(JSON_KEY_LOCATIONOBJECT_2);
			mPosition  = new LatLng(location.getDouble(JSON_KEY_LATITUDE), location.getDouble(JSON_KEY_LONGITUDE));
			mName      = json.getString(JSON_KEY_NAME);
			mReference = json.getString(JSON_KEY_REFERENCE);
		} catch (JSONException e) {
			
		} // end try-catch
	} // end PlaceData(JSONObject)
	
	///////////
	//////////
	/////////   getter
	
	/**
	 * 장소의 위치를 얻는다.
	 * @return 장소의 위치
	 */
	public LatLng getPosition() {
		return mPosition;
	} // end getPosition
	
	/**
	 * 장소의 이름을 얻는다.
	 * @return 장소의 이름
	 */
	public String getName() {
		return mName;
	} // end getName
	
	/**
	 * 장소의 세부 정보 검색 시 필요한 레퍼런스 문자열을 얻는다.
	 * @return 장소의 레퍼런스
	 */
	public String getReference() {
		return mReference;
	} // end getReference
	
	/**
	 * 장소의 마커의 아이디를 리턴한다.
	 * @return 장소의 마커의 아이디
	 */
	public String getMarkerId() {
		return mMarker.getId();
	} // end getMarkerId
	
	/**
	 * 장소 세부 정보가 설정되어 있는가.
	 * @return 설정되어 있다면 true, 아니라면 false
	 */
	public boolean isDetail() {
		return mDetails != null;
	} // end isDetail
	
	/**
	 * 마커를 보이도록/안보이도록 설정한다.
	 * @param visible 보일 것이라면 true, 아니라면 false
	 */
	public void setVisible(boolean visible) {
		mMarker.setVisible(visible);
	} // end setVisible
	
	/**
	 * 장소 세부 정보를 설정한다.
	 * @param details 장소 세부 정보
	 */
	public void setDetails(PlaceDetailData details) {
		mDetails = details;
		mMarker.setSnippet(mDetails.getPhoneNumber());
		mMarker.showInfoWindow();
	} // end setDetails
	
	/**
	 * 갖고 있는 장소 정보를 토대로  마커를 설정한다.
	 * @param map 마커를 꽂을 맵
	 * @param bitmap 마커에 쓰일 비트맵 이미지
	 */
	public void setMarker(GoogleMap map, Bitmap bitmap) {
		MarkerOptions mo = new MarkerOptions().draggable(false).position(mPosition).title(mName);
		
		if(bitmap != null) {
			mo.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
		} // end if
		
		mMarker = map.addMarker(mo);
	} // end setMarker
	
	///////////
	//////////
	/////////   methods
	
	/**
	 * 이 객체를 삭제(메모리 해제)한다.
	 */
	public void remove() {
		mPosition = null;
		mDetails = null;
		mMarker.remove();
		mMarker = null;
	} // end invalid
	
	///////////
	//////////
	/////////   inner class
	
	/**
	 * 장소 세부 정보를 저장하기 위한 클래스.
	 * Place 객체 속 맴버 객체로 존재하고 있으며,
	 * 외부에서 세부 정보만을 주고 받을 때 사용하기 위해 클래스 자체는 공개되어 있다.
	 * 현재는 전화번호 값만 갖고 있지만, 추후에 확장 가능하다.
	 * @author ricale
	 *
	 */
	public static class PlaceDetailData {
		/** 장소 상세 정보 요청에서 전화번호를 확인하기 위해 필요한, JSON 객체의 키값 문자열 상수 */
		private static final String JSON_KEY_PHONENUMBER = "formatted_phone_number";
		
		/** 장소의 전화번호 */
		private String mPhoneNumber;
		
		/**
		 * 생성자
		 * @param json 세부 정보가 담긴 JSONObject
		 */
		public PlaceDetailData(JSONObject json) {
			try {
				mPhoneNumber = json.getString(JSON_KEY_PHONENUMBER);
			} catch(JSONException e) {
				
			} // end try-catch
		} // end addDetails
		
		/**
		 * 장소 전화번호 getter
		 * @return 장소 레퍼런스
		 */
		public String getPhoneNumber() {
			return mPhoneNumber;
		} // end getReference
	} // end PlaceDetailData
} // end PlaceData