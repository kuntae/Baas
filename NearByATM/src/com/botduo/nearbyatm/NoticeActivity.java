package com.botduo.nearbyatm;

import static com.example.bacassample3.util.CommonUtilities.DEVICE_ID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.botduo.nearbyatm.FAQActivity.RankingAsync;
import com.example.bacassample3.util.Client2Server;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;


public class NoticeActivity extends Activity{
	/** 확장 리스트뷰 선언 */
	private ExpandableListView mList;
	
	/** 공지사항 제목 갯수 선언 */
	private String[] NoticeTitle;
	/** 공지사항 내용 갯수 선언 */
	private String[][] NoticeBody = new String[][] {
			{""},{""},{""},
	}; // end NoticeBody
	
	/**
	 */
	private String[] temp;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notice);
		
		/**
		 * 공지사항 제목을 values/array.xml에서 가져오는 부분
		 */
		NoticeTitle = getResources().getStringArray(R.array.notice_title);
		temp = getResources().getStringArray(R.array.notice_body);
				
		mList = (ExpandableListView)findViewById(R.id.notice);

		List<Map<String, String>> Notice_title = new ArrayList<Map<String, String>>();
		List<List<Map<String, String>>> Notice_body = 
			new ArrayList<List<Map<String, String>>>();
		for (int i = 0; i < NoticeTitle.length; i++) {
			
			/**
			 * 공지사항 내용을 values/array.xml에서 가져오는 부분
			 */
			
			
			
			Map<String, String> title = new HashMap<String, String>();
			title.put("noticetitle", NoticeTitle[i]);
			Notice_title.add(title);

			List<Map<String, String>> children = new ArrayList<Map<String, String>>();
			for (int j = 0; j < NoticeBody[i].length; j++) {
				NoticeBody[i][j] = temp[i]; 
				Map<String, String> body = new HashMap<String, String>();
				body.put("noticebody", NoticeBody[i][j]);
				children.add(body);
			}// end for j
			Notice_body.add(children);
		}// end for i

		ExpandableListAdapter adapter = new SimpleExpandableListAdapter(
				this,
				Notice_title,
				R.layout.expandablelist,
				new String[] { "noticetitle" },
				new int[] { android.R.id.text1 },
				Notice_body,
				R.layout.expandablelist,
				new String[] { "noticebody" },
				new int[] { android.R.id.text1 }
		);//end adapter
		mList.setAdapter(adapter);
	}// end onCreate
	
	@Override
	protected void onResume() {

		Log.i("rank", "rank");
		
		RankingAsync rankingAsync = new RankingAsync();
		rankingAsync.execute("used_function=notice&user_id=" + DEVICE_ID);
		
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
	 * 추가 된 메뉴를 선택했을 때의 동작 메인화면으로 돌아가는 부분 추가
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.main:
			Intent main_map = new Intent(this, MainActivity.class);
			startActivity(main_map);
			return true;
		case R.id.faq:
			Intent faq = new Intent(this, FAQActivity.class);
			startActivity(faq);
			return true;
		case R.id.devinfo:
			Intent devinfo = new Intent(this, DevInfoActivity.class);
			startActivity(devinfo);
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
