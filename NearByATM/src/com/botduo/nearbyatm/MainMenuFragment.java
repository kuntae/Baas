package com.botduo.nearbyatm;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * 메뉴로 쓰일 버튼을 관리하는 프래그먼트
 * @author ricale
 *
 */
public class MainMenuFragment extends Fragment {
	///////////
	//////////
	/////////   enum
	
	/**
	 * OnFragmentButtonClickListener에서 현재 눌린 버튼을 전달하기 위한 구분자 enum
	 */
	public static enum BUTTONID {
		/** [현재 위치 추적] 버튼 */
		TRACKING,
		/** 필터링 버튼 */
		FILTERING,
		/** 증강현실 버튼 */
		AUGMENTED
	}
	
	///////////
	//////////
	/////////   variable
	
	/** 현재 위치 추적 설정에 관련된 입력을 받는 버튼 객체 */
	private TrackingButton mTrackingButton;
	
	private Button mFilteringButton;
	
	///////////
	//////////
	/////////   lifecycle callback method
	
	/**
	 * 프래그먼트 생성 시 뷰를 전개하여 반환하는 콜백 메서드
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.mainmenu_fragment, null);
		mTrackingButton = (TrackingButton)v.findViewById(R.id.mainmenu_fragment_bttracking);		
		mFilteringButton = (Button)v.findViewById(R.id.mainmenu_fragment_btfiltering);

		mTrackingButton.setOnClickListener(mOnClickListener);
		mFilteringButton.setOnClickListener(mOnClickListener);
		
		return v;
	} // end onCreateView
	
	///////////
	//////////
	/////////   methods
	
	/**
	 * [현재 위치 추적] 상태를 끈다.
	 * TrackingModeButton.setModeOffManually()의 레퍼 메서드
	 */
	public void trackingModeOffManually() {
		if(TrackingButton.OnTrackingModeChangeListener.class.isAssignableFrom(getActivity().getClass()))
			((TrackingButton.OnTrackingModeChangeListener)getActivity()).onTrackingModeChanged(mTrackingButton.setModeOffManually());
	} // end trackingModeOffManually
	
	public void setFilteringButtonBackground(int resid) {
		mFilteringButton.setBackgroundResource(resid);
	}
	
	///////////
	//////////
	/////////   event listener & callback method
	
	/**
	 * 버튼을 눌렀을 때의 이벤트 리스너
	 */
	private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
		/**
		 * 버튼을 눌렀을 때의 콜백 메서드
		 */
		public void onClick(View v) {
			Activity parent = getActivity();
			
			switch(v.getId()) {
			case R.id.mainmenu_fragment_bttracking:
				if(TrackingButton.OnTrackingModeChangeListener.class.isAssignableFrom(parent.getClass()))
					((TrackingButton.OnTrackingModeChangeListener)parent).onTrackingModeChanged(((TrackingButton)v).changeModeInOrder());
				break;
			case R.id.mainmenu_fragment_btfiltering:
				if(OnFragmentButtonClickListener.class.isAssignableFrom(parent.getClass()))
					((OnFragmentButtonClickListener)getActivity()).onFragmentButtonClick(BUTTONID.FILTERING);
				break;
			} // end switch
		} // end onClick
	}; // end mOnClickListener
	
	///////////
	//////////
	/////////   interface
	
	/**
	 * 프래그먼트의 버튼이 눌렸을 때의 이벤트를 받는 이벤트 리스너.
	 * 이 리스너를 등록한 액티비티만이 이 이벤트를 받을 수 있다.
	 *
	 */
	public static interface OnFragmentButtonClickListener {
		/**
		 * 프래그먼트의 버튼이 눌렸을 때의 콜백 메서드
		 * @param id 버튼의 아이디
		 */
		public void onFragmentButtonClick(BUTTONID id);
	} // end OnFragmentButtonClickListener
} // end MainMenuFragment