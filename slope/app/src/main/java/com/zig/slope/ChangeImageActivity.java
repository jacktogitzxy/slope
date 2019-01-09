package com.zig.slope;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.zig.slope.common.Constants.Constant;
import com.zig.slope.common.utils.CustomProgressDialog;
import com.zig.slope.view.MyImageBt;
import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;
import org.xutils.x;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
public class ChangeImageActivity extends AppCompatActivity {
    private static final String TAG = ChangeImageActivity.class.getName();
    private Toolbar toolbar;
    private MyImageBt[] plays;
    private ImageView video_upload;
    private CustomProgressDialog progressDialog;
    private String id;
    private int type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        id = intent.getStringExtra("pname");
        type = intent.getIntExtra("type",1);
        Log.i(TAG, "onCreate: type=="+type);
        setContentView(R.layout.activity_change_image);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("上传编号:"+id+"图片信息");
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.return_icon);
        toolbar.setTitleMarginStart(150);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             ChangeImageActivity.this.finish();
            }
        });
        plays = new MyImageBt[4];
        plays[0] = (MyImageBt) findViewById(R.id.play);
        plays[1] = (MyImageBt) findViewById(R.id.play0);
        plays[2] = (MyImageBt) findViewById(R.id.play1);
        plays[3] = (MyImageBt) findViewById(R.id.play2);
        video_upload = (ImageView) findViewById(R.id.video_upload);
        video_upload.setOnClickListener(onclicks);
        createProgressDialog(ChangeImageActivity.this, false);
    }
    View.OnClickListener onclicks = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(ChangeImageActivity.this, CameraActivity.class);
            startActivityForResult(intent, 100);
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 101) {
            String  path = data.getStringExtra("bpath");
            for(int i = 0;i<plays.length;i++){
                if(plays[i].getType()==0){
                        plays[i].setType(1);
                        plays[i].setPath(path);
                        plays[i].setImageBitmap(BitmapFactory.decodeFile(path));
                    return;
                }
            }
        }
        if (resultCode == 103) {
            Toast.makeText(this, "请检查相机权限~", Toast.LENGTH_SHORT).show();
        }
    }
    public void startUpload(View v) {
        upLaodImg(id,type+"",plays[0].getPath(),plays[1].getPath(),plays[2].getPath(),plays[3].getPath());
    }
    public void upLaodImg( final String... param) {
        showProgressDialog();
        RequestParams params = new RequestParams(Constant.BASE_URL+"changeAllPicApp");//参数是路径地址
        List<KeyValue> list = new ArrayList<>();
        for (int i = 2; i < param.length; i++) {
            try {
                Log.i(TAG, "upLaodImg: param[i]=="+param[i]);
                list.add(new KeyValue("files",new File(param[i])));
            } catch (Exception e) {
            }
        }
        if(type==1) {
            list.add(new KeyValue("newName", param[0]));
        }
        list.add(new KeyValue("id", param[0]));
        list.add(new KeyValue("type_s", param[1]));
        Log.i(TAG, "upLaodImg: type=="+param[1]);
        Log.i(TAG, "upLaodImg: list=="+list.size());
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
                stopProgressDialog();
                Toast.makeText(ChangeImageActivity.this,"上传失败，服务器繁忙",Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onError: "+ex.getMessage());
                Log.i(TAG, "onError: "+ex.getLocalizedMessage());
                Log.i(TAG, "onError: "+ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.i(TAG, "onCancelled: ");
            }

            @Override
            public void onFinished() {
                stopProgressDialog();
                Toast.makeText(ChangeImageActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onFinished: ");
                ChangeImageActivity.this.finish();
            }
        });
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
