package com.zig.slope.view;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.zig.slope.R;
import com.zig.slope.common.utils.CustomProgressDialog;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class VideoFragment extends Fragment {

    private static final String VIDEO_PATH = "video_path";
    private CustomProgressDialog progressDialog;
    private String videoPath;

    private VideoView mVideoView;

    private Button btnClose;
    boolean ismodel = false;

    public static VideoFragment newInstance(String videoPath) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putString(VIDEO_PATH, videoPath);
        fragment.setArguments(args);
        return fragment;
    }

    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            videoPath = getArguments().getString(VIDEO_PATH);
        }
        createProgressDialog(getActivity(),true);
        showProgressDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
         view = inflater.inflate(R.layout.fragment_video, container, false);
        if(ismodel){
            Log.i("zxy", "onCreateView: ismodel==fragment_videofull");
             view = inflater.inflate(R.layout.fragment_videofull, container, false);
        }else{
            view = inflater.inflate(R.layout.fragment_video, container, false);
        }
        mVideoView = (VideoView) view.findViewById(R.id.video_view);
        btnClose = (Button) view.findViewById(R.id.btn_close);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                stopProgressDialog();
            }
        });
        // 播放相应的视频
        mVideoView.setMediaController(new MediaController(getActivity()));
        mVideoView.setVideoURI(Uri.parse(videoPath));
        mVideoView.start();
        //mVideoView.requestFocus();

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(VideoFragment.this).commit();
            }
        });
        return view;
    }

    public void setWH(boolean ismodel){
       this.ismodel = ismodel;
    }
    /**
     * 创建进度条实例
     */
    public void createProgressDialog(Context cxt, boolean canCancle) {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
            if (progressDialog == null) {
                progressDialog = CustomProgressDialog.createDialog(cxt, canCancle);
                progressDialog.setCanceledOnTouchOutside(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动加载进度条
     */
    public void showProgressDialog(){
        try {
            if (progressDialog != null) {
                progressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭加载进度条
     */
    public void stopProgressDialog() {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
