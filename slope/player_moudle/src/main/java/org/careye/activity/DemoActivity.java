/*
 * Car eye 车辆管理平台: www.car-eye.cn
 * Car eye 开源网址: https://github.com/Car-eye-team
 * Copyright 2018
 */
package org.careye.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.careye.rtmp.careyeplayer.R;

import org.careye.player.media.EyeVideoView;
import org.careye.util.PicUtils;

public class DemoActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "DemoActivity";

    private String       mURL       = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
//    private String mURL = "rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov";

    private EditText mEtInputUrl;
    private EyeVideoView mVideoPlayer1;
    private Button mBtnPlay;
    private Button mBtnStop;
    private Button mBtnPic;

    private String picName = "careye_";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
//        Intent intent = getIntent();
//        mURL = intent.getStringExtra("url");
//        Log.i(TAG, "onCreate: mURL==="+mURL);
//        if(mURL==null){
//            mURL= "rtmp://47.107.32.201:1935/live/111&channel=1";//"rtmp://live.hkstv.hk.lxdns.com/live/hks";
//        }
        initView();
        initListener();
    }

    private void initListener() {
        mBtnStop.setOnClickListener(this);
        mBtnPlay.setOnClickListener(this);
        mBtnPic.setOnClickListener(this);
    }

    private void initView() {
        mVideoPlayer1 = findViewById(R.id.video_player1);
        mBtnPlay = findViewById(R.id.btn_play);
        mBtnStop = findViewById(R.id.btn_stop);
        mEtInputUrl = findViewById(R.id.et_input_url);
        mBtnPic = findViewById(R.id.btn_pic);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onResume() {
        super.onResume();
        play();
    }

    @Override
    public void onPause() {
        super.onPause();
        stop();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    private void play() {

        stop();

        mVideoPlayer1.setVideoPath(mURL);
        mVideoPlayer1.start();
    }

    public void stop() {
        mVideoPlayer1.stopPlayback();
        mVideoPlayer1.release(true);
        mVideoPlayer1.stopBackgroundPlay();
    }

    private void testPlayerPlay(boolean play) {
    }

    private void showUIControls(boolean show, boolean autohide) {
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_stop) {
            stop();
        } else if (id == R.id.btn_play){
//            String url = mEtInputUrl.getText().toString().trim();
//            if (TextUtils.isEmpty(url)) {
//                Toast.makeText(this, "请输入URL地址", Toast.LENGTH_SHORT).show();
//            }
//            mURL = url;
            play();
        } else if (id == R.id.btn_pic) {
            //抓拍
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (mVideoPlayer1 != null) {
                        Bitmap bitmap = mVideoPlayer1.getPic();
                        final boolean []result = new boolean[1];
                        final String []path = new String[1];
                        if (bitmap != null) {
                            String filaName = picName + SystemClock.uptimeMillis() + ".jpg";
                            path[0] = PicUtils.saveImage(bitmap, filaName);
                            if (TextUtils.isEmpty(path[0])) {
                                result[0] = false;
                            } else {
                                result[0] = true;
                            }
                        } else {
                            result[0] = false;
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (result[0]) {
                                    Toast.makeText(DemoActivity.this, path[0], Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(DemoActivity.this, "抓拍失败", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                }
            }).start();
        }

    }
}

