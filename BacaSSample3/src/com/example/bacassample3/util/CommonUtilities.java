/*
 * 호스트 정보와 gcm 정보를 저장할 클래스
 */
package com.example.bacassample3.util;

import org.json.JSONArray;
import org.json.JSONException;

import com.google.android.gcm.GCMRegistrar;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Helper class providing methods and constants common to other classes in the
 * app.
 */
public final class CommonUtilities {

	public static String className = "CommonUtilities";
    /**
     * Base URL of the Demo Server (such as http://my_host:8080/gcm-demo)
     */
    public static String SERVER_URL = "113.198.80.234";
    
    public static String DEVICE_ID = "";
    /**
     * Google API project id registered to use GCM.
     *
     */
    public static final String SENDER_ID = "361274149162";

    /**
     * Tag used on log messages.
     */
    public static final String TAG = "뭐임마";

    /**
     * Intent used to display a message in the screen.
     */
    public static final String DISPLAY_MESSAGE_ACTION =
            "com.google.android.gcm.demo.app.DISPLAY_MESSAGE";

    /**
     * Intent's extra that contains the message to be displayed.
     */
    public static final String EXTRA_MESSAGE = "message";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
    
 // String형태의 json배열을 json배열로 반환
 	public static JSONArray string2Json(String strJson) {
 		
 		JSONArray jArr = null;
 		try {
 			jArr = new JSONArray(strJson);
 			Log.i(className + " @ string2Json : ", jArr.toString());
 		} catch (JSONException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
 		
 		return jArr;
 		
 	}
}
