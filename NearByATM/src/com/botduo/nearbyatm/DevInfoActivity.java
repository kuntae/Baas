package com.botduo.nearbyatm;


import static com.example.bacassample3.util.CommonUtilities.DEVICE_ID;

import com.botduo.nearbyatm.FAQActivity.RankingAsync;
import com.example.bacassample3.util.Client2Server;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.Log;
import android.view.*;

public class DevInfoActivity extends Activity{
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.devinfo);
	}
	
	@Override
	protected void onResume() {

		Log.i("rank", "rank");
		
		RankingAsync rankingAsync = new RankingAsync();
		rankingAsync.execute("used_function=devinfo&user_id=" + DEVICE_ID);
		
		super.onResume();
	}
	
	/**
	 * 메뉴 추가 부분
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		
		return true;
	} // end onCreateOptionsMenu
	/**
	 * 추가 된 메뉴를 선택했을 때의 동작
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.main:
			Intent main = new Intent(this, MainActivity.class);
			startActivity(main);
			return true;
		case R.id.notice:
			Intent notice = new Intent(this, NoticeActivity.class);
			startActivity(notice);
			return true;
		case R.id.faq:
			Intent faq = new Intent(this, FAQActivity.class);
			startActivity(faq);
			return true;

		} // end switch
		return false;
	} // end onOptionsItemSelected
	
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
