package com.zig.slope;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.listener.OnLayoutInflatedListener;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.district.DistrictResult;
import com.baidu.mapapi.search.district.OnGetDistricSearchResultListener;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yinglan.scrolllayout.ScrollLayout;
import com.zig.slope.adapter.MyAdapter;
import com.zig.slope.bean.User;
import com.zig.slope.common.base.bean.LoginBean;
import com.zig.slope.common.base.bean.LoginMsg;
import com.zig.slope.common.base.bean.SlopeBean;
import com.zig.slope.common.utils.PreferenceManager;
import com.zig.slope.test.ListviewAdapter;
import com.zig.slope.test.MainActivity;
import com.zig.slope.test.ScreenUtil;
import com.zig.slope.view.TakePhotoPopTop;
import com.zig.slope.callback.AllInterface;
import com.zig.slope.view.LeftDrawerLayout;
import com.zig.slope.view.LeftMenuFragment;
import com.zig.slope.web.Inofation;
import com.zig.slope.web.WebSocketService;
import com.zig.slope.web.adapter.ChatAdapter;
import com.zig.slope.web.model.ChatModel;
import com.zig.slope.web.model.ItemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.bgabanner.BGABanner;

import static android.content.ContentValues.TAG;

/**
 */
@Route(path = "/map/index")
public class LocationdrawActivity extends BaseActivity implements SensorEventListener, AllInterface.OnMenuSlideListener,OnGetDistricSearchResultListener {
    String TAG = "LocationdrawActivity";
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private SensorManager mSensorManager;
    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;
    LeftDrawerLayout mLeftDrawerLayout;
    LeftMenuFragment mMenuFragment;
    MapView mMapView;
    public BaiduMap mBaiduMap;
    View shadowView;
    private ImageView splash_img,menu_icon;
    private final int DISMISS_SPLASH = 0;
    private boolean isDestroy;
    private LoginMsg mg;
    private List<SlopeBean> slopes;
    private PreferenceManager pm;
    // UI相关
    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;
    private int currentModel=-1;
    private ScrollLayout mScrollLayout;
    private Toolbar toolbar,toolbarmain;
    ListView listView;
    BGABanner bgaBanner;
    private TextView[] tvtypes;
    private RelativeLayout relativeLayout;
    private TextView mainTitle;
    //消息
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private EditText et;
    private TextView tvSend;
    private String content;//消息内容
    private Intent websocketServiceIntent;
    private String operatorId,operatorName;
    private Spinner usersp;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==100){
                slopes = (List<SlopeBean>) msg.obj;
                Log.i("zxy", "handleMessage: points="+slopes.size());
                currentModel=0;
                setPMarks(slopes,0);

            }else{
                    Animator animator = AnimatorInflater.loadAnimator(LocationdrawActivity.this, R.animator.splash);
                    animator.setTarget(splash_img);
                    animator.start();
                    initCity();
                    initData();
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent = getIntent();
        mg = (LoginMsg) intent.getSerializableExtra("data");
        pm = PreferenceManager.getInstance(LocationdrawActivity.this);
        if(mg!=null) {
            pm.putString("operatorName", mg.getOperators().getOperatorName());
            pm.putString("operatorTel", mg.getOperators().getOperatorTel());
            pm.putString("operatorId",mg.getOperators().getOperatorID());
            pm.putString("operatorLevel",mg.getOperators().getOperatorLevel());
            if(mg.getSlopeInfo()!=null){
                slopes = mg.getSlopeInfo();
            }
        }
        operatorName = pm.getPackage("operatorName");
        operatorId = pm.getPackage("operatorId");

        setContentView(R.layout.activity_locationdraw);
        shadowView = (View) findViewById(R.id.shadow);
        menu_icon = findViewById(R.id.menu_icon);
        shadowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    closeMenu();
            }
        });
        menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMenu();
            }
        });
        mLeftDrawerLayout = (LeftDrawerLayout) findViewById(R.id.id_drawerlayout);
        FragmentManager fm = getSupportFragmentManager();
        mMenuFragment = (LeftMenuFragment) fm.findFragmentById(R.id.id_container_menu);
        mLeftDrawerLayout.setOnMenuSlideListener(this);
