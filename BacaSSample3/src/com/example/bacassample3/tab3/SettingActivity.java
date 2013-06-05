package com.example.bacassample3.tab3;

import com.example.bacassample3.R;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.bacassample3.util.CommonUtilities.SERVER_URL;

public class SettingActivity extends Activity {
	String className = "SettingActivity";
	EditText editText;
	Button button;
	Context contextSettingActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(className + " @ onCreate", "init");

		setContentView(R.layout.setting_activity);
		
		contextSettingActivity = this;
		
		editText = (EditText) findViewById(R.id.ip);
		button = (Button) findViewById(R.id.setbutton);
		
		editText.setText(SERVER_URL);
		button.setOnClickListener(new SettingButtonOnClickListener());
	}
	
	class SettingButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			String ip = editText.getText().toString();
			
			SERVER_URL = ip;
			
			Toast toast = Toast.makeText(contextSettingActivity, ip, Toast.LENGTH_SHORT);
			toast.show();
		}
	}
}
