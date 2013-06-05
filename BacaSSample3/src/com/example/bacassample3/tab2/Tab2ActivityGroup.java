package com.example.bacassample3.tab2;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class Tab2ActivityGroup extends ActivityGroup{
	private String className = "Tab2ActivityGroup";
	
	public static Tab2ActivityGroup tab2ActivityGroup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		tab2ActivityGroup = this;
		
		Log.i(className + " @ onCreate", "TAB2 실행");
		
		Intent intent = new Intent(Tab2ActivityGroup.this, LocationActivity.class);
		View view = getLocalActivityManager().startActivity("LocationActivity", intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
		replaceView(view);
	}
	
	// 새로운 Level의 Activity를 추가하는 경우
	public void replaceView(View view) {
		setContentView(view);
	}

}