//        setStatusBar();

        if (mMenuFragment == null) {
            fm.beginTransaction().add(R.id.id_container_menu, mMenuFragment = new LeftMenuFragment()).commit();
        }
        getPoints();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//获取传感器管理服务

        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);

        mLocClient.setLocOption(option);
        mLocClient.start();
        initListener();
        tvtypes = new TextView[4];
        tvtypes[0] = findViewById(R.id.typeSolpe);
        tvtypes[1] = findViewById(R.id.typeThree);
        tvtypes[2] = findViewById(R.id.typeHouse);
        tvtypes[3] = findViewById(R.id.typeWorker);
        int level = Integer.parseInt(pm.getPackage("operatorLevel"));
        if(level>1){
            tvtypes[3].setVisibility(View.VISIBLE);
        }else{
            tvtypes[3].setVisibility(View.INVISIBLE);
        }
        initDrawerLayout();
        relativeLayout = (RelativeLayout) findViewById(R.id.root);
        mScrollLayout = (ScrollLayout) findViewById(R.id.scroll_down_layout);
        listView = (ListView) findViewById(R.id.list_view);
        toolbarmain = (Toolbar) findViewById(R.id.toolbarmain);
        mainTitle = findViewById(R.id.maintitle);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.getBackground().setAlpha(0);
        toolbar.setNavigationIcon(R.mipmap.return_icon);
        toolbar.setTitleMarginStart(200);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScrollLayout.scrollToExit();
            }
        });

        mScrollLayout.setOnScrollChangedListener(mOnScrollChangedListener);
        mScrollLayout.setToExit();
        mScrollLayout.getBackground().setAlpha(0);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScrollLayout.scrollToExit();
            }
        });
        bgaBanner = findViewById(R.id.poitBanner);
        splash_img = (ImageView) findViewById(R.id.splash_img);
        handler.sendEmptyMessageDelayed(DISMISS_SPLASH, 1000);
         //消息
        websocketServiceIntent = new Intent(this, WebSocketService.class);
        startService(websocketServiceIntent);
        initMsgView();
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("com.zig.live");
        registerReceiver(liverecever,localIntentFilter);
    }



    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.mipmap.icon_marka);
    /**
     * 当前地点击点
     */
    private LatLng currentPt;
    private String touchType;

    private void initListener() {
        mBaiduMap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {

            @Override
            public void onTouch(MotionEvent event) {

            }
        });
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker != null && marker.getExtraInfo() != null) {

                    SlopeBean pk = (SlopeBean) marker.getExtraInfo().get("pk");
                    if (pk != null) {
                        showPopupWindow(pk);
                    }
                }
                return false;
            }
        });

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            /**
             * 单击地图
             */
            @Override
            public void onMapClick(LatLng point) {
                touchType = "单击地图";
                currentPt = point;
                updateMapState();
            }

            /**
             * 单击地图中的POI点
             */
            @Override
            public boolean onMapPoiClick(MapPoi poi) {
                touchType = "单击POI点";
                currentPt = poi.getPosition();
                updateMapState();
                return false;
            }
        });
        mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            /**
             * 长按地图
             */
            @Override
            public void onMapLongClick(LatLng point) {
                touchType = "长按";
                currentPt = point;
                updateMapState();
            }
        });
        mBaiduMap.setOnMapDoubleClickListener(new BaiduMap.OnMapDoubleClickListener() {
            /**
             * 双击地图
             */
            @Override
            public void onMapDoubleClick(LatLng point) {
                touchType = "双击";
                currentPt = point;
                updateMapState();
            }
        });

        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus status) {
                updateMapState();
            }

            @Override
            public void onMapStatusChangeStart(MapStatus status, int reason) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus status) {
                updateMapState();
            }

            @Override
            public void onMapStatusChange(MapStatus status) {
                updateMapState();
            }
        });
    }
    /**
     * 更新地图状态显示面板
     */
    Marker lastP =null;
    private void updateMapState() {

        if (currentPt != null) {
            if(lastP!=null){
                lastP.remove();
            }
            MarkerOptions  ooA = new MarkerOptions().position(currentPt).icon(bdA);
            lastP = (Marker) mBaiduMap.addOverlay(ooA);
        }
        MapStatus ms = mBaiduMap.getMapStatus();
        if(currentModel!=-1) {
            if (ms.zoom < 16 && currentModel != 0) {
                Log.i(TAG, "updateMapState: ms.zoom==" + ms.zoom);

                updateMarker(0);
                currentModel = 0;
            }
            if (ms.zoom >= 16 && ms.zoom < 18 && currentModel != 1) {
                Log.i(TAG, "updateMapState: ms.zoom==" + ms.zoom);

                updateMarker(1);
                currentModel = 1;
            }
            if (ms.zoom >= 19 && currentModel != 2) {
                Log.i(TAG, "updateMapState: ms.zoom==" + ms.zoom);

                updateMarker(2);
                currentModel = 2;
            }
        }


    }

    public void updateMarker(int zoom){
        for (int i = 0;i<markers.size();i++){
            Marker marker = markers.get(i);
            if (marker != null && marker.getExtraInfo() != null) {

                SlopeBean pk = (SlopeBean) marker.getExtraInfo().get("pk");
                if (pk != null) {
                    View view = getIcon(pk,zoom);
                    bitmap = BitmapDescriptorFactory.fromView(view);
                    marker.setIcon(bitmap);
                }
            }

        }


    }
    //clermarker
