package com.example.bacassample3.tab1;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class Tab1ActivityGroup extends ActivityGroup{
	private String className = "Tab1ActivityGroup";
	
	public static Tab1ActivityGroup tab1ActivityGroup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		tab1ActivityGroup = this;
		
		Log.i(className + " @ onCreate", "TAB1 실행");
		
		Intent intent = new Intent(Tab1ActivityGroup.this, UserActivity.class);
		View view = getLocalActivityManager().startActivity("UserActivity", intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
		replaceView(view);
	}
	
	// 새로운 Level의 Activity를 추가하는 경우
	public void replaceView(View view) {
		setContentView(view);
	}

}
