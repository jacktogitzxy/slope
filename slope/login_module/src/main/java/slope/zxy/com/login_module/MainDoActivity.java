package slope.zxy.com.login_module;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.zig.slope.common.Constants.Constant;
import com.zig.slope.common.base.bean.LoginMsg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import slope.zxy.com.login_module.widget.LoadingDialog;

public class MainDoActivity extends AppCompatActivity {
    private BGABanner bgaBanner;
//    private TextView contentv;
//    private ScrollView contentScro;
    private LoginMsg data;
    private LoadingDialog mLoadingDialog; //显示正在加载的对话框
//    private ImageView splash_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maindo);
        Intent intent = getIntent();
        data = (LoginMsg) intent.getSerializableExtra("data");
        bgaBanner = findViewById(R.id.poitBanner);
        LayoutInflater layoutInflater =LayoutInflater.from(this);
        List<String> bannerList =  Arrays.asList(data.getBannerList().split("#"));
        List<View> mods = new ArrayList<>();
        for (int i=0;i<bannerList.size();i++){
          //  View v1 = layoutInflater.inflate(R.layout.banner_img,null);
         //   ImageView vb = v1.findViewById(R.id.bannimg);
            ImageView view = new ImageView(MainDoActivity.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(params);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(this).load(Constant.BASE_URL+bannerList.get(i).trim()).placeholder(R.mipmap.webwxgetmsgimg5).into(view);
            mods.add(view);
        }
        bgaBanner.setData(mods);
//        contentv = findViewById(R.id.news_item_content_text_view);
//        contentScro = findViewById(R.id.contentScro);
//        splash_img = (ImageView) findViewById(R.id.splash_img1);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startSplsh();
//            }
//        },1000);
    }
//    private TextView lastv;
//    public void msgType(View view){
//        if(lastv==null){
//            lastv = findViewById(R.id.btn_gt);
//        }
//        lastv.setBackgroundColor(0);
//        lastv.setTextColor(getResources().getColor(R.color.c_gray_6));
//        lastv = (TextView) view;
//        contentScro.scrollTo(0,0);
//        if(view.getId()==R.id.btn_gt){
//            contentv.setText(R.string.gt_content);
//        }
//        if(view.getId()==R.id.btn_jt){
//            contentv.setText(R.string.jt_content);
//        }
//        if(view.getId()==R.id.btn_zj){
//            contentv.setText(R.string.zj_content);
//        }
//        if(view.getId()==R.id.btn_hs){
//            contentv.setText(R.string.hs_content);
//        }
//        lastv.setTextColor(getResources().getColor(R.color.white));
//        lastv.setBackgroundColor(getResources().getColor(R.color.color_back));
//    }


    public void toMapMain(View view){
        if(view.getId()==R.id.typeSlope){
            ARouter.getInstance().build("/map/index").withSerializable("data",data).withInt("type",1).navigation();
            showLoading();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideLoading();
                }
            },1000);
        }
        if(view.getId()==R.id.typeSf){
            ARouter.getInstance().build("/map/index").withSerializable("data",data).withInt("type",3).navigation();
            showLoading();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideLoading();
                }
            },1000);
        }
        if(view.getId()==R.id.typeHouse){
            ARouter.getInstance().build("/map/index").withSerializable("data",data).withInt("type",2).navigation();
            showLoading();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideLoading();
                }
            },1000);
        }
        if(view.getId()==R.id.btn_gt){
            Intent intent = new Intent(MainDoActivity.this,ManagerActivity.class);
            intent.putExtra("type",getResources().getString(R.string.type_gt));
            intent.putExtra("data",data);
            startActivity(intent);
        }
        if(view.getId()==R.id.btn_jt){
            Intent intent = new Intent(MainDoActivity.this,ManagerActivity.class);
            intent.putExtra("type",getResources().getString(R.string.type_jt));
            intent.putExtra("data",data);
            startActivity(intent);
        }
        if(view.getId()==R.id.btn_hs){
            Intent intent = new Intent(MainDoActivity.this,ManagerActivity.class);
            intent.putExtra("type",getResources().getString(R.string.type_hs));
            intent.putExtra("data",data);
            startActivity(intent);
        }
        if(view.getId()==R.id.btn_zj){
            Intent intent = new Intent(MainDoActivity.this,ManagerActivity.class);
            intent.putExtra("type",getResources().getString(R.string.type_zj));
            intent.putExtra("data",data);
            startActivity(intent);
        }
    }
    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this, getString(R.string.loading), false);
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
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){

                if((System.currentTimeMillis() - exitTime) > 2000){
                    Toast.makeText(MainDoActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                    return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

//    public void startSplsh(){
//        Animator animator = AnimatorInflater.loadAnimator(MainDoActivity.this, R.animator.splashlogin);
//        animator.setTarget(splash_img);
//        animator.start();
//    }
}
