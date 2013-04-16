package com.example.httpconnection5;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	HttpClass httpClass;
	SearchAsync searchAsync;
	String query = "";
	String html = ""; 
	
	TextView text1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btnFunction1 = (Button)findViewById(R.id.function1);
		Button btnFunction2 = (Button)findViewById(R.id.function2);
		text1 = (TextView)findViewById(R.id.text1);
		
		btnFunction1.setOnClickListener(new function1OnClickListener());
		btnFunction2.setOnClickListener(new function2OnClickListener());
		httpClass = new HttpClass();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	public class function1OnClickListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			query = "http://113.198.80.234/mobile/rank?used_function='funtion1'&location='busan'&user_id='user00009'&app_id='app00001'";
			searchAsync = new SearchAsync();
			searchAsync.execute(query);
			searchAsync.cancel(true);
	
		}
	}
	
	public class function2OnClickListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			query = "http://113.198.80.234/mobile/rank?used_function='funtion2'&location='busan'&user_id='user00009'&app_id='app00001'";
			searchAsync = new SearchAsync();
			searchAsync.execute(query);
			searchAsync.cancel(true);
	
		}
	}
	
	// 역 이름을 이용해 역정보들을 얻어오기 위한 AsyncTask
	public class SearchAsync extends AsyncTask<String, String, String> {

		private String className = "SearchAsync";
		
		@Override
		protected String doInBackground(String... params) {
			Log.i( className + " @ param", params[0]);
			// HttpClass에서 해당 url을 실행한다.
			String return_value = httpClass.getHTML(params[0]);
			
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
			text1.setText(result);
		}
		
		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
	}
}
