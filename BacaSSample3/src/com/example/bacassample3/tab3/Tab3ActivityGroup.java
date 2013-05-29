package com.example.bacassample3.tab3;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class Tab3ActivityGroup extends ActivityGroup{
	private String className = "Tab3ActivityGroup";
	
	public static Tab3ActivityGroup tab3ActivityGroup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		tab3ActivityGroup = this;
		
		Log.i(className + " @ onCreate", "TAB3 W실행");
		
		Intent intent = new Intent(Tab3ActivityGroup.this, SettingActivity.class);
		View view = getLocalActivityManager().startActivity("SettingActivity", intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
		replaceView(view);
	}
	
	// 새로운 Level의 Activity를 추가하는 경우
	public void replaceView(View view) {
		setContentView(view);
	}

}