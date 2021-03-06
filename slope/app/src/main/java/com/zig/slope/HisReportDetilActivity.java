package com.zig.slope;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zig.slope.bean.UserLoacl;
import com.zig.slope.callback.RequestCallBack;
import com.zig.slope.common.Constants.Constant;
import com.zig.slope.common.base.bean.HisReport;
import com.zig.slope.common.utils.TimeUtils;
import com.zig.slope.util.OkhttpWorkUtil;
import com.zig.slope.view.PicFragment;
import com.zig.slope.view.VideoFragment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;


public class HisReportDetilActivity extends AppCompatActivity {
    private HisReport data;
    private Toolbar toolbar;
    private BGABanner bgaBanner;
    AppCompatTextView textTitle;
    AppCompatTextView textAuthor;
    AppCompatTextView textTime,text_view_content;
    private String tag = HisReportDetilActivity.class.getName();
    private ScrollView msgView;
    private LinearLayout place_search_layout;
    private EditText place_edit;
    private  AppCompatTextView[]cbs;
    private CheckBox ckeck_1,ckeck_2,ckeck_3;
    private AppCompatTextView start_place_edit,destination_edit,start_place_edit2,destination_edit2,
            start_place_edit3,destination_edit3,enterbt;
    String tagc = "0";
    private String operatorLevel,operatorName;
    private int flag;
    private RadioGroup group;
    String isreport = "2";//不上报
    private OkhttpWorkUtil okhttpWorkUtil;
    private int type;//类别
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
        operatorLevel = intent.getStringExtra("operatorLevel");
        operatorName = intent.getStringExtra("operatorName");
        flag = intent.getIntExtra("flag",0);//1未审核2管理审核3领导审核4
        type = intent.getIntExtra("type",1);
        initView();
        initData(data);

    }
    List<String> imgs = null;
    private void initData(HisReport data) {
        Log.i("zxy", "initData: operatorLevel=="+operatorLevel);
        if("3".equals(operatorLevel)&&flag==1) {
            enterbt.setVisibility(View.VISIBLE);
            group.setVisibility(View.VISIBLE);
            ckeck_1.setVisibility(View.VISIBLE);
            if(okhttpWorkUtil==null){
                okhttpWorkUtil = new OkhttpWorkUtil(HisReportDetilActivity.this,okcallBack);
             }
            start_place_edit.setText(getResources().getString(R.string.guanli) + operatorName);
        }else{
            start_place_edit.setText(getResources().getString(R.string.guanli) + data.getAdmins());
        }
        if("2".equals(operatorLevel)&&flag==2) {
            enterbt.setVisibility(View.VISIBLE);
            group.setVisibility(View.VISIBLE);
            ckeck_2.setVisibility(View.VISIBLE);
            if(okhttpWorkUtil==null){
                okhttpWorkUtil = new OkhttpWorkUtil(HisReportDetilActivity.this,okcallBack);
            }
            start_place_edit2.setText(getResources().getString(R.string.lingdao)+operatorName);
        }else{
            start_place_edit2.setText(getResources().getString(R.string.lingdao)+data.getLeaders());
        }
        if("1".equals(operatorLevel)&&flag==3) {
            enterbt.setVisibility(View.VISIBLE);
//            group.setVisibility(View.VISIBLE);
            ckeck_3.setVisibility(View.VISIBLE);
            if(okhttpWorkUtil==null){
                okhttpWorkUtil = new OkhttpWorkUtil(HisReportDetilActivity.this,okcallBack);
            }
            start_place_edit3.setText(getResources().getString(R.string.sjlingdao)+operatorName);
        }else{
            start_place_edit3.setText(getResources().getString(R.string.sjlingdao)+data.getSeniors());
        }
        textTime.setText("上报时间："+ TimeUtils.transleteTime(data.getCreateTime()));
        textAuthor.setText("巡查员："+data.getRemark());
        Drawable drawable = null;
        if(type==1) {
            textTitle.setText("边坡编号：" + data.getNewName());
            drawable = getResources().getDrawable(R.mipmap.solpeiconleft);
        }else  if(type==3){
            textTitle.setText("三防编号：" + data.getT_id());
            drawable = getResources().getDrawable(R.mipmap.sficonleft);
        }
        else  if(type==2){
            textTitle.setText("危房编号：" + data.getDid());
            drawable = getResources().getDrawable(R.mipmap.sficonleft);
        }
        else  if(type==4){
            textTitle.setText("地陷编号：" + data.getSid());
            drawable = getResources().getDrawable(R.mipmap.dxleft);
        }
        else  if(type==5){
            textTitle.setText("工地编号：" + data.getCid());
            drawable = getResources().getDrawable(R.mipmap.gdleft);
        }
        else  if(type==6){
            textTitle.setText("河道编号：" + data.getSid());
            drawable = getResources().getDrawable(R.mipmap.hdleft);
        }
        drawable.setBounds(0, 0, 50, 70);
        textTitle.setCompoundDrawables(drawable,null,null,null);
        text_view_content.setText("情况描述："+data.getContents());
        destination_edit.setText(getResources().getString(R.string.yijian)+data.getAdminsContent());
        destination_edit2.setText(getResources().getString(R.string.yijian)+data.getLeadersContent());
        destination_edit3.setText(getResources().getString(R.string.yijian)+data.getSeniorsContent());
        final String msg = data.getUrl1().trim();
        String vsg = data.getUrl2().trim();

        if(msg!=null&&msg.length()>0) {
            List<String>  imgsa = Arrays.asList(msg.split("#"));
            imgs = new ArrayList<>(imgsa);
        }
        if(vsg!=null&&vsg.length()>0){
            List<String> vsgs =Arrays.asList(vsg.split("#"));
            if(imgs!=null) {
                imgs.addAll(vsgs);
            }else {
                imgs = vsgs;
            }
        }
        bgaBanner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                model = model.trim();
                if(model.endsWith("jpg")) {
                    Glide.with(HisReportDetilActivity.this)
                            .load(Constant.BASE_URL + model)
                            .placeholder(R.mipmap.webwxgetmsgimg5)
                            .error(R.mipmap.icon_error)
                            .centerCrop()
                            .dontAnimate()
                            .into(itemView);
                }else  if(model.endsWith("mp4")){
                    itemView.setImageResource(R.mipmap.mediaicon);
                    itemView.setTag(model);
                }else{//unkunw
                    Log.i("zxy", "fillBannerItem: unkunw  data  model=="+model);
                }
            }
        });
        bgaBanner.setData(imgs,null);
        bgaBanner.setDelegate(dl);
        String remark1 = data.getRemark1();
        if(remark1!=null&&!remark1.equals("")){
            List<String>  valuesa = Arrays.asList(remark1.split("#"));
            if(valuesa!=null&&valuesa.size()==4){
                for (int i = 0;i<4;i++){
                    Log.i("zxy", "initData: valuesa.get(i)="+valuesa.get(i));
                    if(valuesa.get(i).trim().equals("无")){
                        cbs[i].setText(cbs[i].getText()+"            正常");}
                        else {
                        cbs[i].setTextColor(getResources().getColor(R.color.red));
                        cbs[i].setText(cbs[i].getText()+"            异常");
                    }
                }
            }
        }else{
            for (int i = 0;i<4;i++){
                    cbs[i].setText(cbs[i].getText()+"            正常");
                }
            }
    }

    BGABanner.Delegate dl = new BGABanner.Delegate() {
        @Override
        public void onBannerItemClick(BGABanner bgaBanner, View view, Object o, int i) {
            String tag = view.getTag().toString();
            Log.i("zxy", "onBannerItemClick: tag=="+tag);
            if(tag.endsWith(".mp4")){
                //下载播放
                VideoFragment bigPic = VideoFragment.newInstance(Constant.BASE_URL + imgs.get(i));
                bigPic.setWH(true);
                android.app.FragmentManager mFragmentManager = getFragmentManager();
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.replace(R.id.rl_play, bigPic);
                transaction.commit();
            }else{
                Log.i("zxy", "onBannerItemClick: ===="+Constant.BASE_URL+imgs.get(i));
                PicFragment pcf = PicFragment.newInstance(Constant.BASE_URL+imgs.get(i));
                android.app.FragmentManager mFragmentManager = getFragmentManager();
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.replace(R.id.rl_play, pcf);
                transaction.commit();
            }
        }
    };
    private void initView() {
        textTitle = findViewById(R.id.text_view_title);
        textAuthor = findViewById(R.id.text_view_author);
        textTime = findViewById(R.id.text_view_time);
        bgaBanner = findViewById(R.id.numBanner);
        text_view_content = findViewById(R.id.text_view_content);
        msgView = findViewById(R.id.msgView);
        place_search_layout = findViewById(R.id.place_search_layout);
        start_place_edit =findViewById(R.id.start_place_edit);
        destination_edit = findViewById(R.id.destination_edit);
        start_place_edit2 =findViewById(R.id.start_place_edit2);
        destination_edit2 = findViewById(R.id.destination_edit2);
        start_place_edit3 =findViewById(R.id.start_place_edit3);
        destination_edit3 = findViewById(R.id.destination_edit3);
        enterbt = findViewById(R.id.enterbt);
        enterbt.setOnClickListener(onclick);
        place_edit = (EditText) findViewById(R.id.place_edit);
        toolbar = (Toolbar) findViewById(R.id.toolbarhisDetil);
        toolbar.setNavigationIcon(R.mipmap.return_icon);
        toolbar.setTitleMarginStart(250);
//        toolbar.setTitleMarginStart(200);
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setTitle(R.string.type_hisdetil);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HisReportDetilActivity.this.finish();
            }
        });
        cbs = new AppCompatTextView[4];
        cbs[0] = findViewById(R.id.text_view_cb);
        cbs[1] = findViewById(R.id.text_view_cb1);
        cbs[2] = findViewById(R.id.text_view_cb2);
        cbs[3] = findViewById(R.id.text_view_cb3);
        ckeck_1 = findViewById(R.id.ckeck_1);
        ckeck_2 = findViewById(R.id.ckeck_2);
        ckeck_3 = findViewById(R.id.ckeck_3);
        if(type==1){
            cbs[0].setText(getResources().getString(R.string.danger1));
            cbs[1].setText(getResources().getString(R.string.danger2));
            cbs[2].setText(getResources().getString(R.string.danger3));
            cbs[3].setText(getResources().getString(R.string.danger4));
        }
        if(type==3){
            cbs[0].setText(getResources().getString(R.string.danger5));
            cbs[1].setText(getResources().getString(R.string.danger6));
            cbs[2].setText(getResources().getString(R.string.danger7));
            cbs[3].setText(getResources().getString(R.string.danger8));
        }
        if(type==4){
            cbs[0].setText(getResources().getString(R.string.danger9));
            cbs[1].setText(getResources().getString(R.string.danger10));
            cbs[2].setText(getResources().getString(R.string.danger11));
            cbs[3].setText(getResources().getString(R.string.danger12));
        }
        if(type==5){
            cbs[0].setText(getResources().getString(R.string.danger13));
            cbs[1].setText(getResources().getString(R.string.danger14));
            cbs[2].setText(getResources().getString(R.string.danger15));
            cbs[3].setText(getResources().getString(R.string.danger16));
        }
        if(type==6){
            cbs[0].setText(getResources().getString(R.string.danger17));
            cbs[1].setText(getResources().getString(R.string.danger18));
            cbs[2].setText(getResources().getString(R.string.danger19));
            cbs[3].setText(getResources().getString(R.string.danger20));
        }
        group = findViewById(R.id.radioGroupReport);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.noreport) {
                    isreport = "2";
                }
                if (checkedId == R.id.yesreport) {
                    isreport = "1";
                }

            }
        });
    }
    public void showInputDestination(View view) {
        if(enterbt.getVisibility()==View.VISIBLE){
            if(view.getId()==R.id.start_place_edit||view.getId()==R.id.destination_edit){//管理
                if("3".equals(operatorLevel)){
                    startEdit(view);
                }else{
                    Toast.makeText(HisReportDetilActivity.this,"您不是管理员!",Toast.LENGTH_SHORT).show();
                }
            }
            if(view.getId()==R.id.start_place_edit2||view.getId()==R.id.destination_edit2){//领导
                if("2".equals(operatorLevel)){
                    startEdit(view);
                }else{
                    Toast.makeText(HisReportDetilActivity.this,"您不是领导!",Toast.LENGTH_SHORT).show();
                }
            }
            if(view.getId()==R.id.start_place_edit3||view.getId()==R.id.destination_edit3){//上级管理
                if("1".equals(operatorLevel)){
                    startEdit(view);
                }else{
                    Toast.makeText(HisReportDetilActivity.this,"您无权限!",Toast.LENGTH_SHORT).show();
                }
            }
            if(view.getId()==R.id.ckeck_1){
                if(ckeck_1.isChecked()) {
                    destination_edit.setText(getResources().getString(R.string.yijian)+getResources().getString(R.string.quickCkeck));
                }else{
                    destination_edit.setText(getResources().getString(R.string.yijian));
                }
            }
            if(view.getId()==R.id.ckeck_2){
                if(ckeck_2.isChecked()) {
                    destination_edit2.setText(getResources().getString(R.string.yijian)+getResources().getString(R.string.quickCkeck));
                }else{
                    destination_edit2.setText(getResources().getString(R.string.yijian));
                }
            }
            if(view.getId()==R.id.ckeck_3){
                if(ckeck_3.isChecked()) {
                    destination_edit3.setText(getResources().getString(R.string.yijian)+getResources().getString(R.string.quickCkeck));
                }else{
                    destination_edit3.setText(getResources().getString(R.string.yijian));
                }
            }

        }

    }

    public void startEdit(View view){
        String tag = view.getTag().toString();
        place_edit.requestFocus();
        msgView.setVisibility(View.GONE);
        place_search_layout.setVisibility(View.VISIBLE);
        place_edit.setHint(getString(R.string.hint_tip));
        tagc = tag;
    }
    public void backFromEditPlace(View view) {
        place_edit.setText("");
        msgView.setVisibility(View.VISIBLE);
        place_search_layout.setVisibility(View.GONE);
    }
    public void backFromEnter(View view) {
        hideKeyBorad(place_edit);
        if(tagc.equals("1")) {
            start_place_edit.setText(getResources().getString(R.string.guanli)+place_edit.getText());
        }else if(tagc.equals("2"))
        {
            destination_edit.setText(getResources().getString(R.string.yijian)+place_edit.getText());
        }else  if(tagc.equals("3"))
        {
            start_place_edit2.setText(getResources().getString(R.string.lingdao)+place_edit.getText());
        }else if(tagc.equals("4")){
            destination_edit2.setText(getResources().getString(R.string.yijian)+place_edit.getText());
        }
        else  if(tagc.equals("5"))
        {
            start_place_edit3.setText(getResources().getString(R.string.sjlingdao)+place_edit.getText());
        }else if(tagc.equals("6")){
            destination_edit3.setText(getResources().getString(R.string.yijian)+place_edit.getText());
        }
        place_edit.setText("");
        msgView.setVisibility(View.VISIBLE);
        place_search_layout.setVisibility(View.GONE);
    }
    private void hideKeyBorad(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    View.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i("zxcc", "onClick: isreport="+isreport);
            if ("3".equals(operatorLevel)){//管理
                String contents = destination_edit.getText().toString().trim();
                String id =String.valueOf(data.getId());
                okhttpWorkUtil.postAsynHttpHis(Constant.BASE_URL+"adminShenHeApp",id,operatorName,contents.substring(11),type+"",isreport);

            }
            if ("2".equals(operatorLevel)){//领导
                String contents = destination_edit2.getText().toString().trim();
                String id =String.valueOf(data.getId());
                okhttpWorkUtil.postAsynHttpHis(Constant.BASE_URL + "LeaderShenHeApp", id, operatorName, contents.substring(11),type+"",isreport);
            }
            if ("1".equals(operatorLevel)){//1领导
                String contents = destination_edit3.getText().toString().trim();
                String id =String.valueOf(data.getId());
                okhttpWorkUtil.postAsynHttpHis(Constant.BASE_URL + "seniorsShenHeApp", id, operatorName, contents.substring(11),type+"","0");
            }
        }
    };

    RequestCallBack okcallBack = new RequestCallBack() {
        @Override
        public void onSuccess(List<UserLoacl> response) {
           HisReportDetilActivity.this.finish();
        }
        @Override
        public void onFail(String msg) {

        }
    };
}
