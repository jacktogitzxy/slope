/*
	Copyright (c) 2013-2016 EasyDarwin.ORG.  All rights reserved.
	Github: https://github.com/EasyDarwin
	WEChat: EasyDarwin
	Website: http://www.easydarwin.org
*/
package org.easydarwin.easypusher;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.SurfaceTexture;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;

import org.easydarwin.bus.StartRecord;
import org.easydarwin.bus.StopRecord;
import org.easydarwin.bus.StreamStat;
import org.easydarwin.bus.SupportResolution;
import org.easydarwin.config.Config;
import org.easydarwin.easyrtmp.push.EasyRTMP;
import org.easydarwin.push.InitCallback;
import org.easydarwin.push.MediaStream;
import org.easydarwin.util.PresharefenceManager;
import org.easydarwin.util.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static org.easydarwin.easypusher.SettingActivity.REQUEST_OVERLAY_PERMISSION;
@Route(path = "/rtmp/live")
public class StreamActivity extends AppCompatActivity implements View.OnClickListener, TextureView.SurfaceTextureListener {

    static final String TAG = "EasyPusher";
    public static final int REQUEST_MEDIA_PROJECTION = 1002;
    public static final int REQUEST_CAMERA_PERMISSION = 1003;
    public static final int REQUEST_STORAGE_PERMISSION = 1004;

    //默认分辨率
    int width = 1280, height = 720;

    TextView txtStreamAddress;
    ImageView btnSwitchCemera;
    Spinner spnResolution;
    List<String> listResolution = new ArrayList<String>();
    MediaStream mMediaStream;
    TextView txtStatus, streamStat;
    static Intent mResultIntent;
    static int mResultCode;
    TextView textRecordTick;
    private BackgroundCameraService mService;
    private ServiceConnection conn;
    private Runnable mRecordTickRunnable = new Runnable() {
        @Override
        public void run() {
            long duration = System.currentTimeMillis() - mRecordingBegin;
            duration /= 1000;
            textRecordTick.setText(String.format("%02d:%02d", duration / 60, (duration) % 60));
            if (duration % 2 == 0) {
                textRecordTick.setCompoundDrawablesWithIntrinsicBounds(R.drawable.recording_marker_shape, 0, 0, 0);
            } else {
                textRecordTick.setCompoundDrawablesWithIntrinsicBounds(R.drawable.recording_marker_interval_shape, 0, 0, 0);
            }
            textRecordTick.removeCallbacks(this);
            textRecordTick.postDelayed(this, 1000);
        }
    };
    private boolean mNeedGrantedPermission;

    public static final String KEY_ENABLE_VIDEO = "key-enable-video";
    public static final String KEY_ENABLE_AUDIO = "key-enable-audio";
    public static final String CHANNEL_CAMERA = "camera";

    private static Context mApplication;


    public static final Bus BUS = new Bus(ThreadEnforcer.ANY);
    public long mRecordingBegin;
    public boolean mRecording;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        mApplication = this;
        BUS.register(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.RECORD_AUDIO}, REQUEST_CAMERA_PERMISSION);
            mNeedGrantedPermission = true;
            return;
        } else {
            // resume..
        }

        // for compatibility
        resetDefaultServer();
        File youyuan = getFileStreamPath("SIMYOU.ttf");
        if (!youyuan.exists()){
            AssetManager am = getAssets();
            try {
                InputStream is = am.open("zk/SIMYOU.ttf");
                FileOutputStream os = openFileOutput("SIMYOU.ttf", MODE_PRIVATE);
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
                os.close();
                is.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.camera);

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_CAMERA, name, importance);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    private void goonWithPermissionGranted() {
        spnResolution = findViewById(R.id.spn_resolution);
        streamStat = findViewById(R.id.stream_stat);
        streamStat.setText(null);
        txtStatus = findViewById(R.id.txt_stream_status);

        btnSwitchCemera = findViewById(R.id.btn_switchCamera);
        btnSwitchCemera.setOnClickListener(this);
        txtStreamAddress = findViewById(R.id.txt_stream_address);
        textRecordTick = findViewById(R.id.tv_start_record);
        final TextureView surfaceView = findViewById(R.id.sv_surfaceview);
        surfaceView.setSurfaceTextureListener(this);

        surfaceView.setOnClickListener(this);


        View pushScreen = findViewById(R.id.push_screen_container);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            pushScreen.setVisibility(View.GONE);
        }

        ImageView push_screen = findViewById(R.id.streaming_activity_push_screen);
        if (RecordService.mEasyPusher != null) {
            push_screen.setImageResource(R.drawable.push_screen_click);
            TextView viewById = findViewById(R.id.push_screen_url);
            viewById.setText(PresharefenceManager.getUrl(mApplication) + "_s");
        }

        String url = "http://www.easydarwin.org/versions/easyrtmp/version.txt";



        // create background service for background use.
        Intent intent = new Intent(this, BackgroundCameraService.class);
        startService(intent);

        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mService = ((BackgroundCameraService.LocalBinder) iBinder).getService();
