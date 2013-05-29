package com.example.bacassample3.tab1;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.bacassample3.R;
import com.example.bacassample3.util.Client2Server;
import com.example.bacassample3.util.CommonUtilities;
import com.google.android.gcm.GCMRegistrar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import static com.example.bacassample3.util.CommonUtilities.DEVICE_ID;

public class UserActivity extends Activity {
	String className = "UserActivity";
	
	private Context contextUserActivity;
	private UserEntity user;
	private ArrayList<UserEntity> userList;				// userList를 저장하는 ArrayList
	private ListView lvUserList;								// userList를 표시할 ListView
	private UserAdapter userAdapter;						// userList를 표시할 어댑터 
	private UserAsync userAsync;							// user정보를 가져오는 객체
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(className + " @ onCreate", "init");

		setContentView(R.layout.user_activity);
		
		contextUserActivity = this;
		
		userList = new ArrayList<UserEntity>();
		lvUserList = (ListView) findViewById(R.id.list);

		DEVICE_ID = GCMRegistrar.getRegistrationId(this);

//		String parameter = "deviceid=" + DEVICE_ID;
//		userAsync = new UserAsync();
//		userAsync.execute(parameter);
	}
	
//	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
//	@Override
//	protected void onResume() {
//		super.onResume();
//		Log.i(className + " @ onResume", "init");
//		
//		String parameters = "used_function=user&user_id=" + DEVICE_ID;
//		RankingAsync rankingAsync = new RankingAsync();
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
//			rankingAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, parameters);
//		else
//			rankingAsync.execute(parameters);
//		
//		Log.i(className + " @ onResume", "end");
//	}
	
	public class UserAdapter extends BaseAdapter {

		private Context mContext;
		ArrayList<UserEntity> userList;

		public UserAdapter(Context context, ArrayList<UserEntity> userList) {
			mContext = context;
			this.userList = userList;
		}

		@Override
		public int getCount() {
			return userList.size();
		}

		@Override
		public UserEntity getItem(int position) {
			return userList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.user_row, null);
				viewHolder = createViewHolder(convertView);
				convertView.setTag(viewHolder);
			} else {
				if (convertView.getTag() == null) {
					viewHolder = createViewHolder(convertView);
				} else {
					viewHolder = (ViewHolder) convertView.getTag();
				}
			}
			
			String deviceID = getItem(position).getDeviceid();
//			if(deviceID != null) {
//				deviceID = deviceID.substring(0, 20);
//			} else {
//				deviceID = "Can't search";
//			}
			
			viewHolder.id.setText(position + ".");
			viewHolder.mail.setText(deviceID);
			
			return convertView;
		}

		private ViewHolder createViewHolder(View convertView) {
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.id = (TextView) convertView.findViewById(R.id.id);
			viewHolder.mail = (TextView) convertView.findViewById(R.id.mail);
			return viewHolder;
		}

		private final class ViewHolder {
			private TextView id;
			private TextView mail;
		}
	}

     
	// 유저 정보를 가져오는 클래스
	public class UserAsync extends AsyncTask<String, String, String> {
		private final String className = "UserAsync";
		
		private Client2Server client2Server = Client2Server.getInstance();
		
		@Override
		protected String doInBackground(String... params) {
			Log.i( className + " @ param0", params[0]);

			// HttpClass에서 해당 url을 실행한다.
			String parameter = params[0];
			String return_value = client2Server.getUserinfo(parameter);
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
			
			JSONArray jsonUsers = CommonUtilities.string2Json(result);
			
			try {
				userList.clear();
				for(int i=0; i<jsonUsers.length();i++) {
					JSONObject json;
					
						json = jsonUsers.getJSONObject(i);
						
						user = new UserEntity(json.getString("deviceid"));
						
						if(json.has("mail")) {
							user.setMail(json.getString("mail"));
						}
						
						userList.add(user);
					
					Log.i(className + " @ string2Json : ", json.toString());
				}
			} catch (JSONException e) {
				Log.e(className + " @ string2Json : " , e.toString());
			}
			
			Log.i(className + " @ onPostExecute", userList.get(0).getId() + " " + userList.get(0).getPwd() + " " + userList.get(0).getMail() + " " + userList.get(0).getDeviceid());
			if(userList.size() > 0) {
				userAdapter = new UserAdapter(contextUserActivity, userList);
				lvUserList.setAdapter(userAdapter);
			} else {
				Log.i(className + "onPostExecute", "userList <= 0");
			}
			
//			this.cancel(true);
			
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