//    public void clerMaker(){
//        if(bitmaps!=null) {
//            for (int i = 0; i < bitmaps.size(); i++) {
//                bitmaps.get(i).recycle();
//            }
//        }
//    }
    public void setPMarks(List<SlopeBean> points,int zoom){
        for (int i = 0;i<points.size();i++){
            if(points.get(i).getN()!=null&&points.get(i).getE()!=null) {
                Log.i(TAG, "setPMarks: ==========="+i);
                setMark(points.get(i),zoom);
            }
        }
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

    @Override
    public void onMenuSlide(float offset) {
        shadowView.setVisibility(offset == 0 ? View.INVISIBLE : View.VISIBLE);
        int alpha = (int) Math.round(offset * 255 * 0.4);
        String hex = Integer.toHexString(alpha).toUpperCase();
        shadowView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onGetDistrictResult(DistrictResult districtResult) {

    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            mCurrentAccracy = location.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            mMapView.refreshDrawableState();
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(16.0f);//设置缩放比例
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }



    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
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
        isDestroy = true;
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        WebSocketService.closeWebsocket(false);
        stopService(websocketServiceIntent);
        try{
            unregisterReceiver(liverecever);
        }catch (Exception e){

        }
        super.onDestroy();
    }

    private PopupWindow mPopWindow;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showPopupWindow(final SlopeBean pk) {
     //   showPopFormBottom(pk);
        this. cpk = pk;
        if(mScrollLayout.getVisibility()==View.VISIBLE){
            mScrollLayout.scrollToExit();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showInfo(pk);
                }
            },300);
        }else{
            showInfo(pk);
        }


    }


    private SlopeBean cpk = null;
//    TakePhotoPopWin takePhotoPopWin =null;
    TakePhotoPopTop takePhotoPopTop = null;
//    public void showPopFormBottom(SlopeBean pk) {
//        this. cpk = pk;
//        takePhotoPopWin = new TakePhotoPopWin(this);
////        设置Popupwindow显示位置（从底部弹出）
//        takePhotoPopWin.setData(pk);
//        takePhotoPopWin.showAtLocation(findViewById(R.id.bmapView), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//        WindowManager.LayoutParams params = getWindow().getAttributes();
//        //当弹出Popupwindow时，背景变半透明
//        params.alpha = 0.7f;
//        getWindow().setAttributes(params);
//        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
//        takePhotoPopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                WindowManager.LayoutParams params = getWindow().getAttributes();
//                params.alpha = 1f;
//                getWindow().setAttributes(params);
//            }
//        });
//    }
    public void showPopFormTop(View view) {
        if(toolbar.getVisibility()==View.VISIBLE){
            takePhotoPopTop = new TakePhotoPopTop(this,true);
            takePhotoPopTop.showAtLocation(findViewById(R.id.topmenu_icon), Gravity.TOP | Gravity.RIGHT, 0, 220);
            WindowManager.LayoutParams params = getWindow().getAttributes();
            //当弹出Popupwindow时，背景变半透明
            if(mScrollLayout.getVisibility()==View.INVISIBLE){
                params.alpha = 0.0f;
            }
            getWindow().setAttributes(params);
            //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
            takePhotoPopTop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams params = getWindow().getAttributes();
                    params.alpha = 1f;
                    getWindow().setAttributes(params);
                }
            });
        }else {
            toggleRightSliding();
        }

