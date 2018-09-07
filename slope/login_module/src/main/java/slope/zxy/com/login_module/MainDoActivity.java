package slope.zxy.com.login_module;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.zig.slope.common.base.bean.LoginMsg;

import cn.bingoogolapple.bgabanner.BGABanner;
import slope.zxy.com.login_module.widget.LoadingDialog;

public class MainDoActivity extends AppCompatActivity {
    private BGABanner bgaBanner;
    private TextView contentv;
    private ScrollView contentScro;
    private LoginMsg data;
    private LoadingDialog mLoadingDialog; //显示正在加载的对话框
    private ImageView splash_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maindo);
        Intent intent = getIntent();
        data = (LoginMsg) intent.getSerializableExtra("data");
        bgaBanner = findViewById(R.id.poitBanner);
        Log.i("zxy", "onCreate: bgaBanner=="+bgaBanner);
        bgaBanner.setData(R.mipmap.webwxgetmsgimg, R.mipmap.webwxgetmsgimg2, R.mipmap.webwxgetmsgimg3, R.mipmap.webwxgetmsgimg4,
                R.mipmap.webwxgetmsgimg5, R.mipmap.webwxgetmsgimg6, R.mipmap.webwxgetmsgimg7, R.mipmap.webwxgetmsgimg8,
                R.mipmap.webwxgetmsgimg9 );
        contentv = findViewById(R.id.news_item_content_text_view);
        contentScro = findViewById(R.id.contentScro);
        splash_img = (ImageView) findViewById(R.id.splash_img1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startSplsh();
            }
        },1000);
    }
    private View lastv;
    public void msgType(View view){
        if(lastv==null){
            lastv = findViewById(R.id.btn_gt);
        }
        lastv.setBackground(getResources().getDrawable(R.drawable.item_text_bg_no));
        lastv = view;
        contentScro.scrollTo(0,0);
        if(view.getId()==R.id.btn_gt){
            contentv.setText(R.string.gt_content);
        }
        if(view.getId()==R.id.btn_jt){
            contentv.setText(R.string.jt_content);
        }
        if(view.getId()==R.id.btn_zj){
            contentv.setText(R.string.zj_content);
        }
        if(view.getId()==R.id.btn_hs){
            contentv.setText(R.string.hs_content);
        }
        view.setBackground(getResources().getDrawable(R.drawable.item_text_bg));
    }


    public void toMapMain(View view){
        if(view.getId()==R.id.typeSlope){
            ARouter.getInstance().build("/map/index").withSerializable("data",data).navigation();
            showLoading();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideLoading();
                }
            },1000);
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

    public void startSplsh(){
        Animator animator = AnimatorInflater.loadAnimator(MainDoActivity.this, R.animator.splashlogin);
        animator.setTarget(splash_img);
        animator.start();
    }
}
