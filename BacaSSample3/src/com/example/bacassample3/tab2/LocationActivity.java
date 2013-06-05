package com.example.bacassample3.tab2;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.bacassample3.R;
import com.example.bacassample3.util.Client2Server;
import com.example.bacassample3.util.CommonUtilities;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.example.bacassample3.util.CommonUtilities.DEVICE_ID;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class LocationActivity extends Activity {
	private String className = "LocationActivity";

	private Context contextLocationActivity;

	private LocationAsync locationAsync;								// loaction 정보를 받아오는 AsyncTask
	private LocationEntity location;										// location 1개를 저장

	private GoogleMap mMap;
	LocationClient mLocationClient = null;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(className + " @ onCreate", "init");

		setContentView(R.layout.location_activity);

		contextLocationActivity = this;

		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		mMap.setMyLocationEnabled(true);

	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
	@Override
	protected void onResume() {
		super.onResume();
		Log.i(className + " @ onResume", "init");
		
		RankingAsync rankingAsync = new RankingAsync();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			rankingAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "used_function=location&user_id=" + DEVICE_ID);
		else
			rankingAsync.execute("used_function=location&user_id=" + DEVICE_ID);
		
		
		mLocationClient = new LocationClient(this, 
				new GooglePlayServicesClient.ConnectionCallbacks(){

					@Override
					public void onConnected(Bundle connectionHint) {
						Location myloc = mLocationClient.getLastLocation();
						double lat = 37.5828941;
						double lng = 127.00912659999995; 
						
						if(myloc != null) {
							lat = myloc.getLatitude();
							lng = myloc.getLongitude();
						}
						LatLng p = new LatLng(lat, lng);
						
						Log.i(className + " @ current position", "lat" + lat + " lng" + lng);
						
						CameraUpdate center= CameraUpdateFactory.newLatLng(p);
						CameraUpdate zoom=CameraUpdateFactory.zoomTo(14);
						
						mMap.moveCamera(center);
					    mMap.animateCamera(zoom);
					    
						locationAsync = new LocationAsync();
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
							locationAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "lat=" + lat + "&lng=" + lng);
						else
							locationAsync.execute("lat=" + lat + "&lng=" + lng);
					}

					@Override
					public void onDisconnected() {
						// TODO Auto-generated method stub
						
					}
			
		}, new GooglePlayServicesClient.OnConnectionFailedListener() {
			
			@Override
			public void onConnectionFailed(ConnectionResult result) {
				// TODO Auto-generated method stub
				
			}
		});
		mLocationClient.connect();
		Log.i(className + " @ onResume", "end");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void mark(LocationEntity location) {

		Log.i(className + "mark", location.lat + " " + location.lng);
		LatLng latLng = new LatLng(Double.parseDouble(location.lat), Double.parseDouble(location.lng));

		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.mark));
		markerOptions.position(latLng);
		
		String title = "";
		if(!location.store.equals("")) {
			title += "이름 : " + location.store + "\n";
		}
		if(!location.memo.equals("")) {
			title += "설명 : " + location.memo + "\n";
		}
		title += "주소 : " +  location.address + "\n";
		
		markerOptions.title(title);
		

		mMap.addMarker(markerOptions);
		

	}

	public class LocationAsync extends AsyncTask<String, String, String> {
		private final String className = "LocationAsync";

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
			String return_value = "";
			try{
				return_value = client2Server.getLocationinfo(parameter);
			
				// 쓰레드 cancel을 위한 코드
				int count = params.length;
				for (int i = 0; i < count; i++) {
		             if (isCancelled()) break;
		        }
				return return_value;
				
			} catch (Exception e)	{
				Log.e(className + "doInBackground", "통신 에러");
			}
			return return_value;
		}

		// doInBackground 함수 다음에 실행되는 함수, text1에 내용을 저장하여 activity에 뿌린다.
		@Override
		protected void onPostExecute(String result) {
			Log.i(className + "onPostExecute", result);
			
			if(result == null) {
				Log.e(className + " @ string2Json : " , "result is null");
			} else {

				JSONArray jsonLocation = CommonUtilities.string2Json(result);
	
				try {
					for(int i=0;i<jsonLocation.length();i++) {
						JSONObject json = jsonLocation.getJSONObject(i);
	
						location = new LocationEntity(json.getString("lat"), json.getString("lng"), json.getString("address"));
						
						if(json.has("store")) {
							location.setStore(json.getString("store"));
						}
						
						if(json.has("phonenumber")) {
							location.setStore(json.getString("phonenumber"));
						}
						
						if(json.has("memo")) {
							location.setStore(json.getString("memo"));
						}
						
						mark(location);
					}
				} catch (JSONException e) {
					Log.e(className + " @ string2Json : " , e.toString());
				}
				this.cancel(true);
			}
			
			super.onPostExecute(result);
		}
	}

	// Ranking 정보를 가져오는 AsyncTask
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