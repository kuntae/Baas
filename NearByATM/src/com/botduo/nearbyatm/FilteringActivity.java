package com.botduo.nearbyatm;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class FilteringActivity extends Activity {
	/** ATM과 편의점 view 선언 */
	private View ATMpage;

	/** ATM과 편의점 view 선언 */
	private View CSpage;

	private Button ATM_btn, CS_btn;

	/** MainActivity로 Intent를 넘길때의 Tag */
	public static final String KEY_KEYWORD = "value";
	/** ATM버튼인지 CS버튼인지 구별하기 위한 TYPE, MainActivity로 Intent를 넘길때의 Tag */
	public static final String KEY_TYPE = "type_value";
	/** ATM버튼 = 0을 넘겨줌. */
	public static final int ATM = 0;
	/** 편의점버튼 = 1을 넘겨줌. */
	public static final int CS = 1;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filteringmenu);

		ATMpage = findViewById(R.id.page1);
		CSpage = findViewById(R.id.page2);
		int type = getIntent().getIntExtra(KEY_TYPE, ATM);
		switch (type) {
		case ATM:

			break;
		case CS:

			break;
		}
		ATM_btn = (Button) findViewById(R.id.ATM);
		CS_btn = (Button) findViewById(R.id.CS);
		/**
		 * 은행 버튼과 편의점의 초기 상태 설정
		 */
		ATM_btn.setEnabled(false);
		CS_btn.setEnabled(true);
		/**
		 * 버튼이 setEnabled(false)상태가 되어도 TextColor를 유지하기 위한 코드
		 */
		ATM_btn.setTextColor(Color.BLACK);
		CS_btn.setTextColor(Color.BLACK);
		findViewById(R.id.ATM).setOnClickListener(mClickListener);
		findViewById(R.id.CS).setOnClickListener(mClickListener);
	}

	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.atm1:
			intent.putExtra(KEY_KEYWORD, BDApp.KEYWORD_KB);
			intent.putExtra(KEY_TYPE, ATM);
			break;
		case R.id.atm2:
			intent.putExtra(KEY_KEYWORD, BDApp.KEYWORD_NH);
			intent.putExtra(KEY_TYPE, ATM);
			break;
		case R.id.atm3:
			intent.putExtra(KEY_KEYWORD, BDApp.KEYWORD_WOORI);
			intent.putExtra(KEY_TYPE, ATM);
			break;
		case R.id.atm4:
			intent.putExtra(KEY_KEYWORD, BDApp.KEYWORD_SC);
			intent.putExtra(KEY_TYPE, ATM);
			break;
		case R.id.atm5:
			intent.putExtra(KEY_KEYWORD, BDApp.KEYWORD_IBK);
			intent.putExtra(KEY_TYPE, ATM);
			break;
		case R.id.atm6:
			intent.putExtra(KEY_KEYWORD, BDApp.KEYWORD_KEB);
			intent.putExtra(KEY_TYPE, ATM);
			break;
		case R.id.atm7:
			intent.putExtra(KEY_KEYWORD, BDApp.KEYWORD_SH);
			intent.putExtra(KEY_TYPE, ATM);
			break;
		case R.id.atm8:
			intent.putExtra(KEY_KEYWORD, BDApp.KEYWORD_SHINHAN);
			intent.putExtra(KEY_TYPE, ATM);
			break;
		case R.id.atm9:
			intent.putExtra(KEY_KEYWORD, BDApp.KEYWORD_CB);
			intent.putExtra(KEY_TYPE, ATM);
			break;
		case R.id.atm10:
			intent.putExtra(KEY_KEYWORD, BDApp.KEYWORD_DGB);
			intent.putExtra(KEY_TYPE, ATM);
			break;
		case R.id.atm11:
			intent.putExtra(KEY_KEYWORD, BDApp.KEYWORD_BB);
			intent.putExtra(KEY_TYPE, ATM);
			break;
		case R.id.atm12:
			intent.putExtra(KEY_KEYWORD, BDApp.KEYWORD_KDB);
			intent.putExtra(KEY_TYPE, ATM);
			break;
		case R.id.atm13:
			intent.putExtra(KEY_KEYWORD, BDApp.KEYWORD_GJB);
			intent.putExtra(KEY_TYPE, ATM);
			break;
		case R.id.atm14:
			intent.putExtra(KEY_KEYWORD, BDApp.KEYWORD_JJB);
			intent.putExtra(KEY_TYPE, ATM);
			break;
		case R.id.atm15:
			intent.putExtra(KEY_KEYWORD, BDApp.KEYWORD_KJB);
			intent.putExtra(KEY_TYPE, ATM);
			break;
		case R.id.atm16:
			intent.putExtra(KEY_KEYWORD, BDApp.KEYWORD_KNB);
			intent.putExtra(KEY_TYPE, ATM);
			break;
		case R.id.atm17:
			intent.putExtra(KEY_KEYWORD, BDApp.KEYWORD_HANA);
			intent.putExtra(KEY_TYPE, ATM);
			break;
		case R.id.atm18:
			intent.putExtra(KEY_KEYWORD, BDApp.KEYWORD_POST);
			intent.putExtra(KEY_TYPE, ATM);
			break;
		case R.id.atm19:
			intent.putExtra(KEY_KEYWORD, BDApp.KEYWORD_KFCC);
			intent.putExtra(KEY_TYPE, ATM);
			break;
		case R.id.atm20:
			intent.putExtra(KEY_KEYWORD, BDApp.KEYWORD_SHINHYUP);
			intent.putExtra(KEY_TYPE, ATM);
			break;
		case R.id.cs1:
			intent.putExtra(KEY_KEYWORD, BDApp.KEYWORD_GS25);
			intent.putExtra(KEY_TYPE, CS);
			break;
		case R.id.cs2:
			intent.putExtra(KEY_KEYWORD, BDApp.KEYWORD_SEVENELEVEN);
			intent.putExtra(KEY_TYPE, CS);
			break;
		case R.id.cs3:
			intent.putExtra(KEY_KEYWORD, BDApp.KEYWORD_MINISTOP);
			intent.putExtra(KEY_TYPE, CS);
			break;
		case R.id.cs4:
			intent.putExtra(KEY_KEYWORD, BDApp.KEYWORD_CU);
			intent.putExtra(KEY_TYPE, CS);
			break;
		case R.id.cs5:
			intent.putExtra(KEY_KEYWORD, BDApp.KEYWORD_BYTHEWAY);
			intent.putExtra(KEY_TYPE, CS);
			break;
		case R.id.atmall:
			intent.putExtra(KEY_KEYWORD, "");
			intent.putExtra(KEY_TYPE, ATM);
			break;
		case R.id.csall:
			intent.putExtra(KEY_KEYWORD, "");
			intent.putExtra(KEY_TYPE, CS);
			break;
		} // end switch

		setResult(RESULT_OK, intent);
		finish();
	}

	/**
	 * FilteringActivity 버튼 이벤트리스너
	 */
	Button.OnClickListener mClickListener = new Button.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ATM:
				ATM_btn.setEnabled(false);
				CS_btn.setEnabled(true);
				ATMpage.setVisibility(View.VISIBLE);
				CSpage.setVisibility(View.INVISIBLE);
				break;
			case R.id.CS:
				ATM_btn.setEnabled(true);
				CS_btn.setEnabled(false);
				ATMpage.setVisibility(View.INVISIBLE);
				CSpage.setVisibility(View.VISIBLE);
				break;
			}
		}
	}; // end mClickListener
}