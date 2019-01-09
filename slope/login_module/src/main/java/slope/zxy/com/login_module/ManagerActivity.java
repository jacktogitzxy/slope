package slope.zxy.com.login_module;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.zig.slope.common.base.bean.LoginMsg;

import slope.zxy.com.login_module.widget.LoadingDialog;

public class ManagerActivity extends AppCompatActivity {
    private TextView mangerName,news_item_content_text_view,
    typehd,typegd,typeDx,typeHouse,typeSf,typeSlope;
    private LoginMsg data;
    private LoadingDialog mLoadingDialog; //显示正在加载的对话框
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        initView();
    }

    private void initView() {
        mangerName = findViewById(R.id.mangerName);
        news_item_content_text_view =findViewById(R.id.news_item_content_text_view);
        typehd = findViewById(R.id.typehd);
        typegd = findViewById(R.id.typegd);
        typeDx = findViewById(R.id.typeDx);
        typeHouse = findViewById(R.id.typeHouse);
        typeSf = findViewById(R.id.typeSf);
        typeSlope = findViewById(R.id.typeSlope);
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        data = (LoginMsg) intent.getSerializableExtra("data");
        mangerName.setText(type);
        if(type.equals(getResources().getString(R.string.type_gt))){
            typehd.setVisibility(View.GONE);
            typegd.setVisibility(View.GONE);
            typeDx.setVisibility(View.GONE);
            typeHouse.setVisibility(View.GONE);
            typeSf.setVisibility(View.GONE);
            news_item_content_text_view.setText(getResources().getString(R.string.gt_content));
        }
        if(type.equals(getResources().getString(R.string.type_jt))){
            news_item_content_text_view.setText(getResources().getString(R.string.jt_content));
        }
        if(type.equals(getResources().getString(R.string.type_hs))){
            typeDx.setVisibility(View.GONE);
            typeHouse.setVisibility(View.GONE);
            typeSlope.setVisibility(View.GONE);
            news_item_content_text_view.setText(getResources().getString(R.string.hs_content));
        }
        if(type.equals(getResources().getString(R.string.type_zj))){
            typeSlope.setVisibility(View.GONE);
            typeSf.setVisibility(View.GONE);
            typehd.setVisibility(View.GONE);
            news_item_content_text_view.setText(getResources().getString(R.string.zj_content));
        }
    }

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

        if(view.getId()==R.id.typeDx){
            ARouter.getInstance().build("/map/index").withSerializable("data",data).withInt("type",4).navigation();
            showLoading();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideLoading();
                }
            },1000);
        }
        if(view.getId()==R.id.typegd){
            ARouter.getInstance().build("/map/index").withSerializable("data",data).withInt("type",5).navigation();
            showLoading();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideLoading();
                }
            },1000);
        }

        if(view.getId()==R.id.typehd){
            ARouter.getInstance().build("/map/index").withSerializable("data",data).withInt("type",6).navigation();
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
                    ManagerActivity.this.finish();
                }
            });

        }
    }
}
