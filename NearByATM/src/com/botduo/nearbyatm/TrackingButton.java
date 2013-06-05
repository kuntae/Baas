package com.botduo.nearbyatm;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * [현재 위치 추적] 상태를 표시/제어하는 버튼
 * @author ricale
 *
 */
public class TrackingButton extends Button {
	///////////
	//////////
	/////////   enum
	
	/**
	 * [현재 위치 추적] 상태를 표현하는 이넘 클래스
	 */
	public enum STATE {
		/** 꺼진 상태 */
		OFF,
		/** 켜진 상태  */
		ON_WITHOUT_BEARING,
		/** 켜진 상태. 방향 추적도 켜져 있는 상태 */
		ON_WITH_BEARING
	} // end MyLocationTrackingMode
	
	///////////
	//////////
	/////////   variable
	
	/** 현재 위치 추적 상태 */
	private STATE mMode;
	
	///////////
	//////////
	/////////   constructor
	
	/**
	 * 생성자
	 */
	public TrackingButton(Context context) {
		this(context, null);
	} // end TrackingModeButton(Context context)
	
	/**
	 * 생성자
	 */
	public TrackingButton(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.buttonStyle);
	} // end TrackingModeButton(Context context, AttributeSet attrs)
	
	/**
	 * 생성자
	 */
	public TrackingButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mMode = STATE.OFF;
	} // end TrackingModeButton(Context context, AttributeSet attrs, int defStyle)

	
	///////////
	//////////
	/////////   methods
	
	/**
	 * [현재 위치 추적] 상태를 변경한다.
	 * STATE.OFF,
	 * STATE.ON_WITHOUT_BEARING,
	 * STATE.ON_WITH_BEARING 순으로 변경한다.
	 * @return 변경된 [현재 위치 추적] 상태
	 */
	public STATE changeModeInOrder() {
		switch(mMode) {
		case OFF:
			mMode = STATE.ON_WITHOUT_BEARING;
			break;
		case ON_WITHOUT_BEARING:
			mMode = STATE.ON_WITH_BEARING;
			break;
		case ON_WITH_BEARING:
			mMode = STATE.OFF;
			break;
		} // end switch
		
		setLabel();
		return mMode;
	} // end changModeInOrder
	
	/**
	 * [현재 위치 추적] 상태를 끈다.
	 */
	public STATE setModeOffManually() {
		mMode = STATE.OFF;
		setLabel();
		return STATE.OFF;
	} // end setMode
	
	/**
	 * 현재 위치 추적 상태 변경 시 버튼에 표시되는 문자열을 변경한다.
	 */
	private void setLabel() {
		switch(mMode) {
		case OFF:
			setBackgroundResource(R.drawable.current_location_button_default);
			break;
		case ON_WITHOUT_BEARING:
			setBackgroundResource(R.drawable.current_location_button_click);
			break;
		case ON_WITH_BEARING:
			setBackgroundResource(R.drawable.current_location_button_compass);
			break;
		} // end switch
	} // end setLabel
	
	///////////
	//////////
	/////////   interface
	
	/**
	 * 현재 위치 추적 상태의 변경에 대한 인터페이스
	 * @author ricale
	 *
	 */
	public interface OnTrackingModeChangeListener {
		/**
		 * 현재 위치 추적 상태가 변경될 때 호출될 콜백 메서드
		 * @param trackingMode 변경된 현재 위치 추적 모드
		 */
		public void onTrackingModeChanged(TrackingButton.STATE trackingMode);
	} // end ChangeTrackingModeListener
} // end TrackingModeButton