//                mMediaStream = EasyApplication.sMS;

                if (surfaceView.isAvailable()) {
                    goonWithAvailableTexture(surfaceView.getSurfaceTexture());
                }

            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
            }
        };
        bindService(new Intent(this, BackgroundCameraService.class), conn, 0);


//        if (mMediaStream != null){
//            stopService(new Intent(this, BackgroundCameraService.class));
//            mMediaStream.setSurfaceTexture(surface);
//            mMediaStream.createCamera();
//            mMediaStream.startPreview();
//            return;
//        }

        if (mRecording) {
            textRecordTick.setVisibility(View.VISIBLE);

            textRecordTick.removeCallbacks(mRecordTickRunnable);
            textRecordTick.post(mRecordTickRunnable);
        } else {
            textRecordTick.setVisibility(View.INVISIBLE);
            textRecordTick.removeCallbacks(mRecordTickRunnable);
        }

    }

    @Subscribe
    public void onStartRecord(StartRecord sr) {
        mRecording = true;
        mRecordingBegin = System.currentTimeMillis();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                textRecordTick.setVisibility(View.VISIBLE);

                textRecordTick.removeCallbacks(mRecordTickRunnable);
                textRecordTick.post(mRecordTickRunnable);


                ImageView ib = findViewById(R.id.streaming_activity_record);
                ib.setImageResource(R.drawable.record_pressed);
            }
        });
    }

    @Subscribe
    public void onStopRecord(StopRecord sr) {
        mRecording = false;
        mRecordingBegin = 0;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                textRecordTick.setVisibility(View.INVISIBLE);
                textRecordTick.removeCallbacks(mRecordTickRunnable);
                ImageView ib = findViewById(R.id.streaming_activity_record);
                ib.setImageResource(R.drawable.record);
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION: {
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    mNeedGrantedPermission = false;
                    goonWithPermissionGranted();

                } else {
                    finish();
                }
                break;
            }
        }
    }


    private void startScreenPushIntent() {

        if (StreamActivity.mResultIntent != null && StreamActivity.mResultCode != 0) {
            Intent intent = new Intent(getApplicationContext(), RecordService.class);
            startService(intent);
            ImageView im = findViewById(R.id.streaming_activity_push_screen);
            im.setImageResource(R.drawable.push_screen_click);

            TextView viewById = findViewById(R.id.push_screen_url);
            viewById.setText(PresharefenceManager.getUrl(mApplication) + "_s");
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                MediaProjectionManager mMpMngr = (MediaProjectionManager) getApplicationContext().getSystemService(MEDIA_PROJECTION_SERVICE);
                startActivityForResult(mMpMngr.createScreenCaptureIntent(), StreamActivity.REQUEST_MEDIA_PROJECTION);
            }
        }
    }

    public void onPushScreen(final View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            new AlertDialog.Builder(this).setMessage("推送屏幕需要安卓5.0以上,您当前系统版本过低,不支持该功能。").setTitle("抱歉").show();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {

                new AlertDialog.Builder(this).setMessage("推送屏幕需要APP出现在顶部.是否确定?").setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        final Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                        startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION);
                    }
                }).setNegativeButton(android.R.string.cancel,null).setCancelable(false).show();
                return;
            }
        }

        if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("alert_screen_background_pushing", false)) {
            new AlertDialog.Builder(this).setTitle("提醒").setMessage("屏幕直播将要开始,直播过程中您可以切换到其它屏幕。不过记得直播结束后,再进来停止直播哦!").setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    PreferenceManager.getDefaultSharedPreferences(StreamActivity.this).edit().putBoolean("alert_screen_background_pushing", true).apply();
                    onPushScreen(view);
                }
            }).show();
            return;
        }

        if (RecordService.mEasyPusher != null) {
            Intent intent = new Intent(getApplicationContext(), RecordService.class);
            stopService(intent);

            TextView viewById = findViewById(R.id.push_screen_url);
            viewById.setText(PresharefenceManager.getUrl(mApplication) + "_s");

            ImageView im = findViewById(R.id.streaming_activity_push_screen);
            im.setImageResource(R.drawable.push_screen);
        } else {
            startScreenPushIntent();
        }
    }


    private static final String STATE = "state";
    private static final int MSG_STATE = 1;

    private void sendMessage(String message) {
        Message msg = Message.obtain();
        msg.what = MSG_STATE;
        Bundle bundle = new Bundle();
        bundle.putString(STATE, message);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_STATE:
                    String state = msg.getData().getString("state");
                    txtStatus.setText(state);
                    break;
            }
        }
    };

    private void initSpninner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spn_item, listResolution);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnResolution.setAdapter(adapter);
        int position = listResolution.indexOf(String.format("%dx%d", width, height));
        spnResolution.setSelection(position, false);
        spnResolution.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mMediaStream != null && mMediaStream.isStreaming()) {
                    int pos = listResolution.indexOf(String.format("%dx%d", width, height));
                    if (pos == position) {
                        return;
                    }
                    spnResolution.setSelection(pos, false);
                    Toast.makeText(StreamActivity.this, "正在推送中,无法切换分辨率", Toast.LENGTH_SHORT).show();
                    return;
                }
                String r = listResolution.get(position);
                String[] splitR = r.split("x");

                int wh = Integer.parseInt(splitR[0]);
                int ht = Integer.parseInt(splitR[1]);
                if (width != wh || height != ht) {
                    width = wh;
                    height = ht;
                    if (mMediaStream != null) {
                        mMediaStream.updateResolution(width, height);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void startCamera() {
        mMediaStream.updateResolution(width, height);
        mMediaStream.setDgree(getDgree());
        mMediaStream.createCamera();
        mMediaStream.startPreview();

        if (mMediaStream.isStreaming()) {
            sendMessage("推流中");
            String url = PresharefenceManager.getUrl(mApplication);
            txtStreamAddress.setText(url);
        }
    }

    private int getDgree() {
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break; // Natural orientation
            case Surface.ROTATION_90:
                degrees = 90;
                break; // Landscape left
            case Surface.ROTATION_180:
                degrees = 180;
                break;// Upside down
            case Surface.ROTATION_270:
                degrees = 270;
                break;// Landscape right
        }
        return degrees;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.sv_surfaceview) {
            try {
                mMediaStream.getCamera().autoFocus(null);
            } catch (Exception e) {
            }

        } else if (i == R.id.btn_switchCamera) {
            mMediaStream.switchCamera();

        }
    }

    @Subscribe
    public void onStreamStat(final StreamStat stat) {
        streamStat.post(new Runnable() {
            @Override
            public void run() {
                streamStat.setText(getString(R.string.stream_stat, stat.fps, stat.bps / 1024));
            }
        });
    }

    @Subscribe
    public void onSupportResolution(SupportResolution resolution) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listResolution = Util.getSupportResolution(getApplicationContext());
                boolean supportdefault = listResolution.contains(String.format("%dx%d", width, height));
                if (!supportdefault) {
                    String r = listResolution.get(0);
                    String[] splitR = r.split("x");
                    width = Integer.parseInt(splitR[0]);
                    height = Integer.parseInt(splitR[1]);
                }
                initSpninner();
            }
        });
    }

    @Override
    protected void onDestroy() {
        BUS.unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (resultCode == RESULT_OK) {
                Log.e(TAG, "get capture permission success!");
                mResultCode = resultCode;
                mResultIntent = data;

                startScreenPushIntent();

            }
        }
    }


    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        boolean isStreaming = mMediaStream != null && mMediaStream.isStreaming();
        if (isStreaming && PreferenceManager.getDefaultSharedPreferences(this).getBoolean(SettingActivity.KEY_ENABLE_BACKGROUND_CAMERA, false)) {
            new AlertDialog.Builder(this).setTitle("是否允许后台上传？")
                    .setMessage("您设置了使能摄像头后台采集,是否继续在后台采集并上传视频？如果是，记得直播结束后,再回来这里关闭直播。")
                    .setNeutralButton("后台采集", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    PreferenceManager.getDefaultSharedPreferences(StreamActivity.this).edit().putBoolean("background_camera_alert", true).apply();
                    StreamActivity.super.onBackPressed();
                }
            }).setPositiveButton("退出程序", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mMediaStream.stopStream();
                    StreamActivity.super.onBackPressed();
                    Toast.makeText(StreamActivity.this, "程序已退出。", Toast.LENGTH_SHORT).show();
                }
            }).setNegativeButton(android.R.string.cancel, null).show();
            return;
        } else {
            super.onBackPressed();
        }
    }

    public void onAbut(View view) {
      //  startActivity(new Intent(this, AboutActivity.class));
    }

    @Override
    public void onSurfaceTextureAvailable(final SurfaceTexture surface, int width, int height) {
        if (mService != null) {
            goonWithAvailableTexture(surface);
        }
    }

    private void goonWithAvailableTexture(SurfaceTexture surface) {
        final File easyPusher = new File(Environment.getExternalStorageDirectory() +"/EasyRTMP");
        easyPusher.mkdir();
        MediaStream ms = mService.getMediaStream();
        if (ms != null) {    // switch from background to front
            ms.stopPreview();
            mService.inActivePreview();
            ms.setSurfaceTexture(surface);
            ms.startPreview();
            mMediaStream = ms;

            if (ms.isStreaming()) {
                String url = PresharefenceManager.getUrl(mApplication);
                txtStreamAddress.setText(url);
                sendMessage("推流中");
                ImageView startPush = findViewById(R.id.streaming_activity_push);
                startPush.setImageResource(R.drawable.start_push_pressed);
            }
        } else {
            ms = new MediaStream(getApplicationContext(), surface, PreferenceManager.getDefaultSharedPreferences(this)
                    .getBoolean(KEY_ENABLE_VIDEO, true));
            ms.setRecordPath(easyPusher.getPath());
            mMediaStream = ms;
            startCamera();
            mService.setMediaStream(ms);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }


    @Override
    protected void onPause() {
        if (!mNeedGrantedPermission) {
            unbindService(conn);
            handler.removeCallbacksAndMessages(null);
        }
        boolean isStreaming = mMediaStream != null && mMediaStream.isStreaming();
        if (mMediaStream != null) {
            mMediaStream.stopPreview();
            if (isStreaming && PreferenceManager.getDefaultSharedPreferences(StreamActivity.this)
                    .getBoolean(SettingActivity.KEY_ENABLE_BACKGROUND_CAMERA, false)) {
                mService.activePreview();
            } else {
                mMediaStream.stopStream();
                mMediaStream.release();
                mMediaStream = null;

                stopService(new Intent(this, BackgroundCameraService.class));
            }
        }

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mNeedGrantedPermission){
            goonWithPermissionGranted();
        }

    }

    public void onRecord(View view) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
            return;
        }
        ImageView ib = findViewById(R.id.streaming_activity_record);
        if (mMediaStream != null) {
            if (mMediaStream.isRecording()) {
                mMediaStream.stopRecord();
                ib.setImageResource(R.drawable.record_pressed);
            } else {
                mMediaStream.startRecord();
                ib.setImageResource(R.drawable.record);
            }
        }
    }

    public void onClickResolution(View view) {
        findViewById(R.id.spn_resolution).performClick();
    }

    public void onSwitchOrientation(View view) {
        if (mMediaStream != null) {
            if (mMediaStream.isStreaming()){
                Toast.makeText(this,"正在推送中,无法更改屏幕方向", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        int orientation = getRequestedOrientation();
        if (orientation == SCREEN_ORIENTATION_UNSPECIFIED || orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
//        if (mMediaStream != null) mMediaStream.setDgree(getDgree());
    }

    public void onStartOrStopPush(View view) {
        ImageView ib = findViewById(R.id.streaming_activity_push);
        if (!mMediaStream.isStreaming()) {
            String url = PresharefenceManager.getUrl(mApplication);
            mMediaStream.startStream(url, new InitCallback() {
                @Override
                public void onCallback(int code) {
                    switch (code) {
                        case EasyRTMP.OnInitPusherCallback.CODE.EASY_ACTIVATE_INVALID_KEY:
                            sendMessage("无效Key");
                            break;
                        case EasyRTMP.OnInitPusherCallback.CODE.EASY_ACTIVATE_SUCCESS:
                            sendMessage("激活成功");
                            break;
                        case EasyRTMP.OnInitPusherCallback.CODE.EASY_RTMP_STATE_CONNECTING:
                            sendMessage("连接中");
                            break;
                        case EasyRTMP.OnInitPusherCallback.CODE.EASY_RTMP_STATE_CONNECTED:
                            sendMessage("连接成功");
                            break;
                        case EasyRTMP.OnInitPusherCallback.CODE.EASY_RTMP_STATE_CONNECT_FAILED:
                            sendMessage("连接失败");
                            break;
                        case EasyRTMP.OnInitPusherCallback.CODE.EASY_RTMP_STATE_CONNECT_ABORT:
                            sendMessage("连接异常中断");
                            break;
                        case EasyRTMP.OnInitPusherCallback.CODE.EASY_RTMP_STATE_PUSHING:
                            sendMessage("推流中");
                            String url = PresharefenceManager.getUrl(mApplication);
                            Log.i("zxy", "startCamera: send com.zig.live");
                            Intent intent= new Intent("com.zig.live");
                            intent.putExtra("liveurl",url);
                            sendBroadcast(intent);
                            break;
                        case EasyRTMP.OnInitPusherCallback.CODE.EASY_RTMP_STATE_DISCONNECTED:
                            sendMessage("断开连接");
                            break;
                        case EasyRTMP.OnInitPusherCallback.CODE.EASY_ACTIVATE_PLATFORM_ERR:
                            sendMessage("平台不匹配");
                            break;
                        case EasyRTMP.OnInitPusherCallback.CODE.EASY_ACTIVATE_COMPANY_ID_LEN_ERR:
                            sendMessage("断授权使用商不匹配");
                            break;
                        case EasyRTMP.OnInitPusherCallback.CODE.EASY_ACTIVATE_PROCESS_NAME_LEN_ERR:
                            sendMessage("进程名称长度不匹配");
                            break;
                    }
                }
            });
            ib.setImageResource(R.drawable.start_push_pressed);
            txtStreamAddress.setText(url);
        } else {
            mMediaStream.stopStream();
            ib.setImageResource(R.drawable.start_push);
            sendMessage("断开连接");
        }
    }


    private void resetDefaultServer() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultIP = sharedPreferences.getString(Config.SERVER_IP, Config.DEFAULT_SERVER_IP);
        if ("114.55.107.180".equals(defaultIP)
                || "121.40.50.44".equals(defaultIP)
                || "www.easydarwin.org".equals(defaultIP)) {
            sharedPreferences.edit().putString(Config.SERVER_IP, Config.DEFAULT_SERVER_IP).apply();
        }

        String defaultRtmpURL = sharedPreferences.getString(Config.SERVER_URL, Config.DEFAULT_SERVER_URL);
        int result1 = defaultRtmpURL.indexOf("rtmp://www.easydss.com/live");
        int result2 = defaultRtmpURL.indexOf("rtmp://121.40.50.44/live");
        if (result1 != -1 || result2 != -1) {
            sharedPreferences.edit().putString(Config.SERVER_URL, Config.DEFAULT_SERVER_URL).apply();
        }
    }
    public void onSetting(View view) {
        startActivity(new Intent(this, SettingActivity.class));
    }
}