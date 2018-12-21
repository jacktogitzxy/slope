/*
	Copyright (c) 2013-2016 EasyDarwin.ORG.  All rights reserved.
	Github: https://github.com/EasyDarwin
	WEChat: EasyDarwin
	Website: http://www.easydarwin.org
*/

package org.easydarwin.easypusher;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.easydarwin.config.Config;
import org.easydarwin.util.PresharefenceManager;

public class SettingActivity extends AppCompatActivity {

    private static final boolean TEST_ = true;

    public static final int REQUEST_OVERLAY_PERMISSION = 1004;
    public static final String KEY_ENABLE_BACKGROUND_CAMERA = "key_enable_background_camera";
    private static final int REQUEST_SCAN_TEXT_URL = 1003;
    EditText rtmpUrl;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        context = this;
        final EditText txtIp = (EditText) findViewById(R.id.edt_server_address);
        final EditText txtPort = (EditText) findViewById(R.id.edt_server_port);
        final EditText txtId = (EditText) findViewById(R.id.edt_stream_id);
        final View rtspGroup = findViewById(R.id.rtsp_group);
        rtmpUrl = (EditText) findViewById(R.id.rtmp_url);
        if (BuildConfig.FLAVOR.equals("rtmp")) {
            rtspGroup.setVisibility(View.GONE);
            rtmpUrl.setVisibility(View.VISIBLE);
        }else{
            rtspGroup.setVisibility(View.VISIBLE);
            rtmpUrl.setVisibility(View.GONE);
        }
        String ip = PresharefenceManager.getIp(context);
        String port = PresharefenceManager.getPort(context);
        String id = PresharefenceManager.getId(context);

        txtIp.setText(ip);
        txtPort.setText(port);
        txtId.setText(id);

        rtmpUrl.setText(PresharefenceManager.getUrl(context));
        Button btnSave = (Button) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ipValue = txtIp.getText().toString().trim();
                String portValue = txtPort.getText().toString().trim();
                String idValue = txtId.getText().toString().trim();
                String url = rtmpUrl.getText().toString().trim();

                if (TextUtils.isEmpty(ipValue)) {
                    ipValue = Config.DEFAULT_SERVER_IP;
                }

                if (TextUtils.isEmpty(portValue)) {
                    portValue = Config.DEFAULT_SERVER_PORT;
                }

                if (TextUtils.isEmpty(idValue)) {
                    idValue = Config.DEFAULT_STREAM_ID;
                }

                if (TextUtils.isEmpty(url)) {
                    url = Config.DEFAULT_SERVER_URL;
                }

                PresharefenceManager.saveStringIntoPref(Config.SERVER_IP, ipValue,context);
                PresharefenceManager.saveStringIntoPref(Config.SERVER_PORT, portValue,context);
                PresharefenceManager.saveStringIntoPref(Config.STREAM_ID, idValue,context);
                PresharefenceManager.saveStringIntoPref(Config.SERVER_URL, url,context);

