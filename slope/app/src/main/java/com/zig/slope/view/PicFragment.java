package com.zig.slope.view;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.zig.slope.R;

import java.io.File;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class PicFragment extends Fragment {

    private static final String VIDEO_PATH = "video_path";

    private String videoPath;

    private ImageView mVideoView;

    private Button btnClose;

    public static PicFragment newInstance(String videoPath) {
        PicFragment fragment = new PicFragment();
        Bundle args = new Bundle();
        args.putString(VIDEO_PATH, videoPath);
        fragment.setArguments(args);
        return fragment;
    }

    public PicFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            videoPath = getArguments().getString(VIDEO_PATH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pic, container, false);
        mVideoView = view.findViewById(R.id.image_view);
        Glide.with(getActivity()).load(new File(videoPath)).into(mVideoView);
        mVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(PicFragment.this).commit();
            }
        });
        return view;
    }

}