//        if(mScrollLayout.getVisibility()==View.INVISIBLE){
//            takePhotoPopTop = new TakePhotoPopTop(this,false);
//        }else{
//            takePhotoPopTop = new TakePhotoPopTop(this,true);
//        }
//        //  设置Popupwindow显示位置


    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public void morePage(View v){
//        if(takePhotoPopWin!=null){
//            takePhotoPopWin.dismiss();
//            takePhotoPopWin= null;
//        }
//        //详情
//        Log.i(TAG, "morePage: ");
//        Intent intent = new Intent(this, ExpectAnimScrollActivity.class);
//        intent.putExtra("pk",cpk);
//        startActivity(intent);
//        overridePendingTransition(R.anim.pop_enter_anim,R.anim.pop_exit_anim);
//
//    }
//
//    public void submitPage(View v){
//        //详情
//        Log.i(TAG, "submitPage: ");
//        if(takePhotoPopWin!=null){
//            takePhotoPopWin.dismiss();
//            takePhotoPopWin= null;
//        }
//        //详情
//        Log.i(TAG, "morePage: ");
//        Intent intent = new Intent(this, ReportActivity.class);
//        intent.putExtra("pk",cpk);
//        startActivity(intent);
//    }
    public void Canclepoup(View v){
        if(mPopWindow!=null){
            mPopWindow.dismiss();
            mPopWindow = null;
        }
    }
    //读取json
    public  String getJson(String fileName,Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
    //json --》 list
    public List<SlopeBean>  getDefultPoints(String json){
        List<SlopeBean>  points = null;
        try {
            JSONArray ja = new JSONArray(json);
            points = new ArrayList<SlopeBean>();
            for (int i = 0;i<ja.length();i++){

                JSONObject jb =  ja.getJSONObject(i);
                String newname = null;
                try {
                    newname  = jb.getString("newname");
                }catch (Exception e){
                    e.printStackTrace();
                    newname = null;
                }
                double[] latLng  = new double[2];
                String name = null;
                Log.i(TAG, "getDefultPoints: name=="+jb.getString("name"));
                if(newname!=null&&newname.length()>24) {
                    String point0 = newname.substring(newname.lastIndexOf("(") + 1, newname.lastIndexOf(","));
                    Log.i(TAG, "getDefultPoints: point0="+point0);
                    latLng[0] = Double.parseDouble(point0.substring(0, 12));
                    latLng[1] = Double.parseDouble(point0.substring(13, 24));
                    name =newname.substring(0, newname.lastIndexOf("("));
                         SlopeBean pk = new SlopeBean(jb.getString("name"),name,
                        jb.getString("citynum"),
                        jb.getString("street"),jb.getString("community"),
                        jb.getString("company"),jb.getString("dangername"),
                        jb.getString("address"),jb.getString("type"),
                        jb.getString("x"),jb.getString("y"),
                        latLng[0],latLng[1],
                        jb.getString("features"),jb.getString("long"),
                        jb.getString("height"),jb.getString("slope"),
                        jb.getString("stability"),jb.getString("object"),
                        jb.getString("number"),jb.getString("area"),
                        jb.getString("peoples"),jb.getString("loss"),
                        jb.getString("grade"),jb.getString("danger"),
                        jb.getString("work"),jb.getString("contacts"),
                        jb.getString("tel"),jb.getString("precautions"),
                        jb.getString("process"),jb.getString("isdoing"),
                        jb.getString("year"),jb.getString("management"),
                        jb.getString("doself"),jb.getString("note")
                );
                    points.add(pk);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //获取JSONObject中的数组数据
        return  points;
    }

    public void getPoints(){
        if(slopes!=null){
            Message msg = new Message();
            msg.what = 100;
            msg.obj = slopes;
            handler.sendMessage(msg);
        }else {//读取本地
            String loacalData = pm.getPackage("localdata");
            slopes = new Gson().fromJson(loacalData, new TypeToken<List<SlopeBean>>() {
            }.getType());
            Message msg = new Message();
            msg.what = 100;
            msg.obj = slopes;
            handler.sendMessage(msg);
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    slopes = getDefultPoints(getJson("points.json", LocationdrawActivity.this));
//                    Message msg = new Message();
//                    msg.what = 100;
//                    msg.obj = slopes;
//                    handler.sendMessage(msg);
//                }
//            }).start();
        }
    }
    List<Marker> markers = new ArrayList<Marker>();;
    BitmapDescriptor bitmap;
    public void setMark(SlopeBean pk,int zoom){
        CoordinateConverter converter  = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.COMMON);
        // sourceLatLng待转换坐标
        //converter.coord(new LatLng( pk.getN(),pk.getE()));
        LatLng desLatLng = new LatLng( pk.getN(),pk.getE());
        View view = getIcon(pk,zoom);
        // 构建BitmapDescriptor
        bitmap = BitmapDescriptorFactory.fromView(view);
        Marker marker =(Marker) mBaiduMap.addOverlay( new MarkerOptions().position(desLatLng)
                .zIndex(1)
                .title(pk.getNewName())
                .draggable(true)
                .icon(bitmap));
        Bundle bundle3 = new Bundle();
        bundle3.putSerializable("pk",pk);
        marker.setExtraInfo(bundle3);
        markers.add(marker);
    }

    public View getIcon(SlopeBean pk,int zoom) {
        View view = View.inflate(LocationdrawActivity.this, R.layout.markerv, null);
        // 填充数据
        ImageView iconView = (ImageView) view.findViewById(R.id.point_icon);
        TextView pnameView = (TextView) view.findViewById(R.id.point_dangername);
        TextView infoView = (TextView) view.findViewById(R.id.point_info);
        if(zoom==0){
            pnameView.setVisibility(View.INVISIBLE);
            infoView.setVisibility(View.INVISIBLE);
        }
        if(zoom==1){
            pnameView.setVisibility(View.INVISIBLE);
            infoView.setVisibility(View.VISIBLE);
            infoView.setText(pk.getNewName());
            //   +",坡长:"+pk.getLongs()+",坡高:" +pk.getHeight()+",坡度:"+pk.getSlope());
        }
        if(zoom==2){
            pnameView.setVisibility(View.VISIBLE);
            infoView.setVisibility(View.VISIBLE);
            pnameView.setText(pk.getDangerName());
            //+",坡长:"+pk.getLongs()+",坡高:"+pk.getHeight()+",坡度:"+pk.getSlope());
            infoView.setText(pk.getNewName());
            //+",联系人:"+pk.getContacts()+",联系电话:"+pk.getTel()+",治理进度:"+pk.getProcess());
        }
        if(pk.getDanger().equals(getResources().getString(R.string.gread1))){
            iconView.setImageResource(R.mipmap.slopegreen);
        }
        if(pk.getDanger().equals(getResources().getString(R.string.gread2))){
            iconView.setImageResource(R.mipmap.slopeyellow);
        }
        if(pk.getDanger().equals(getResources().getString(R.string.gread3))){
            iconView.setImageResource(R.mipmap.slopered);
        }
        return view;
    }
    public void openMenu() {
        if(mg!=null) {
            mMenuFragment.setdata(mg.getOperators());
        }else {//本地缓存
            String loacalLoginData = pm.getPackage("logindata");
            mMenuFragment.setdata(new Gson().fromJson(loacalLoginData, LoginBean.class));
        }
        mLeftDrawerLayout.openDrawer();
        shadowView.setVisibility(View.VISIBLE);
    }

    public void closeMenu() {
        mLeftDrawerLayout.closeDrawer();
        shadowView.setVisibility(View.GONE);
    }
    //设置
    public void StartSetting(View v){
        Intent intent =  new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
        startActivity(intent);
    }
    //上报
    public  void StartReport(View v){
        Intent intent = new Intent(LocationdrawActivity.this,ReportActivity.class);
        startActivity(intent);
    }
    //个人
    public  void StartPerson(View v){
        Intent intent = new Intent(LocationdrawActivity.this,PersonActivity.class);
        intent.putExtra("logindata",mMenuFragment.getBean());
        startActivity(intent);
    }
    //签到
    public void registers(View v){
        Intent intent = new Intent(LocationdrawActivity.this,MainActivity.class);
//        intent.putExtra("v1",);
//        intent.putExtra("v2",);
        startActivity(intent);
    }
    //跳转到直播
    public void startLive(View view){
        ARouter.getInstance().build("/rtmp/live").navigation();
    }
    //查询历史
    public void startHisReport(View view){
        Intent intentHis = new Intent(this, HisReportActivity.class);
        startActivity(intentHis);
    }

    //行政信息
    public void  InformationAdm(View v){

    }
    //引导
    public void StartGuide(View v){
        Animation enterAnimation = new AlphaAnimation(0f, 1f);
        enterAnimation.setDuration(600);
        enterAnimation.setFillAfter(true);

        Animation exitAnimation = new AlphaAnimation(1f, 0f);
        exitAnimation.setDuration(600);
        exitAnimation.setFillAfter(true);
        closeMenu();
        View view = findViewById(R.id.topmenu_icon);
        View view1 = findViewById(R.id.menu_icon);
        NewbieGuide.with(this)
                .setLabel("guide1")
                .alwaysShow(true)//总是显示，调试时可以打开
                .addGuidePage(
                        GuidePage.newInstance()//创建一个实例
                                .addHighLight(view, HighLight.Shape.CIRCLE, 5)//添加高亮的view

                                .setLayoutRes(R.layout.view_guide)//设置引导页布局
                                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                                    @Override
                                    public void onLayoutInflated(View view) {
                                        //引导页布局填充后回调，用于初始化
                                        TextView tv = view.findViewById(R.id.textView);
                                        tv.setText("点击打开分类菜单");
                                    }
                                })
                                .setEnterAnimation(enterAnimation)//进入动画
                                .setExitAnimation(exitAnimation)//退出动画
                )
                .addGuidePage(//添加一页引导页
                        GuidePage.newInstance()//创建一个实例
                                .addHighLight(view1, HighLight.Shape.CIRCLE, 5)//添加高亮的view
                                .setLayoutRes(R.layout.view_guide2)//设置引导页布局
                                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                                    @Override
                                    public void onLayoutInflated(View view) {
                                        //引导页布局填充后回调，用于初始化
                                        TextView tv = view.findViewById(R.id.textView2);
                                        tv.setText("点击打开左侧菜单");
                                    }
                                })
                                .setEnterAnimation(enterAnimation)//进入动画
                                .setExitAnimation(exitAnimation)//退出动画
                )
                .addGuidePage(//添加一页引导页
                        GuidePage.newInstance()//创建一个实例
                                .addHighLight(view1)//添加高亮的view
                                .setLayoutRes(R.layout.view_guide3)//设置引导页布局
                                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                                    @Override
                                    public void onLayoutInflated(View view) {
                                        //引导页布局填充后回调，用于初始化
                                        TextView tv = view.findViewById(R.id.textView3);
                                        tv.setText("点击地图标注的地点显示更多信息");
                                    }
                                })
                                .setEnterAnimation(enterAnimation)//进入动画
                                .setExitAnimation(exitAnimation)//退出动画
                )
