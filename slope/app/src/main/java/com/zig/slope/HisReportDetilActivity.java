package com.zig.slope;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zig.slope.common.Constants.Constant;
import com.zig.slope.common.base.bean.HisReport;
import com.zig.slope.common.utils.CustomProgressDialog;
import com.zig.slope.view.VideoFragment;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

public class HisReportDetilActivity extends AppCompatActivity {
    private HisReport data;
    private Toolbar toolbar;
    private BGABanner bgaBanner;
    TextView textTitle;
    AppCompatTextView textAuthor;
    AppCompatTextView textTime,text_view_content;
    private String tag = HisReportDetilActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_his_report_detil);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        Intent intent = getIntent();
        data = (HisReport) intent.getSerializableExtra("data");
        initView();
        initData(data);
    }

    private void initData(HisReport data) {

        textTime.setText("上报时间："+data.getCreateTime());
        textAuthor.setText("巡查员："+data.getPatrollerID());
        textTitle.setText("边坡编号："+data.getNewName());
        text_view_content.setText("情况描述："+data.getContents());
        String msg = data.getPicAddress();
        String vsg = data.getVideoAddress();
        List<String> imgs = null;
        if(msg!=null) {
            List<String>  imgsa = Arrays.asList(msg.trim().split("#"));
            imgs = new ArrayList<>(imgsa);
        }
        if(vsg!=null){
            List<String> vsgs =Arrays.asList(vsg.trim().split("#"));
            if(imgs!=null) {
                imgs.addAll(vsgs);
            }else {
                imgs = vsgs;
            }
        }
        bgaBanner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                if(model.endsWith(".jpg")) {
                    Glide.with(HisReportDetilActivity.this)
                            .load(Constant.BASE_URL + model)
                            .placeholder(R.drawable.bga)
                            .error(R.drawable.bga)
                            .centerCrop()
                            .dontAnimate()
                            .into(itemView);
                }else{
                    itemView.setImageResource(R.drawable.bgb);
                    itemView.setTag(model);
                }
            }
        });
        bgaBanner.setData(imgs,null);
        bgaBanner.setDelegate(dl);
    }

    BGABanner.Delegate dl = new BGABanner.Delegate() {
        @Override
        public void onBannerItemClick(BGABanner bgaBanner, View view, Object o, int i) {
            String tag = view.getTag().toString();
            if(tag.endsWith(".mp4")){
                //下载播放
                VideoFragment bigPic = VideoFragment.newInstance(Constant.BASE_URL + tag);
                bigPic.setWH(true);
                android.app.FragmentManager mFragmentManager = getFragmentManager();
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.replace(R.id.rl_play, bigPic);
                transaction.commit();
              //  downMp4Play(Constant.BASE_URL + tag);
            }
        }
    };
    private void initView() {
        textTitle = findViewById(R.id.text_view_title);
        textAuthor = findViewById(R.id.text_view_author);
        textTime = findViewById(R.id.text_view_time);
        bgaBanner = findViewById(R.id.numBanner);
        text_view_content = findViewById(R.id.text_view_content);
        toolbar = (Toolbar) findViewById(R.id.toolbarhisDetil);
        toolbar.setNavigationIcon(R.mipmap.return_icon);
        toolbar.setTitleMarginStart(200);
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setTitle(R.string.type_hisdetil);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HisReportDetilActivity.this.finish();
            }
        });
    }

    ProgressDialog mProgressDialog;
    private void downMp4Play(String url) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            mProgressDialog = new ProgressDialog(HisReportDetilActivity.this);
            File file  = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/zig");
            if(!file.exists()){
                file.mkdirs();
            }
            Log.i(tag, "downMp4Play: path=="+file.getAbsolutePath());
            RequestParams requestParams = new RequestParams(url);
            // 为RequestParams设置文件下载后的保存路径
            requestParams.setSaveFilePath(file.getAbsolutePath()+File.separator);
            // 下载完成后自动为文件命名
            requestParams.setAutoRename(true);
            x.http().get(requestParams, new Callback.ProgressCallback<File>() {

                @Override
                public void onSuccess(File result) {
                    Log.i(tag, "下载成功"+result.getAbsolutePath());
                    mProgressDialog.dismiss();
                    VideoFragment bigPic = VideoFragment.newInstance(result.getAbsolutePath());
                    android.app.FragmentManager mFragmentManager = getFragmentManager();
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    transaction.replace(R.id.rl_play, bigPic);
                    transaction.commit();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.i(tag, "下载失败");
                    mProgressDialog.dismiss();
                }

                @Override
                public void onCancelled(CancelledException cex) {
                    Log.i(tag, "取消下载");
                    mProgressDialog.dismiss();
                }

                @Override
                public void onFinished() {
                    Log.i(tag, "结束下载");
                    mProgressDialog.dismiss();
                }

                @Override
                public void onWaiting() {
                    // 网络请求开始的时候调用
                    Log.i(tag, "等待下载");
                }

                @Override
                public void onStarted() {
                    // 下载的时候不断回调的方法
                    Log.i(tag, "开始下载");
                }

                @Override
                public void onLoading(long total, long current, boolean isDownloading) {
                    // 当前的下载进度和文件总大小
                    Log.i(tag, "正在下载中......");
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgressDialog.setMessage("正在下载中......");
                    mProgressDialog.show();
                    mProgressDialog.setMax((int) total);
                    mProgressDialog.setProgress((int) current);
                }
            });
        }
    }

}
