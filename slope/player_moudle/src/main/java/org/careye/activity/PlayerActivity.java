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
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.careye.rtmp.careyeplayer.R;

import org.careye.player.media.EyeVideoView;
import org.careye.player.media.source.IMediaPlayer;
import org.careye.util.PicUtils;

@Route(path = "/player/play")
public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "DemoActivity";
    private String mURL;
    private EyeVideoView mVideoPlayer1;
    private ImageButton mBtnPlay;
    private ImageButton mBtnPic;
    private TextView stream_bps;
    private String picName = "careye_";
    private boolean isPlaying = false;
    private LoadingDialog mLoadingDialog; //显示正在加载的对话框

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_play);
        Intent intent = getIntent();
        mURL = intent.getStringExtra("url");
        Log.i(TAG, "onCreate: mURL===" + mURL);
        if (mURL == null) {
            mURL = "rtmp://47.107.32.201:1935/live/111&channel=1";//"rtmp://live.hkstv.hk.lxdns.com/live/hks";
        }

        initView();
        initListener();
    }

    private void initListener() {
        mBtnPlay.setOnClickListener(this);
        mBtnPic.setOnClickListener(this);
    }

    private void initView() {
        stream_bps = findViewById(R.id.stream_bps);
        mVideoPlayer1 = findViewById(R.id.video_player1);
        mBtnPlay = findViewById(R.id.live_video_bar_record);
        mBtnPic = findViewById(R.id.live_video_bar_take_picture);
        mVideoPlayer1.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                hideLoading();
                mHandler.postDelayed(mTimerRunnable, 1000);
            }
        });
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


    private void play() {
        mVideoPlayer1.setVideoPath(mURL);
        mVideoPlayer1.start();
        isPlaying = true;
        showLoading();
    }

    public void stop() {
        isPlaying = false;
        mVideoPlayer1.stopPlayback();
        mVideoPlayer1.release(true);
        mVideoPlayer1.stopBackgroundPlay();
        mHandler.removeCallbacks(mTimerRunnable);

    }

    private void testPlayerPlay(boolean play) {
    }

    private void showUIControls(boolean show, boolean autohide) {
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.live_video_bar_record) {
            mBtnPlay = (ImageButton) v;
            if (isPlaying) {
                stop();
                mBtnPlay.setImageResource(R.drawable.icn_media_play);
            } else {
                play();
                mBtnPlay.setImageResource(R.drawable.icn_media_pause);
            }
        } else if (id == R.id.live_video_bar_take_picture) {
            //抓拍
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (mVideoPlayer1 != null) {
                        Bitmap bitmap = mVideoPlayer1.getPic();
                        final boolean[] result = new boolean[1];
                        final String[] path = new String[1];
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
                                    Toast.makeText(PlayerActivity.this, path[0], Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(PlayerActivity.this, "抓拍失败", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                }
            }).start();
        }

    }

    public void exitPlayer(View view) {
        stop();
        PlayerActivity.this.finish();
    }

    public void onFullscreen(View view) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setNavVisibility(false);
        // 横屏情况下,播放窗口横着排开
        mVideoPlayer1.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        mVideoPlayer1.fullOrnamelScreen();
    }

    public void setNavVisibility(boolean visible) {
        if (!ViewConfiguration.get(this).hasPermanentMenuKey()) {
            int newVis = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            if (!visible) {
                newVis |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
            }
            getWindow().getDecorView().setSystemUiVisibility(newVis);
        }
    }

    @Override
    public void onBackPressed() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        if ((attrs.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setNavVisibility(true);
            mVideoPlayer1.getLayoutParams().height = getResources().getDimensionPixelSize(R.dimen.render_wnd_height);
            mVideoPlayer1.requestLayout();
            mVideoPlayer1.fullOrnamelScreen();
            return;
        }
        if (mLoadingDialog != null) {
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.cancel();
                return;
            }
        }
        super.onBackPressed();
    }

    public void onEnableOrDisablePlayAudio(View view) {
        boolean enable = mVideoPlayer1.closeMedia();
        Toast.makeText(PlayerActivity.this, enable ? "声音已关闭" : "声音已打开", Toast.LENGTH_SHORT).show();
        ImageView mPlayAudio = (ImageView) view;
        mPlayAudio.setImageState(enable ? new int[]{android.R.attr.state_pressed} : new int[]{}, true);
    }

    /**
     * 显示加载的进度款
     */
    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this, getString(R.string.loading), true);
        }
        mLoadingDialog.show();
    }

    /**
     * 隐藏加载的进度框
     */
    public void hideLoading() {
        if (mLoadingDialog != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLoadingDialog.hide();
                }
            });

        }
    }
    private final Handler mHandler = new Handler();
    private long mLastReceivedLength;
    private final Runnable mTimerRunnable = new Runnable() {
        @Override
        public void run() {
            long length = mVideoPlayer1.getCurrentPosition2();
            Log.i(TAG, "run: length=========="+length);
            if (length == 0) {
                mLastReceivedLength = 0;
            }
            if (length < mLastReceivedLength) {
                mLastReceivedLength = 0;
            }
            int x = (int) ((length - mLastReceivedLength)/1024/1024);
           // int num = (int) ((Math.random() * 10));
            stream_bps.setText(x + "Kbps");
            mLastReceivedLength = length;

            mHandler.postDelayed(this, 1000);
        }
    };

}