                finish();
            }
        });


        CheckBox backgroundPushing = (CheckBox) findViewById(R.id.enable_background_camera_pushing);
        backgroundPushing.setChecked(PreferenceManager.getDefaultSharedPreferences(this).getBoolean(KEY_ENABLE_BACKGROUND_CAMERA, false));

        backgroundPushing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (!Settings.canDrawOverlays(SettingActivity.this)) {

                                new AlertDialog.Builder(SettingActivity.this).setTitle("后台上传视频").setMessage("后台上传视频需要APP出现在顶部.是否确定?").setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        final Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                                        startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION);
                                    }
                                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        PreferenceManager.getDefaultSharedPreferences(SettingActivity.this).edit()
                                                .putBoolean(KEY_ENABLE_BACKGROUND_CAMERA, false).apply();
                                        buttonView.toggle();
                                    }
                                }).setCancelable(false).show();
                            }
                        }else {
                            PreferenceManager.getDefaultSharedPreferences(SettingActivity.this).edit().putBoolean(KEY_ENABLE_BACKGROUND_CAMERA, true).apply();
                        }
                }else {
                    PreferenceManager.getDefaultSharedPreferences(SettingActivity.this).edit().putBoolean(KEY_ENABLE_BACKGROUND_CAMERA, false).apply();
                }
            }
        });


        CheckBox x264enc = (CheckBox) findViewById(R.id.use_x264_encode);
        x264enc.setChecked(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("key-sw-codec", false));

        x264enc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceManager.getDefaultSharedPreferences(SettingActivity.this).edit().putBoolean("key-sw-codec", isChecked).apply();
            }
        });

        CheckBox enable_video_overlay = (CheckBox) findViewById(R.id.enable_video_overlay);
        enable_video_overlay.setChecked(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("key_enable_video_overlay", false));

        enable_video_overlay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceManager.getDefaultSharedPreferences(SettingActivity.this).edit().putBoolean("key_enable_video_overlay", isChecked).apply();
            }
        });


        RadioGroup push_content = findViewById(R.id.push_content);
        boolean videoEnable = PreferenceManager.getDefaultSharedPreferences(SettingActivity.this)
                .getBoolean(StreamActivity.KEY_ENABLE_VIDEO, true);
        if (videoEnable){
            boolean audioEnable = PreferenceManager.getDefaultSharedPreferences(SettingActivity.this)
                    .getBoolean(StreamActivity.KEY_ENABLE_AUDIO, true);

            if (audioEnable){
                RadioButton push_av = findViewById(R.id.push_av);
                push_av.setChecked(true);
            }else{
                RadioButton push_v = findViewById(R.id.push_v);
                push_v.setChecked(true);
            }
        }else{
            RadioButton push_a = findViewById(R.id.push_a);
            push_a.setChecked(true);
        }
        push_content.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.push_av){
                    PreferenceManager.getDefaultSharedPreferences(SettingActivity.this)
                            .edit()
                            .putBoolean(StreamActivity.KEY_ENABLE_VIDEO, true)
                            .putBoolean(StreamActivity.KEY_ENABLE_AUDIO, true)
                            .apply();
                }else if (checkedId == R.id.push_a){
                    PreferenceManager.getDefaultSharedPreferences(SettingActivity.this)
                            .edit()
                            .putBoolean(StreamActivity.KEY_ENABLE_VIDEO, false)
                            .putBoolean(StreamActivity.KEY_ENABLE_AUDIO, true)
                            .apply();

                }else if (checkedId == R.id.push_v){
                    PreferenceManager.getDefaultSharedPreferences(SettingActivity.this)
                            .edit()
                            .putBoolean(StreamActivity.KEY_ENABLE_VIDEO, true)
                            .putBoolean(StreamActivity.KEY_ENABLE_AUDIO, false)
                            .apply();
                }
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void onOpenLocalRecord(View view) {
        startActivity(new Intent(this, MediaFilesActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OVERLAY_PERMISSION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                boolean canDraw = Settings.canDrawOverlays(this);
                PreferenceManager.getDefaultSharedPreferences(SettingActivity.this).edit()
                        .putBoolean(KEY_ENABLE_BACKGROUND_CAMERA, canDraw).apply();
                if (!canDraw){
                    CheckBox backgroundPushing = (CheckBox) findViewById(R.id.enable_background_camera_pushing);
                    backgroundPushing.setChecked(false);
                }
            }
        }else if (requestCode == REQUEST_SCAN_TEXT_URL){
            if (resultCode == RESULT_OK){
                String url = data.getStringExtra("text");
                rtmpUrl.setText(url);

                PresharefenceManager.saveStringIntoPref(Config.SERVER_URL, url,context);
            }
        }
    }

    public void onScreenPushResolution(View view) {
        int defaultIdx = PreferenceManager.getDefaultSharedPreferences(this).getInt("screen_pushing_res_index", 3);
        new AlertDialog.Builder(this).setTitle("推送屏幕分辨率").setSingleChoiceItems(
                new CharSequence[]{"1倍屏幕大小","0.75倍屏幕大小","0.5倍屏幕大小","0.3倍屏幕大小","0.25倍屏幕大小","0.2倍屏幕大小"}, defaultIdx, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PreferenceManager.getDefaultSharedPreferences(SettingActivity.this).edit().putInt("screen_pushing_res_index", which).apply();
                Toast.makeText(SettingActivity.this,"配置更改将在下次启动屏幕推送时生效", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }).show();
    }

    public void onScanQRCode(View view) {
        Intent intent = new Intent(this, ScanQRActivity.class);
        startActivityForResult(intent, REQUEST_SCAN_TEXT_URL);
//        overridePendingTransition(R.anim.activity_open_enter,0);
    }


}