//                .addGuidePage(//添加一页引导页
//                        GuidePage.newInstance()//创建一个实例
//                                .addHighLight(group,HighLight.Shape.CIRCLE, 5)//添加高亮的view
//                                .setLayoutRes(R.layout.view_guide4)//设置引导页布局
//                                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
//                                    @Override
//                                    public void onLayoutInflated(View view) {
//                                        //引导页布局填充后回调，用于初始化
//                                        TextView tv = view.findViewById(R.id.textView4);
//                                        tv.setText("点击切换地图模式");
//                                    }
//                                })
//                                .setEnterAnimation(enterAnimation)//进入动画
//                                .setExitAnimation(exitAnimation)//退出动画
//                )

                .show();
    }



    private TextView lastTv,currentTv;
    //分类标注 边坡，危房。。。。
    public void  selectType(View view){
        if(takePhotoPopTop!=null){
            takePhotoPopTop.dismiss();
            takePhotoPopTop = null;
        }
       String tag = view.getTag().toString();
        Log.i(TAG, "selectType: ==========tag="+tag);
        switch (tag){
            case "1":
                break;
            case "2":
                break;
            case "3":
                break;
            case "4":
                break;
            case "5":
                Intent intent = new Intent(this, ReportActivity.class);
                intent.putExtra("pname",cpk.getNewName());
                startActivity(intent);
                break;
            case "6":
                Intent intentHis = new Intent(this, HisReportActivity.class);
                startActivity(intentHis);
                break;
            case "7":
                break;
            case "8":
                break;
            case "9":
                currentTv = (TextView) view;
                changeColor(currentTv);
                break;
            case "10":
                currentTv = (TextView) view;
                changeColor(currentTv);
                break;
            case "11":
                currentTv = (TextView) view;
                changeColor(currentTv);
                break;
            case "12":
                currentTv = (TextView) view;
                changeColor(currentTv);
                break;
        }


    }


    public void changeColor(TextView currentTv){
        if(lastTv==null){
            lastTv = findViewById(R.id.typeSolpe);
        }
        if(currentTv.getId()==lastTv.getId()){
            return;
        }
        currentTv.setTextColor(getResources().getColor(R.color.orange_main));
        mainTitle.setText(currentTv.getText());
        lastTv.setTextColor(getResources().getColor(R.color.color_757575));
        lastTv = currentTv;
    }

    private ScrollLayout.OnScrollChangedListener mOnScrollChangedListener = new ScrollLayout.OnScrollChangedListener() {
        @Override
        public void onScrollProgressChanged(float currentProgress) {

            if (currentProgress >= 0) {
                float precent = 255 * currentProgress;
                if (precent > 255) {
                    precent = 255;
                } else if (precent < 0) {

                    precent = 0;
                }

                if(precent<200){
                    toolbar.setVisibility(View.VISIBLE);
                }else {
                    toolbar.setVisibility(View.INVISIBLE);
                }

                mScrollLayout.getBackground().setAlpha(255 - (int) precent);
                toolbar.getBackground().setAlpha(255 - (int) precent);
            }


        }

        @Override
        public void onScrollFinished(ScrollLayout.Status currentStatus) {
            if (currentStatus.equals(ScrollLayout.Status.EXIT)) {
                mScrollLayout.setVisibility(View.INVISIBLE);

            }
        }

        @Override
        public void onChildScroll(int top) {

        }
    };

    public void showInfo(SlopeBean pk){

        toolbar.setTitle("编号:"+pk.getNewName());
        listView.setAdapter(new ListviewAdapter(this,pk.PltoList()));
        mScrollLayout.setVisibility(View.VISIBLE);
        mScrollLayout.scrollToOpen();
        AlphaAnimation al = new AlphaAnimation(0,1);
        al.setDuration(300);
        mScrollLayout.setAnimation(al);

        bgaBanner = findViewById(R.id.poitBanner);
        bgaBanner.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner bgaBanner, View view, Object o, int i) {
                //onclick
            }
        });
        bgaBanner.setData(R.drawable.bga, R.drawable.bgb,R.drawable.bgc,R.drawable.bgd);
    }
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(mScrollLayout.getVisibility()==View.VISIBLE){
                mScrollLayout.scrollToExit();
                return true;
            }else{
                if((System.currentTimeMillis() - exitTime) > 2000){
                    Toast.makeText(LocationdrawActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                    return true;
                }
            }
        }

        return super.onKeyDown(keyCode, event);
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
                Double v1 = Double.parseDouble(pos.get(i));
                Double v2 = Double.parseDouble(pos.get(i+1));
                ll = new LatLng(v2,v1);
                converter.coord(ll);
                pts.add(converter.convert());
            }
        }
        //构建用户绘制多边形的Option对象
        OverlayOptions polygonOption = new PolygonOptions()
                .points(pts)
                .stroke(new Stroke(5, 0xAA39b500))
                .fillColor(0x2239b500);

