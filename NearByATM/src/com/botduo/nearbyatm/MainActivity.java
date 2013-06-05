package com.botduo.nearbyatm;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.botduo.nearbyatm.FAQActivity.RankingAsync;
import com.example.bacassample3.util.Client2Server;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import static com.example.bacassample3.util.CommonUtilities.DEVICE_ID;
import static com.example.bacassample3.util.CommonUtilities.IMEI_ID;
import static com.example.bacassample3.util.CommonUtilities.SERVER_URL;
import static com.example.bacassample3.util.CommonUtilities.EXTRA_MESSAGE;
import static com.example.bacassample3.util.CommonUtilities.SENDER_ID;

/**
 * 메인 액티비티.
 * 지도를 띄우고 다른 각종 정보/설정 액티비티들로 이동할 수 있도록 해준다.
 * MainMenuFragment.OnFragmentButtonClickListener, TrackingButton.OnTrackingModeChangeListener, SearchManager.OnSearchingPlacesCompleteListener 인터페이스를 구현한다.
 * @author ricale
 *
 */
public class MainActivity extends FragmentActivity implements MainMenuFragment.OnFragmentButtonClickListener,
                                                              TrackingButton.OnTrackingModeChangeListener,
                                                              SearchManager.OnSearchingPlacesCompleteListener {
	///////////
	//////////
	/////////   constant
	
	/** 자주 호출되는 센서 관련 이벤트 리스너의 호출 빈도를 제어하기 위한 상수 */
	private static final int SENSOR_LISTENER_CALL_FREQUENCY = 30;
	/** 기존 위치에서 이 수치만큼 이동하지 않으면 장소 검색을 하지 않는다.  */
	private static final int DISTANCE_FOR_RE_SEARCH = 200;
	/** 방향 추적 상태 시 지도의 방향을 돌리기 위한 최소 각도 값(변위) */
	private static final float DEGREE_FOR_ROTATE_BEARING = 5.0f;
	/** 인터넷 연결이 안 되 장소 검색에 실패했을 때의 메시지 */
	private static final String ERROR_FAIL_SEARCHING = "인터넷 연결이 원활하지 않아 자동으로 종료됩니다.";
	/** 사용하고 있던 위치 관리자가 사용 불가 상태가 되었을 때의 메시지 */
	private static final String ERROR_DISABLE_LOCATION_PROVIDER = "위치 관리자를 사용할수 없습니다. 다시 시도해주세요.";
	/** 장소 검색을 위한 최소한의 지도 확대 수준 */
	private static final int MIN_ZOOM_LEVEL_FOR_SEARCHING = 13;
	/** Admob 게시자 아이디 */
	private static final String MY_AD_UNIT_ID = "a15124294325dc2";
	
	
	///////////
	//////////
	/////////   variable
	
	/** 프레퍼런스 */
	private SharedPreferences mPreference;

	/** 카메라의 현재 포지션.  */
	private CameraPosition mCameraPosition;
	/** 구글 맵 객체 */
	private GoogleMap mMap;
	/** 현재 클릭된 마커가 속한 Place 객체. 관련 처리가 끝나면 null 값이 된다. */
	private Place mPlaceSelected;
	
	/** [현재 위치 추적] 상태. */
	private TrackingButton.STATE mTrackingMode;
	
	/** 로케이션 매니져 객체. 현재 위치 추적을 위해 필요하다. */
	private LocationManager mLocationManager;
	/** 위치 제공자(Location Provider) 객체를 가져올 때 필요한 객체. 위치 제공자에 대한 요구 조건을 표현한다. 굳이 멤버로 갖고 있어야 하나 고민하고 있는 변수 */
	private Criteria mCriteria;
	/** 위치 제공자를 표현하는 문자열. 굳이 멤버로 갖고 있어야 하나 고민하고 있는 변수 */
	private String mProvider;
	/** 구글 맵에서의 자기 위치 추적 및 표현 기능을 위해, 구글 맵에서 제공하는 이벤트 리스너 */
	private LocationSource.OnLocationChangedListener mLocationChangedListener;
	
	/** 센서 매니져. 현재 방향 값 확인에 쓰인다. */
	private SensorManager mSensorManager;
	/** 자주 호출되는 Sensor 이벤트 리스너의 호출 회수를 제어하기 위한 카운터 */
	private int mCountForFrequency;
	/** 현재 방향 값 계산에 쓰이는 가속계센서값 */
	private float[] mGravity;
	/** 현재 방향 값 계산에 쓰이는 자기계센서값 */
	private float[] mGeoMagnetic;
	
	/** 메뉴 기능을 담당하는 프래그먼트 */
	private MainMenuFragment mMenuFragment;
	
	/** 장소 검색 기능을 제공하는 객체 */
	private SearchManager mSearcher;
	/** 장소 검색 결과로 화면에 표시되는 마커들을 포함하는 Place 객체 리스트 */
	private LinkedList<Place> mPlaces;
	/** 장소 검색 시 필터링 기준 (은행 or 편의점)*/
	private int mFilteringType;
	/** 장소 검색 시 필터링될 단어 */
	private String mFilteringKeyword;
	
	/** 재검색 요청을 위해 기준이 되는 장소. 여기에서 DISTANCE_FOR_RE_SEARCH이상 이동해야 장소 검색이 다시 이루어진다. */
	private LatLng mLastSearchingPosition;
	/** 위치 관리자에 의해 가장 최근에 최신화된 지점. 지도의 카메라를 움직인 것이 사용자인가 위치관리자인가를 판단할 때 쓰인다. */
	private LatLng mLastLocationPosition;
	
	/** 마커에 쓰일 비트맵 아이콘(drawable 리소스)을 bitmap 형태로 미리 저장해놓은 맵 */
	private HashMap<String, Bitmap> mMarkerIcons;
	
	/** ADView 추가 */
	private AdView adView;
	
	///////////
	//////////
	/////////   lifecycle callback method
	
	/**
	 * 액티비티 생성 시 실행되는 콜백 메서드.
	 * 각종 초기화를 담당한다.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// 프래퍼런스를 불러온다.
		mPreference = getSharedPreferences(BDApp.PREFERENCE_MAPS, 0);
		
		// 지도에서의 현재 위치 / 지도를 초기화한다.
		mCameraPosition = new CameraPosition.Builder().target(new LatLng(0.0f, 0.0f)).build();
		setUpMapIfNeeded();		

		// [현재 위치 추적 상태]를 초기화한다.
		mTrackingMode = TrackingButton.STATE.OFF;
		
		// 위치 관리자와 센서 관리자를 초기화한다.
		mLocationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
		mCriteria = new Criteria();
		mCriteria.setAccuracy(Criteria.ACCURACY_FINE);
		mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		
		// 메뉴 프래그먼트를 저장한다.
		mMenuFragment = (MainMenuFragment)getSupportFragmentManager().findFragmentById(R.id.main_menu);
		
		// 위치 검색 관리 객체 / 위치 검색 결과 리스트 / 위치 검색 시 검색 종류(은행or편의점) / 위치 검색 시 검색 상호명 을 초기화한다.
		mSearcher = new SearchManager(this);
		mPlaces   = new LinkedList<Place>();
		mFilteringType    = mPreference.getInt(BDApp.PREF_KEY_FILTERING_TYPE, FilteringActivity.ATM);
		mFilteringKeyword = mPreference.getString(BDApp.PREF_KEY_FILTERING_KEYWORD, "");
		setFilteringButtonBackground(mFilteringKeyword);
		
		// 마커에 쓰일 리소스 이미지들을 비트맵 형식으로 저장해 놓는다.
		mMarkerIcons = new HashMap<String, Bitmap>();
		mMarkerIcons.put(BDApp.KEYWORD_KB,       BitmapFactory.decodeResource(getResources(), R.drawable.png_marker_kb));
		mMarkerIcons.put(BDApp.KEYWORD_NH,       BitmapFactory.decodeResource(getResources(), R.drawable.png_marker_nh));
		mMarkerIcons.put(BDApp.KEYWORD_WOORI,    BitmapFactory.decodeResource(getResources(), R.drawable.png_marker_woori));
		mMarkerIcons.put(BDApp.KEYWORD_IBK,      BitmapFactory.decodeResource(getResources(), R.drawable.png_marker_ibk));
		mMarkerIcons.put(BDApp.KEYWORD_KEB,      BitmapFactory.decodeResource(getResources(), R.drawable.png_marker_keb));
		mMarkerIcons.put(BDApp.KEYWORD_SH,       BitmapFactory.decodeResource(getResources(), R.drawable.png_marker_sh));
		mMarkerIcons.put(BDApp.KEYWORD_SHINHAN,  BitmapFactory.decodeResource(getResources(), R.drawable.png_marker_shinhan));
		mMarkerIcons.put(BDApp.KEYWORD_CB,       BitmapFactory.decodeResource(getResources(), R.drawable.png_marker_cb));
		mMarkerIcons.put(BDApp.KEYWORD_DGB,      BitmapFactory.decodeResource(getResources(), R.drawable.png_marker_dgb));
		mMarkerIcons.put(BDApp.KEYWORD_BB,       BitmapFactory.decodeResource(getResources(), R.drawable.png_marker_bb));
		mMarkerIcons.put(BDApp.KEYWORD_KDB,      BitmapFactory.decodeResource(getResources(), R.drawable.png_marker_kdb));
		mMarkerIcons.put(BDApp.KEYWORD_GJB,      BitmapFactory.decodeResource(getResources(), R.drawable.png_marker_gjb));
		mMarkerIcons.put(BDApp.KEYWORD_JJB,      BitmapFactory.decodeResource(getResources(), R.drawable.png_marker_jjb));
		mMarkerIcons.put(BDApp.KEYWORD_KJB,      BitmapFactory.decodeResource(getResources(), R.drawable.png_marker_kjb));
		mMarkerIcons.put(BDApp.KEYWORD_KNB,      BitmapFactory.decodeResource(getResources(), R.drawable.png_marker_knb));
		mMarkerIcons.put(BDApp.KEYWORD_HANA,     BitmapFactory.decodeResource(getResources(), R.drawable.png_marker_hana));
		mMarkerIcons.put(BDApp.KEYWORD_POST,     BitmapFactory.decodeResource(getResources(), R.drawable.png_marker_post));
		mMarkerIcons.put(BDApp.KEYWORD_KFCC,     BitmapFactory.decodeResource(getResources(), R.drawable.png_marker_kfcc));
		mMarkerIcons.put(BDApp.KEYWORD_SHINHYUP, BitmapFactory.decodeResource(getResources(), R.drawable.png_marker_shinhyup));
		mMarkerIcons.put(BDApp.KEYWORD_SC,       BitmapFactory.decodeResource(getResources(), R.drawable.png_marker_sc));
		
		mMarkerIcons.put(BDApp.KEYWORD_GS25,        BitmapFactory.decodeResource(getResources(), R.drawable.png_marker_gs25));
		mMarkerIcons.put(BDApp.KEYWORD_GS25_2,      BitmapFactory.decodeResource(getResources(), R.drawable.png_marker_gs25));
		mMarkerIcons.put(BDApp.KEYWORD_SEVENELEVEN, BitmapFactory.decodeResource(getResources(), R.drawable.png_marker_seveneleven));
		mMarkerIcons.put(BDApp.KEYWORD_MINISTOP,    BitmapFactory.decodeResource(getResources(), R.drawable.png_marker_ministop));
		mMarkerIcons.put(BDApp.KEYWORD_CU,          BitmapFactory.decodeResource(getResources(), R.drawable.png_marker_cu));
		mMarkerIcons.put(BDApp.KEYWORD_CU_2,        BitmapFactory.decodeResource(getResources(), R.drawable.png_marker_cu));
		mMarkerIcons.put(BDApp.KEYWORD_BYTHEWAY,    BitmapFactory.decodeResource(getResources(), R.drawable.png_marker_bytheway));
		
		mMarkerIcons.put("empty", BitmapFactory.decodeResource(getResources(), R.drawable.png_marker_empty));
		
		/// 광고를 부착한다.
		
		adView = new AdView(this,AdSize.BANNER,MY_AD_UNIT_ID);
		
		LinearLayout layout = (LinearLayout)findViewById(R.id.adLayout);
		layout.addView(adView);
		adView.loadAd(new AdRequest());

		
		// IMEI_ID 얻기
		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		IMEI_ID = telephonyManager.getDeviceId();
		
		// GCM_ID 얻기
		SERVER_URL = "113.198.80.234";
		
		checkNotNull(SERVER_URL, "SERVER_URL");
		checkNotNull(SENDER_ID, "SENDER_ID");
		
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);
		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);
		
		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				"com.google.android.gcm.demo.app.DISPLAY_MESSAGE"));
		
		DEVICE_ID = GCMRegistrar.getRegistrationId(this);
		
		if(DEVICE_ID.equals(""))
				GCMRegistrar.register(this, "361274149162");
		
		Log.i("MainActivity @ onCreate", "IMEI_ID:" + IMEI_ID + " GCM_ID:" + DEVICE_ID);
		
		RegistDeviceIDAsync registDeviceIDAsync = new RegistDeviceIDAsync();
		registDeviceIDAsync.execute("imeiid=" + IMEI_ID + "&deviceid=" + DEVICE_ID);
		
		Log.i("rank", "rank");
		RankingAsync rankingAsync = new RankingAsync();
		rankingAsync.execute("used_function=atm&user_id=" + DEVICE_ID);
	} // end onCreate
	
	
	
	/**
	 * 메뉴가 초기화될 때의 콜백 메서드
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		
		return true;
	} // end onCreateOptionsMenu
	
	/**
	 * 액티비티 활성화 시 실행되는 콜백 메서드.
	 * 자기 위치 추적에 관련된 초기화를 담당한다.
	 */
	@Override
	public void onResume() {
		super.onResume();
		mMap.setLocationSource(mLocationSource);
		
	} // end onResume
	
	/**
	 * 액티비티 비활성화 시 실행되는 콜백 메서드.
	 * 자기 위치 추적에 관련된 뒷정리를 담당한다.
	 */
	@Override
	public void onPause() {
		mMap.setLocationSource(null);
		
		mMenuFragment.trackingModeOffManually();
		super.onPause();
	} // end onPause
	
	
	/**
	 * Intent로 실행한 액티비티가 종료되어 다시 활성화될 때의 콜백 메서드.
	 * 필터링 액티비티가 필터링 값을 전달해준다면 전달 받은 값을 토대로 장소 검색을 재실행한다.
	 * 기존에 존재하던 검색 결과는 모두 삭제된다.
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
		case BDApp.ACTION_FILTERING:
			if(resultCode == RESULT_OK) {
				mFilteringType =    data.getIntExtra(FilteringActivity.KEY_TYPE, FilteringActivity.ATM);
				mFilteringKeyword = data.getStringExtra(FilteringActivity.KEY_KEYWORD);
				
				SearchManager.TYPES type;
				switch(mFilteringType) {
				case FilteringActivity.ATM:
					type = SearchManager.TYPES.ATM;
					break;
				case FilteringActivity.CS:
				default:
					type = SearchManager.TYPES.CS;
					break;
				} // end switch
				
				setFilteringButtonBackground(mFilteringKeyword);
				
				mSearcher.requestNearbyPlaces(type, mCameraPosition.target.latitude, mCameraPosition.target.longitude, BDApp.SEARCH_RADIUS, mFilteringKeyword);
				mLastSearchingPosition = mCameraPosition.target;
				
				// 기존에 존재하던 검색결과값은 모두 지운다.
				Iterator<Place> ip = mPlaces.iterator();
				while(ip.hasNext()) {
					Place place = ip.next();
					place.remove();
					ip.remove();
				} // end while
				
			} // end if
			break;
		} // end switch
	} // end onActivityResult
	
	/**
	 * 어플리케이션 종료 시 실행되는 콜백 메서드.
	 * 프레퍼런스를 저장한다.
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		adView.destroy();
		
		CameraPosition position = mMap.getCameraPosition();
		mPreference.edit().putFloat(BDApp.PREF_KEY_ZOOM, position.zoom)
		                  .putFloat(BDApp.PREF_KEY_LAT,  (float)position.target.latitude)
		                  .putFloat(BDApp.PREF_KEY_LNG,  (float)position.target.longitude)
		                  .putInt(BDApp.PREF_KEY_FILTERING_TYPE, mFilteringType)
		                  .putString(BDApp.PREF_KEY_FILTERING_KEYWORD, mFilteringKeyword)
		                  .commit();
	} // end onDestroy
	
	///////////
	//////////
	/////////   methods
	
	private void setFilteringButtonBackground(String keyword) {
		if(keyword.equals(BDApp.KEYWORD_BB)) {
			mMenuFragment.setFilteringButtonBackground(R.drawable.marker_bb);
		} else if(keyword.equals(BDApp.KEYWORD_BYTHEWAY)) {
			mMenuFragment.setFilteringButtonBackground(R.drawable.marker_bytheway);
		} else if(keyword.equals(BDApp.KEYWORD_CB)) {
			mMenuFragment.setFilteringButtonBackground(R.drawable.marker_cb);
		} else if(keyword.equals(BDApp.KEYWORD_CU)) {
			mMenuFragment.setFilteringButtonBackground(R.drawable.marker_cu);
		} else if(keyword.equals(BDApp.KEYWORD_DGB)) {
			mMenuFragment.setFilteringButtonBackground(R.drawable.marker_dgb);
		} else if(keyword.equals(BDApp.KEYWORD_GJB)) {
			mMenuFragment.setFilteringButtonBackground(R.drawable.marker_gjb);
		} else if(keyword.equals(BDApp.KEYWORD_GS25)) {
			mMenuFragment.setFilteringButtonBackground(R.drawable.marker_gs25);
		} else if(keyword.equals(BDApp.KEYWORD_HANA)) {
			mMenuFragment.setFilteringButtonBackground(R.drawable.marker_hana);
		} else if(keyword.equals(BDApp.KEYWORD_IBK)) {
			mMenuFragment.setFilteringButtonBackground(R.drawable.marker_ibk);
		} else if(keyword.equals(BDApp.KEYWORD_JJB)) {
			mMenuFragment.setFilteringButtonBackground(R.drawable.marker_jjb);
		} else if(keyword.equals(BDApp.KEYWORD_KB)) {
			mMenuFragment.setFilteringButtonBackground(R.drawable.marker_kb);
		} else if(keyword.equals(BDApp.KEYWORD_KDB)) {
			mMenuFragment.setFilteringButtonBackground(R.drawable.marker_kdb);
		} else if(keyword.equals(BDApp.KEYWORD_KEB)) {
			mMenuFragment.setFilteringButtonBackground(R.drawable.marker_keb);
		} else if(keyword.equals(BDApp.KEYWORD_KFCC)) {
			mMenuFragment.setFilteringButtonBackground(R.drawable.marker_kfcc);
		} else if(keyword.equals(BDApp.KEYWORD_KJB)) {
			mMenuFragment.setFilteringButtonBackground(R.drawable.marker_kjb);
		} else if(keyword.equals(BDApp.KEYWORD_KNB)) {
			mMenuFragment.setFilteringButtonBackground(R.drawable.marker_knb);
		} else if(keyword.equals(BDApp.KEYWORD_MINISTOP)) {
			mMenuFragment.setFilteringButtonBackground(R.drawable.marker_ministop);
		} else if(keyword.equals(BDApp.KEYWORD_NH)) {
			mMenuFragment.setFilteringButtonBackground(R.drawable.marker_nh);
		} else if(keyword.equals(BDApp.KEYWORD_POST)) {
			mMenuFragment.setFilteringButtonBackground(R.drawable.marker_post);
		} else if(keyword.equals(BDApp.KEYWORD_SC)) {
			mMenuFragment.setFilteringButtonBackground(R.drawable.marker_sc);
		} else if(keyword.equals(BDApp.KEYWORD_SEVENELEVEN)) {
			mMenuFragment.setFilteringButtonBackground(R.drawable.marker_seveneleven);
		} else if(keyword.equals(BDApp.KEYWORD_SH)) {
			mMenuFragment.setFilteringButtonBackground(R.drawable.marker_sh);
		} else if(keyword.equals(BDApp.KEYWORD_SHINHAN)) {
			mMenuFragment.setFilteringButtonBackground(R.drawable.marker_shinhan);
		} else if(keyword.equals(BDApp.KEYWORD_SHINHYUP)) {
			mMenuFragment.setFilteringButtonBackground(R.drawable.marker_shinhyup);
		} else if(keyword.equals(BDApp.KEYWORD_WOORI)) {
			mMenuFragment.setFilteringButtonBackground(R.drawable.marker_woori);
		} else {
			mMenuFragment.setFilteringButtonBackground(R.drawable.filtering_button_default);
		}
	}
	
	/**
	 * 구글 맵 객체를 얻어와 관련된 초기화를 한다.
	 */
	private void setUpMapIfNeeded() {
	    if (mMap == null) {
	        mMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
	        
	        if (mMap != null) {
	        	mMap.setMyLocationEnabled(true);
//	            mMap.getUiSettings().setMyLocationButtonEnabled(true);
	            mMap.getUiSettings().setMyLocationButtonEnabled(false);
	            
	            mMap.setOnCameraChangeListener(mCameraChangeListener);
	            mMap.setLocationSource(mLocationSource);
	            mMap.setOnMarkerClickListener(mMarkerClickListener);
	            mMap.setOnMapClickListener(mMapClickListener);
	            mMap.setOnMapLongClickListener(mMapLongClickListener);
	            
	            float zoom = mPreference.getFloat(BDApp.PREF_KEY_ZOOM, 0.0f);
	            
	            if(zoom != 0.0f) {
	            	double latitude  = mPreference.getFloat(BDApp.PREF_KEY_LAT, 0.0f);
		            double longitude = mPreference.getFloat(BDApp.PREF_KEY_LNG, 0.0f);
	            	updateCameraWithoutBearingAndTilt(latitude, longitude, zoom);
	            } // end if
	        } // end if
	    } // end if
	} // end setUpMapIfNeeded
	
	/**
	 * updateCamera(latitude, longitude, mCameraPosition.bearing, mCameraPosition.tilt, zoom)를 호출한다.
	 * 카메라의 위치와 확대/축소 레벨은 변경하되 방향, 기울기는 변경하지 않는다.
	 * 위치 및 확대/축소 레벨도 0.0f를 대입함으로써 변경하지 않을 수 있다.
	 */
	private void updateCameraWithoutBearingAndTilt(double latitude, double longitude, float zoom) {
		updateCamera(latitude, longitude, mCameraPosition.bearing, mCameraPosition.tilt, zoom);
	} // end updateCameraByInitBearingAndTilt
	
	/**
	 * 구글 맵의 카메라 위치를 설정한다.
	 * 인자들 중 적어도 하나는 의미가 있는 값이어야 한다.
	 * @param latitude  카메라의 위치(위도). longitude와 함께 0.0d를 입력하면 무시되고 기존값으로 유지된다.
	 * @param longitude 카메라의 위치(경도). latitude와 함께 0.0d를 입력하면 무시되고 기존값으로 유지된다.
	 * @param bearing   카메라의 방향
	 * @param tilt      카메라의 각도
	 * @param zoom      카메라의 확대/축소 레벨, 0.0f를 입력하면 무시되고 기존값으로 유지된다.
	 */
	private void updateCamera(double latitude, double longitude, float bearing, float tilt, float zoom) {
        if(latitude == 0.0d && longitude == 0.0d) {
        	latitude  = mCameraPosition.target.latitude;
        	longitude = mCameraPosition.target.longitude;
        } // end if
        if(zoom == 0.0f) {
        	zoom = mCameraPosition.zoom;
        } // end if
        
        mLastLocationPosition = new LatLng(latitude, longitude);
        
        CameraPosition cp = new CameraPosition.Builder().target(mLastLocationPosition)
        		                                        .bearing(bearing)
        		                                        //.tilt(tilt)
        		                                        .zoom(zoom)
        		                                        .build();
        CameraUpdate update = CameraUpdateFactory.newCameraPosition(cp);
        
        Log.i("CAMERA", "lat: " + mLastLocationPosition.latitude + "  lng: " + mLastLocationPosition.longitude);
        mMap.animateCamera(update);
	} // end updateCamera
	
	/**
	 * 현재 위치 추적 상태를 설정한다.
	 * 추적 상태에 따라 위치 관리자 / 센서 관리자의 설정을 변경한다.
	 * @param state 설정할 현재 위치 추적 상태
	 */
	private void setTrackingMode(TrackingButton.STATE state) {		
		switch(state) {
		case OFF:
			if(mTrackingMode != TrackingButton.STATE.OFF) {
				mLocationManager.removeUpdates(mLocationListener);
				
				if(mTrackingMode == TrackingButton.STATE.ON_WITH_BEARING) {
					mSensorManager.unregisterListener(mSensorListener);
				} // end if
			} // end if
			
			break;
		case ON_WITHOUT_BEARING:
		case ON_WITH_BEARING:
			if(mTrackingMode == TrackingButton.STATE.OFF) {
				mProvider = mLocationManager.getBestProvider(mCriteria, true);
				mLocationManager.requestLocationUpdates(mProvider, 0L, 0.0f, mLocationListener);
				
				Location location = mLocationManager.getLastKnownLocation(mProvider);
				Log.i("PROVIDER", mProvider + " " + location);
				if(location != null) {
					mLocationChangedListener.onLocationChanged(location);
					updateCameraWithoutBearingAndTilt(location.getLatitude(), location.getLongitude(), 0.0f);
				}
			} // end if
			
			if(state == TrackingButton.STATE.ON_WITH_BEARING) {
				mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
				mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI);
			} // end if
			break;
		} // end switch
		
		mTrackingMode = state;
	} // end setTrackingMode
	
	/**
	 * 두 LatLng 객체 간 거리를 구한다.
	 * @param l1 LatLng 객체
	 * @param l2 LatLng 객체
	 * @return 두 객체 간 거리 (meter)
	 */
	private float distanceBetweenTwoLatLngs(LatLng l1, LatLng l2) {
		Location loc1 = new Location(mProvider);
		loc1.setLatitude(l1.latitude);
		loc1.setLongitude(l1.longitude);
		Location loc2 = new Location(mProvider);
		loc2.setLatitude(l2.latitude);
		loc2.setLongitude(l2.longitude);
		
		return Math.abs(loc1.distanceTo(loc2));
	} // end distanceBetweenTwoLatLngs
	
	/**
	 * 마커의 아이디로 해당 마커를 갖고 있는 Place 객체를 찾는다.
	 * Place 객체를 관리하는 LinkedList들을 HashMap으로 바꿀까도 생각해보다가,
	 * (key를 마커의 아이디로 설정해버리면 찾기 편할테니까)
	 * Place 객체들의 숫자가 많은 편이 아니고 또 계속 들락날락 하기 때문에
	 * 일단은 LinkedList로 유지하고,
	 * Place 객체들의 마커 아이디값을 일일이 확인하는 방법으로 해당 Place 객체를 찾는다.
	 * @param markerId
	 * @return
	 */
	private Place findPlaceByMarkerId(String markerId) {
		for(Place place : mPlaces) {
			if(place.getMarkerId().equals(markerId)) {
				return place;
			} // end if
		} // end for
		return null;
	} // end findPlaceByMarkerId
	
	/**
	 * 검색된 장소 정보들을 토대로 마커들을 만든다.
	 * @param datas 검색된 장소 정보들
	 */
	private void setMarkers(Place[] datas) {
		for(Place data : datas) {
			boolean isValid = true;
			for(Place place : mPlaces) {
				if(place.getPosition().equals(data.getPosition()) && place.getName().equals(data.getName())) {
					isValid = false;
					break;
				} // end if
			} // end for
			
			if(isValid) {
				Bitmap mark = mMarkerIcons.get("empty");
				
				if(mFilteringKeyword != null && !mFilteringKeyword.equals("")) {
					mark = mMarkerIcons.get(mFilteringKeyword);
				} else {
					String name = data.getName();
					
					Iterator<String> iIcons = mMarkerIcons.keySet().iterator();
					while(iIcons.hasNext()) {
						String key = iIcons.next();
						if(name.contains(key)) {
							mark = mMarkerIcons.get(key);
							break;
						} // end if
					} // end while
				} // end if
				
				data.setMarker(mMap, mark);
				mPlaces.add(data);
			} // end if
		} // end for
	} // end setMarkers
	
	///////////
	//////////
	/////////   event listener & callback method
	
	/**
	 * 메뉴의 아이템을 클릭했을 때의 콜백 메서드.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.notice:
			Intent notice = new Intent(MainActivity.this, NoticeActivity.class);
			startActivity(notice);
			return true;
		case R.id.faq:
			Intent faq = new Intent(MainActivity.this, FAQActivity.class);
			startActivity(faq);
			return true;
		case R.id.devinfo:
			Intent devinfo = new Intent(MainActivity.this, DevInfoActivity.class);
			startActivity(devinfo);
			return true;
		} // end switch
		return false;
	} // end onOptionsItemSelected
	
	/**
	 * [현재 위치 추적] 상태 설정을 변경했을 때의 이벤트 리스너 콜백 메서드.
	 * 정확히는 [현재 위치 추적] 상태를 변경하라는 입력이 (TrackingButton을 통해) 들어왔을 때의 콜백 메서드. 
	 * setTrackingMode(MyLocationTrackingMode)를 호출한다.
	 */
	@Override
	public void onTrackingModeChanged(TrackingButton.STATE trackingMode) {
		setTrackingMode(trackingMode);
	} // end onTrackingModeChanged
	
	/**
	 * 메뉴 프래그먼트에서 버튼이 눌렸을 때의 콜백 메서드.
	 * 단 트래킹 메뉴 버튼의 클릭 이벤트는 TrackingModeButton.onTrackingModeChanged에서 처리한다.
	 */
	@Override
	public void onFragmentButtonClick(MainMenuFragment.BUTTONID id) {
		Intent intent;
		
		switch(id)
		{
		case FILTERING: // 여기에 Intent를 넣어줘야함.
			intent = new Intent(MainActivity.this, FilteringActivity.class);
			intent.putExtra(FilteringActivity.KEY_TYPE, mFilteringType);
			startActivityForResult(intent, BDApp.ACTION_FILTERING);
			break;
		default:
			break;
		} // end switch
	} // end onFragmentButtonClick
	
	/**
	 * 장소 검색이 끝났을 때의 이벤트 리스너 콜백 메서드.
	 * 현재 장소와 검색 반경 이상으로 멀어진 기존의 검색 결과들을 모두 지우고, 추가된 검색 장소에 마커를 꽂는다.
	 */
	@Override
	public void onCompleteSearchingPlaces(Place[] datas) {
		Log.i("PLACE", "---------------------------");
		Iterator<Place> iPlaces = mPlaces.iterator();
		while(iPlaces.hasNext()) {
			Place place = iPlaces.next();
			Log.i("PLACE", place.getName());
			
			if(distanceBetweenTwoLatLngs(place.getPosition(), mCameraPosition.target) > BDApp.SEARCH_RADIUS) {
				place.remove();
				iPlaces.remove();
			} // end if
		} // end for
		
		Log.i("PLACE", "========================");
		setMarkers(datas);
	} // end onSearchPlacesCompleted
	
	/**
	 * 추가적인 장소 검색이 끝났을 때의 이벤트 리스너 콜백 메서드.
	 * 검색 장소에 마커를 꽂는다.
	 */
	@Override
	public void onCompleteAdditionalSearchingPlaces(Place[] datas) {
		setMarkers(datas);
	} // end onCompleteAdditionalSearchingPlaces
	
	/**
	 * 장소 세부 정보 요청이 끝났을 때의 이벤트 리스너 콜백 메서드.
	 * 마커의 인포 윈도우에 세부 정보를 붙인다.
	 */
	@Override
	public void onCompleteFindOutDetail(Place.PlaceDetailData detail) {
		mPlaceSelected.setDetails(detail);
	} // end onCompleteFindOutDetail
	
	/**
	 * 장소 검색이 실패했을 때의 콜백 메서드.
	 * 현재는 인터넷 연결에 실패했을 경우에만 호출되며, 호출되면 간략한 메시지를 띄우며 어플리케이션을 종료시킨다.
	 * (왜냐면, 인터넷 연결이 되지 않는 상태에서는 이 어플이 실행되어 있을 까닭이 없기 때문이다.)
	 */
	@Override
	public void onFailSearching() {
		Toast.makeText(this, ERROR_FAIL_SEARCHING, Toast.LENGTH_LONG).show();
		finish();
	} // end onFailSearching
	
	/**
	 * 맵 카메라가 바뀌었을 때의 이벤트 리스너.
	 * 카메라의 위치 뿐만 아니라 방향, 각도, 확대/축소 변경 시에도 적용된다.
	 */
	private final GoogleMap.OnCameraChangeListener mCameraChangeListener = new GoogleMap.OnCameraChangeListener() {
		/**
		 * 맵 카메라가 바뀌었을 때의 콜백 메서드. 하는 일로는
		 * 1. [현재 위치 추적] 상태가 켜져 있는데도 (위치 관리자가 찾아낸) 현재 위치와 지도에 표시된 현재 위치가 다르다면 사용자가 위치 추적 중 임의로 위치를 바꿨다고 판단, [현재 위치 추적] 상태를 끈다.
		 * 2. 카메라의 확대/축소 레벨이 설정값(MIN_ZOOM_LEVEL_FOR_SEARCHING)보다 작아지면 모든 마커를 보이지 않게 하고, 커지면 보이지 않고 있던 마커들을 다시 보이게 하거나 변경된 위치에 맞게 다시 장소 검색을 실시한다.
		 */
		@Override
		public void onCameraChange(CameraPosition position) {
			
			Log.i("CAMERA", "lat: " + position.target.latitude + "  lng: " + position.target.longitude + "  zoom: " + position.zoom);
			if(mTrackingMode != TrackingButton.STATE.OFF && mLastLocationPosition != null && distanceBetweenTwoLatLngs(position.target, mLastLocationPosition) > 1.0f) {
				mMenuFragment.trackingModeOffManually();
			} // end if
			
			if(position.zoom >= MIN_ZOOM_LEVEL_FOR_SEARCHING) {
				if(mCameraPosition.zoom < MIN_ZOOM_LEVEL_FOR_SEARCHING) {
					for(Place place : mPlaces) {
						place.setVisible(true);
					} // end for
				} // end if
				
				if(mLastSearchingPosition == null || distanceBetweenTwoLatLngs(mLastSearchingPosition, position.target) > DISTANCE_FOR_RE_SEARCH) {
					SearchManager.TYPES type;
					switch(mFilteringType) {
					case FilteringActivity.ATM:
						type = SearchManager.TYPES.ATM;
						break;
					case FilteringActivity.CS:
					default:
						type = SearchManager.TYPES.CS;
						break;
					} // end switch
					
					mSearcher.requestNearbyPlaces(type, position.target.latitude, position.target.longitude, BDApp.SEARCH_RADIUS, mFilteringKeyword);
					if(mFilteringKeyword.equals(BDApp.KEYWORD_GS25)) {
						mSearcher.requestAdditionalNearbyPlaces(type, position.target.latitude, position.target.longitude, BDApp.SEARCH_RADIUS, BDApp.KEYWORD_GS25_2);
					} else if(mFilteringKeyword.equals(BDApp.KEYWORD_CU)) {
						mSearcher.requestAdditionalNearbyPlaces(type, position.target.latitude, position.target.longitude, BDApp.SEARCH_RADIUS, BDApp.KEYWORD_CU_2);
					}
					mLastSearchingPosition = position.target;
				} // end if
				
			} else {
				if(mCameraPosition.zoom >= MIN_ZOOM_LEVEL_FOR_SEARCHING) {
					for(Place place : mPlaces) {
						place.setVisible(false);
					} // end for
				} // end if
			} // end if
			
			mCameraPosition = position;
		} // end onCamaraChange
	}; // end mCameraChangeListener
	
	/**
	 * 마커를 클릭했을 시의 이벤트 리스너.
	 */
	private final GoogleMap.OnMarkerClickListener mMarkerClickListener = new GoogleMap.OnMarkerClickListener() {
		/**
		 * 마커를 클릭했을 시의 콜백 메서드
		 */
		@Override
		public boolean onMarkerClick(Marker marker) {
			mPlaceSelected = findPlaceByMarkerId(marker.getId());
			if(mPlaceSelected != null && !mPlaceSelected.isDetail()) {
				mSearcher.requestPlaceDetail(mPlaceSelected.getReference());
			} // end if
			marker.showInfoWindow();
			
			return true;
		} // end onMarkerClick
		
	}; // end mMarkerClickListener
	
	/**
	 * 맵을 클릭했을 때의 이벤트 리스너.
	 */
	private final GoogleMap.OnMapClickListener mMapClickListener = new GoogleMap.OnMapClickListener() {
		/**
		 * 맵을 클릭했을 때의 콜백 메서드.
		 * 잉여다. 하는 일 없다. 그렇다면 나는 이것을 왜 만들어 놓은 것일까. 대체 왜. 여하튼 지우지 않을 것이다. 내 마음이다.
		 */
		@Override
		public void onMapClick(LatLng point) {
			
		} // end onMapClick
	}; // end OnMapClickListener
	
	/**
	 * 맵을 길게 클릭했을 때의 이벤트 리스너.
	 */
	private final GoogleMap.OnMapLongClickListener mMapLongClickListener = new GoogleMap.OnMapLongClickListener() {
		/**
		 * 맵을 길게 클릭했을 때의 콜백 메서드.
		 * 이놈도 잉여다. 이놈도 그렇고 mMapClickListener도 그렇고, 사실 내가 원했던 것은 드래그 리스너였다. 하지만 드래그 리스너는 api레벨이 높았기에 이 두놈으로 어떻게든 대체해보려고 했다.
		 * 하지만 실패했다. 
		 */
		@Override
		public void onMapLongClick(LatLng point) {
			
		} // end onMapLongClick
	}; // end OnMapLongClickListener
	
	/**
	 * [현재 위치 추적] 기능 활성화/비활성화 시의 이벤트 리스너.
	 */
	private final LocationSource mLocationSource = new LocationSource() {
		/**
		 * [현재 위치 추적] 기능 활성화 시의 콜백 메서드.
		 * [현재 위치 추적] 시의 구글맵의 기본 이벤트를 발생시키기 위해, 해당 (구글맵)이벤트 리스너의 참조를 저장해둔다.
		 */
		@Override
		public void activate(LocationSource.OnLocationChangedListener listener) {
			mLocationChangedListener = listener;
		} // end activate

		/**
		 * 현재 위치 기능 비활성화 시의 콜백 메서드.
		 * activate에서 저장해두었던 리스너를 삭제(=null)한다.
		 */
		@Override
		public void deactivate() {
			mLocationChangedListener = null;
		} // end deactivate
		
	}; // end mLocationSource
	
	/**
	 * 위치 제공자와 관련된 이벤트 리스너.
	 */
	private final LocationListener mLocationListener = new LocationListener() {
		/**
		 * 위치 제공자가 위치 변경을 알려올 때의 콜백 메서드.
		 * 지도 카메라의 위치를 변경한다.
		 */
		@Override
		public void onLocationChanged(Location location) {
			Log.i("LOCATION", location.getLatitude() + " " + location.getLongitude());
			if (mLocationChangedListener != null) {
				Log.i("LOCATION", "in");
				mLocationChangedListener.onLocationChanged(location);
				
				updateCameraWithoutBearingAndTilt(location.getLatitude(), location.getLongitude(), 0.0f);
			} //end if
		} // end onLocationChanged
		
		/** 
		 * 위치 제공자의 상태가 사용 불가능이 되었을 때의 콜백 메서드.
		 * [현재 위치 추적] 상태를 꺼버린다. 사용자는 다시 [현재 위치 추적] 기능을 켰을 때 활성화된 다른 위치 제공자와의 연결을 기대할 수 있을 것이다.
		 */
		@Override
		public void onProviderDisabled(String provider) {
			Toast.makeText(MainActivity.this, provider + ERROR_DISABLE_LOCATION_PROVIDER, Toast.LENGTH_LONG).show();
			mMenuFragment.trackingModeOffManually();
			Log.e("LOCATION", provider + " " + "diabled");
		} // end onProviderDisabled
		
		/** 사용하지 않는다. 디버그용. */
		@Override
		public void onProviderEnabled(String provider) {
			Log.e("LOCATION", provider + " " + "enabled");
		} // end onProviderEnabled
		
		/** 사용하지 않는다. 디버그용. */
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			String sStatus = "";
			switch(status) {
			case LocationProvider.OUT_OF_SERVICE:
				sStatus = "Out of Service";
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				sStatus = "temporarly unavailable";
				break;
			case LocationProvider.AVAILABLE:
				sStatus = "Available";
				break;
			} // end switch
			Log.e("LOCATION", provider + " " + sStatus);
		} // end onStatusChanged
		
	}; // end mLocationListener
	
	/**
	 * 센서에 이벤트가 생길때의 이벤트 리스너.
	 */
	private final SensorEventListener mSensorListener = new SensorEventListener() {
		/** 사용하지 않는다. */
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			
		} // end onAccuracyChanged
		
		/**
		 * 센서가 변경되었을 때의 콜백 메서드.
		 * 현재 방향 값을 구글맵 객체에 전달해 구글맵의 방향을 변경하는데, 실행 조건은
		 * 1. 어플리케이션이 실행된 이래로 중력가속도 센서값의 변경과 자기장 센서값의 변경이 모두 일어난 적이 있어야한다.
		 * 2. (지나치게 자주 호출되기 때문에) 특정 값 (SENSOR_LISTENER_CALL_FREQUENCY) 만큼의 회수만큼 호출될 때마다 작동한다.
		 * 3. 방향 값이 특정 값(DEGREE_FOR_ROTATE_BEARING) 이상 변경되어야만 한다.
		 */
		@Override
		public void onSensorChanged(SensorEvent event) {
			switch(event.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER:
				mGravity = event.values.clone();
				break;
			case Sensor.TYPE_MAGNETIC_FIELD:
				mGeoMagnetic = event.values.clone();
				break;
			} // end switch
			
			if(mGravity != null && mGeoMagnetic != null && (mCountForFrequency++ % SENSOR_LISTENER_CALL_FREQUENCY == 0)) {
				float[] mr = new float[9];
				float[] mi = new float[9];
				float[] mv = new float[3];
				SensorManager.getRotationMatrix(mr, mi, mGravity, mGeoMagnetic);
				float inclination = SensorManager.getInclination(mi);
				SensorManager.getOrientation(mr, mv);
				Log.i("SENSOR", "mv[0]: " + mv[0]);
				/*
				float azimuth = 360 - (mv[0] * 180 / (float)Math.PI);
				float bearing = mCameraPosition.bearing;
				float min = DEGREE_FOR_ROTATE_BEARING;
	
				//Log.i("SENSOR", "azimuth: " + Math.toDegrees(mv[0]) + "  bearing: " + bearing + "  min: " + min);
				
				updateCamera(0.0f, 0.0f, azimuth, 0.0f, 0.0f);
				
				if((bearing < min && (azimuth > bearing + min && azimuth < bearing - min + 360)) ||
						(bearing > 360 - min && (azimuth < bearing - min && azimuth > bearing + min - 360)) ||
						(azimuth > bearing + min || azimuth < bearing - min)) {
					updateCamera(0.0f, 0.0f, azimuth, inclination, 0.0f);
				} // end if
*/				
				
				float azimuth = mv[0] * 180 / (float)Math.PI + 180;
				float bearing = mCameraPosition.bearing;
				float min = DEGREE_FOR_ROTATE_BEARING;
				
				Log.i("SENSOR", "azimuth: " + azimuth + "  bearing: " + bearing + "  min: " + min);
				
				if((bearing < min && (azimuth > bearing + min && azimuth < bearing - min + 360)) ||
						(bearing > 360 - min && (azimuth < bearing - min && azimuth > bearing + min - 360)) ||
						(azimuth > bearing + min || azimuth < bearing - min)) {
					Log.i("SENSOR", "call");
					updateCamera(0.0f, 0.0f, azimuth, inclination, 0.0f);
				}

			} // end if
		} // end onSensorChanged
	}; // end mSensorListener
	
	// gcm에 필요한 SERVER_URL, SENDER_ID가 설정 되어있는지 확인
	private void checkNotNull(Object reference, String name) {
		if (reference == null) {
			throw new NullPointerException(getString(R.string.error_config,
					name));
		}
	}
	
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			
			RankingAsync rankingAsync = new RankingAsync();
			rankingAsync.execute("used_function=gcm&user_id=" + DEVICE_ID);
		}
	};
	
	// 유저 정보를 저장 클래스
	public class RegistDeviceIDAsync extends AsyncTask<String, String, String> {
		private final String className = "RegistDeviceIDAsync";
		
		private Client2Server client2Server = Client2Server.getInstance();
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
		@Override
		protected String doInBackground(String... params) {
			Log.i( className + " @ param0", params[0]);

			// HttpClass에서 해당 url을 실행한다.
			String parameter = params[0];
			String return_value = client2Server.registUserDeviceID(parameter);
					
			// 쓰레드 cancel을 위한 코드
			int count = params.length;
			for (int i = 0; i < count; i++) {
	             if (isCancelled()) break;
	        }
			return return_value;
		}
		
		// doInBackground 함수 다음에 실행되는 함수, text1에 내용을 저장하여 activity에 뿌린다.
		@Override
		protected void onPostExecute(String result) {
			Log.i( className + " @ onPostExecute", result);
			
			super.onPostExecute(result);
		}
		
		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
	}
	
	// Ranking 정보를 저장하는 AsyncTask
	public class RankingAsync extends AsyncTask<String, String, String> {

		private final String className = "RankingAsync";
		private Client2Server client2Server = Client2Server.getInstance();
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected String doInBackground(String... params) {
			Log.i( className + " @ param0", params[0]);

			// HttpClass에서 해당 url을 실행한다.
			String parameter = params[0];
			String return_value = client2Server.saveRanking(parameter);
					// 쓰레드 cancel을 위한 코드
			int count = params.length;
			for (int i = 0; i < count; i++) {
	             if (isCancelled()) break;
	        }
			return return_value;
		}
		
		// doInBackground 함수 다음에 실행되는 함수, text1에 내용을 저장하여 activity에 뿌린다.
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}
		
		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
	}
}

