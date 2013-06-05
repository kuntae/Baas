package com.botduo.nearbyatm;

import android.app.Application;

/**
 * 전역적인 상수 및 메서드를 보관하기 위한 어플리케이션 클래스
 * @author ricale
 *
 */
public class BDApp extends Application {
	/** 프래퍼런스의 이름. */
	public static final String PREFERENCE_MAPS = "maps";
	/** 프레퍼런스 키 값. 확대/축소 레벨 값을 가리킨다. */
	public static final String PREF_KEY_ZOOM = "zoom";
	/** 프레퍼런스 키 값. 위도 값을 가리킨다.. */
	public static final String PREF_KEY_LAT = "latitude";
	/** 프레퍼런스 키 값. 경도 값을 가리킨다. */
	public static final String PREF_KEY_LNG = "longitude";
	/** 프레퍼런스 키 값. 장소 검색 시 검색 반경 값을 가리킨다. */
	public static final String PREF_KEY_RADIUS = "radius";
	/** 프레퍼런스 키 값. 장소 검색 시 필터링 타입 값을 가리킨다. */
	public static final String PREF_KEY_FILTERING_TYPE = "filtering.type";
	/** 프레퍼런스 키 값. 장소 검색 시 필터링 키워드 값을 가리킨다. */
	public static final String PREF_KEY_FILTERING_KEYWORD = "filtering.keyword";
	
	/** 필터링 액티비티를 실행할 때의 인텐트 액션 */
	public static final int ACTION_FILTERING = 0;
	/** 장소 검색 반경*/
	public static final int SEARCH_RADIUS = 1000;
	
	
	/** "국민"을 넘겨주기 위한 String 변수 */
	public static final String KEYWORD_KB = "국민은행";
	/** "농협"을 넘겨주기 위한 String 변수 */
	public static final String KEYWORD_NH = "농협";
	/** "우리"을 넘겨주기 위한 String 변수 */
	public static final String KEYWORD_WOORI = "우리은행";
	/** "스탠다드"을 넘겨주기 위한 String 변수 (스탠다드차타드)SC */
	public static final String KEYWORD_SC = "스탠다드";
	/** "기업"을 넘겨주기 위한 String 변수 */
	public static final String KEYWORD_IBK = "기업은행";
	/** "외환"을 넘겨주기 위한 String 변수 */
	public static final String KEYWORD_KEB = "외환은행";
	/** "수협"을 넘겨주기 위한 String 변수 */
	public static final String KEYWORD_SH = "수협";
	/** "신한"을 넘겨주기 위한 String 변수 */
	public static final String KEYWORD_SHINHAN = "신한은행";
	/** "씨티"을 넘겨주기 위한 String 변수 한국씨티은행 */
	public static final String KEYWORD_CB = "씨티은행";
	/** "대구"을 넘겨주기 위한 String 변수 */
	public static final String KEYWORD_DGB = "대구은행";
	/** "부산"을 넘겨주기 위한 String 변수 */
	public static final String KEYWORD_BB = "부산은행";
	/** "산업"을 넘겨주기 위한 String 변수 KDB한국산업 */
	public static final String KEYWORD_KDB = "산업은행";
	/** "광주"을 넘겨주기 위한 String 변수 */
	public static final String KEYWORD_GJB = "광주은행";
	/** "제주"을 넘겨주기 위한 String 변수 */
	public static final String KEYWORD_JJB = "제주은행";
	/** "전북"을 넘겨주기 위한 String 변수 */
	public static final String KEYWORD_KJB = "전북은행";
	/** "경남"을 넘겨주기 위한 String 변수 */
	public static final String KEYWORD_KNB = "경남은행";
	/** "하나"을 넘겨주기 위한 String 변수 */
	public static final String KEYWORD_HANA = "하나은행";
	/** "우체국"을 넘겨주기 위한 String 변수 */
	public static final String KEYWORD_POST = "우체국";
	/** "새마을"을 넘겨주기 위한 String 변수 */
	public static final String KEYWORD_KFCC = "새마을금고"; 
	/** "신협"을 넘겨주기 위한 String 변수 */
	public static final String KEYWORD_SHINHYUP = "신협";
	
	/** "GS25"을 넘겨주기 위한 String 변수 */
	public static final String KEYWORD_GS25 = "GS25";
	public static final String KEYWORD_GS25_2 = "지에스25";
	/** "세븐일레븐"을 넘겨주기 위한 String 변수 */
	public static final String KEYWORD_SEVENELEVEN = "세븐일레븐";
	/** "미니스톱"을 넘겨주기 위한 String 변수 */
	public static final String KEYWORD_MINISTOP = "미니스톱";
	/** "CU"을 넘겨주기 위한 String 변수 */
	public static final String KEYWORD_CU = "CU";
	public static final String KEYWORD_CU_2 = "훼미리마트";
	/** "바이더웨이"을 넘겨주기 위한 String 변수 */
	public static final String KEYWORD_BYTHEWAY = "바이더웨이";
} // end BDApp