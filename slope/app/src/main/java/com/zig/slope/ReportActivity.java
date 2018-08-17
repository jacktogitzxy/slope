package com.zig.slope;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.cjt2325.cameralibrary.util.FileUtil;
import com.zig.slope.common.utils.PreferenceManager;
import com.zig.slope.view.MyImageBt;
import com.zig.slope.view.PicFragment;
import com.zig.slope.view.VideoFragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;
import org.xutils.x;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import slope.zxy.com.login_module.LoginMActivity;


public class ReportActivity extends BaseActivity {
    private ImageView video_upload;//,photo_upload;
    // 文件路径
//    private String path = "";
//    private String vpath = "";
    private TextView report_name1, report_name, report_worker,report_date;// report_worker_tel,
    private EditText report_note;
    // 播放按钮
    private MyImageBt [] plays;
    private PreferenceManager pm;
    private String Pname,userName;
    private String TAG="REPORT";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        setStatusBar();
        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra("pname") != null) {
            Pname = intent.getStringExtra("pname");
        }
        pm = PreferenceManager.getInstance(ReportActivity.this);
        userName = pm.getPackage("operatorName");
        initView();
        initListener();
    }

    private void initListener() {

        video_upload.setOnClickListener(onclicks);
//        photo_upload.setOnClickListener(onclicks);
        for (int i=0;i<plays.length;i++){
            plays[i].setOnClickListener(new View.OnClickListener() {
                @SuppressLint("NewApi")
                @Override
                public void onClick(View v) {
                    // 显示播放页面
                    MyImageBt btn = (MyImageBt) v;
                    int type = btn.getType();
                    if (type == 2) {
                        String vpath = btn.getPath();
                        if (vpath != null && !vpath.equalsIgnoreCase("")) {
                            VideoFragment bigPic = VideoFragment.newInstance(vpath);
                            android.app.FragmentManager mFragmentManager = getFragmentManager();
                            FragmentTransaction transaction = mFragmentManager.beginTransaction();
                            transaction.replace(R.id.main_menu, bigPic);
                            transaction.commit();
                        }
                    }
                    if(type==1){
                        String path = btn.getPath();
                        if (path != null && !path.equalsIgnoreCase("")) {
                            PicFragment pcf = PicFragment.newInstance(path);
                            android.app.FragmentManager mFragmentManager = getFragmentManager();
                            FragmentTransaction transaction = mFragmentManager.beginTransaction();
                            transaction.replace(R.id.main_menu, pcf);
                            transaction.commit();
                        }
                    }
                }
            });
            plays[i].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    MyImageBt btn = (MyImageBt) view;
                    btn.setType(0);
                    btn.setPath(null);
                    btn.setImageResource(R.mipmap.addimage);
                    return true;
                }
            });
        }

    }

    private void initView() {
        video_upload = (ImageView) findViewById(R.id.video_upload);
        plays = new MyImageBt[4];
        plays[0] = (MyImageBt) findViewById(R.id.play);
        plays[1] = (MyImageBt) findViewById(R.id.play0);
        plays[2] = (MyImageBt) findViewById(R.id.play1);
        plays[3] = (MyImageBt) findViewById(R.id.play2);
        report_name1 = (TextView) findViewById(R.id.report_name1);
        report_worker = (TextView) findViewById(R.id.report_worker);
//        report_worker_tel = (TextView) findViewById(R.id.report_worker_tel);
        report_date = (TextView) findViewById(R.id.report_date);
        report_note = (EditText) findViewById(R.id.report_note);
        report_worker.setText(pm.getPackage("operatorName"));
        if (Pname != null) {//补充数据
            report_name1.setText(Pname);
        }
        report_worker.setText(userName);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
//获取当前时间
        Date date = new Date(System.currentTimeMillis());
        report_date.setText("上报日期：" + simpleDateFormat.format(date));
    }
//上传数据
    public void startReportdo(View v) {
        String text = report_note.getText().toString();
        String slopeId = report_name1.getText().toString();
        String operatorID = pm.getPackage("operatorId");
        upLaodImg(operatorID,slopeId,text,plays[0].getPath(),plays[1].getPath(),plays[2].getPath(),plays[3].getPath());
    }

    View.OnClickListener onclicks = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(ReportActivity.this, CameraActivity.class);
            startActivityForResult(intent, 100);
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 101) {
            String vpath = null;
            String  path = data.getStringExtra("bpath");
            boolean isvideo = data.getBooleanExtra("isvideo",false);
            if(isvideo) {
                vpath = data.getStringExtra("url");
            }
            View view = View.inflate(ReportActivity.this, R.layout.photosign, null);
            // 填充数据
            ImageView iconView = (ImageView) view.findViewById(R.id.photo_icon);
            TextView nameView = (TextView) view.findViewById(R.id.photo_name);
            TextView nameView1 = (TextView) view.findViewById(R.id.photo_name1);
            TextView nameView2 = (TextView) view.findViewById(R.id.photo_name2);
            iconView.setImageBitmap(BitmapFactory.decodeFile(path));
            nameView.setText("巡查员姓名:"+userName);
            nameView1.setText(report_date.getText());
            nameView2.setText("巡查地点:"+report_name1.getText());
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(view);
            for(int i = 0;i<plays.length;i++){
                if(plays[i].getType()==0){
                    if(isvideo) {
                        plays[i].setType(2);
                        plays[i].setPath(vpath);
                    }else{
                        plays[i].setType(1);
                        plays[i].setPath(FileUtil.saveBitmap("JCamera",bitmap.getBitmap()));
                    }
                    plays[i].setImageBitmap(bitmap.getBitmap());
                    return;
                }
            }
        }
        if (resultCode == 103) {
            Toast.makeText(this, "请检查相机权限~", Toast.LENGTH_SHORT).show();
        }
    }



//http://divitone.3322.org:8081/fx/filesUpload?slopeCode=admin&patrollerID=277&isContainPic=1&isContainVideo=1&videoAddress=d:/123

    public void upLaodImg( final String... param) {
        final ProgressDialog dia = new ProgressDialog(this);
        dia.setMessage("上报中....");
        dia.show();
        RequestParams params = new RequestParams("http://divitone.3322.org:8081/fx/filesUpload");//参数是路径地址
        List<KeyValue> list = new ArrayList<>();
        for (int i = 3; i < param.length; i++) {
            try {
                list.add(new KeyValue("files",new File(param[i])));
            } catch (Exception e) {
            }
        }
        Log.i(TAG, "upLaodImg:  param[0]=="+ param[0]);
        list.add(new KeyValue("patrollerID", param[0]));
        list.add(new KeyValue("newName", param[1]));
        list.add(new KeyValue("contents", param[2]));
        Log.i(TAG, "upLaodImg: list==="+list.size());
        //设置编码格式为UTF-8，保证参数不乱码
        MultipartBody body = new MultipartBody(list, "UTF-8");
        params.setRequestBody(body);
        params.setMultipart(true);

        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result=="+result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                dia.dismiss();
                Toast.makeText(ReportActivity.this,"上传失败，服务器繁忙",Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onError: ");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.i(TAG, "onCancelled: ");
            }

            @Override
            public void onFinished() {

                dia.dismiss();
                Toast.makeText(ReportActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
              //  ReportActivity.this.finish();
                Log.i(TAG, "onFinished: ");
                ReportActivity.this.finish();
            }
        });
    }
}
