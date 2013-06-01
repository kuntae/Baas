package com.example.bacassample3;

import com.example.bacassample3.tab1.Tab1ActivityGroup;
import com.example.bacassample3.tab2.Tab2ActivityGroup;
import com.example.bacassample3.tab3.Tab3ActivityGroup;
import com.example.bacassample3.util.Client2Server;
import com.google.android.gcm.GCMRegistrar;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.Toast;

import static com.example.bacassample3.util.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.example.bacassample3.util.CommonUtilities.EXTRA_MESSAGE;
import static com.example.bacassample3.util.CommonUtilities.SENDER_ID;
import static com.example.bacassample3.util.CommonUtilities.SERVER_URL;
import static com.example.bacassample3.util.CommonUtilities.DEVICE_ID;

public class MainActivity extends TabActivity {
	private String className = "MainActivity";

	private TabHost tabHost;											// 탭 호스트
	private TabWidget tabWidget;										// 탭 위젯
	
	private AsyncTask<Void, Void, Void> mRegisterTask;		// gcm 등록
	private Toast toast;													// gcm 메시지
	
//	private RegistDeviceIDAsync registDeviceIDAsync;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(className + " @ onCreate", "메인 실행");
		
		setContentView(R.layout.activity_main);
		
		tabHost = getTabHost();
		tabWidget = tabHost.getTabWidget();
		
		Intent intentTab1 = new Intent(this, Tab1ActivityGroup.class);
		Intent intentTab2 = new Intent(this, Tab2ActivityGroup.class);
		Intent intentTab3 = new Intent(this, Tab3ActivityGroup.class);
		
		TabSpec tabSpac1 = tabHost.newTabSpec("user").setIndicator("사용자", getResources().getDrawable(R.drawable.star2));
		tabSpac1.setContent(intentTab1);
		tabHost.addTab(tabSpac1);
		
		TabSpec tabSpac2 = tabHost.newTabSpec("location").setIndicator("지역정보", getResources().getDrawable(R.drawable.star2));
		tabSpac2.setContent(intentTab2);
		tabHost.addTab(tabSpac2);
		
		TabSpec tabSpac3 = tabHost.newTabSpec("setting").setIndicator("설정", getResources().getDrawable(R.drawable.star2));
		tabSpac3.setContent(intentTab3);
		tabHost.addTab(tabSpac3);
		
		tabWidget.getChildAt(0).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Log.i(className + " @ onCreate", "tab1Click");
				tabWidget.setCurrentTab(0);
				tabHost.setCurrentTab(0);
			}
		});
		
		tabWidget.getChildAt(1).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Log.i(className + " @ onCreate", "tab1Click");
				tabWidget.setCurrentTab(1);
				tabHost.setCurrentTab(1);
			}
		});
		
		tabWidget.getChildAt(2).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Log.i(className + " @ onCreate", "tab1Click");
				tabWidget.setCurrentTab(2);
				tabHost.setCurrentTab(2);
			}
		});
		
		// gcm 작동 시키기
		checkNotNull(SERVER_URL, "SERVER_URL");
		checkNotNull(SENDER_ID, "SENDER_ID");
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);
		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);
		
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		
		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));
		final String regId = GCMRegistrar.getRegistrationId(this);
		
		DEVICE_ID = regId;
		
		if (regId.equals("")) {
			// Automatically registers application on startup.
			GCMRegistrar.register(this, SENDER_ID);
		} else {
			// Device is already registered on GCM, check server.
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				// Skips registration.
				toast = Toast.makeText(this, getString(R.string.already_registered), Toast.LENGTH_SHORT);
				toast.show();
			} else {
				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						boolean registered = ServerUtilities.register(context,
								regId);
						// At this point all attempts to register with the app
						// server failed, so we need to unregister the device
						// from GCM - the app will try to register again when
						// it is restarted. Note that GCM will send an
						// unregistered callback upon completion, but
						// GCMIntentService.onUnregistered() will ignore it.
						if (!registered) {
							GCMRegistrar.unregister(context);
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}

				};
				mRegisterTask.execute(null, null, null);
			}
		}
		
		Log.i(className + " @ onCreate", "regId=" + regId);
		
//		registDeviceIDAsync = new RegistDeviceIDAsync();
//		registDeviceIDAsync.execute("deviceid=" + regId);
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i(className + " @ onStop", "메인 STOP");
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	// gcm에 필요한 SERVER_URL, SENDER_ID가 설정 되어있는지 확인
	private void checkNotNull(Object reference, String name) {
		if (reference == null) {
			throw new NullPointerException(getString(R.string.error_config,
					name));
		}
	}
	
	
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);

			String parameters = "used_function=gcm&user_id=" + DEVICE_ID;
			RankingAsync rankingAsync = new RankingAsync();
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				rankingAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, parameters);
			else
				rankingAsync.execute(parameters);
		}
	};
	
	// Ranking 정보를 저장하는 AsyncTask
	public class RankingAsync extends AsyncTask<String, String, String> {

		private final String className = "RankingAsync";
		private Client2Server client2Server = Client2Server.getInstance();
		
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
//			this.cancel(true);
		}
		
		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
	}

}
