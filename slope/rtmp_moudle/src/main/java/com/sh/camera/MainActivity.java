/*  car eye 车辆管理平台
 * car-eye管理平台   www.car-eye.cn
 * car-eye开源网址:  https://github.com/Car-eye-team
 * Copyright
 */

package com.sh.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.sh.RTMP_Pusher.R;
import com.sh.camera.permission.FloatWindowManager;
import com.sh.camera.service.MainService;
import com.sh.camera.util.Constants;



public class MainActivity extends Activity {
	public static MainActivity mainactivity;
	private static final int REQUEST_EXTERNAL_STORAGE = 1;
	private static String[] PERMISSIONS_STORAGE = { Manifest.permission.READ_EXTERNAL_STORAGE,
	Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,
	Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.RECORD_AUDIO,
	Manifest.permission.READ_PHONE_STATE};
	public FloatWindowManager.MyListener listener;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		Log.d("-------------------", "MainActivity onCreate" );
		Constants.setParam(this);
		int version = android.os.Build.VERSION.SDK_INT;
		setContentView(R.layout.activity_splash);
		gotoService();
	}



	public void gotoService(){
		if(!MainService.isrun){
			startService(new Intent(MainActivity.this, MainService.class));
			Log.d("MainActivity" , "You got error here 0?");
		}else{
			Intent intent = new Intent(MainService.ACTION);
			intent.putExtra("type", MainService.FULLSCREEN);
			sendBroadcast(intent);
		}
		finish();
	}


}



