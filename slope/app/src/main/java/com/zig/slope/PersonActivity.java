package com.zig.slope;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zig.slope.common.base.bean.LoginBean;
import com.zig.slope.common.utils.PreferenceManager;

import java.lang.reflect.Field;

public class PersonActivity extends BaseActivity {
    EditText place_edit;
    TextView start_place_edit,destination_edit,start_place_edit2,destination_edit2;
    LinearLayout place_search_layout;
    RelativeLayout title_content_layout;
    PreferenceManager pm;
    String tagc = "0";
    private LoginBean bean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        Intent intent = getIntent();
        bean = (LoginBean) intent.getSerializableExtra("logindata");
        pm = PreferenceManager.getInstance(this);
        setStatusBar();
        initView();
    }

    public void initView(){
        place_edit = (EditText) findViewById(R.id.place_edit);
        start_place_edit =findViewById(R.id.start_place_edit);
        destination_edit = findViewById(R.id.destination_edit);
        start_place_edit2 =findViewById(R.id.start_place_edit2);
        destination_edit2 = findViewById(R.id.destination_edit2);
        place_search_layout = (LinearLayout) findViewById(R.id.place_search_layout);
        title_content_layout = (RelativeLayout) findViewById(R.id.title_content_layout);
        start_place_edit.setText("用户名："+bean.getOperatorName());
        destination_edit.setText("用户ID："+bean.getOperatorID());
        String admin = null;
        if(bean.getOperatorLevel().equals("1")){
            admin = "admin";
        }
        if(bean.getOperatorLevel().equals("2")){
            admin = "领导";
        }
        if(bean.getOperatorLevel().equals("3")){
            admin = "管理员";
        }
        if(bean.getOperatorLevel().equals("4")){
            admin = "巡查员";
        }
        start_place_edit2.setText("管理权限："+admin);
        destination_edit2.setText("联系电话："+bean.getOperatorTel());
    }

    public void showInputDestination(View view) {
        String tag = view.getTag().toString();
        place_edit.requestFocus();
        setStatusBarLayout();
        title_content_layout.setVisibility(View.GONE);
        place_search_layout.setVisibility(View.VISIBLE);
        place_edit.setHint(getString(R.string.hint_tip));
        tagc = tag;
    }



    public void backFromSearchPlace(View view) {
        setStatusBar();
        place_edit.setText("");
        title_content_layout.setVisibility(View.VISIBLE);
        place_search_layout.setVisibility(View.GONE);
    }

    public void backFromEnter(View view) {
        setStatusBar();
        if(tagc.equals("1")) {
            start_place_edit.setText("用户名："+place_edit.getText());
        }else  if(tagc.equals("2"))
            {
            destination_edit.setText("用户ID："+place_edit.getText());
        }else  if(tagc.equals("3"))
        {
            start_place_edit2.setText(place_edit.getText());
        }else{
            destination_edit2.setText(place_edit.getText());
        }
        place_edit.setText("");
        title_content_layout.setVisibility(View.VISIBLE);
        place_search_layout.setVisibility(View.GONE);
    }
}
