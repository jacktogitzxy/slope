/*
 * Car eye 车辆管理平台: www.car-eye.cn
 * Car eye 开源网址: https://github.com/Car-eye-team
 * Copyright 2018
 */
package org.careye.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.careye.rtmp.careyeplayer.R;

import org.careye.player.media.EyeVideoView;
import org.careye.util.VideoBean;

import java.util.List;

@Route(path = "/player/plays")
public class MoreLiveActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "MoreLiveActivity";
    private EyeVideoView[] mVideoPlayers;
    private Toolbar toolbar;
    private String newName;
    private int size=1;
    private List<VideoBean> videos;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_more);
        Intent intent = getIntent();
        videos = intent.getParcelableArrayListExtra("videos");
        newName = intent.getStringExtra("newName");
        initView();
        initListener();
    }

    private void initListener() {
        for (int i =0;i<mVideoPlayers.length;i++){
            mVideoPlayers[i].setOnClickListener(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        play();
    }

    private void initView() {
        mVideoPlayers = new EyeVideoView[8];
        mVideoPlayers[0] = findViewById(R.id.video_player1);
        mVideoPlayers[1] = findViewById(R.id.video_player2);
        mVideoPlayers[2] = findViewById(R.id.video_player3);
        mVideoPlayers[3] = findViewById(R.id.video_player4);
        mVideoPlayers[4] = findViewById(R.id.video_player5);
        mVideoPlayers[5] = findViewById(R.id.video_player6);
        mVideoPlayers[6] = findViewById(R.id.video_player7);
        mVideoPlayers[7] = findViewById(R.id.video_player8);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.return_icon);
        toolbar.setTitleMarginStart(120);
        toolbar.setTitle("编号："+newName+"监控视频列表");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoreLiveActivity.this.finish();
            }
        });
    }

    @Override
    public void onDestroy() {
        stop();
        super.onDestroy();
    }



    private void play() {
        stop();
        for (int i =0;i<size;i++){
            if(null!=videos.get(i)) {
                mVideoPlayers[i].setVideoPath(videos.get(i).getUrl());
                mVideoPlayers[i].start();
            }
        }
    }

    public void stop() {
        for (int i =0;i<size;i++){
            mVideoPlayers[i].stopPlayback();
            mVideoPlayers[i].release(true);
            mVideoPlayers[i].stopBackgroundPlay();
        }
    }

    @Override
    public void onClick(View v) {
        int tag= Integer.parseInt( v.getTag().toString());
        if(mVideoPlayers[tag].isPlaying()) {
            stop();
            Intent intent = new Intent(MoreLiveActivity.this, PlayerActivity.class);
            intent.putExtra("url", videos.get(tag).getUrl());
            startActivity(intent);
        }
    }
}

