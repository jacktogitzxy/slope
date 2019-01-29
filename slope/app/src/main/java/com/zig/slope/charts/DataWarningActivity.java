package com.zig.slope.charts;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.zig.slope.R;
import com.zig.slope.adapter.DatawAdapter;
import com.zig.slope.adapter.GrideAdapter;
import com.zig.slope.bean.DataWarnBean;
import com.zig.slope.callback.RequestVideoCallBack;
import com.zig.slope.common.Constants.Constant;
import com.zig.slope.common.base.BaseMvpActivity;
import com.zig.slope.common.base.bean.BaseResponseBean;
import com.zig.slope.common.base.bean.DataBean;
import com.zig.slope.contract.SensorContract;
import com.zig.slope.presenter.SensorPresenterImpl;
import com.zig.slope.util.OkhttpWorkUtil;


import org.easydarwin.video.EasyPlayerClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import slope.zxy.com.rtmp.Constante;
import slope.zxy.com.rtmp.VideoBean;

//http://120.79.174.224:8081/fx/queryMonitorByNewNameApp?newName=409 视频路径
public class DataWarningActivity extends BaseMvpActivity<SensorContract.SensorView,SensorPresenterImpl>
        implements SensorContract.SensorView,SensorEventListener {
    private List<DataWarnBean> dataBeans;
    private  String newName;
    private int type = 0,currentType;
    private RecyclerView recycler_view;
    private LinearLayout emptyView;
    private LinearLayoutManager layoutManager;
    private NestedScrollView nestedScrollView;
    private SwipeRefreshLayout refreshLayout;
    private DatawAdapter datawAdapter;
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    boolean isFirstLoc = true; // 是否首次定位
    private SensorManager mSensorManager;
    MapView mMapView;
    public BaiduMap mBaiduMap;
    private MyLocationData locData;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;
    private TextView displayName,displayName2;
    private Double lastX = 0.0;
    LatLng desLatLng = new LatLng(22.755346594895,113.91997779108);
    private GridView videogride;
    private   List<VideoBean> urls;
    private  GrideAdapter adapter;
    private OkhttpWorkUtil okhttpWorkUtil;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_data_warning;
    }
    @Override
    protected SensorPresenterImpl createPresenter() {
         return new SensorPresenterImpl();
    }
    @Override
    protected void findViews() {
        okhttpWorkUtil = new OkhttpWorkUtil(DataWarningActivity.this,null);
        recycler_view = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(DataWarningActivity.this);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setVisibility(View.VISIBLE);
        displayName = findViewById(R.id.displayName);
        displayName2 = findViewById(R.id.displayName2);
        emptyView = findViewById(R.id.empty_view);
        emptyView.setVisibility(View.INVISIBLE);
        videogride = findViewById(R.id.videogride);
        Intent intent = getIntent();
        type = intent.getIntExtra("type",2);
        currentType = intent.getIntExtra("currentType",1);
        newName = intent.getStringExtra("newName");
        desLatLng = intent.getParcelableExtra("zLatLng");
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDataw);
        toolbar.setNavigationIcon(R.mipmap.return_icon);
        toolbar.setTitleMarginStart(180);
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        if(type==2){
            toolbar.setTitle(getTitles(currentType)+newName+getResources().getString(R.string.sensorWarning));
            if(currentType==1) {
                displayName.setText("名称：" +intent.getStringExtra("zName"));
                displayName2.setText("地点：" +intent.getStringExtra("address"));
            }
        }
        if(type==4){
            toolbar.setTitle(newName+getResources().getString(R.string.sensorWarning2));
        }

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataWarningActivity.this.finish();
            }
        });
        nestedScrollView = findViewById(R.id.nested_scroll_view);
        refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(DataWarningActivity.this, R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
               //     Toast.makeText(DataWarningActivity.this,getResources().getString(R.string.full_text),Toast.LENGTH_SHORT).show();
                }
            }
        });

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//获取传感器管理服务
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
//        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        getVideos();

    }
    private String getTitles(int type){
        String s = null;
        if(type==1){
            s= "边坡编号";
        }
        if(type==3){
            s= "三防编号";
        }
        if(type==4){
            s= "地陷编号";
        }
        if(type==5){
            s= "工地编号";
        }
        if(type==6){
            s= "河道编号";
        }
        return  s;

    }
    @Override
    protected void setViews() {
    }

    @Override
    protected void getData() {
        getPresenter().requestSensorForcastData(DataWarningActivity.this,newName);
    }
    @Override
    public void onSensorSucess(BaseResponseBean<List<DataBean>> data) {

    }
    @Override
    public void onSensorFail(String msg) {

    }
    @Override
    public void onSensorSucessw(BaseResponseBean<List<DataWarnBean>> data) {
        dataBeans = data.getData();
        if(data.getCode()==2){
            showEmptyView(true);
            Toast.makeText(DataWarningActivity.this,getResources()
                    .getString(R.string.empty_tip),Toast.LENGTH_SHORT).show();
            return;
        }
        showEmptyView(false);
        if(datawAdapter==null) {
            datawAdapter  = new DatawAdapter(DataWarningActivity.this,dataBeans,new Handler(),newName);
            recycler_view.setAdapter(datawAdapter);
        }else{
            datawAdapter.updateData(dataBeans,true);
        }
        recycler_view.setAdapter(datawAdapter);
    }

    @Override
    public void onSensorFailw(String msg) {
        showEmptyView(false);
    }

    public void showEmptyView(boolean toShow) {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        });
        emptyView.setVisibility(toShow?View.VISIBLE:View.INVISIBLE);
        nestedScrollView.setVisibility(!toShow?View.VISIBLE:View.INVISIBLE);
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = sensorEvent.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(mCurrentLat)
                    .longitude(mCurrentLon).build();
            mBaiduMap.setMyLocationData(locData);
        }
        lastX = x;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (isFirstLoc) {
                isFirstLoc = false;
                setMark(desLatLng);
                initCity();
            }
        }
        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
    public static boolean isForeground = false;
    @Override
    protected void onPause() {
        isForeground = false;
        mMapView.onPause();
        stopPlay();
        super.onPause();
    }
    private void stopPlay(){
        if(urls==null||urls.size()==0||clients==null){
            return;
        }
        for (int i =0;i<clients.size();i++){
            clients.get(i).pause();
        }
    }

    private List<EasyPlayerClient> clients=null;
    private void startPlay(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(urls==null||urls.size()==0){
                    return;
                }
                if(clients==null) {
                    clients = new ArrayList<>();
                    for (int i = 0; i < urls.size(); i++) {
                        TextureView videoView = videogride.getChildAt(i).findViewById(R.id.video_player_item);
                        EasyPlayerClient client = new EasyPlayerClient(DataWarningActivity.this, Constante.KEY, videoView, null, null);
                        client.play(urls.get(i).getUrl());
                        clients.add(client);
                    }
                }else{
                    for (int i = 0; i < clients.size(); i++) {
                        clients.get(i).play(urls.get(i).getUrl());
                    }
                }
            }
        },500);
    }
    @Override
    protected void onResume() {
        mMapView.onResume();
        isForeground = true;
        startPlay();
        super.onResume();
        //为系统的方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
    }


    @Override
    protected void onStop() {
        //取消注册传感器监听
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        if(clients!=null) {
            for (int i = 0; i < clients.size(); i++) {
                clients.get(i).stop();
            }
        }
        super.onDestroy();
    }

    public void setMark( LatLng desLatLng){
        BitmapDrawable bd = (BitmapDrawable) getResources().getDrawable(R.drawable.slopered);
        BitmapDescriptor bitmap1 =BitmapDescriptorFactory.fromBitmap(bd.getBitmap());
        BitmapDrawable bd2 = (BitmapDrawable) getResources().getDrawable(R.drawable.slopeyellow);
        BitmapDescriptor bitmap2 =BitmapDescriptorFactory.fromBitmap(bd2.getBitmap());
//        BitmapDrawable bd3= (BitmapDrawable) getResources().getDrawable(R.drawable.slopegreen);
//        BitmapDescriptor bitmap4 =BitmapDescriptorFactory.fromBitmap(bd3.getBitmap());
//        BitmapDrawable bd4= (BitmapDrawable) getResources().getDrawable(R.drawable.solpeiconleft);
//        BitmapDescriptor bitmap3 =BitmapDescriptorFactory.fromBitmap(bd4.getBitmap());
        ArrayList<BitmapDescriptor> list = new ArrayList<>();
        list.add(bitmap1);
        list.add(bitmap2);
//        list.add(bitmap4);
//        list.add(bitmap3);
        OverlayOptions oo =new MarkerOptions().position(desLatLng)
                .zIndex(9)
                .draggable(true)
                .icons(list)
                ;
        mBaiduMap.addOverlay(oo);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(desLatLng).zoom(14.0f);//设置缩放比例
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }
    private void initCity() {
        String res = "113.91286413,22.76387936,113.91286278,22.76332713,113.91144258,22.76318607,113.91147009,22.76255608,113.91038948,22.76227469,113.90980873,22.76256376,113.90965027,22.76298199,113.90926570,22.76226578,113.90889456,22.76200304,113.90837066,22.76155295,113.90784778,22.76145735,113.90485827,22.76065728,113.90526869,22.76000492,113.90325513,22.75952490,113.90344515,22.75872768,113.90176223,22.75815899,113.90073154,22.75792760,113.90038382,22.75899971,113.90013488,22.75885049,113.90021610,22.75856601,113.89993908,22.75848677,113.89978640,22.75862914,113.89966951,22.75886740,113.89957798,22.75882431,113.89950373,22.75897584,113.89922569,22.75888702,113.89930703,22.75873191,113.89908036,22.75853945,113.89949423,22.75776788,113.89924939,22.75767339,113.89874009,22.75812584,113.89857360,22.75777278,113.89790806,22.75733997,113.89801877,22.75717431,113.89776675,22.75705035,113.89780698,22.75697897,113.89742955,22.75673519,113.89688856,22.75623217,113.89593615,22.75542773,113.89426986,22.75420887,113.89312547,22.75298059,113.89307498,22.75301061,113.89296648,22.75289007,113.89251503,22.75293882,113.89178984,22.75206799,113.89210808,22.75179393,113.89179561,22.75135475,113.89185684,22.75123549,113.89116058,22.75085624,113.89061786,22.75025656,113.89069498,22.74950882,113.89158856,22.74905099,113.89245256,22.74883178,113.89304344,22.74863281,113.89365491,22.74830485,113.89335808,22.74783288,113.89308000,22.74756275,113.89317518,22.74687170,113.89281496,22.74659036,113.89217933,22.74732231,113.89150528,22.74704347,113.89049297,22.74655128,113.88984620,22.74546502,113.88921068,22.74460690,113.88890651,22.74398051,113.88964446,22.74360801,113.88943500,22.74298027,113.88926516,22.74267510,113.88821660,22.74244332,113.88759669,22.74119547,113.88714822,22.74066123,113.88707948,22.73999058,113.88638609,22.73919011,113.88561140,22.73919176,113.88555705,22.73869128,113.88787458,22.73873150,113.88980296,22.73876560,113.89436709,22.73942997,113.89480453,22.73905267,113.89562022,22.73890881,113.89594884,22.73851116,113.89663977,22.73812835,113.89711375,22.73792340,113.89744546,22.73768204,113.89792263,22.73783925,113.89838583,22.73867639,113.89859239,22.73905417,113.89916503,22.73898467,113.89950285,22.73891688,113.89969866,22.73903872,113.89980372,22.73937287,113.90093191,22.73938492,113.90110992,22.73937623,113.90130299,22.73937547,113.90156821,22.73925291,113.90190434,22.73915929,113.90235292,22.73898411,113.90265275,22.73890487,113.90303152,22.73873779,113.90501863,22.73831344,113.90493093,22.73808465,113.90457036,22.73777895,113.90456029,22.73732821,113.90476266,22.73713045,113.90584754,22.73629550,113.90655166,22.73694530,113.90711524,22.73731794,113.90689359,22.73777146,113.90732586,22.73840091,113.90827756,22.73808468,113.91039980,22.73706912,113.90976889,22.73583432,113.91063919,22.73547312,113.91148975,22.73534427,113.91178291,22.73558476,113.91249103,22.73481512,113.91325432,22.73382871,113.91351173,22.73307454,113.91372165,22.73168555,113.91444273,22.73110357,113.91479321,22.73041198,113.91489449,22.73017976,113.91582673,22.73025805,113.91631548,22.73098166,113.91744177,22.73155494,113.91780647,22.73183260,113.91838984,22.73174275,113.91902845,22.73125060,113.91941265,22.73143369,113.91993316,22.73090578,113.92047760,22.73096357,113.92240888,22.72775128,113.92320654,22.72771498,113.92575763,22.72806575,113.92708166,22.72810946,113.92748082,22.72329297,113.92859899,22.72243014,113.92944606,22.72092224,113.93015923,22.71953598,113.93181502,22.71812091,113.93244097,22.71743344,113.93326618,22.71593592,113.93341530,22.71542980,113.93380421,22.71434684,113.93405740,22.71068052,113.93512816,22.71051947,113.93854783,22.71323675,113.94162677,22.71304038,113.94389658,22.71561811,113.94595435,22.71679733,113.94827922,22.72107300,113.95187490,22.72008323,113.95329950,22.72044267,113.95366691,22.71929226,113.95484270,22.71727183,113.95617779,22.71702582,113.95781688,22.71777881,113.95873475,22.71672792,113.96045736,22.71584059,113.96166885,22.71533567,113.96649657,22.71426444,113.96836611,22.71403212,113.97149512,22.71526040,113.97447211,22.71698851,113.97647231,22.71907118,113.97393104,22.72401032,113.97047366,22.72801137,113.96952117,22.72836400,113.96926642,22.72907306,113.96912395,22.73000195,113.96907069,22.73065272,113.96885815,22.73150923,113.96854008,22.73358768,113.96839121,22.73464672,113.96593863,22.73427019,113.96391565,22.73393185,113.96160320,22.73410046,113.96021841,22.73437027,113.95852106,22.73512123,113.95635227,22.73634165,113.95415181,22.73745660,113.95129332,22.73900141,113.94914349,22.74000556,113.94760511,22.74062925,113.94554695,22.74112495,113.94338018,22.74143459,113.94184669,22.74180801,113.93964821,22.74242777,113.93816926,22.74305780,113.93660929,22.74378965,113.93504486,22.74489598,113.93321615,22.74653404,113.93110905,22.74881782,113.92949711,22.75063766,113.92767871,22.75287689,113.92644592,22.75407347,113.92495071,22.75460796,113.92297161,22.75468889,113.92183023,22.75515858,113.92061789,22.75652436,113.91984508,22.75762560,113.91831927,22.75951265,113.91745392,22.76045917,113.91569349,22.76221059,113.91428037,22.76422167,113.91286413,22.76387936";
        List<String> pos = Arrays.asList(res.split(","));
        List<LatLng> pts = new ArrayList<LatLng>();
        CoordinateConverter converter  = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.COMMON);
        LatLng ll;

        for (int i = 0;i<pos.size();i++){
            if(i%2==0){
                Double v1 = Double.parseDouble(pos.get(i))+0.005;
                Double v2 = Double.parseDouble(pos.get(i+1))-0.0025;
                ll = new LatLng(v2,v1);
                converter.coord(ll);
                pts.add(converter.convert());
            }
        }
        //构建用户绘制多边形的Option对象
        OverlayOptions polygonOption = new PolygonOptions()
                .points(pts)
                .stroke(new Stroke(5, 0xAAFF0000))
                .fillColor(0x2239b500);

//在地图上添加多边形Option，用于显示
        mBaiduMap.addOverlay(polygonOption);
    }

public void onClicktoPlay(View view){
    Log.i("zxy", "onClicktoPlay: ");
   String x = view.findViewById(R.id.videonum).getTag().toString();
    ARouter.getInstance().build("/player/play").withString("play_url",urls.get(Integer.parseInt(x)).getUrl()).navigation();
}
    public void getVideos(){
        okhttpWorkUtil.postAsynHttpVideos(Constant.BASE_URL + "queryMonitorByNewNameApp?newName=" + newName
                , new RequestVideoCallBack() {
                    @Override
                    public void onSuccess(ArrayList<VideoBean> response) {

                        urls = response;
                        if(urls.size()>2){
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,750);
                            videogride.setLayoutParams(params);
                        }
                        adapter= new GrideAdapter(DataWarningActivity.this,urls);
                        videogride.setAdapter(adapter);
                    }
                    @Override
                    public void onFail(String msg) {
                        Log.i("zxy", "onFail: ---------msg="+msg);
                    }
                });
    }

}
