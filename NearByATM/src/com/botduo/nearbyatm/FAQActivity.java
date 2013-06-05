package com.botduo.nearbyatm;

import static com.example.bacassample3.util.CommonUtilities.DEVICE_ID;

import java.util.*;

import com.botduo.nearbyatm.MainActivity.RankingAsync;
import com.example.bacassample3.util.Client2Server;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.Log;
import android.view.*;
import android.widget.*;

public class FAQActivity extends Activity {
	/** 확장리스트뷰 선언 */
	private ExpandableListView mList;

	/** FAQ 제목 갯수 선언 */
	private String[] FAQTitle;
	/** FAQ 내용 갯수 선언 */
	private String[][] FAQBody = new String[][] { { "" }, { "" }, { "" }, }; // end FAQBody

	/** 1차원 배열로 가져온 내용을 2차원 배열에 넣기 위한 임시 배열 */
	private String[] temp;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.faq);

		/**
		 * FAQ 제목을 values/array.xml에서 가져오는 부분
		 */
		FAQTitle = getResources().getStringArray(R.array.faq_title);
		/**
		 * FAQ 내용을 values/array.xml에서 가져오는 부분
		 */
		temp = getResources().getStringArray(R.array.faq_body);

		mList = (ExpandableListView) findViewById(R.id.faq);

		List<Map<String, String>> FAQ_title = new ArrayList<Map<String, String>>();
		List<List<Map<String, String>>> FAQ_body = new ArrayList<List<Map<String, String>>>();
		for (int i = 0; i < FAQTitle.length; i++) {

			

			Map<String, String> title = new HashMap<String, String>();
			title.put("FAQtitle", FAQTitle[i]);
			FAQ_title.add(title);

			List<Map<String, String>> children = new ArrayList<Map<String, String>>();
			for (int j = 0; j < FAQBody[i].length; j++) {
				FAQBody[i][j] = temp[i];
				Map<String, String> body = new HashMap<String, String>();
				body.put("FAQbody", FAQBody[i][j]);
				children.add(body);
			}// end for j
			FAQ_body.add(children);
		}// end for i

		ExpandableListAdapter adapter = new SimpleExpandableListAdapter(
				this,
				FAQ_title, 
				R.layout.expandablelist,
				new String[] { "FAQtitle" },
				new int[] { android.R.id.text1 }, 
				FAQ_body,
				R.layout.expandablelist, 
				new String[] { "FAQbody" },
				new int[] { android.R.id.text1 }
		);// end adapter
		mList.setAdapter(adapter);
	}// end onCreate
	
	@Override
	protected void onResume() {

		RankingAsync rankingAsync = new RankingAsync();
		rankingAsync.execute("used_function=faq&user_id=" + DEVICE_ID);
		
		Log.i("rank", "rank");
		
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
