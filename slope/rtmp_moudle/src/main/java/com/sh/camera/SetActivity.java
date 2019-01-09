/*  car eye 车辆管理平台 
 * car-eye管理平台   www.car-eye.cn
 * car-eye开源网址:  https://github.com/Car-eye-team
 * Copyright
 */

package com.sh.camera;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sh.RTMP_Pusher.R;
import com.sh.camera.ServerManager.PreManager;
import com.sh.camera.util.Constants;
import com.sh.camera.version.VersionBiz;

public class SetActivity extends Activity {
	private PreManager pm;
	EditText et1,et2,et3,et4,et5,et6;
	//,et_editTextkzport;
	Button bt1,bt2;
	//CheckBox[] cbs;
	//int[] cbids = {R.id.checkBox1, R.id.checkBox2, R.id.checkBox3, R.id.checkBox4};
	//RadioGroup rg;
	//public static int[] rgids = {R.id.radio0, R.id.radio1};

	SharedPreferences sp;
	SharedPreferences.Editor sped;
	private TextView tv_version;

	/**是否有版本检测跳转至该界面*/
	private boolean fromUpdateVersion = false;
	public static SetActivity instance;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.set_activity);
		pm = PreManager.getInstance(this);
		instance = this;
		tv_version = (TextView) findViewById(R.id.tv_version);
		et1 = (EditText) findViewById(R.id.set_editText1);
		et2 = (EditText) findViewById(R.id.set_editText2);
		//et_editTextkzport = (EditText) findViewById(R.id.et_editTextkzport);
		et3 = (EditText) findViewById(R.id.set_editText3);
		et4 = (EditText) findViewById(R.id.set_editText4);

		bt1 = (Button) findViewById(R.id.set_button1);
		bt2 = (Button) findViewById(R.id.set_button2);
		//rg = (RadioGroup) findViewById(R.id.radioGroup1);
		et5 = (EditText) findViewById(R.id.set_editText5);
		et6 = (EditText) findViewById(R.id.set_editText6);
		/*cbs = new CheckBox[4];
		for (int i = 0; i < cbs.length; i++) {
			cbs[i] = (CheckBox) findViewById(cbids[i]);
		}*/

		sp = getSharedPreferences("fcoltest", MODE_PRIVATE);
		sped = sp.edit();
		//rg.check(ServerManager.getInstance().getMode());
		et1.setText(pm.getIp());
		et2.setText(pm.getPort());
		et3.setText(pm.getStreamname());
		et4.setText(sp.getString(Constants.fps, String.valueOf(Constants.FRAMERATE)));
		et5.setText(pm.getURL());
		et6.setText(pm.getapp());
		et1.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}
			@Override
			public void afterTextChanged(Editable editable) {
				Log.d("CMD", "afterTextChanged 被执行---->" + editable);
				et5.setText("rtmp://"+editable+":"+et2.getText()+"/"+et6.getText()+"/"+et3.getText()+"&channel=1");
			}
		});
		et2.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}
			@Override
			public void afterTextChanged(Editable editable) {
				Log.d("CMD", "afterTextChanged 被执行---->" + editable);
				et5.setText("rtmp://"+et1.getText()+":"+editable+"/"+et6.getText()+"/"+et3.getText()+"&channel=1");
			}
		});

		et3.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}
			@Override
			public void afterTextChanged(Editable editable) {
				Log.d("CMD", "afterTextChanged 被执行---->" + editable);
				et5.setText("rtmp://"+et1.getText()+":"+et2.getText()+"/"+et6.getText()+"/"+editable+"&channel=1");
			}
		});

		et6.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}
			@Override
			public void afterTextChanged(Editable editable) {
				Log.d("CMD", "afterTextChanged 被执行---->" + editable);
				et5.setText("rtmp://"+et1.getText()+":"+et2.getText()+"/"+editable+"/"+et3.getText()+"&channel=1");
			}
		});

		bt1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String rstr = "";
				/*for (int i = 0; i < cbs.length; i++) {
					if(cbs[i].isChecked()){
						rstr+=String.valueOf(i);
					}
				}*/
				sped.putString(Constants.rule, rstr);
				sped.putString(Constants.ip, et1.getText().toString());
				sped.putString(Constants.port, et2.getText().toString());
				sped.putString(Constants.name, et3.getText().toString());
				//sped.putString(Constants.addPort, et_editTextkzport.getText().toString());
				sped.putString(Constants.fps, et4.getText().toString());
				sped.putString(Constants.URL, et5.getText().toString());
				sped.putString(Constants.application, et6.getText().toString());
				///sped.putInt(Constants.mode, rg.getCheckedRadioButtonId());
				sped.commit();					
				Intent intent = new Intent("com.zig.restart");
				sendBroadcast(intent);
				isSend = true;
				finish();
			}
		});

		bt2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		VersionBiz v = new VersionBiz(this);
		String version = "";
		try {
			version = v.getVersionName(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tv_version.setText("v"+version);
		
//		fromUpdateVersion = (Boolean) getIntent().getBooleanExtra("fromUpdateVersion", false);
//		v.doCheckVersion(false,fromUpdateVersion);

	}

	/**
	 * 给设置端口 输入框赋值
	 * @param text
	 *//*
	private void setSetttingport(String text){
		et_editTextkzport.setText(text);
	}
	*//**
	 * 获取设置端口 输入框赋值
	 * @param text
	 *//*
	private String getSetttingPort(){
		return et_editTextkzport.getText().toString().trim();
	}*/
	private String getSettingPortPre(){

		String settingPort = sp.getString(Constants.addPort,Constants.SERVER_ADDPORT);
		return settingPort;
	}


	boolean isSend = false;
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		instance = null;

	}

}