//在地图上添加多边形Option，用于显示
        mBaiduMap.addOverlay(polygonOption);
    }

    //初始化消息
    private void initDrawerLayout() {
        //注意：初始化的是drawerlayout整个大布局，不是初始化抽屉的那个id
        drawerLayout = (DrawerLayout) super.findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        //v4控件 actionbar上的抽屉开关，可以实现一些开关的动态效果
        toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbarmain, R.mipmap.message_icon
                , R.mipmap.message_icon) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);//抽屉关闭后
                shadowView.setBackgroundColor(Color.argb(0, 0, 0, 0));
                shadowView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);//抽屉打开后
                shadowView.setVisibility(View.VISIBLE);
                int alpha = (int) Math.round(255 * 0.4);
                shadowView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
            }
        };
        drawerLayout.addDrawerListener(toggle);
    }

    public void toggleRightSliding(){//该方法控制右侧边栏的显示和隐藏
        if(drawerLayout.isDrawerOpen(GravityCompat.END)){
            drawerLayout.closeDrawer(GravityCompat.END);//关闭抽屉


        }else{
            drawerLayout.openDrawer(GravityCompat.END);//打开抽屉

        }
    }

    private void initMsgView() {
        recyclerView = (RecyclerView) findViewById(R.id.recylerView);
        et = (EditText) findViewById(R.id.et);
        tvSend = (TextView) findViewById(R.id.tvSend);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter = new ChatAdapter());
        usersp = findViewById(R.id.userOnline);
        usersp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(usersOnline!=null){
                    User u = usersOnline.get(i);
                    if(u!=null){
                        currentUser=u;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initData() {
                WebSocketService.webSocketConnect(new Inofation() {
                    @Override
                    public void onMsg(String msg) {
                        Log.i(TAG, "onMsg: "+msg);
                        getMessage(msg);
                    }

                    @Override
                    public void onConnect() {
                        Log.i(TAG, "onConnect: ");
                    }

                    @Override
                    public void onDisConnect(int code, String reason) {
                        Log.i(TAG, "onDisConnect: ");
                    }
                },operatorId);

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                content = s.toString().trim();
            }
        });
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentUser!=null){
                    String opid = currentUser.getOperatorID();
                    if(opid.equals("all")){
                        WebSocketService.sendMsg(content,operatorId,"0");
                    }else{
                        WebSocketService.sendMsg(content,operatorId,opid);
                    }
                }

                et.setText("");
                hideKeyBorad(et);

            }
        });
    }
    List<User> usersOnline = new ArrayList<>();
    private User currentUser = null;
    @SuppressLint("ResourceAsColor")
    private void getMessage(String msg) {

        List<String> lis = Arrays.asList(msg.split("#"));
        if(lis.get(0).equals("100")){//直播

        }
        if(lis.get(0).equals("1")){
            usersOnline.clear();
            Log.i(TAG, "getMessage: msg=="+msg);
            Gson gs = new Gson();
            usersOnline = gs.fromJson(lis.get(1), new TypeToken<List<User>>(){}.getType());
            usersOnline.add(new User("all","所有人"));
            usersp.setAdapter(new MyAdapter(LocationdrawActivity.this,usersOnline));
            if(currentUser!=null){
               for (int i =0;i<usersOnline.size();i++){
                   if(usersOnline.get(i).getOperatorID().equals(currentUser.getOperatorID())){
                       usersp.setSelection(i,true);
                       currentUser = usersOnline.get(i);
                   }
               }
            }else {
                usersp.setSelection(usersOnline.size() - 1, true);
                currentUser = usersOnline.get(usersOnline.size() - 1);
            }
        }
        if(lis.get(0).endsWith("2")){//返回消息
            ArrayList<ItemModel> data = new ArrayList<>();
            ChatModel model = new ChatModel();
            View view = View.inflate(LocationdrawActivity.this, R.layout.icon_us, null);
            TextView name = view.findViewById(R.id.name_info);
            name.setText(lis.get(1));
            if(lis.get(1).equals(operatorName)) {//发送的
                name.setBackgroundColor(R.color.color_2c6edf);
                Bitmap icon = BitmapDescriptorFactory.fromView(view).getBitmap();
                model.setIcons(icon);
                data.add(new ItemModel(ItemModel.CHAT_B, model));
            }else{//收到的
                name.setBackgroundColor(R.color.orange_main);
                Bitmap icon = BitmapDescriptorFactory.fromView(view).getBitmap();
                model.setIcons(icon);
                data.add(new ItemModel(ItemModel.CHAT_A, model));
            }
            model.setContent(lis.get(3));
            adapter.addAll(data);
        }


    }

    private void hideKeyBorad(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    BroadcastReceiver liverecever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String url = intent.getStringExtra("liveurl");
            WebSocketService.sendMsg(url,operatorId,"0");
        }
    };


}
