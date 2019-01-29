package com.zig.slope;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.search.district.DistrictResult;
import com.baidu.mapapi.search.district.OnGetDistricSearchResultListener;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.util.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.yinglan.scrolllayout.ScrollLayout;
import com.zig.slope.adapter.ChatsAdapter;
import com.zig.slope.adapter.ListviewAdapter;
import com.zig.slope.adapter.MyAdapter;
import com.zig.slope.adapter.OnRecyclerViewItemOnClickListener;
import com.zig.slope.bean.DiXian;
import com.zig.slope.bean.GongDi;
import com.zig.slope.bean.PaiWu;
import com.zig.slope.bean.SanFan;
import com.zig.slope.bean.User;
import com.zig.slope.bean.UserLoacl;
import com.zig.slope.bean.WeiFang;
import com.zig.slope.bean.WeiFangBean;
import com.zig.slope.callback.RequestCallBack;
import com.zig.slope.callback.RequestVideoCallBack;
import com.zig.slope.callback.RequestWeatherCallBack;
import com.zig.slope.charts.DataWarningActivity;
import com.zig.slope.charts.ListViewMultiChartActivity;
import com.zig.slope.clusterutil.MyItem;
import com.zig.slope.clusterutil.clustering.ClusterManager;
import com.zig.slope.common.Constants.Constant;
import com.zig.slope.common.base.BaseMvpActivity;
import com.zig.slope.common.base.bean.LoginBean;
import com.zig.slope.common.base.bean.LoginMsg;
import com.zig.slope.common.base.bean.ProcessBean;
import com.zig.slope.common.base.bean.SlopeBean;
import com.zig.slope.common.utils.PreferenceManager;
import com.zig.slope.common.utils.TimeUtils;
import com.zig.slope.contract.ProcessContract;
import com.zig.slope.presenter.ProcessPresenterImpl;
import com.zig.slope.util.AMapUtil;
import com.zig.slope.util.NetworkUtil;
import com.zig.slope.util.OkhttpWorkUtil;
import com.zig.slope.util.ToolUtils;
import com.zig.slope.util.UdpMessageTool;
import com.zig.slope.view.DoProgressDialog;
import com.zig.slope.view.NaviSelectDialog;
import com.zig.slope.callback.AllInterface;
import com.zig.slope.view.LeftDrawerLayout;
import com.zig.slope.view.LeftMenuFragment;
import com.zig.slope.view.TakePhotoPopTop;
import com.zig.slope.web.Inofation;
import com.zig.slope.web.WebSocketService;
import com.zig.slope.web.model.ChatModel;
import com.zig.slope.web.model.ItemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.bingoogolapple.bgabanner.BGABanner;
import slope.zxy.com.rtmp.VideoBean;
import slope.zxy.com.weather_moudle.bean.WeatherBean;


/**
 */
@Route(path = "/map/index")
public class LocationdrawActivity extends BaseMvpActivity<ProcessContract.ProcessView,ProcessPresenterImpl> implements SensorEventListener, AllInterface.OnMenuSlideListener,OnGetDistricSearchResultListener,ProcessContract.ProcessView {
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
    private ImageView splash_img,menu_icon,message_new;
    private final int DISMISS_SPLASH = 0;
    private boolean isDestroy;
    private LoginMsg mg;
    private List<SlopeBean> slopes;
    private List<SanFan> sanFans;
    private List<GongDi> gongDis;
    private List<DiXian> diXians;
    private List<PaiWu> paiWus;
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
//    private DrawerLayout drawerLayout;
    private  SlidingMenu menu;
    private ActionBarDrawerToggle toggle;
    private RecyclerView recyclerView;
    private ChatsAdapter adapter;
    private EditText et;
    private TextView tvSend;
    private String content;//消息内容
    private Intent websocketServiceIntent;
    private String operatorId,operatorName;
    private Spinner usersp;
    private boolean isDrawerOpened = false;//点击穿透问题
    private OkhttpWorkUtil okhttpWorkUtil;
    private String locationCity;
    private TextView maintitle_weather;
    private ImageView weather_icon;
    List<ProcessBean> dataProcessBean;//治理进度
    public static boolean isForeground = false;
    public static final String MESSAGE_RECEIVED_ACTION = "com.zig.slope.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    private LatLng defaultll = new LatLng(22.747520986909,113.92984114558);
    private int currentType = 1;
    private TakePhotoPopTop popuWf;
    //定时发送位置
    private ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);
    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            sendDataByUDP(operatorId+"_"+mCurrentLon+"_"+mCurrentLat);
            Log.i(TAG, "run: ==============local="+mCurrentLat+","+mCurrentLon);
        }
    };
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==100){
                slopes = (List<SlopeBean>) msg.obj;
                currentModel=0;
                int type = msg.arg1;
                if(type==1){
                    setPMarks(slopes, 0);
                }
                Log.i(TAG, "handleMessage: type=="+type);
                if(type==6){
                    currentTv = (TextView) findViewById(R.id.typeHd);
                    changeColor(currentTv);
                    getPresenter().requesHDData(LocationdrawActivity.this);
                }
                if(type==5){
                    currentTv = (TextView) findViewById(R.id.typeGd);
                    changeColor(currentTv);
                    getPresenter().requesGDData(LocationdrawActivity.this);
                }
                if(type==4){
                    mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
                    currentTv = (TextView) findViewById(R.id.typeDx);
                    changeColor(currentTv);
                    getPresenter().requesDXData(LocationdrawActivity.this);
                }
                if(type==3){
                    currentTv = (TextView) findViewById(R.id.typeThree);
                    changeColor(currentTv);
                    getPresenter().requestSFData(LocationdrawActivity.this);

                }
                if(type==2){
                    currentTv = (TextView) findViewById(R.id.typeHouse);
                    changeColor(currentTv);
                    initCity(WF,0x33ff0000,0xFFFF0000);
                    showWFPoint();
                }
                if(mg!=null) {
                    mMenuFragment.setdata(mg.getOperators());
                }else {//本地缓存
                    String loacalLoginData = pm.getPackage("logindata");
                    mMenuFragment.setdata(new Gson().fromJson(loacalLoginData, LoginBean.class));
                }
                mMenuFragment.setChecked("0".equals(pm.getPackage("openLocation")));
            }else if(msg.what==200){//地陷加载完
                    okhttpWorkUtil.stopProgressDialog();
                    isFirstshow = false;
                    MapStatus.Builder builder = new MapStatus.Builder();
                    float x = mBaiduMap.getMapStatus().zoom;
                    builder.target(new LatLng(22.747520986909+Math.random()/1000000000,113.92984114558+Math.random()/1000000000)).zoom(x);//设置缩放比例
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
            else{
//                    Animator animator = AnimatorInflater.loadAnimator(LocationdrawActivity.this, R.animator.splash);
//                    animator.setTarget(splash_img);
//                    animator.start();
                initCity(BJ,0x2239b500,0xAAFF0000);
                initData();
            }
        }
    };

    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.mipmap.icon_marka);
    /**
     * 当前地点击点
     */
    private LatLng currentPt;
    private String touchType;

    public void initListeners() {
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
                        showPopupWindow(pk,1);
                    }
                    SanFan sf = (SanFan) marker.getExtraInfo().get("sf");
                    if(sf!=null){
                        showPopupWindow(sf,3);
                    }
                    GongDi gd = (GongDi) marker.getExtraInfo().get("gd");
                    if(gd!=null){
                        showPopupWindow(gd,5);
                    }
                    DiXian dx = (DiXian) marker.getExtraInfo().get("dx");
                    if(dx!=null){
                        showPopupWindow(dx,4);
                    }
                    PaiWu pw = (PaiWu) marker.getExtraInfo().get("hd");
                    if(pw!=null){
                        showPopupWindow(pw,6);
                    }

                    Object id = marker.getExtraInfo().get("name");
                    if(id!=null){
                        currentWfs = (Integer) id;
                        currentPage = INDEX;
                        isLoadMore = false;
                        //请求危房
                        getPresenter().requesWFData(LocationdrawActivity.this,INDEX, currentWfs);
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
        if(currentType==4){
            mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
        }else {
            mBaiduMap.setOnMapStatusChangeListener(onMapStatusChangeListener);
        }
    }
    BaiduMap.OnMapStatusChangeListener onMapStatusChangeListener = new BaiduMap.OnMapStatusChangeListener() {
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
    };


    /**
     * 更新地图状态显示面板
     */
    Marker lastP =null;
    private void updateMapState() {

        if (currentPt != null) {
            if(lastP!=null){
                lastP.remove();
            }
//            MarkerOptions  ooA = new MarkerOptions().position(currentPt).icon(bdA);
//            lastP = (Marker) mBaiduMap.addOverlay(ooA);
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
        boolean scaled = zoom==2;
        Log.i("zxy", "updateMarker: zoom===="+zoom);
        if(currentType==1) {
            if (markers != null && markers.size() != 0) {
                for (int i = 0; i < markers.size(); i++) {
                    Marker marker = markers.get(i);
                    if (marker != null && marker.getExtraInfo() != null) {
                        SlopeBean pk = (SlopeBean) marker.getExtraInfo().get("pk");
                        if (pk != null) {
                            View view = getIcon(pk, zoom, 1);
                            bitmap = BitmapDescriptorFactory.fromBitmap(getViewBitmap(view,scaled));//OOM
                            if (marker.getZIndex()==9) {
                                marker.setIcon(bitmap);
                                marker.setZIndex(8);
                            } else {
                                if(zoom==0){
                                    marker.setZIndex(9);
                                }
                                BitmapDescriptor b = marker.getIcon();
                                marker.setIcon(bitmap);
                                b.recycle();
                                Log.i("zxy", "updateMarker: markers====recycle");
                            }
                        }
                    }

                }
            }
        }else if(currentType==3) {
            if (markerssf != null && markerssf.size() != 0) {
                for (int i = 0; i < markerssf.size(); i++) {
                    Marker marker = markerssf.get(i);
                    if (marker != null && marker.getExtraInfo() != null) {
                        SanFan pk = (SanFan) marker.getExtraInfo().get("sf");
                        if (pk != null) {
                            View view = getIcon(pk, zoom, 2);
                            bitmap = BitmapDescriptorFactory.fromBitmap(getViewBitmap(view,scaled));
                            if (marker.getZIndex()==9) {
                                marker.setIcon(bitmap);
                                marker.setZIndex(8);
                            } else {
                                if(zoom==0){
                                    marker.setZIndex(9);
                                }
                                BitmapDescriptor b = marker.getIcon();
                                marker.setIcon(bitmap);
                                b.recycle();
                                Log.i("zxy", "updateMarker: markers====recycle");
                            }
                        }
                    }
                }
            }
        }
        if(currentType==2) {
            if (markerswf != null && markerswf.size() != 0) {
                for (int i = 0; i < markerswf.size(); i++) {
                    Marker marker = markerswf.get(i);
                    if (marker != null && marker.getExtraInfo() != null) {
                        String name = marker.getExtraInfo().getString("name");
                        if (name != null) {
                            View view = getIcon(name, zoom, 3);
                            bitmap = BitmapDescriptorFactory.fromBitmap(getViewBitmap(view,scaled));
                            if (marker.getZIndex()==9) {
                                marker.setIcon(bitmap);
                                marker.setZIndex(8);
                            } else {
                                if(zoom==0){
                                    marker.setZIndex(9);
                                }
                                BitmapDescriptor b = marker.getIcon();
                                marker.setIcon(bitmap);
                                b.recycle();
                                Log.i("zxy", "updateMarker: markers====recycle");
                            }
                        }
                    }
                }
            }
        }
        if(currentType==5) {
            if (markersgd != null && markersgd.size() != 0) {
                for (int i = 0; i < markersgd.size(); i++) {
                    Marker marker = markersgd.get(i);
                    if (marker != null && marker.getExtraInfo() != null) {
                        GongDi pk = (GongDi) marker.getExtraInfo().get("gd");
                        if (pk != null) {
                            View view = getIcon(pk, zoom, 5);
                            bitmap = BitmapDescriptorFactory.fromBitmap(getViewBitmap(view,scaled));
                            if (marker.getZIndex()==9) {
                                marker.setIcon(bitmap);
                                marker.setZIndex(8);
                            } else {
                                if(zoom==0){
                                    marker.setZIndex(9);
                                }
                                BitmapDescriptor b = marker.getIcon();
                                marker.setIcon(bitmap);
                                b.recycle();
                                Log.i("zxy", "updateMarker: markers====recycle");
                            }
                        }
                    }
                }
            }
        }

//        if(currentType==4) {
//            if (markersdx != null && markersdx.size() != 0) {
//                for (int i = 0; i < markersdx.size(); i++) {
//                    Marker marker = markersdx.get(i);
//                    if (marker != null && marker.getExtraInfo() != null) {
//                        DiXian pk = (DiXian) marker.getExtraInfo().get("dx");
//                        if (pk != null) {
//                            View view = getIcon(pk, zoom, 4);
//                            bitmap = BitmapDescriptorFactory.fromBitmap(getViewBitmap(view));
//                            if (marker.getZIndex()==9) {
//                                marker.setIcon(bitmap);
//                                marker.setZIndex(8);
//                            } else {
//                                if(zoom==0){
//                                    marker.setZIndex(9);
//                                }
//                                BitmapDescriptor b = marker.getIcon();
//                                marker.setIcon(bitmap);
//                                b.recycle();
//                                Log.i("zxy", "updateMarker: markers====recycle");
//                            }
//                        }
//                    }
//                }
//            }
//        }

        if(currentType==6) {
            if (markershd != null && markershd.size() != 0) {
                for (int i = 0; i < markershd.size(); i++) {
                    Marker marker = markershd.get(i);
                    if (marker != null && marker.getExtraInfo() != null) {
                        PaiWu pk = (PaiWu) marker.getExtraInfo().get("hd");
                        if (pk != null) {
                            View view = getIcon(pk, zoom, 6);
                            bitmap = BitmapDescriptorFactory.fromBitmap(getViewBitmap(view,scaled));
                            if (marker.getZIndex()==9) {
                                marker.setIcon(bitmap);
                                marker.setZIndex(8);
                            } else {
                                if(zoom==0){
                                    marker.setZIndex(9);
                                }
                                BitmapDescriptor b = marker.getIcon();
                                marker.setIcon(bitmap);
                                b.recycle();
                                Log.i("zxy", "updateMarker: markers====recycle");
                            }
                        }
                    }
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
            locationCity =location.getCity();
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
                builder.target(defaultll).zoom(14.0f);//设置缩放比例
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    protected void onPause() {
        isForeground = false;
        mMapView.onPause();
        super.onPause();
    }
    @Override
    protected void onResume() {
        mMapView.onResume();
        isForeground = true;
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
        Log.i(TAG, "onDestroy: ");
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
            task.cancel();
            pool.shutdown();
        }catch (Exception e){
            e.printStackTrace();
        }
        if (Util.isOnMainThread()) {
            if(!LocationdrawActivity.this.isDestroyed()) {
                Glide.with(LocationdrawActivity.this).pauseRequests();
            }
        }
        super.onDestroy();
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showPopupWindow(final Object pk, final int type) {
            if (mScrollLayout.getVisibility() == View.VISIBLE) {
                mScrollLayout.scrollToExit();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showInfo(pk,type);
                    }
                }, 300);
            } else {
                showInfo(pk,type);
            }
    }


    private SlopeBean cpk = null;
    private SanFan csf = null;
    private GongDi cgd = null;
    private DiXian cdx = null;
    private PaiWu cpu = null;
    private WeiFang cwf = null;
    public void showPopFormTop(View view) {
        Log.i(TAG, "showPopFormTop: -------"+toolbar.getVisibility()+"");
        if(toolbar.getVisibility()!=View.VISIBLE){
            toggleRightSliding();
            message_new.setVisibility(View.INVISIBLE);
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

    public void getPoints(int type){
        if(slopes!=null){
            Message msg = new Message();
            msg.what = 100;
            msg.arg1=type;
            msg.obj = slopes;
            handler.sendMessage(msg);
        }else {//读取本地
            String loacalData = pm.getPackage("localdata");
            slopes = new Gson().fromJson(loacalData, new TypeToken<List<SlopeBean>>() {
            }.getType());
            Message msg = new Message();
            msg.what = 100;
            msg.arg1=type;
            msg.obj = slopes;
            handler.sendMessage(msg);
        }
    }
    List<Marker> markershd = new ArrayList<Marker>();
    List<OverlayOptions> ooshd = new ArrayList<OverlayOptions>();
    List<Marker> markersdx = new ArrayList<Marker>();
    List<OverlayOptions> oosdx = new ArrayList<OverlayOptions>();
    List<Marker> markersgd = new ArrayList<Marker>();
    List<OverlayOptions> oosgd = new ArrayList<OverlayOptions>();
    List<Marker> markerswf = new ArrayList<Marker>();
    List<OverlayOptions> ooswf = new ArrayList<OverlayOptions>();
    List<Marker> markerssf = new ArrayList<Marker>();
    List<OverlayOptions> oossf = new ArrayList<OverlayOptions>();
    List<Marker> markers = new ArrayList<Marker>();
    List<OverlayOptions> oos = new ArrayList<OverlayOptions>();
    BitmapDescriptor bitmap;
    public void setMark(SlopeBean pk,int zoom){
        //CoordinateConverter converter  = new CoordinateConverter();
        //converter.from(CoordinateConverter.CoordType.COMMON);
        // sourceLatLng待转换坐标
        //converter.coord(new LatLng( pk.getN(),pk.getE()));
        if(pk.getN()==null&&pk.getE()==null){
            return;
        }
        LatLng desLatLng = new LatLng( pk.getN(),pk.getE());
        View view = getIcon(pk,zoom,1);
        // 构建BitmapDescriptor
        bitmap = BitmapDescriptorFactory.fromBitmap(getViewBitmap(view,false));
        Bundle bundle3 = new Bundle();
        bundle3.putSerializable("pk",pk);
        OverlayOptions oo =new MarkerOptions().position(desLatLng)
                .zIndex(9)
                .title(pk.getNewName())
                .draggable(true)
                .icon(bitmap)
                .extraInfo(bundle3);
        Marker marker =(Marker) mBaiduMap.addOverlay(oo);
        marker.setZIndex(9);
        markers.add(marker);

        oos.add(oo);
    }
    private Bitmap getViewBitmap(View addViewContent,boolean scaled) {
        addViewContent.setDrawingCacheEnabled(true);
        addViewContent.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0, addViewContent.getMeasuredWidth(), addViewContent.getMeasuredHeight());
        addViewContent.buildDrawingCache();
        Bitmap cacheBitmap = addViewContent.getDrawingCache();
        if(scaled) {
            Bitmap bitmap = Bitmap.createScaledBitmap(cacheBitmap, cacheBitmap.getWidth() * 2 / 3, cacheBitmap.getHeight() * 2 / 3, true);
            cacheBitmap.recycle();
            return bitmap;
        }else{
            Bitmap bitmap = Bitmap.createScaledBitmap(cacheBitmap, cacheBitmap.getWidth(), cacheBitmap.getHeight(), true);
            cacheBitmap.recycle();
            return bitmap;
        }
    }

    public View getIcon(Object object,int zoom,int type) {//type 1==边坡  tytpe  2==三防
        View view =null;
        if(zoom==0){
            view =  View.inflate(LocationdrawActivity.this, R.layout.makerzoom0, null);
            ImageView iconView = (ImageView) view.findViewById(R.id.m_icon);
            if(type ==1){
                SlopeBean pk = (SlopeBean)object;
                if(pk.getDanger()!=null) {
                    if (pk.getDanger().equals(getResources().getString(R.string.gread1))) {
                        iconView.setImageResource(R.mipmap.slopegreen);
                    }
                    if (pk.getDanger().equals(getResources().getString(R.string.gread2))) {
                        iconView.setImageResource(R.mipmap.slopeyellow);
                    }
                    if (pk.getDanger().equals(getResources().getString(R.string.gread3))) {
                        iconView.setImageResource(R.mipmap.slopered);
                    }
                }else{
                    iconView.setImageResource(R.mipmap.slopegreen);
                }
            }
            if(type==2){
                SanFan sanFan = (SanFan)object;
                if(sanFan.getTypes().equals("内涝点")) {
                    iconView.setImageResource(R.mipmap.sfyellow);
                }
                else if(sanFan.getTypes().equals("隐患点")) {
                    iconView.setImageResource(R.mipmap.sfgreen);
                }
                else if(sanFan.getTypes().equals("积水点")) {
                    iconView.setImageResource(R.mipmap.sfread);
                }else if(sanFan.getTypes().equals("易涝点")){
                    iconView.setImageResource(R.mipmap.sfread);
                }
            }
            if(type==3){
                iconView.setImageResource(R.mipmap.wfred);
            }
            if(type==4) {
                DiXian dx = (DiXian) object;
                String repair = dx.getRepair();
                String curing = dx.getCuring();
                int a=0,b=0;
                try{
                    a=Integer.parseInt(repair);
                }catch (Exception e){
                    a=0;
                }
                try{
                    b=Integer.parseInt(curing);
                }catch (Exception e){
                    b=0;
                }
                int x = (a>=b)?a:b;
                if(dx.getId().equals("99")){
                    Log.i("zxy", "getIcon: a="+a+"====b="+b+"======repair"+repair+"====curing"+curing);
                }
                if (x<=2) {
                    iconView.setImageResource(R.mipmap.dxgreen);
                }
                if (x==3) {
                    iconView.setImageResource(R.mipmap.dxyellow);
                }
                if (x>=4) {
                    iconView.setImageResource(R.mipmap.dxred);
                }
            }
            if(type==5){
                GongDi gd = (GongDi)object;
                if(gd.getState().equals("施工中")) {
                    iconView.setImageResource(R.mipmap.gdred);
                }
                else if(gd.getState().equals("未开工")) {
                    iconView.setImageResource(R.mipmap.gdyellow);
                }
                else if(gd.getState().equals("已完工")) {
                    iconView.setImageResource(R.mipmap.gdgreen);
                }else if(gd.getState().equals("已停工")){
                    iconView.setImageResource(R.mipmap.gdyellow);
                }
            }
            if(type==6){
                PaiWu gd = (PaiWu)object;
                iconView.setImageResource(R.mipmap.hdred);
            }
        }
        else if(zoom==1){
            view = View.inflate(LocationdrawActivity.this, R.layout.makerzoom1, null);
            ImageView iconView = (ImageView) view.findViewById(R.id.m_icon);
            TextView nameView = (TextView) view.findViewById(R.id.m_nameId);
            if(type==1){
                SlopeBean pk = (SlopeBean)object;
                nameView.setText(pk.getNewName());
                if(pk.getDanger()!=null) {
                    if (pk.getDanger().equals(getResources().getString(R.string.gread1))) {
                        iconView.setImageResource(R.mipmap.slopegreen);
                    }
                    if (pk.getDanger().equals(getResources().getString(R.string.gread2))) {
                        iconView.setImageResource(R.mipmap.slopeyellow);
                    }
                    if (pk.getDanger().equals(getResources().getString(R.string.gread3))) {
                        iconView.setImageResource(R.mipmap.slopered);
                    }
                }else{
                    iconView.setImageResource(R.mipmap.slopegreen);
                }
            }
            if(type==2){
                SanFan sanFan = (SanFan)object;
                nameView.setText(sanFan.getId());
                if(sanFan.getTypes().equals("内涝点")) {
                    iconView.setImageResource(R.mipmap.sfyellow);
                }
                else if(sanFan.getTypes().equals("隐患点")) {
                    iconView.setImageResource(R.mipmap.sfgreen);
                }
                else if(sanFan.getTypes().equals("积水点")) {
                    iconView.setImageResource(R.mipmap.sfread);
                }else if(sanFan.getTypes().equals("易涝点")){
                    iconView.setImageResource(R.mipmap.sfread);
                }
            }
            if(type==3){
                nameView.setText(object.toString());
                iconView.setImageResource(R.mipmap.wfred);
            }
            if(type==4) {
                DiXian dx = (DiXian) object;
                nameView.setText(dx.getId());
                String repair = dx.getRepair();
                String curing = dx.getCuring();
                int a=0,b=0;
                try{
                    a=Integer.parseInt(repair);
                }catch (Exception e){
                    a=0;
                }
                try{
                    b=Integer.parseInt(curing);
                }catch (Exception e){
                    b=0;
                }
                int x = (a>=b)?a:b;
                if(dx.getId().equals("99")){
                    Log.i("zxy", "getIcon: a="+a+"====b="+b+"======repair"+repair+"====curing"+curing);
                }
                if (x<=2) {
                    iconView.setImageResource(R.mipmap.dxgreen);
                }
                if (x==3) {
                    iconView.setImageResource(R.mipmap.dxyellow);
                }
                if (x>=4) {
                    iconView.setImageResource(R.mipmap.dxred);
                }
            }
            if(type==5){
                GongDi gd = (GongDi)object;
                nameView.setText(gd.getId());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.gravity=Gravity.CENTER_HORIZONTAL;
                if(gd.getState().equals("施工中")) {
                    iconView.setImageResource(R.mipmap.gdred);
                }
                else if(gd.getState().equals("未开工")) {
                    iconView.setImageResource(R.mipmap.gdyellow);
                }
                else if(gd.getState().equals("已完工")) {
                    iconView.setImageResource(R.mipmap.gdgreen);
                }else if(gd.getState().equals("已停工")){
                    iconView.setImageResource(R.mipmap.gdyellow);
                }
            }
            if(type==6){
                PaiWu gd = (PaiWu)object;
                nameView.setText(gd.getSewageId());
                iconView.setImageResource(R.mipmap.hdred);
            }

        } else if(zoom==2){
            view = View.inflate(LocationdrawActivity.this, R.layout.makerzoom2, null);
            LinearLayout ll = view.findViewById(R.id.cont_1);
            ImageView iconView = (ImageView) view.findViewById(R.id.m_icon);
            TextView nameView = (TextView) view.findViewById(R.id.m_nameId);
            TextView contacts = (TextView) view.findViewById(R.id.m_ontacts);
            TextView tel = (TextView) view.findViewById(R.id.m_tel);
            TextView adress = (TextView) view.findViewById(R.id.m_adress);
            if(type==1){
                SlopeBean pk = (SlopeBean)object;
                nameView.setText(pk.getNewName());
                contacts.setText("联系人："+pk.getContacts());
                tel.setText(pk.getTel());
                adress.setText("地址："+pk.getDangerName());
                if(pk.getDanger()!=null) {
                    if (pk.getDanger().equals(getResources().getString(R.string.gread1))) {
                        iconView.setImageResource(R.mipmap.slopegreen);
                    }
                    if (pk.getDanger().equals(getResources().getString(R.string.gread2))) {
                        iconView.setImageResource(R.mipmap.slopeyellow);
                    }
                    if (pk.getDanger().equals(getResources().getString(R.string.gread3))) {
                        iconView.setImageResource(R.mipmap.slopered);
                    }
                }else{
                    iconView.setImageResource(R.mipmap.slopegreen);
                }
            }
            if(type==2){
                SanFan sanFan = (SanFan)object;
                nameView.setText(sanFan.getId());
                contacts.setText("联系人："+sanFan.getContacts());
                tel.setText(sanFan.getTel());
                adress.setText("地址："+sanFan.getAddress());
                if(sanFan.getTypes().equals("内涝点")) {
                    iconView.setImageResource(R.mipmap.sfyellow);
                }
                else if(sanFan.getTypes().equals("隐患点")) {
                    iconView.setImageResource(R.mipmap.sfgreen);
                }
                else if(sanFan.getTypes().equals("积水点")) {
                    iconView.setImageResource(R.mipmap.sfread);
                }else if(sanFan.getTypes().equals("易涝点")){
                    iconView.setImageResource(R.mipmap.sfread);
                }
            }
            if(type==3){
                nameView.setText(object.toString());
                iconView.setImageResource(R.mipmap.wfred);
            }
            if(type==4) {
                DiXian dx = (DiXian) object;
                nameView.setText(dx.getId());
                contacts.setText("道路：" + dx.getRoad());
                tel.setText("管道类型：" + dx.getPipelineType());
                adress.setText("地址：" + dx.getAddress());
                if (dx.getGrade().equals("1") || dx.getGrade().equals("2")) {
                    iconView.setImageResource(R.mipmap.dxgreen);
                }
                if (dx.getGrade().equals("3") || dx.getGrade().equals("4")) {
                    iconView.setImageResource(R.mipmap.dxyellow);
                }
                if (dx.getGrade().equals("5") || dx.getGrade().equals("6")) {
                    iconView.setImageResource(R.mipmap.dxred);
                }
            }
            if(type==5){
                GongDi gd = (GongDi)object;
                nameView.setText(gd.getId());
                contacts.setVisibility(View.GONE);
                // contacts.setText("建设单位联系人:"+"\n"+"电话：");
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.gravity=Gravity.CENTER_HORIZONTAL;
                tel.setLayoutParams(params);
                tel.setText("联系人:"+gd.getConstructionTel());
                adress.setText("地址："+gd.getConstructionAddress());
                if(gd.getState().equals("施工中")) {
                    iconView.setImageResource(R.mipmap.gdred);
                }
                else if(gd.getState().equals("未开工")) {
                    iconView.setImageResource(R.mipmap.gdyellow);
                }
                else if(gd.getState().equals("已完工")) {
                    iconView.setImageResource(R.mipmap.gdgreen);
                }else if(gd.getState().equals("已停工")){
                    iconView.setImageResource(R.mipmap.gdyellow);
                }
            }
            if(type==6){
                PaiWu gd = (PaiWu)object;
                nameView.setText(gd.getSewageId());
                contacts.setText("水体名称："+gd.getSewageName());
                tel.setText("排放口类型："+gd.getSewageType());
                adress.setText("地址："+gd.getAddress());
                iconView.setImageResource(R.mipmap.hdred);
            }
        }
        return view;
    }

    public void openMenu() {

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
        intent.putExtra("y",mCurrentLat);
        intent.putExtra("x",mCurrentLon);
        if(currentType==7){
            intent.putExtra("type",1);
        }else {
            intent.putExtra("type", currentType);
        }
        startActivity(intent);
    }
    public  void setOpenLocation(boolean isOpen){
        Log.i(TAG, "setOpenLocation: isOpen==="+isOpen);
        if(isOpen) {
            pm.putString("openLocation","0" );
        }else{
            pm.putString("openLocation","1" );
        }
    }
    //个人
    public  void StartPerson(View v){
        Intent intent = new Intent(LocationdrawActivity.this,PersonActivity.class);
        intent.putExtra("logindata",mMenuFragment.getBean());
        startActivity(intent);
    }

    //跳转到直播
    public void startLive(View view){
        if(isDrawerOpened){
            return;
        }
        ARouter.getInstance().build("/rtmp/live").navigation();
    }
    //查询历史
    public void startHisReport(View view){
        if(isDrawerOpened){
            return;
        }
        Intent intentHis = new Intent(this, HisReportActivity.class);
        if(currentType==7){
            intentHis.putExtra("type", 1);
        }
        else {
            intentHis.putExtra("type", currentType);
        }
        startActivity(intentHis);
    }

    //主页信息
    public void  InformationAdm(View v){
        if(isDrawerOpened){
            return;
        }
        LocationdrawActivity.this.finish();
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
        View view1 = findViewById(R.id.item_bt1);
        View view2 = findViewById(R.id.item_bt2);
        View view3 = findViewById(R.id.item_bt3);
        View view4 = findViewById(R.id.item_bt4);
        NewbieGuide.with(this)
                .setLabel("guide1")
                .alwaysShow(true)//总是显示，调试时可以打开
                .addGuidePage(
                        GuidePage.newInstance()//创建一个实例
                                .addHighLight(view, HighLight.Shape.OVAL, 5)//添加高亮的view

                                .setLayoutRes(R.layout.view_guide)//设置引导页布局
                                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                                    @Override
                                    public void onLayoutInflated(View view) {
                                        //引导页布局填充后回调，用于初始化
                                        TextView tv = view.findViewById(R.id.textView);
                                        tv.setText("点击打开通讯消息");
                                    }
                                })
                                .setEnterAnimation(enterAnimation)//进入动画
                                .setExitAnimation(exitAnimation)//退出动画
                )
                .addGuidePage(//添加一页引导页
                        GuidePage.newInstance()//创建一个实例
                                .addHighLight(menu_icon, HighLight.Shape.OVAL, 5)//添加高亮的view
                                .setLayoutRes(R.layout.view_guide2)//设置引导页布局
                                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                                    @Override
                                    public void onLayoutInflated(View view) {
                                        //引导页布局填充后回调，用于初始化
                                        TextView tv = view.findViewById(R.id.textView2);
                                        tv.setText("点击打开个人菜单");
                                    }
                                })
                                .setEnterAnimation(enterAnimation)//进入动画
                                .setExitAnimation(exitAnimation)//退出动画
                )
                .addGuidePage(//添加一页引导页
                        GuidePage.newInstance()//创建一个实例
                                .addHighLight(tvtypes[0])//添加高亮的view
                                .setLayoutRes(R.layout.view_guide3)//设置引导页布局
                                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                                    @Override
                                    public void onLayoutInflated(View view) {
                                        //引导页布局填充后回调，用于初始化
                                        TextView tv = view.findViewById(R.id.textView3);
                                        tv.setText("点击查看业务分类");
                                    }
                                })
                                .setEnterAnimation(enterAnimation)//进入动画
                                .setExitAnimation(exitAnimation)//退出动画
                ).addGuidePage(//添加一页引导页
                GuidePage.newInstance()//创建一个实例
                        .addHighLight(view1)//添加高亮的view
                        .setLayoutRes(R.layout.view_guide4)//设置引导页布局
                        .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                            @Override
                            public void onLayoutInflated(View view) {
                                //引导页布局填充后回调，用于初始化
                                TextView tv = view.findViewById(R.id.textView4);
                                tv.setText("点击上报数据");
                            }
                        })
                        .setEnterAnimation(enterAnimation)//进入动画
                        .setExitAnimation(exitAnimation)//退出动画
                )
                .addGuidePage(//添加一页引导页
                        GuidePage.newInstance()//创建一个实例
                                .addHighLight(view2)//添加高亮的view
                                .setLayoutRes(R.layout.view_guide4)//设置引导页布局
                                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                                    @Override
                                    public void onLayoutInflated(View view) {
                                        //引导页布局填充后回调，用于初始化
                                        TextView tv = view.findViewById(R.id.textView4);
                                        tv.setText("点击手机直播");
                                    }
                                })
                                .setEnterAnimation(enterAnimation)//进入动画
                                .setExitAnimation(exitAnimation)//退出动画
                )
                .addGuidePage(//添加一页引导页
                        GuidePage.newInstance()//创建一个实例
                                .addHighLight(view3)//添加高亮的view
                                .setLayoutRes(R.layout.view_guide4)//设置引导页布局
                                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                                    @Override
                                    public void onLayoutInflated(View view) {
                                        //引导页布局填充后回调，用于初始化
                                        TextView tv = view.findViewById(R.id.textView4);
                                        tv.setText("点击查看巡查上报记录");
                                    }
                                })
                                .setEnterAnimation(enterAnimation)//进入动画
                                .setExitAnimation(exitAnimation)//退出动画
                ) .addGuidePage(//添加一页引导页
                GuidePage.newInstance()//创建一个实例
                        .addHighLight(view4)//添加高亮的view
                        .setLayoutRes(R.layout.view_guide4)//设置引导页布局
                        .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                            @Override
                            public void onLayoutInflated(View view) {
                                //引导页布局填充后回调，用于初始化
                                TextView tv = view.findViewById(R.id.textView4);
                                tv.setText("点击返回主页面");
                            }
                        })
                        .setEnterAnimation(enterAnimation)//进入动画
                        .setExitAnimation(exitAnimation)//退出动画
        )

                .show();
    }



    private TextView lastTv,currentTv;
    //分类标注 边坡，危房。。。。
    public void  selectType(View view){
        int id = view.getId();
        if(id!=R.id.typeDx){
            mBaiduMap.setOnMapStatusChangeListener(onMapStatusChangeListener);
        }else{
            mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
        }
        switch (id){
            case R.id.typeSolpe://边坡
                if(currentType!=1) {
                    currentType = 1;
                    currentTv = (TextView) view;
                    changeColor(currentTv);
                    hiddenAllMarker();
                    showSlopes();
                }
                break;
            case R.id.typeThree://三防
                if(currentType!=3) {
                    currentType = 3;
                    currentTv = (TextView) view;
                    changeColor(currentTv);
                    hiddenAllMarker();
                    if (sanFans == null) {
                        getPresenter().requestSFData(LocationdrawActivity.this);
                    } else {
                        showSanFan();
                    }
                }
                break;
            case R.id.typeHouse://危房
                if(currentType!=2) {
                    currentType = 2;
                    currentTv = (TextView) view;
                    changeColor(currentTv);
                    hiddenAllMarker();
                    initCity(WF, 0x33ff0000, 0xFFFF0000);
                    showWFPoint();

                }
                break;
            case R.id.typeDx://地陷
                if(currentType!=4) {
                    currentType = 4;
                    currentTv = (TextView) view;
                    changeColor(currentTv);
                    hiddenAllMarker();
                    Log.i(TAG, "selectType: diXians=="+diXians);
                    if (diXians == null) {
                        getPresenter().requesDXData(LocationdrawActivity.this);
                    } else {
                        showDiXian();
                    }
                }
                break;
            case R.id.typeGd://工地
                if(currentType!=5) {
                    currentType = 5;
                    currentTv = (TextView) view;
                    changeColor(currentTv);
                    hiddenAllMarker();
                    if (gongDis == null) {
                        getPresenter().requesGDData(LocationdrawActivity.this);
                    } else {
                       showGongDi();


                    }
                }
                break;
            case R.id.typeHd://河道
                if(currentType!=6) {
                    currentType = 6;
                    currentTv = (TextView) view;
                    changeColor(currentTv);
                    hiddenAllMarker();
                    if (paiWus == null) {
                        getPresenter().requesHDData(LocationdrawActivity.this);
                    } else {
                        showPaiWu();
                    }
                }
                break;
            case R.id.typeWorker://巡查员
                if(currentType!=7) {
                    currentType = 7;
                    currentTv = (TextView) view;
                    changeColor(currentTv);
                    hiddenAllMarker();
                    okhttpWorkUtil.postAsynHttp(Constant.BASE_URL + "queryAllOperatorPositionApp");
                }
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
        currentTv.setTextColor(getResources().getColor(R.color.main_color));
        currentTv.setBackground(getResources().getDrawable(R.drawable.item_text_view_bg2));
       // mainTitle.setText(currentTv.getText());
        String s = currentTv.getText().toString();
        if(s.contains(getResources().getString(R.string.type_slope))){
            mainTitle.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.slopeyellow),
                    null, null, null);
        }
        if(s.contains(getResources().getString(R.string.type_weater))){
            mainTitle.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.sfyellow),
                    null, null, null);
        }
        if(s.contains(getResources().getString(R.string.type_house))){
            mainTitle.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.wfyellow),
                    null, null, null);
        }
        if(s.contains(getResources().getString(R.string.type_dx))){
            mainTitle.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.dxyellow),
                    null, null, null);
        }
        if(s.contains(getResources().getString(R.string.type_gd))){
            mainTitle.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.gdyellow),
                    null, null, null);
        }
        if(s.contains(getResources().getString(R.string.type_hd))){
            mainTitle.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.hdyellow),
                    null, null, null);
        }
        if(s.contains(getResources().getString(R.string.type_worker))){
            mainTitle.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.worker),
                    null, null, null);
        }

        lastTv.setTextColor(getResources().getColor(R.color.color_757575));
        lastTv.setBackground(null);
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
                showImg(false);
            }
        }

        @Override
        public void onChildScroll(int top) {

        }
    };

    public void showInfo(Object o,int type){
//        if(type==3){
//            return;
//        }
        String img = null;
        if(type==1){
            SlopeBean pk = (SlopeBean) o;
            this.cpk = pk;
            toolbar.setTitle("边坡编号:"+pk.getNewName());
            listView.setAdapter(new ListviewAdapter(this,pk.PltoList()));
            img = pk.getImageAddress1().trim();
        }
        if(type==2){
            WeiFang pk = (WeiFang) o;
            this.cwf = pk;
            toolbar.setTitle("危房编号:"+pk.getId());
            listView.setAdapter(new ListviewAdapter(this,pk.PltoList()));
            img = pk.getImageAddress1().trim();
        }
        if(type==3){
            SanFan sf = (SanFan) o;
            this.csf = sf;
            toolbar.setTitle("三防编号:"+sf.getId());
            listView.setAdapter(new ListviewAdapter(this,sf.PltoList()));
            img =sf.getImageAddress1().trim();
        } if(type==4){
            DiXian dx = (DiXian) o;
            this.cdx = dx;
            toolbar.setTitle("地陷编号:"+dx.getId());
            listView.setAdapter(new ListviewAdapter(this,dx.PltoList()));
            img =dx.getImageAddress1().trim();
        }
        if(type==5){
            GongDi gd = (GongDi) o;
            this.cgd = gd;
            toolbar.setTitle("工地编号:"+gd.getId());
            listView.setAdapter(new ListviewAdapter(this,gd.PltoList()));
            img =gd.getImageAddress1().trim();
        }
        if(type==6){
            PaiWu gd = (PaiWu) o;
            this.cpu = gd;
            toolbar.setTitle("河道编号:"+gd.getId());
            listView.setAdapter(new ListviewAdapter(this,gd.PltoList()));
            img =gd.getImageAddress1().trim();
        }
        if(type!=2) {
            showImg(true);
        }
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
        List<String> imgs = null;
        if(img!=null&&img.length()>0&&!img.equals("null")) {
            List<String>  imgsa = Arrays.asList(img.split("#"));
            imgs = new ArrayList<>(imgsa);
        }
        if(imgs!=null&&imgs.size()>0){
            bgaBanner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
                @Override
                public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                    if(model==null){
                        return;
                    }
                    model = model.trim();
                    if(model.endsWith("jpg")) {
                        if(!LocationdrawActivity.this.isDestroyed()) {
                            Glide.with(LocationdrawActivity.this)
                                    .load(Constant.BASE_URL + model)
                                    .placeholder(R.mipmap.webwxgetmsgimg5)
                                    .error(R.mipmap.webwxgetmsgimg5)
                                    .centerCrop()
                                    .dontAnimate()
                                    .into(itemView);
                        }
                    }else{//unkunw
                        Log.i("zxy", "fillBannerItem: unkunw  data  model=="+model);
                    }
                }
            });
            bgaBanner.setData(imgs,null);
        }else {
            bgaBanner.setData(R.mipmap.webwxgetmsgimg5);
        }
    }
    //    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(mScrollLayout.getVisibility()==View.VISIBLE){
                mScrollLayout.scrollToExit();
                return true;
            }
            if(menu.isMenuShowing()){
                menu.showContent();
                return true;
            }
            if(shadowView.getVisibility()==View.VISIBLE){
                closeMenu();
                return true;
            }
//            else{
//                if((System.currentTimeMillis() - exitTime) > 2000){
//                    Toast.makeText(LocationdrawActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
//                    exitTime = System.currentTimeMillis();
//                    return true;
//                }
//            }
        }

        return super.onKeyDown(keyCode, event);
    }
    private void initCity(String BJ[],int fillColor,int strokeColor) {
        for (int j = 0;j<BJ.length;j++){
            String res = BJ[j];
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
//                    AMapUtil.gcj02_To_Bd09(v2,v1);
//                    pts.add(new LatLng(AMapUtil.gcj02_To_Bd09(v2,v1)[0],AMapUtil.gcj02_To_Bd09(v2,v1)[1]));
                }
            }
            //构建用户绘制多边形的Option对象
            OverlayOptions polygonOption = new PolygonOptions()
                    .points(pts)
                    .stroke(new Stroke(5, strokeColor))
                    .fillColor(fillColor);
            //在地图上添加多边形Option，用于显示
            mBaiduMap.addOverlay(polygonOption);
        }
    }

    //初始化消息
    private void initDrawerLayout() {

          menu = new SlidingMenu(this);
          menu.setMode(SlidingMenu.RIGHT);
          // 设置触摸屏幕的模式
        //  menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
          //menu.setShadowWidthRes(R.dimen.shadow_width);
          // 设置滑动菜单视图的宽度
          menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
          // 设置渐入渐出效果的值
          menu.setFadeDegree(0.35f);
          /**
            * SLIDING_WINDOW will include the Title/ActionBar in the content
            * section of the SlidingMenu, while SLIDING_CONTENT does not.
            */
          menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
          //为侧滑菜单设置布局
          menu.setMenu(R.layout.activity_ui);

        //注意：初始化的是drawerlayout整个大布局，不是初始化抽屉的那个id
//        drawerLayout = (DrawerLayout) super.findViewById(R.id.drawer_layout);
//        //v4控件 actionbar上的抽屉开关，可以实现一些开关的动态效果
//        toggle = new ActionBarDrawerToggle(this, drawerLayout,
//                toolbarmain, R.mipmap.message_icon
//                , R.mipmap.message_icon) {
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                isDrawerOpened = false;
//                super.onDrawerClosed(drawerView);//抽屉关闭后
////                shadowView.setBackgroundColor(Color.argb(0, 0, 0, 0));
////                shadowView.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                isDrawerOpened = true;
//                super.onDrawerOpened(drawerView);//抽屉打开后
////                shadowView.setVisibility(View.VISIBLE);
////                int alpha = (int) Math.round(255 * 0.4);
////                shadowView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
//            }
//        };
//        drawerLayout.addDrawerListener(toggle);
    }

    public void toggleRightSliding(){//控制右侧边栏的显示和隐藏
        if(menu.isMenuShowing()){
            menu.showContent();
        }else{
            menu.showMenu();
        }
//        if(drawerLayout.isDrawerOpen(GravityCompat.END)){
//            drawerLayout.closeDrawer(GravityCompat.END);//关闭抽屉
//        }else{
//            drawerLayout.openDrawer(GravityCompat.END);//打开抽屉
//
//        }
    }

    private void initMsgView() {
        message_new = findViewById(R.id.message_new);
        recyclerView = (RecyclerView) findViewById(R.id.recylerView);
        et = (EditText) findViewById(R.id.et);
        tvSend = (TextView) findViewById(R.id.tvSend);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        recyclerView.setAdapter(adapter = new ChatsAdapter());
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
    private String lastmsg = "";
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
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                    }
                },5000);
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
                    int data = 0;
                    if(opid.equals("all")){
                        data= WebSocketService.sendMsg(content,operatorId,"0");
                    }else{
                        data= WebSocketService.sendMsg(content,operatorId,opid);
                    }
                    if(content.equals("测试socket")){
                        testSocket();
                    }
                    lastmsg = content;
                    lastsendMsgTime = System.currentTimeMillis();
                    handler.postDelayed(checkSocket,3000);
                    if(data==-1||data==-2){
                        Log.i(TAG, "onClick: WebSocketService.infs===null start restart WebSocketService");
                        restartSocket();
                    }
                }
                et.setText("");
                hideKeyBorad(et);
            }
        });
    }

    public void sendMsgToUser(){
        int data = 0;
        Log.i(TAG, "sendMsgToUser: lastmsg=="+lastmsg);
        data= WebSocketService.sendMsg(lastmsg,operatorId,"0");
        lastsendMsgTime = System.currentTimeMillis();
        handler.postDelayed(checkSocket,3000);
        if(data==-1||data==-2){
            Log.i(TAG, "onClick: WebSocketService.infs===null start restart WebSocketService");
            restartSocket();
        }
    }
    List<User> usersOnline = new ArrayList<>();
    private User currentUser = null;
    private long lastgetMsgTime = 0;
    private long lastsendMsgTime = 0;
    @SuppressLint("ResourceAsColor")
    private void getMessage(String msg) {
        Log.i(TAG, "getMessage: msg=="+msg);
        times = 0;
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
        if(lis.get(0).equals("2")){//返回消息
            handler.removeCallbacks(checkSocket);
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
            message_new.setVisibility(View.VISIBLE);
            model.setContent(lis.get(3));
            if(adapter==null){
                adapter = new ChatsAdapter(LocationdrawActivity.this,data);
                recyclerView.setAdapter(adapter);
            }else {
                adapter.updateData(data, false);
            }
            lastgetMsgTime = System.currentTimeMillis();
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
            if (intent.getAction().equals("com.zig.live")) {
                String url = intent.getStringExtra("liveurl");
                Log.i("zxy", "startCamera: get com.zig.live");
                int data = WebSocketService.sendMsg(url, operatorId, "0");
                lastsendMsgTime = System.currentTimeMillis();
                handler.postDelayed(checkSocket, 3000);
                if (data == -1 || data == -2) {
                    Log.i(TAG, "onClick: WebSocketService.infs===null start restart WebSocketService");
                    restartSocket();
                }
            }
            if(intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")){
                if(NetworkUtil.isNetworkAvailable(LocationdrawActivity.this)){
                    //check  upload
                    String lastUpload = pm.getPackage("lastUpload");
                    if(lastUpload!=null){
                        Log.i(TAG, "onReceive: net connected  start upload............lastUpload=="+lastUpload);
                       try {
                           Gson gson = new Gson();
                           List<String> updatat = gson.fromJson(lastUpload, new TypeToken<List<String>>() {
                           }.getType());
                           Log.i(TAG, "onReceive: updatat===" + updatat.size());
                           upLaodImg(updatat);
                       }catch (Exception e){
                           pm.delete("lastUpload");
                           e.printStackTrace();
                       }
                    }
                }
            }
        }
    };

    public void upLaodImg(List<String> param) {
        String uri = Constant.BASE_URL+"filesUpload";
        RequestParams params = new RequestParams(uri);//参数是路径地址
        List<KeyValue> list = new ArrayList<>();
        for (int i = 8; i < param.size(); i++) {
            try {
                list.add(new KeyValue("files",new File(param.get(i))));
            } catch (Exception e) {
            }
        }
        list.add(new KeyValue("patrollerID", param.get(0)));
        if(param.get(7).trim().equals("1")) {
            list.add(new KeyValue("newName", param.get(1)));
        }
        if(param.get(7).trim().equals("3")){
            list.add(new KeyValue("id", param.get(1)));
        }
        list.add(new KeyValue("contents", param.get(2)));
        list.add(new KeyValue("x", param.get(3)));
        list.add(new KeyValue("y", param.get(4)));
        list.add(new KeyValue("remark1", param.get(5)));
        list.add(new KeyValue("regeditID", param.get(6)));
        list.add(new KeyValue("type_s", param.get(7)));
        Log.i(TAG, "upLaodImg: list==="+list.size());
        //设置编码格式为UTF-8，保证参数不乱码
        MultipartBody body = new MultipartBody(list, "UTF-8");
        params.setRequestBody(body);
        params.setMultipart(true);
        org.xutils.x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result=="+result);
                pm.delete("lastUpload");
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
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
                Log.i(TAG, "onFinished: ");
            }
        });
    }

    private UdpMessageTool mUdpMessageTool;
    private void sendDataByUDP(String CONTENT) {
        try {
            mUdpMessageTool = UdpMessageTool.getInstance();
            DatagramSocket mDatagramSocket = new DatagramSocket();
            mDatagramSocket.setSoTimeout(5000);
            mUdpMessageTool.setmDatagramSocket(mDatagramSocket);
            // 向服务器发数据
            mUdpMessageTool.send(Constant.BASE_HOST, Constant.BASE_UDP_PORT, CONTENT.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mUdpMessageTool.close();
    }
    RequestCallBack okcallBack = new RequestCallBack() {
        @Override
        public void onSuccess(List<UserLoacl> response) {
            Log.i(TAG, "onSuccess: response======"+response.size());
            if(response!=null&response.size()>=0){
                for (int i=0;i<response.size();i++){
                    setUserLocal(response.get(i));
                }
            }
        }
        @Override
        public void onFail(String msg) {
            Log.i(TAG, "onFail: ");
        }
    };
    public void setUserLocal(UserLoacl user){
        LatLng desLatLng = new LatLng( user.getY(),user.getX());
        View view = View.inflate(LocationdrawActivity.this, R.layout.makeruser, null);
        TextView m_name = view.findViewById(R.id.m_name);
        m_name.setText(user.getRemark()+"  时间:"+ TimeUtils.transleteTime(user.getCreateTime()));
        bitmap = BitmapDescriptorFactory.fromBitmap(getViewBitmap(view,false));
        MarkerOptions  ooA = new MarkerOptions().position(desLatLng).icon(bitmap);
        mBaiduMap.addOverlay(ooA);
    }

    //隐藏所有
    public void hiddenAllMarker(){
        synchronized (this) {
            mBaiduMap.clear();
            if (markers != null && markers.size() > 0 && markers.get(0).getZIndex() == 8) {
                for (int i = 0; i < markers.size(); i++) {
                    BitmapDescriptor b = markers.get(i).getIcon();
                    Log.i(TAG, "hiddenAllMarker: b=="+b.getBitmap());
                    b.getBitmap().recycle();
                    b.recycle();
                }
            }
            if (markerssf != null && markerssf.size() > 0 && markerssf.get(0).getZIndex() == 8) {
                for (int i = 0; i < markerssf.size(); i++) {
                    BitmapDescriptor b = markerssf.get(i).getIcon();
                    b.getBitmap().recycle();
                    b.recycle();
                }
            }
            if (markerswf != null && markerswf.size() > 0 && markerswf.get(0).getZIndex() == 8) {
                for (int i = 0; i < markerswf.size(); i++) {
                    BitmapDescriptor b = markerswf.get(i).getIcon();
                    b.getBitmap().recycle();
                    b.recycle();
                }
            }
            if (markersgd != null && markersgd.size() > 0 && markersgd.get(0).getZIndex() == 8) {
                for (int i = 0; i < markersgd.size(); i++) {
                    BitmapDescriptor b = markersgd.get(i).getIcon();
                    b.getBitmap().recycle();
                    b.recycle();
                }
            }
            if (markershd != null && markershd.size() > 0 && markershd.get(0).getZIndex() == 8) {
                for (int i = 0; i < markershd.size(); i++) {
                    BitmapDescriptor b = markershd.get(i).getIcon();
                    b.getBitmap().recycle();
                    b.recycle();
                }
            }
            initCity(BJ, 0x2239b500, 0xAAFF0000);
        }
    }
    //显示所有slope
    public void showSlopes(){
        if(oos!=null&&oos.size()>0){
            markers.clear();
            for (int i =0;i<oos.size();i++){
                Marker marker =(Marker) mBaiduMap.addOverlay(oos.get(i));
                markers.add(marker);
            }
        }else{
            setPMarks(slopes, 0);
        }
    }
    //显示三防
    public void showSanFan(){
        if(oossf!=null&&oossf.size()>0){
            markerssf.clear();
            for (int i =0;i<oossf.size();i++){
                Marker marker =(Marker) mBaiduMap.addOverlay(oossf.get(i));
                markerssf.add(marker);
            }
        }else {
            if(sanFans==null||sanFans.size()==0) {
                String json = getJson("pointssf.json", LocationdrawActivity.this);
                sanFans = getDefultSanfan(json);
            }
            for (int i = 0; i < sanFans.size(); i++) {
                setMarksf(sanFans.get(i), 0);
            }
        }
    }
    private ClusterManager<MyItem> mClusterManager;
    boolean isFirstshow = true;
    List<MyItem> items =null;
    //显示地陷
    public void showDiXian(){
        if(okhttpWorkUtil!=null&&isFirstshow) {
                okhttpWorkUtil.showProgressDialog();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
//                if(oosdx!=null&&oosdx.size()>0){
//                    markersdx.clear();
//                    for (int i =0;i<oosdx.size();i++){
//                        Marker marker =(Marker) mBaiduMap.addOverlay(oosdx.get(i));
//                        markersdx.add(marker);
//                    }
//                    Log.i(TAG, "run: markersdx==="+markersdx.size());
//                }else {
                if(diXians==null||diXians.size()==0) {
                    Log.i(TAG, "showDiXian: 地陷无数据");
                }else {
                    if(items==null) {
                        items = new ArrayList<>();
                        for (int i = 0; i < diXians.size(); i++) {
                            DiXian sf = diXians.get(i);
                            if (sf.getN() != null && sf.getE() != null) {
                                LatLng desLatLng = new LatLng(sf.getN(), sf.getE());
                                View view = getIcon(sf, 1, 4);
                                // 构建BitmapDescriptor
                                bitmap = BitmapDescriptorFactory.fromBitmap(getViewBitmap(view,false));
                                Bundle bundle3 = new Bundle();
                                bundle3.putSerializable("dx", sf);
                                items.add(new MyItem(desLatLng, bundle3, bitmap));
                            }
                            //setMarkdx(diXians.get(i), 0);
                        }
                        mClusterManager.addItems(items);
                        handler.sendEmptyMessage(200);
                    }
                }
            }
//            }
        }).start();
        if(!isFirstshow) {
            MapStatus.Builder builder = new MapStatus.Builder();
            float x = mBaiduMap.getMapStatus().zoom;
            builder.target(new LatLng(22.747520986909+Math.random()/1000000000,113.92984114558+Math.random()/1000000000)).zoom(x);//设置缩放比例
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
    }
    //显示河道
    public void showPaiWu(){
        if(ooshd!=null&&ooshd.size()>0){
            markershd.clear();
            for (int i =0;i<ooshd.size();i++){
                Marker marker =(Marker) mBaiduMap.addOverlay(ooshd.get(i));
                markershd.add(marker);
            }
        }else {
            if(paiWus==null||paiWus.size()==0) {
                Log.i(TAG, "showPaiWu: 河道无数据");
            }else {
                for (int i = 0; i < paiWus.size(); i++) {
                    setMarkhd(paiWus.get(i), 0);
                }
            }
        }
    }

    //显示工地
    public void showGongDi(){
        if(oosgd!=null&&oosgd.size()>0){
            markersgd.clear();
            for (int i =0;i<oosgd.size();i++){
                Marker marker =(Marker) mBaiduMap.addOverlay(oosgd.get(i));
                markersgd.add(marker);
            }
        }else {
            if(gongDis==null||gongDis.size()==0) {
                Log.i(TAG, "showGongDi: 工地无数据");
            }else {
                for (int i = 0; i < gongDis.size(); i++) {
                    setMarkgd(gongDis.get(i), 0);
                }
            }
        }
    }

    public void setMarkdx(DiXian sf,int zoom){

        if(sf.getN()==null&&sf.getE()==null){
            return;
        }
        LatLng desLatLng = new LatLng( sf.getN(),sf.getE());
        View view = getIcon(sf,zoom,4);
        boolean scaled = zoom==2;
        // 构建BitmapDescriptor
        bitmap = BitmapDescriptorFactory.fromBitmap(getViewBitmap(view,scaled));
        Bundle bundle3 = new Bundle();
        bundle3.putSerializable("dx",sf);
        OverlayOptions oo =new MarkerOptions().position(desLatLng)
                .zIndex(9)
                .title(sf.getId())
                .draggable(true)
                .icon(bitmap)
                .extraInfo(bundle3);
        Marker marker =(Marker) mBaiduMap.addOverlay(oo);
        marker.setZIndex(9);
        markersdx.add(marker);
        oosdx.add(oo);
    }
    public void setMarkhd(PaiWu sf,int zoom){
        if(sf.getN()==null&&sf.getE()==null){
            return;
        }
        LatLng desLatLng = new LatLng( sf.getN(),sf.getE());
        View view = getIcon(sf,zoom,6);
        boolean scaled = zoom==2;
        // 构建BitmapDescriptor
        bitmap = BitmapDescriptorFactory.fromBitmap(getViewBitmap(view,scaled));
        Bundle bundle3 = new Bundle();
        bundle3.putSerializable("hd",sf);
        OverlayOptions oo =new MarkerOptions().position(desLatLng)
                .zIndex(9)
                .title(sf.getId())
                .draggable(true)
                .icon(bitmap)
                .extraInfo(bundle3);
        Marker marker =(Marker) mBaiduMap.addOverlay(oo);
        marker.setZIndex(9);
        markershd.add(marker);
        ooshd.add(oo);
    }
    public void setMarkgd(GongDi sf,int zoom){
        if(sf.getN()==null&&sf.getE()==null){
            return;
        }
        LatLng desLatLng = new LatLng( sf.getN(),sf.getE());
        View view = getIcon(sf,zoom,5);
        boolean scaled = zoom==2;
        // 构建BitmapDescriptor
        bitmap = BitmapDescriptorFactory.fromBitmap(getViewBitmap(view,scaled));
        Bundle bundle3 = new Bundle();
        bundle3.putSerializable("gd",sf);
        OverlayOptions oo =new MarkerOptions().position(desLatLng)
                .zIndex(9)
                .title(sf.getId())
                .draggable(true)
                .icon(bitmap)
                .extraInfo(bundle3);
        Marker marker =(Marker) mBaiduMap.addOverlay(oo);
        marker.setZIndex(9);
        markersgd.add(marker);
        oosgd.add(oo);
    }

    public void setMarksf(SanFan sf,int zoom){
        if(sf.getN()==null&&sf.getE()==null){
            return;
        }
        LatLng desLatLng = new LatLng( sf.getN(),sf.getE());
        View view = getIcon(sf,zoom,2);
        boolean scaled = zoom==2;
        // 构建BitmapDescriptor
        bitmap = BitmapDescriptorFactory.fromBitmap(getViewBitmap(view,scaled));
        Bundle bundle3 = new Bundle();
        bundle3.putSerializable("sf",sf);
        OverlayOptions oo =new MarkerOptions().position(desLatLng)
                .zIndex(9)
                .title(sf.getId())
                .draggable(true)
                .icon(bitmap)
                .extraInfo(bundle3);
        Marker marker =(Marker) mBaiduMap.addOverlay(oo);
        marker.setZIndex(9);
        markerssf.add(marker);
        oossf.add(oo);
    }

    //json --》 list
    public List<SanFan>  getDefultSanfan(String json){
        List<SanFan>  points = null;
        try {
            JSONArray ja = new JSONArray(json);
            points = new ArrayList<SanFan>();
            for (int i = 0;i<ja.length();i++){
                JSONObject jb =  ja.getJSONObject(i);
                SanFan pk = new SanFan(
                        jb.getDouble("e"),
                    jb.getDouble("n"), jb.getString("id"),
                      jb.getString("community"),
                    jb.getString("type"),jb.getString("address"),
                    jb.getString("imageAddress1"),jb.getString("danger"),
                    jb.getString("reason"),jb.getString("remark"),
                    jb.getString("projectName"),jb.getString("projectContent"),
                    jb.getString("projectFund"),jb.getString("process"),
                    jb.getString("note"),jb.getString("contacts"),
                    jb.getString("tel"),jb.getString("leaderContacts"),
                    jb.getString("leaderTel")
                    );
                    points.add(pk);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //获取JSONObject中的数组数据
        return  points;
    }


    public void showWFPoint(){
        if(ooswf!=null&&ooswf.size()>0){
            markerswf.clear();
            for (int i =0;i<ooswf.size();i++){
                Marker marker =(Marker) mBaiduMap.addOverlay(ooswf.get(i));
                markerswf.add(marker);
            }
        }else {
            List<LatLng> pointswf = getwfpoints();
            List<String> names = getwfnames();
            int[] ids = new int[]{5,1,1,3,2,4,4};
            for (int i = 0; i < pointswf.size(); i++) {
                setMarkwf(pointswf.get(i), 0, names.get(i),ids[i]);
            }
        }
    }

    public void setMarkwf(LatLng desLatLng,int zoom,String lab,int id){
        CoordinateConverter converter  = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.COMMON);
        converter.coord(desLatLng);
        View view = getIcon(lab,zoom,3);
        boolean scaled = zoom==2;
        // 构建BitmapDescriptor
        bitmap = BitmapDescriptorFactory.fromBitmap(getViewBitmap(view,scaled));
        Bundle bundle3 = new Bundle();
        bundle3.putInt("name",id);
        OverlayOptions oo =new MarkerOptions().position(converter.convert())
                .zIndex(9)
                .title(lab)
                .draggable(true)
                .icon(bitmap)
                .extraInfo(bundle3);
        Marker marker =(Marker) mBaiduMap.addOverlay(oo);
        marker.setZIndex(9);
        markerswf.add(marker);
        ooswf.add(oo);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.msgmenu, menu);
        return true;
    }

    private  String getCurrentId(){
        String id = null;
        if(currentType==1) {
            id =  cpk.getNewName();
        }
        if(currentType==2){
            id = cwf.getId();
        }
        if(currentType==3){
            id = csf.getId();
        }
        if(currentType==4){
            id = cdx.getId();
        }
        if(currentType==5){
            id = cgd.getId();
        }
        if(currentType==6){
            id = cpu.getId();
        }
        if(currentType==7){
            id =  cpk.getNewName();
        }
        return id;
    }
    Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();
            Log.i(TAG, "onMenuItemClick: id=="+id);
            switch (id){
                case R.id.report_data_menu://上报数据
                    Intent intent = new Intent(LocationdrawActivity.this, ReportActivity.class);
                    intent.putExtra("pname",getCurrentId());
                    intent.putExtra("y",mCurrentLat);
                    intent.putExtra("x",mCurrentLon);
                    if(currentType==7){
                        intent.putExtra("type",1);
                    }else {
                        intent.putExtra("type", currentType);
                    }
                    LocationdrawActivity.this.startActivity(intent);
                    break;
                case R.id.change_imgs://修改图片
                    Intent intentc = new Intent(LocationdrawActivity.this, ChangeImageActivity.class);
                    intentc.putExtra("pname",getCurrentId());
                    if(currentType==7){
                        intentc.putExtra("type",1);
                    }else {
                        intentc.putExtra("type", currentType);
                    }
                    LocationdrawActivity.this.startActivity(intentc);
                    break;
                case R.id.cam_video://检测视频
                  //  ARouter.getInstance().build("/player/plays").navigation();
                    getVideos(getCurrentId());
                   break;
                case R.id.cam_data ://检测数据
                    Intent intentdata = new Intent(LocationdrawActivity.this, ListViewMultiChartActivity.class);
                    intentdata.putExtra("newName",getCurrentId());
                    if(currentType==7){
                        intentdata.putExtra("type",1);
                    }else {
                        intentdata.putExtra("type", currentType);
                    }
                    LocationdrawActivity.this.startActivity(intentdata);
                    break;
                case R.id.do_process ://治理进度
                    int level = Integer.parseInt(pm.getPackage("operatorLevel"));
                    if(level<4) {
                        createProgressDialog(LocationdrawActivity.this, true);
                        if(dataProcessBean!=null&&dataProcessBean.size()>0) {
                            int x=0;
                            try {
                                x = Integer.parseInt(cpk.getProcess());
                            }catch (Exception e){
                                x=0;
                                e.printStackTrace();
                            }
                            showProgressDialog(dataProcessBean,x);
                        }else{
                            Toast.makeText(LocationdrawActivity.this,"网络繁忙!",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(LocationdrawActivity.this,"您无权限!",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.do_danger ://预警
                  Intent  i = new Intent(LocationdrawActivity.this, DataWarningActivity.class);
                  if(currentType==1) {
                      i.putExtra("newName", cpk.getNewName());
                      i.putExtra("address",cpk.getDangerName());
                      i.putExtra("zLatLng", new LatLng(cpk.getN(), cpk.getE()));
                      i.putExtra("zName",cpk.getCityNum());
                  }else if(currentType==3){
                      i.putExtra("newName", csf.getId());
                      i.putExtra("zLatLng", new LatLng(csf.getN(), csf.getE()));
                  }else if(currentType==4){
                      i.putExtra("newName", cdx.getId());
                      i.putExtra("zLatLng", new LatLng(cdx.getN(), cdx.getE()));
                  }
                  else if(currentType==5){
                      i.putExtra("newName", cgd.getId());
                      i.putExtra("zLatLng", new LatLng(cgd.getN(), cgd.getE()));
                  }
                  else if(currentType==6){
                      i.putExtra("newName", cpu.getId());
                      i.putExtra("zLatLng", new LatLng(cpu.getN(), cpu.getE()));
                  }
                    if(currentType==7){
                        i.putExtra("currentType",1);
                    }else {
                        i.putExtra("currentType", currentType);
                    }
                  i.putExtra("type",2);
                   // Intent  i = new Intent(LocationdrawActivity.this, TestActivity.class);
                  startActivity(i);
                    break;
            }
            return true;
        }
    };

    DoProgressDialog progressDialog;
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
                progressDialog = DoProgressDialog.createDialog(cxt, canCancle);
                progressDialog.setCanceledOnTouchOutside(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 启动加载进度条
     */
    public void showProgressDialog(List<ProcessBean> data,int x){
        try {
            if (progressDialog != null) {
                progressDialog.setdata(data,x);
                progressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_locationdraw;
    }

    @Override
    protected ProcessPresenterImpl createPresenter() {
        return new ProcessPresenterImpl();
    }

    @Override
    protected void findViews() {
        Intent intent = getIntent();
        currentType = intent.getIntExtra("type",1);
        mg = (LoginMsg) intent.getSerializableExtra("data");
        pm = PreferenceManager.getInstance(LocationdrawActivity.this);
        okhttpWorkUtil =  new OkhttpWorkUtil(this,okcallBack);
        if(mg!=null) {
            pm.putString("operatorName", mg.getOperators().getOperatorName());
            pm.putString("operatorTel", mg.getOperators().getOperatorTel());
            pm.putString("operatorId",mg.getOperators().getOperatorID());
            pm.putString("operatorLevel",mg.getOperators().getOperatorLevel());
            if(mg.getSlopeInfo()!=null){
                slopes = mg.getSlopeInfo();
            }
        }
        maintitle_weather = findViewById(R.id.maintitle_weather);
        weather_icon =findViewById(R.id.weather_today_icon);
        operatorName = pm.getPackage("operatorName");
        operatorId = pm.getPackage("operatorId");
        shadowView = (View) findViewById(R.id.shadow);
        menu_icon = findViewById(R.id.menu_icon);
        mLeftDrawerLayout = (LeftDrawerLayout) findViewById(R.id.id_drawerlayout);
        FragmentManager fm = getSupportFragmentManager();
        mMenuFragment = (LeftMenuFragment) fm.findFragmentById(R.id.id_container_menu);

        if (mMenuFragment == null) {
            fm.beginTransaction().add(R.id.id_container_menu, mMenuFragment = new LeftMenuFragment()).commit();
        }
        getPoints(currentType);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//获取传感器管理服务

        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mClusterManager = new ClusterManager<MyItem>(this, mBaiduMap);
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
        tvtypes = new TextView[4];
        tvtypes[0] = findViewById(R.id.typeSolpe);
        tvtypes[1] = findViewById(R.id.typeThree);
        tvtypes[2] = findViewById(R.id.typeHouse);
        tvtypes[3] = findViewById(R.id.typeWorker);
        int level = Integer.parseInt(pm.getPackage("operatorLevel"));
        if(level<4){
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
        toolbar.setTitleMarginStart(250);
        setSupportActionBar(toolbar);
        bgaBanner = findViewById(R.id.poitBanner);
        splash_img = (ImageView) findViewById(R.id.splash_img);
        //消息
        websocketServiceIntent = new Intent(this, WebSocketService.class);
        startService(websocketServiceIntent);
        initMsgView();
        getWeather();
    }
    @Override
    protected void setViews() {
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
        mLeftDrawerLayout.setOnMenuSlideListener(this);
        initListeners();
        toolbar.setOnMenuItemClickListener(onMenuItemClickListener);
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

        handler.sendEmptyMessageDelayed(DISMISS_SPLASH, 1000);//隐藏splash
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("com.zig.live");
        localIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(liverecever,localIntentFilter);
        pool.scheduleAtFixedRate(task, 10, 10*60, TimeUnit.SECONDS);
    }
    @Override
    protected void getData() {
        int level = Integer.parseInt(pm.getPackage("operatorLevel"));
        if(level<4) {
            getPresenter().requestProcessData(LocationdrawActivity.this);
        }
    }

    @Override
    public void onProcessSucess(List<ProcessBean> data) {
        Log.i(TAG, "onProcessSucess: data===="+data.size());
        this.dataProcessBean = data;
    }

    @Override
    public void onProcessFail(String msg) {
        Log.i(TAG, "onProcessFail: msg===="+msg);
    }

    @Override
    public void onThreeDefenseSucess(List<SanFan> data) {
        Log.i(TAG, "onThreeDefenseSucess: data===="+data.size());
        sanFans=data;
        showSanFan();
    }

    @Override
    public void onThreeDefenseFail(String msg) {
        Log.i(TAG, "onThreeDefenseFail: msg===="+msg);
        showSanFan();
    }

    @Override
    public void onConstructionSucess(List<GongDi> data) {
        gongDis=data;
        Log.i(TAG, "onConstructionSucess: msg===="+data.size());
        showGongDi();
    }

    @Override
    public void onConstructionFail(String msg) {
        Log.i(TAG, "onConstructionFail: msg===="+msg);
    }

    @Override
    public void onSubsidenceSucess(List<DiXian> data) {
        Log.i(TAG, "onSubsidenceSucess: data===="+data.size());
        diXians = data;
        showDiXian();

    }

    @Override
    public void onSubsidenceFail(String msg) {
        Log.i(TAG, "onSubsidenceFail: msg===="+msg);
    }
    @Override
    public void onSewageSucess(List<PaiWu> data) {
        Log.i(TAG, "onSewageSucess: data===="+data.size());
        paiWus = data;
        showPaiWu();

    }
    @Override
    public void onSewageFail(String msg) {
        Log.i(TAG, "onSewageFail: msg===="+msg);

    }
    @Override
    public void onDangerousSucess(WeiFangBean data) {
        totalPage = data.getPageTotal();
        if(popuWf==null){
            popuWf = new TakePhotoPopTop(LocationdrawActivity.this,onClickListener,onRefreshListener,onScrollChangeListener);
        }
        if(isLoadMore){
            popuWf.updateData(data.getList());
            datap.addAll(data.getList());
        }else {
            datap = data.getList();
            popuWf.setData(data.getList(), listener, getCurrentWFS(currentWfs));
        }
        popuWf.showAtLocation(currentTv, Gravity.BOTTOM,0,0);
        popuWf.hiddenRefresh();
    }

    @Override
    public void onDangerousFail(String msg) {
        Log.i(TAG, "onDangerousFail: msg===="+msg);
    }

    public void processClick(View v){
        if(v.getId()==R.id.pop_btn_cancel){
            if(progressDialog!=null){
                progressDialog.cancel();
                progressDialog.dismiss();
            }
        }
        if(v.getId()==R.id.pop_btn_enter){
            if(progressDialog!=null){
                final int currentprocess = progressDialog.getSelectProcess();
                okhttpWorkUtil.postAsynHttpProcess(Constant.BASE_URL + "modifySlopeinfoApp", currentprocess + "", cpk.getNewName(), new RequestCallBack() {
                    @Override
                    public void onSuccess(List<UserLoacl> response) {
                        cpk.setProcess(currentprocess+"");
                        progressDialog.cancel();
                        progressDialog.dismiss();
                    }
                    @Override
                    public void onFail(String msg) {
                    }
                });
            }
        }

    }

    int times = 0;
    public void restartSocket(){
        times+=1;
        //Toast.makeText(LocationdrawActivity.this,getResources().getString(R.string.restartsocket),Toast.LENGTH_SHORT).show();
        WebSocketService.closeWebsocket(false);
        stopService(websocketServiceIntent);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startService(websocketServiceIntent);
            }
        }, 200);
        initData();
        if(times<3) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    sendMsgToUser();
                }
            }, 500);
        }
    }
    Runnable checkSocket = new Runnable() {
        @Override
        public void run() {
            if(lastgetMsgTime==0){//第一次没收到
                restartSocket();
            }else if(lastsendMsgTime>lastgetMsgTime){//3秒后没收到
                restartSocket();
            }
        }
    };

    public void startWeather(View view){

       ARouter.getInstance().build("/weather/index").withString("city",locationCity).navigation();
    }

    public void getWeather(){
            okhttpWorkUtil.postAsynHttpWeather(Constant.BASE_URL + "getWeather?cityName=深圳", new RequestWeatherCallBack() {
                @Override
                public void onSuccess(WeatherBean response) {
                    setWeatherData(response);
                }

                @Override
                public void onFail(String msg) {

                }
            });
            
    }
    public void setWeatherData(WeatherBean weatherData){
        maintitle_weather.setText("深圳:"+weatherData.getmTodayWeatherBean().getmNight_Air_Temperature() + "°~"+
                weatherData.getmTodayWeatherBean().getmDay_Air_Temperature() + "°");
        if (Util.isOnMainThread()) {
            if(!LocationdrawActivity.this.isDestroyed()) {
                Glide.with(LocationdrawActivity.this).load(weatherData.getmNowWeatherBean().getmWeather_Pic()).diskCacheStrategy(DiskCacheStrategy.ALL).
                        into(weather_icon);
            }
        }
    }


    public  void showChoiceNaviWayDialog(final Activity activity, final LatLng startLL, final LatLng endLL) {
        final ArrayList<String> mapApps = new ArrayList<String>();
        if (ToolUtils.hasApp(activity, ToolUtils.APP_BAIDU_MAP)) {
            mapApps.add(activity.getString(R.string.baidu_navi));
        }
        if (ToolUtils.hasApp(activity, ToolUtils.APP_AMAP)) {
            mapApps.add(activity.getString(R.string.gaode_navi));
        }
        if(mapApps.size()==0){
            Toast.makeText(activity,getResources().getString(R.string.no_navi),Toast.LENGTH_SHORT).show();
            return;
        }
        final NaviSelectDialog rcd = new NaviSelectDialog(activity);
        rcd.setCanceledOnTouchOutside(false);
        rcd.setCancelable(false);
        rcd.setItems(mapApps, new NaviSelectDialog.OnDlgItemClickListener() {
            @Override
            public void onEnsureClicked(Dialog dialog, String value, boolean isChecked) {
                dialog.dismiss();
                if (activity.getString(R.string.baidu_navi).equals(value)) {
                    startNavi(activity, startLL, endLL);
                } else if (activity.getString(R.string.gaode_navi).equals(value)) {
                    double[]gps = AMapUtil.bd09_To_Gcj02(endLL.latitude,endLL.longitude);
                    AMapUtil.goToNaviActivity(activity,"test",null,
                            gps,"1","2");
                }
                if (isChecked) {
                    //记住我的选择
                }
            }

            @Override
            public void onCancleClicked(Dialog dialog) {
                dialog.dismiss();
            }
        }, true).show();
    }



//开启百度导航
    public void startNavi(Activity activity, LatLng startLL, LatLng endLL) {
        NaviParaOption para = new NaviParaOption();
        para.startPoint(startLL);
        para.startName("从这里开始");
        para.endPoint(endLL);
        para.endName("到这里结束");
        try {
            BaiduMapNavigation.openBaiduMapNavi(para, activity);
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            Toast.makeText(activity,"您尚未安装百度地图或地图版本过低",Toast.LENGTH_SHORT).show();
        }
    }


    public void showImg(boolean ishow){
        if(ishow) {
            findViewById(R.id.showImage).setVisibility(View.VISIBLE);
        }else {
            findViewById(R.id.showImage).setVisibility(View.GONE);
        }
    }
    public void showNai(View view){
        if(currentType==1) {
            showChoiceNaviWayDialog(LocationdrawActivity.this, new LatLng(mCurrentLat, mCurrentLon), new LatLng(cpk.getN(), cpk.getE()));
        }
        if(currentType==3){
            showChoiceNaviWayDialog(LocationdrawActivity.this, new LatLng(mCurrentLat, mCurrentLon), new LatLng(csf.getN(), csf.getE()));
        }
        if(currentType==4){
            showChoiceNaviWayDialog(LocationdrawActivity.this, new LatLng(mCurrentLat, mCurrentLon), new LatLng(cdx.getN(), cdx.getE()));
        }
        if(currentType==5){
            showChoiceNaviWayDialog(LocationdrawActivity.this, new LatLng(mCurrentLat, mCurrentLon), new LatLng(cgd.getN(), cgd.getE()));
        }
        if(currentType==6){
            showChoiceNaviWayDialog(LocationdrawActivity.this, new LatLng(mCurrentLat, mCurrentLon), new LatLng(cpu.getN(), cpu.getE()));
        }
        }

    public void getVideos(final String newName){
        if(currentType==1) {
            okhttpWorkUtil.postAsynHttpVideos(Constant.BASE_URL + "queryMonitorByNewNameApp?newName=" + newName
                    , new RequestVideoCallBack() {
                        @Override
                        public void onSuccess(ArrayList<VideoBean> response) {
                            Log.i("zxy", "onSuccess: response=="+response.size());
                            ARouter.getInstance().build("/player/plays").withParcelableArrayList("videos", response).withString("newName", newName).navigation();
                        }
                        @Override
                        public void onFail(String msg) {
                            Toast.makeText(LocationdrawActivity.this,"暂无监控视频",Toast.LENGTH_SHORT).show();
                            Log.i("zxy", "onFail: ---------msg=" + msg);
                        }
                    });
        }
    }
    public  List getwfpoints(){
        List<LatLng>wfs = new ArrayList<LatLng>();
        wfs.add(new LatLng(22.73035885-0.0025,113.94462913+0.005));
        wfs.add(new LatLng(22.73695611-0.0025,113.92942435+0.005));
        wfs.add(new LatLng(22.73407473-0.0025,113.93333112+0.005));
        wfs.add(new LatLng(22.75801557-0.0025,113.91068817+0.005));
        wfs.add(new LatLng(22.73700765-0.0025,113.91253397+0.005));
        wfs.add(new LatLng(22.75629450-0.0025,113.89676707+0.005));
        wfs.add(new LatLng(22.74859069-0.0025,113.90628017+0.005));
        return wfs;
    }
    public List<String> getwfnames(){
        List<String>names = new ArrayList<String>();
        names.add("凤凰社区凤凰村老旧房片区");
        names.add("塘家社区老旧房屋片区");
        names.add("塘家社区老旧房片区");
        names.add("东坑社区老旧房片区");
        names.add("甲子塘老旧房片区");
        names.add("塘尾社区旧村老旧房片区");
        names.add("塘尾社区工业区老旧房片区");
        return names;
    }
    private String getCurrentWFS(int x){
        //1塘家
       //2甲子塘
       //3东坑
       //4塘尾
       //5凤凰
        String s = "凤凰社区老旧房片区";
        if(x==1){
            s = "塘家社区老旧房屋片区";
        }else if(x==2){
            s="甲子塘老旧房片区";
        }else if(x==3){
            s = "东坑社区老旧房片区";
        }else if(x==4){
            s="塘尾社区老旧房片区";
        }else {
            s="凤凰社区老旧房片区";
        }
        return  s;
    }
    private String WF[]=new String[]{
            //凤凰社区凤凰村老旧房片区
            "113.94462913,22.73035885,113.94463181,22.73039224,113.94463984,22.73042561,113.94465457,22.73045774,113.94467466,22.73048985,113.94470011,22.73052072,113.94472957,22.73055034,113.94476305,22.73057872,113.94479922,22.73060586,113.94483806,22.73063052,113.94487958,22.73065517,113.94492243,22.73067735,113.94496663,22.73069829,113.94501217,22.73071799,113.94505771,22.73073646,113.94510190,22.73075245,113.94514744,22.73076844,113.94519164,22.73078320,113.94523584,22.73079672,113.94527870,22.73080900,113.94532155,22.73082005,113.94536173,22.73083110,113.94540191,22.73084092,113.94544075,22.73084950,113.94547959,22.73085809,113.94551576,22.73086544,113.94555192,22.73087279,113.94558674,22.73088014,113.94562022,22.73088626,113.94565237,22.73089238,113.94568451,22.73089727,113.94571397,22.73090216,113.94574478,22.73090705,113.94577290,22.73091070,113.94580103,22.73091560,113.94582782,22.73091926,113.94585326,22.73092292,113.94587871,22.73092534,113.94590416,22.73092900,113.94592826,22.73093143,113.94595103,22.73093386,113.94597380,22.73093629,113.94599523,22.73093872,113.94601800,22.73094115,113.94603809,22.73094359,113.94605818,22.73094602,113.94607827,22.73094722,113.94609835,22.73094965,113.94611711,22.73095086,113.94613586,22.73095206,113.94615327,22.73095326,113.94617068,22.73095570,113.94618809,22.73095690,113.94620550,22.73095811,113.94622291,22.73095931,113.94623898,22.73096051,113.94625505,22.73096048,113.94626978,22.73096169,113.94628586,22.73096290,113.94630059,22.73096411,113.94631532,22.73096408,113.94633005,22.73096529,113.94634478,22.73096649,113.94635952,22.73096647,113.94637291,22.73096768,113.94638764,22.73096765,113.94640103,22.73096886,113.94641443,22.73096883,113.94642782,22.73097004,113.94644121,22.73097002,113.94645461,22.73097123,113.94646666,22.73097121,113.94648005,22.73097118,113.94649211,22.73097239,113.94650550,22.73097237,113.94651755,22.73097234,113.94652961,22.73097232,113.94654166,22.73097353,113.94655371,22.73097351,113.94656577,22.73097349,113.94657782,22.73097346,113.94658987,22.73097344,113.94660193,22.73097342,113.94661398,22.73097463,113.94662603,22.73097461,113.94663809,22.73097458,113.94665014,22.73097456,113.94666086,22.73097454,113.94667291,22.73097452,113.94668496,22.73097449,113.94669702,22.73097447,113.94670773,22.73097445,113.94671978,22.73097443,113.94673184,22.73097440,113.94674255,22.73097438,113.94675460,22.73097436,113.94676666,22.73097433,113.94677871,22.73097431,113.94679076,22.73097305,113.94680282,22.73097303,113.94681487,22.73097300,113.94682693,22.73097298,113.94683898,22.73097296,113.94685103,22.73097293,113.94686309,22.73097167,113.94687514,22.73097165,113.94688719,22.73097163,113.94690058,22.73097160,113.94691264,22.73097034,113.94692603,22.73097032,113.94693808,22.73097029,113.94695148,22.73096903,113.94696487,22.73096900,113.94697826,22.73096774,113.94699165,22.73096771,113.94700505,22.73096645,113.94701978,22.73096642,113.94703317,22.73096516,113.94704790,22.73096513,113.94706264,22.73096387,113.94707737,22.73096260,113.94709210,22.73096257,113.94710683,22.73096131,113.94712290,22.73096004,113.94713763,22.73095877,113.94715370,22.73095874,113.94716978,22.73095747,113.94718719,22.73095620,113.94720460,22.73095493,113.94722201,22.73095366,113.94723942,22.73095115,113.94725683,22.73094988,113.94727558,22.73094861,113.94729433,22.73094734,113.94731441,22.73094482,113.94733450,22.73094355,113.94735459,22.73094104,113.94737468,22.73093852,113.94739745,22.73093600,113.94741888,22.73093349,113.94744164,22.73093097,113.94746441,22.73092845,113.94748852,22.73092593,113.94751396,22.73092217,113.94753941,22.73091965,113.94756485,22.73091589,113.94759164,22.73091213,113.94761976,22.73090712,113.94764789,22.73090336,113.94767869,22.73089835,113.94770815,22.73089335,113.94774029,22.73088834,113.94777243,22.73088209,113.94780592,22.73087584,113.94784074,22.73086835,113.94787689,22.73086086,113.94791305,22.73085337,113.94795189,22.73084464,113.94799073,22.73083590,113.94803091,22.73082593,113.94807108,22.73081472,113.94811394,22.73080351,113.94815679,22.73079106,113.94820099,22.73077736,113.94824518,22.73076244,113.94829071,22.73074627,113.94833491,22.73073011,113.94838044,22.73071147,113.94842598,22.73069159,113.94847017,22.73067048,113.94851303,22.73064813,113.94855454,22.73062332,113.94859338,22.73059851,113.94862954,22.73057123,113.94866302,22.73054272,113.94869248,22.73051298,113.94871793,22.73048201,113.94873802,22.73044982,113.94875275,22.73041764,113.94876079,22.73038424,113.94876481,22.73035084,113.94876079,22.73031870,113.94875276,22.73028532,113.94873803,22.73025320,113.94871795,22.73022109,113.94869250,22.73019022,113.94866304,22.73016061,113.94862957,22.73013223,113.94859341,22.73010510,113.94855458,22.73008044,113.94851306,22.73005579,113.94847021,22.73003362,113.94842602,22.73001269,113.94838049,22.72999299,113.94833496,22.72997453,113.94829076,22.72995855,113.94824523,22.72994256,113.94820104,22.72992781,113.94815685,22.72991430,113.94811400,22.72990201,113.94807114,22.72989097,113.94803097,22.72987992,113.94799079,22.72987011,113.94795195,22.72986153,113.94791312,22.72985295,113.94787696,22.72984560,113.94784080,22.72983825,113.94780598,22.72983090,113.94777250,22.72982479,113.94774036,22.72981867,113.94770822,22.72981378,113.94767876,22.72980889,113.94764796,22.72980401,113.94761983,22.72980035,113.94759171,22.72979546,113.94756493,22.72979181,113.94753948,22.72978815,113.94751403,22.72978572,113.94748859,22.72978206,113.94746448,22.72977963,113.94744172,22.72977721,113.94741895,22.72977478,113.94739752,22.72977235,113.94737476,22.72976992,113.94735467,22.72976749,113.94733458,22.72976505,113.94731449,22.72976386,113.94729440,22.72976142,113.94727565,22.72976022,113.94725690,22.72975902,113.94723949,22.72975782,113.94722208,22.72975538,113.94720467,22.72975418,113.94718726,22.72975298,113.94716985,22.72975177,113.94715378,22.72975057,113.94713771,22.72975060,113.94712298,22.72974939,113.94710691,22.72974819,113.94709218,22.72974698,113.94707744,22.72974701,113.94706271,22.72974580,113.94704798,22.72974459,113.94703325,22.72974462,113.94701986,22.72974341,113.94700513,22.72974344,113.94699173,22.72974223,113.94697834,22.72974225,113.94696495,22.72974104,113.94695155,22.72974107,113.94693816,22.72973986,113.94692611,22.72973988,113.94691272,22.72973991,113.94690066,22.72973870,113.94688727,22.72973872,113.94687522,22.72973875,113.94686316,22.72973877,113.94685111,22.72973756,113.94683906,22.72973758,113.94682700,22.72973760,113.94681495,22.72973763,113.94680290,22.72973765,113.94679084,22.72973767,113.94677879,22.72973646,113.94676674,22.72973648,113.94675468,22.72973651,113.94674263,22.72973653,113.94673192,22.72973655,113.94671986,22.72973657,113.94670781,22.72973660,113.94669709,22.72973662,113.94668504,22.72973664,113.94667299,22.72973666,113.94666093,22.72973669,113.94665022,22.72973671,113.94663817,22.72973673,113.94662611,22.72973676,113.94661406,22.72973678,113.94660201,22.72973804,113.94658995,22.72973806,113.94657790,22.72973809,113.94656585,22.72973811,113.94655379,22.72973813,113.94654174,22.72973816,113.94652968,22.72973942,113.94651763,22.72973944,113.94650558,22.72973946,113.94649218,22.72973949,113.94648013,22.72974075,113.94646674,22.72974077,113.94645468,22.72974080,113.94644129,22.72974206,113.94642790,22.72974209,113.94641451,22.72974335,113.94640111,22.72974337,113.94638772,22.72974464,113.94637299,22.72974466,113.94635959,22.72974593,113.94634486,22.72974595,113.94633013,22.72974722,113.94631540,22.72974848,113.94630067,22.72974851,113.94628593,22.72974978,113.94626986,22.72975105,113.94625513,22.72975231,113.94623906,22.72975234,113.94622299,22.72975361,113.94620558,22.72975488,113.94618816,22.72975615,113.94617075,22.72975742,113.94615334,22.72975993,113.94613593,22.72976120,113.94611718,22.72976247,113.94609843,22.72976374,113.94607834,22.72976625,113.94605825,22.72976753,113.94603816,22.72977004,113.94601807,22.72977255,113.94599530,22.72977507,113.94597387,22.72977758,113.94595111,22.72978010,113.94592834,22.72978262,113.94590423,22.72978513,113.94587878,22.72978889,113.94585333,22.72979141,113.94582789,22.72979517,113.94580110,22.72979893,113.94577297,22.72980393,113.94574485,22.72980769,113.94571404,22.72981270,113.94568458,22.72981771,113.94565243,22.72982271,113.94562029,22.72982896,113.94558681,22.72983520,113.94555198,22.72984269,113.94551582,22.72985018,113.94547966,22.72985767,113.94544082,22.72986640,113.94540197,22.72987513,113.94536179,22.72988509,113.94532161,22.72989630,113.94527875,22.72990751,113.94523589,22.72991996,113.94519169,22.72993364,113.94514749,22.72994857,113.94510195,22.72996473,113.94505775,22.72998089,113.94501221,22.72999952,113.94496667,22.73001939,113.94492247,22.73004050,113.94487961,22.73006284,113.94483809,22.73008765,113.94479925,22.73011245,113.94476308,22.73013972,113.94472960,22.73016823,113.94470013,22.73019796,113.94467468,22.73022893,113.94465458,22.73026111,113.94463985,22.73029329,113.94463181,22.73032670,113.94462913,22.73035885",
            //塘家社区老旧房屋片区
            "113.92942435,22.73695611,113.92942435,22.73696477,113.92942435,22.73697342,113.92942569,22.73698084,113.92942703,22.73698949,113.92942837,22.73699691,113.92942971,22.73700433,113.92943105,22.73701298,113.92943373,22.73702040,113.92943507,22.73702782,113.92943775,22.73703647,113.92944043,22.73704388,113.92944311,22.73705130,113.92944713,22.73705871,113.92944981,22.73706613,113.92945383,22.73707355,113.92945785,22.73707972,113.92946187,22.73708714,113.92946589,22.73709455,113.92947125,22.73710073,113.92947527,22.73710814,113.92948063,22.73711432,113.92948465,22.73712050,113.92949001,22.73712667,113.92949537,22.73713285,113.92950073,22.73713902,113.92950610,22.73714520,113.92951146,22.73715014,113.92951816,22.73715631,113.92952352,22.73716125,113.92953022,22.73716743,113.92953558,22.73717237,113.92954228,22.73717731,113.92954764,22.73718225,113.92955434,22.73718718,113.92956104,22.73719212,113.92956640,22.73719706,113.92957311,22.73720076,113.92957981,22.73720570,113.92958651,22.73720940,113.92959321,22.73721310,113.92959991,22.73721680,113.92960661,22.73722174,113.92961331,22.73722544,113.92962001,22.73722791,113.92962671,22.73723161,113.92963342,22.73723531,113.92964012,22.73723901,113.92964682,22.73724148,113.92965352,22.73724518,113.92966022,22.73724764,113.92966692,22.73725011,113.92967362,22.73725257,113.92968032,22.73725627,113.92968703,22.73725874,113.92969373,22.73726120,113.92970043,22.73726367,113.92970713,22.73726490,113.92971383,22.73726736,113.92972053,22.73726983,113.92972723,22.73727229,113.92973393,22.73727352,113.92974063,22.73727598,113.92974734,22.73727721,113.92975404,22.73727844,113.92976074,22.73728090,113.92976744,22.73728213,113.92977414,22.73728336,113.92978084,22.73728459,113.92978754,22.73728582,113.92979424,22.73728705,113.92980095,22.73728827,113.92980765,22.73728950,113.92981435,22.73729073,113.92982105,22.73729196,113.92982775,22.73729319,113.92983311,22.73729318,113.92983981,22.73729441,113.92984651,22.73729564,113.92985322,22.73729563,113.92985992,22.73729686,113.92986662,22.73729685,113.92987332,22.73729684,113.92988002,22.73729807,113.92988538,22.73729806,113.92989208,22.73729805,113.92989878,22.73729928,113.92990549,22.73729927,113.92991219,22.73729926,113.92991889,22.73729926,113.92992559,22.73729925,113.92993095,22.73729924,113.92993765,22.73729923,113.92994435,22.73729922,113.92995105,22.73729921,113.92995776,22.73729797,113.92996446,22.73729796,113.92996982,22.73729795,113.92997652,22.73729671,113.92998322,22.73729670,113.92998992,22.73729669,113.92999662,22.73729545,113.93000332,22.73729544,113.93001003,22.73729419,113.93001673,22.73729295,113.93002209,22.73729294,113.93002879,22.73729170,113.93003549,22.73729045,113.93004219,22.73728921,113.93004889,22.73728796,113.93005559,22.73728672,113.93006230,22.73728547,113.93006900,22.73728423,113.93007570,22.73728298,113.93008240,22.73728174,113.93008910,22.73728049,113.93009580,22.73727801,113.93010250,22.73727677,113.93010921,22.73727552,113.93011591,22.73727304,113.93012261,22.73727180,113.93012931,22.73726931,113.93013601,22.73726683,113.93014271,22.73726435,113.93014941,22.73726311,113.93015611,22.73726062,113.93016282,22.73725814,113.93016952,22.73725566,113.93017622,22.73725194,113.93018292,22.73724946,113.93018962,22.73724698,113.93019632,22.73724450,113.93020302,22.73724078,113.93020973,22.73723830,113.93021643,22.73723458,113.93022313,22.73723086,113.93022983,22.73722714,113.93023653,22.73722466,113.93024323,22.73722094,113.93024993,22.73721599,113.93025664,22.73721227,113.93026334,22.73720855,113.93027004,22.73720484,113.93027674,22.73719988,113.93028344,22.73719616,113.93028880,22.73719121,113.93029550,22.73718625,113.93030221,22.73718130,113.93030757,22.73717635,113.93031427,22.73717139,113.93031963,22.73716644,113.93032633,22.73716025,113.93033169,22.73715530,113.93033839,22.73714910,113.93034375,22.73714415,113.93034912,22.73713796,113.93035448,22.73713177,113.93035984,22.73712558,113.93036520,22.73711939,113.93036922,22.73711321,113.93037458,22.73710702,113.93037860,22.73709959,113.93038397,22.73709340,113.93038799,22.73708598,113.93039201,22.73707855,113.93039603,22.73707237,113.93040005,22.73706494,113.93040273,22.73705752,113.93040675,22.73705009,113.93040943,22.73704267,113.93041211,22.73703525,113.93041479,22.73702659,113.93041614,22.73701917,113.93041882,22.73701175,113.93042016,22.73700309,113.93042150,22.73699567,113.93042284,22.73698825,113.93042418,22.73697959,113.93042552,22.73697217,113.93042552,22.73696351,113.93042552,22.73695486,113.93042552,22.73694744,113.93042552,22.73693878,113.93042418,22.73693136,113.93042284,22.73692271,113.93042150,22.73691529,113.93042016,22.73690787,113.93041882,22.73689922,113.93041614,22.73689180,113.93041480,22.73688439,113.93041212,22.73687573,113.93040944,22.73686832,113.93040676,22.73686090,113.93040274,22.73685349,113.93040006,22.73684607,113.93039604,22.73683866,113.93039202,22.73683248,113.93038800,22.73682507,113.93038398,22.73681765,113.93037862,22.73681148,113.93037460,22.73680406,113.93036924,22.73679789,113.93036522,22.73679171,113.93035986,22.73678553,113.93035450,22.73677936,113.93034914,22.73677318,113.93034378,22.73676700,113.93033842,22.73676206,113.93033172,22.73675589,113.93032636,22.73675095,113.93031966,22.73674478,113.93031430,22.73673984,113.93030759,22.73673490,113.93030223,22.73672996,113.93029553,22.73672502,113.93028883,22.73672009,113.93028347,22.73671515,113.93027677,22.73671144,113.93027007,22.73670651,113.93026337,22.73670281,113.93025667,22.73669910,113.93024997,22.73669540,113.93024327,22.73669047,113.93023657,22.73668676,113.93022986,22.73668430,113.93022316,22.73668060,113.93021646,22.73667690,113.93020976,22.73667320,113.93020306,22.73667073,113.93019636,22.73666703,113.93018966,22.73666457,113.93018296,22.73666210,113.93017626,22.73665964,113.93016956,22.73665594,113.93016285,22.73665347,113.93015615,22.73665101,113.93014945,22.73664854,113.93014275,22.73664731,113.93013605,22.73664485,113.93012935,22.73664238,113.93012265,22.73663992,113.93011595,22.73663869,113.93010925,22.73663623,113.93010254,22.73663500,113.93009584,22.73663377,113.93008914,22.73663131,113.93008244,22.73663008,113.93007574,22.73662885,113.93006904,22.73662762,113.93006234,22.73662639,113.93005564,22.73662517,113.93004894,22.73662394,113.93004223,22.73662271,113.93003553,22.73662148,113.93002883,22.73662025,113.93002213,22.73661902,113.93001677,22.73661903,113.93001007,22.73661780,113.93000337,22.73661658,113.92999667,22.73661658,113.92998997,22.73661536,113.92998326,22.73661536,113.92997656,22.73661537,113.92996986,22.73661414,113.92996450,22.73661415,113.92995780,22.73661416,113.92995110,22.73661293,113.92994440,22.73661294,113.92993770,22.73661295,113.92993099,22.73661296,113.92992563,22.73661296,113.92991893,22.73661297,113.92991223,22.73661298,113.92990553,22.73661299,113.92989883,22.73661300,113.92989213,22.73661424,113.92988543,22.73661425,113.92988006,22.73661426,113.92987336,22.73661550,113.92986666,22.73661551,113.92985996,22.73661552,113.92985326,22.73661676,113.92984656,22.73661677,113.92983986,22.73661802,113.92983315,22.73661926,113.92982779,22.73661927,113.92982109,22.73662051,113.92981439,22.73662176,113.92980769,22.73662300,113.92980099,22.73662425,113.92979429,22.73662549,113.92978759,22.73662674,113.92978088,22.73662798,113.92977418,22.73662923,113.92976748,22.73663047,113.92976078,22.73663172,113.92975408,22.73663420,113.92974738,22.73663544,113.92974068,22.73663669,113.92973397,22.73663917,113.92972727,22.73664041,113.92972057,22.73664290,113.92971387,22.73664538,113.92970717,22.73664786,113.92970047,22.73664910,113.92969377,22.73665159,113.92968706,22.73665407,113.92968036,22.73665655,113.92967366,22.73666027,113.92966696,22.73666275,113.92966026,22.73666523,113.92965356,22.73666771,113.92964685,22.73667143,113.92964015,22.73667391,113.92963345,22.73667763,113.92962675,22.73668135,113.92962005,22.73668506,113.92961335,22.73668754,113.92960664,22.73669126,113.92959994,22.73669622,113.92959324,22.73669994,113.92958654,22.73670365,113.92957984,22.73670737,113.92957314,22.73671233,113.92956643,22.73671604,113.92956107,22.73672100,113.92955437,22.73672595,113.92954767,22.73673091,113.92954231,22.73673586,113.92953561,22.73674081,113.92953025,22.73674577,113.92952354,22.73675196,113.92951818,22.73675691,113.92951148,22.73676310,113.92950612,22.73676805,113.92950076,22.73677424,113.92949540,22.73678043,113.92949003,22.73678662,113.92948467,22.73679281,113.92948065,22.73679900,113.92947529,22.73680519,113.92947127,22.73681261,113.92946591,22.73681880,113.92946189,22.73682623,113.92945786,22.73683365,113.92945384,22.73683984,113.92944982,22.73684726,113.92944714,22.73685468,113.92944312,22.73686211,113.92944044,22.73686953,113.92943776,22.73687695,113.92943508,22.73688561,113.92943374,22.73689303,113.92943105,22.73690046,113.92942971,22.73690911,113.92942837,22.73691654,113.92942703,22.73692396,113.92942569,22.73693261,113.92942435,22.73694003,113.92942435,22.73694869,113.92942435,22.73695611",
            //塘家社区老旧房片区
            "113.93333112,22.73407473,113.93333112,22.73409080,113.93333111,22.73410688,113.93333111,22.73412172,113.93333245,22.73413779,113.93333379,22.73415386,113.93333379,22.73416870,113.93333513,22.73418478,113.93333781,22.73420085,113.93333915,22.73421569,113.93334183,22.73423176,113.93334317,22.73424659,113.93334584,22.73426267,113.93334852,22.73427750,113.93335254,22.73429357,113.93335522,22.73430964,113.93335924,22.73432448,113.93336192,22.73434055,113.93336594,22.73435538,113.93336996,22.73437145,113.93337532,22.73438628,113.93337934,22.73440112,113.93338470,22.73441718,113.93338872,22.73443202,113.93339407,22.73444808,113.93339943,22.73446292,113.93340613,22.73447775,113.93341149,22.73449258,113.93341819,22.73450864,113.93342489,22.73452347,113.93343159,22.73453830,113.93343829,22.73455313,113.93344633,22.73456796,113.93345303,22.73458279,113.93346107,22.73459762,113.93346911,22.73461244,113.93347715,22.73462727,113.93348652,22.73464086,113.93349456,22.73465569,113.93350394,22.73467051,113.93351332,22.73468410,113.93352270,22.73469893,113.93353342,22.73471252,113.93354414,22.73472610,113.93355352,22.73474093,113.93356424,22.73475452,113.93357630,22.73476810,113.93358702,22.73478045,113.93359908,22.73479404,113.93361114,22.73480762,113.93362320,22.73481997,113.93363526,22.73483356,113.93364732,22.73484590,113.93366072,22.73485825,113.93367411,22.73487060,113.93368751,22.73488294,113.93370091,22.73489405,113.93371565,22.73490640,113.93373039,22.73491751,113.93374513,22.73492862,113.93375987,22.73493972,113.93377461,22.73494960,113.93379069,22.73496070,113.93380543,22.73497057,113.93382151,22.73498044,113.93383759,22.73499031,113.93385367,22.73500018,113.93387109,22.73500881,113.93388717,22.73501745,113.93390459,22.73502608,113.93392201,22.73503347,113.93393943,22.73504210,113.93395685,22.73504950,113.93397561,22.73505565,113.93399303,22.73506305,113.93401179,22.73506921,113.93403055,22.73507536,113.93404930,22.73508028,113.93406806,22.73508520,113.93408682,22.73509012,113.93410558,22.73509504,113.93412434,22.73509872,113.93414444,22.73510240,113.93416320,22.73510609,113.93418330,22.73510853,113.93420206,22.73511098,113.93422216,22.73511218,113.93424092,22.73511339,113.93426102,22.73511460,113.93428112,22.73511581,113.93430122,22.73511578,113.93431998,22.73511575,113.93434008,22.73511449,113.93436018,22.73511322,113.93437894,22.73511196,113.93439904,22.73511069,113.93441780,22.73510819,113.93443790,22.73510569,113.93445666,22.73510195,113.93447676,22.73509821,113.93449552,22.73509448,113.93451428,22.73508950,113.93453304,22.73508453,113.93455180,22.73507956,113.93457056,22.73507458,113.93458932,22.73506837,113.93460808,22.73506216,113.93462550,22.73505472,113.93464426,22.73504851,113.93466168,22.73504106,113.93467910,22.73503238,113.93469652,22.73502494,113.93471394,22.73501626,113.93473002,22.73500758,113.93474744,22.73499890,113.93476352,22.73498898,113.93477960,22.73497906,113.93479568,22.73496915,113.93481042,22.73495923,113.93482650,22.73494808,113.93484124,22.73493817,113.93485598,22.73492702,113.93487072,22.73491587,113.93488546,22.73490471,113.93490020,22.73489233,113.93491360,22.73488118,113.93492700,22.73486879,113.93494040,22.73485641,113.93495380,22.73484402,113.93496586,22.73483164,113.93497792,22.73481802,113.93498998,22.73480564,113.93500204,22.73479202,113.93501410,22.73477840,113.93502482,22.73476601,113.93503689,22.73475239,113.93504761,22.73473878,113.93505699,22.73472392,113.93506771,22.73471031,113.93507843,22.73469669,113.93508781,22.73468183,113.93509719,22.73466822,113.93510657,22.73465337,113.93511461,22.73463852,113.93512399,22.73462490,113.93513203,22.73461005,113.93514007,22.73459520,113.93514811,22.73458035,113.93515481,22.73456550,113.93516285,22.73455065,113.93516955,22.73453580,113.93517625,22.73452095,113.93518295,22.73450610,113.93518965,22.73449002,113.93519502,22.73447517,113.93520172,22.73446032,113.93520708,22.73444547,113.93521244,22.73442939,113.93521646,22.73441455,113.93522182,22.73439846,113.93522584,22.73438362,113.93523120,22.73436877,113.93523522,22.73435269,113.93523924,22.73433784,113.93524192,22.73432176,113.93524594,22.73430692,113.93524862,22.73429084,113.93525264,22.73427476,113.93525533,22.73425992,113.93525801,22.73424384,113.93525935,22.73422900,113.93526203,22.73421292,113.93526337,22.73419808,113.93526605,22.73418200,113.93526739,22.73416592,113.93526739,22.73415108,113.93526873,22.73413500,113.93527007,22.73411892,113.93527007,22.73410409,113.93527008,22.73408801,113.93527142,22.73407193,113.93527008,22.73405710,113.93527008,22.73404102,113.93527008,22.73402618,113.93526874,22.73401011,113.93526740,22.73399403,113.93526740,22.73397920,113.93526606,22.73396312,113.93526339,22.73394705,113.93526205,22.73393221,113.93525937,22.73391614,113.93525803,22.73390130,113.93525535,22.73388523,113.93525267,22.73387040,113.93524865,22.73385433,113.93524597,22.73383826,113.93524195,22.73382342,113.93523928,22.73380735,113.93523526,22.73379252,113.93523124,22.73377645,113.93522588,22.73376162,113.93522186,22.73374679,113.93521650,22.73373072,113.93521248,22.73371589,113.93520712,22.73369982,113.93520177,22.73368499,113.93519507,22.73367016,113.93518971,22.73365533,113.93518301,22.73363926,113.93517631,22.73362443,113.93516961,22.73360960,113.93516291,22.73359477,113.93515487,22.73357995,113.93514818,22.73356512,113.93514014,22.73355029,113.93513210,22.73353546,113.93512406,22.73352064,113.93511468,22.73350705,113.93510664,22.73349222,113.93509726,22.73347739,113.93508788,22.73346381,113.93507851,22.73344898,113.93506779,22.73343539,113.93505707,22.73342181,113.93504769,22.73340698,113.93503697,22.73339340,113.93502491,22.73337981,113.93501419,22.73336746,113.93500214,22.73335388,113.93499008,22.73334029,113.93497802,22.73332794,113.93496596,22.73331436,113.93495390,22.73330201,113.93494050,22.73328966,113.93492710,22.73327732,113.93491370,22.73326497,113.93490031,22.73325386,113.93488557,22.73324152,113.93487083,22.73323041,113.93485609,22.73321930,113.93484135,22.73320820,113.93482661,22.73319832,113.93481053,22.73318722,113.93479579,22.73317735,113.93477972,22.73316748,113.93476364,22.73315761,113.93474756,22.73314774,113.93473014,22.73313911,113.93471406,22.73313048,113.93469664,22.73312185,113.93467922,22.73311445,113.93466180,22.73310582,113.93464438,22.73309843,113.93462562,22.73309227,113.93460821,22.73308488,113.93458945,22.73307872,113.93457069,22.73307257,113.93455193,22.73306765,113.93453317,22.73306273,113.93451441,22.73305781,113.93449565,22.73305289,113.93447689,22.73304921,113.93445679,22.73304553,113.93443803,22.73304184,113.93441793,22.73303940,113.93439917,22.73303695,113.93437907,22.73303575,113.93436031,22.73303454,113.93434021,22.73303333,113.93432011,22.73303212,113.93430135,22.73303215,113.93428125,22.73303218,113.93426115,22.73303344,113.93424105,22.73303471,113.93422229,22.73303597,113.93420219,22.73303724,113.93418343,22.73303974,113.93416333,22.73304224,113.93414457,22.73304598,113.93412447,22.73304971,113.93410571,22.73305345,113.93408695,22.73305842,113.93406819,22.73306340,113.93404943,22.73306837,113.93403067,22.73307334,113.93401191,22.73307955,113.93399315,22.73308576,113.93397573,22.73309321,113.93395697,22.73309942,113.93393955,22.73310686,113.93392213,22.73311554,113.93390471,22.73312299,113.93388729,22.73313167,113.93387121,22.73314035,113.93385379,22.73314903,113.93383771,22.73315894,113.93382162,22.73316886,113.93380554,22.73317877,113.93379080,22.73318869,113.93377472,22.73319984,113.93375998,22.73320975,113.93374524,22.73322090,113.93373050,22.73323205,113.93371576,22.73324320,113.93370102,22.73325559,113.93368762,22.73326674,113.93367422,22.73327912,113.93366081,22.73329151,113.93364741,22.73330389,113.93363535,22.73331628,113.93362329,22.73332990,113.93361123,22.73334228,113.93359917,22.73335590,113.93358711,22.73336952,113.93357639,22.73338190,113.93356433,22.73339552,113.93355360,22.73340913,113.93354422,22.73342399,113.93353350,22.73343760,113.93352278,22.73345122,113.93351340,22.73346607,113.93350402,22.73347969,113.93349464,22.73349454,113.93348660,22.73350939,113.93347722,22.73352301,113.93346917,22.73353786,113.93346113,22.73355271,113.93345309,22.73356756,113.93344639,22.73358241,113.93343835,22.73359726,113.93343165,22.73361211,113.93342495,22.73362695,113.93341825,22.73364180,113.93341155,22.73365789,113.93340618,22.73367273,113.93339948,22.73368758,113.93339412,22.73370243,113.93338876,22.73371851,113.93338474,22.73373336,113.93337938,22.73374944,113.93337536,22.73376428,113.93337000,22.73377913,113.93336598,22.73379521,113.93336195,22.73381006,113.93335927,22.73382614,113.93335525,22.73384098,113.93335257,22.73385706,113.93334855,22.73387314,113.93334587,22.73388798,113.93334319,22.73390406,113.93334185,22.73391890,113.93333917,22.73393498,113.93333782,22.73394982,113.93333514,22.73396590,113.93333380,22.73398198,113.93333380,22.73399682,113.93333246,22.73401290,113.93333112,22.73402897,113.93333112,22.73404381,113.93333112,22.73405989,113.93333112,22.73407473",
            //东坑社区老旧房片区
            "113.91068817,22.75801557,113.91068817,22.75802793,113.91068817,22.75803906,113.91068817,22.75805018,113.91068817,22.75806131,113.91068951,22.75807244,113.91068951,22.75808356,113.91069085,22.75809469,113.91069219,22.75810582,113.91069353,22.75811694,113.91069487,22.75812807,113.91069621,22.75813920,113.91069756,22.75815032,113.91069890,22.75816145,113.91070158,22.75817258,113.91070292,22.75818370,113.91070560,22.75819483,113.91070828,22.75820595,113.91071097,22.75821708,113.91071365,22.75822821,113.91071633,22.75823933,113.91071901,22.75825046,113.91072169,22.75826035,113.91072572,22.75827147,113.91072974,22.75828260,113.91073242,22.75829372,113.91073645,22.75830485,113.91074047,22.75831597,113.91074450,22.75832710,113.91074852,22.75833823,113.91075388,22.75834935,113.91075791,22.75836048,113.91076327,22.75837160,113.91076864,22.75838273,113.91077400,22.75839261,113.91077937,22.75840374,113.91078473,22.75841486,113.91079010,22.75842599,113.91079547,22.75843588,113.91080217,22.75844700,113.91080888,22.75845813,113.91081559,22.75846801,113.91082229,22.75847914,113.91082900,22.75848903,113.91083571,22.75850015,113.91084375,22.75851004,113.91085046,22.75852116,113.91085851,22.75853105,113.91086656,22.75854094,113.91087461,22.75855082,113.91088399,22.75856071,113.91089204,22.75857060,113.91090143,22.75858048,113.91091082,22.75859037,113.91091887,22.75860026,113.91092960,22.75860891,113.91093899,22.75861880,113.91094838,22.75862745,113.91095911,22.75863610,113.91096984,22.75864598,113.91098057,22.75865463,113.91099131,22.75866205,113.91100204,22.75867070,113.91101277,22.75867935,113.91102484,22.75868676,113.91103691,22.75869417,113.91104765,22.75870159,113.91105972,22.75870900,113.91107313,22.75871641,113.91108521,22.75872259,113.91109728,22.75873000,113.91111069,22.75873618,113.91112411,22.75874235,113.91113752,22.75874729,113.91115093,22.75875347,113.91116435,22.75875841,113.91117776,22.75876335,113.91119118,22.75876829,113.91120459,22.75877199,113.91121935,22.75877569,113.91123410,22.75877940,113.91124752,22.75878310,113.91126227,22.75878557,113.91127703,22.75878803,113.91129179,22.75879050,113.91130654,22.75879297,113.91131996,22.75879420,113.91133471,22.75879543,113.91134947,22.75879666,113.91136422,22.75879665,113.91138032,22.75879664,113.91139508,22.75879664,113.91140983,22.75879663,113.91142459,22.75879539,113.91143934,22.75879415,113.91145276,22.75879290,113.91146751,22.75879043,113.91148227,22.75878795,113.91149703,22.75878547,113.91151178,22.75878299,113.91152520,22.75877927,113.91153995,22.75877556,113.91155471,22.75877184,113.91156812,22.75876813,113.91158154,22.75876318,113.91159495,22.75875823,113.91160837,22.75875327,113.91162178,22.75874709,113.91163520,22.75874214,113.91164861,22.75873595,113.91166203,22.75872976,113.91167410,22.75872234,113.91168617,22.75871615,113.91169959,22.75870873,113.91171166,22.75870130,113.91172239,22.75869388,113.91173447,22.75868646,113.91174654,22.75867904,113.91175727,22.75867038,113.91176800,22.75866172,113.91177873,22.75865429,113.91178947,22.75864564,113.91180020,22.75863574,113.91181093,22.75862708,113.91182032,22.75861842,113.91182971,22.75860853,113.91184044,22.75859987,113.91184849,22.75858997,113.91185788,22.75858008,113.91186727,22.75857018,113.91187532,22.75856029,113.91188471,22.75855040,113.91189276,22.75854050,113.91190081,22.75853061,113.91190886,22.75852071,113.91191557,22.75850958,113.91192362,22.75849969,113.91193032,22.75848856,113.91193703,22.75847867,113.91194374,22.75846754,113.91195045,22.75845764,113.91195716,22.75844651,113.91196386,22.75843538,113.91196923,22.75842549,113.91197460,22.75841436,113.91197996,22.75840323,113.91198533,22.75839210,113.91199069,22.75838221,113.91199606,22.75837108,113.91200143,22.75835995,113.91200545,22.75834882,113.91201082,22.75833769,113.91201484,22.75832656,113.91201887,22.75831543,113.91202289,22.75830430,113.91202692,22.75829318,113.91202960,22.75828205,113.91203363,22.75827092,113.91203765,22.75825979,113.91204033,22.75824990,113.91204302,22.75823877,113.91204570,22.75822764,113.91204839,22.75821651,113.91205107,22.75820539,113.91205375,22.75819426,113.91205644,22.75818313,113.91205778,22.75817200,113.91206046,22.75816087,113.91206180,22.75814975,113.91206315,22.75813862,113.91206449,22.75812749,113.91206583,22.75811636,113.91206717,22.75810524,113.91206851,22.75809411,113.91206986,22.75808298,113.91206986,22.75807185,113.91207120,22.75806073,113.91207120,22.75804960,113.91207120,22.75803847,113.91207120,22.75802734,113.91207254,22.75801498,113.91207120,22.75800385,113.91207120,22.75799273,113.91207120,22.75798160,113.91207120,22.75797047,113.91206986,22.75795935,113.91206986,22.75794822,113.91206852,22.75793709,113.91206718,22.75792597,113.91206584,22.75791484,113.91206450,22.75790371,113.91206316,22.75789259,113.91206182,22.75788146,113.91206048,22.75787033,113.91205780,22.75785921,113.91205646,22.75784808,113.91205378,22.75783696,113.91205109,22.75782583,113.91204841,22.75781471,113.91204573,22.75780358,113.91204305,22.75779245,113.91204036,22.75778133,113.91203768,22.75777144,113.91203366,22.75776031,113.91202964,22.75774919,113.91202695,22.75773806,113.91202293,22.75772694,113.91201891,22.75771581,113.91201488,22.75770469,113.91201086,22.75769356,113.91200549,22.75768244,113.91200147,22.75767131,113.91199611,22.75766019,113.91199074,22.75764906,113.91198538,22.75763917,113.91198001,22.75762805,113.91197465,22.75761692,113.91196928,22.75760580,113.91196392,22.75759591,113.91195721,22.75758479,113.91195050,22.75757366,113.91194380,22.75756378,113.91193709,22.75755265,113.91193038,22.75754276,113.91192368,22.75753164,113.91191563,22.75752175,113.91190892,22.75751063,113.91190088,22.75750074,113.91189283,22.75749086,113.91188478,22.75748097,113.91187539,22.75747108,113.91186734,22.75746119,113.91185795,22.75745131,113.91184857,22.75744142,113.91184052,22.75743153,113.91182979,22.75742288,113.91182040,22.75741300,113.91181101,22.75740435,113.91180028,22.75739570,113.91178955,22.75738581,113.91177882,22.75737716,113.91176808,22.75736975,113.91175735,22.75736110,113.91174662,22.75735245,113.91173455,22.75734504,113.91172248,22.75733762,113.91171175,22.75733021,113.91169968,22.75732280,113.91168626,22.75731539,113.91167419,22.75730921,113.91166212,22.75730180,113.91164870,22.75729562,113.91163529,22.75728945,113.91162187,22.75728451,113.91160846,22.75727833,113.91159505,22.75727339,113.91158163,22.75726845,113.91156822,22.75726351,113.91155480,22.75725981,113.91154005,22.75725611,113.91152529,22.75725240,113.91151188,22.75724870,113.91149712,22.75724623,113.91148237,22.75724377,113.91146761,22.75724130,113.91145286,22.75723883,113.91143944,22.75723760,113.91142469,22.75723637,113.91140993,22.75723514,113.91139518,22.75723515,113.91138042,22.75723392,113.91136432,22.75723516,113.91134957,22.75723517,113.91133481,22.75723641,113.91132005,22.75723765,113.91130664,22.75723890,113.91129188,22.75724137,113.91127713,22.75724385,113.91126237,22.75724633,113.91124762,22.75724881,113.91123420,22.75725253,113.91121944,22.75725624,113.91120469,22.75725996,113.91119127,22.75726367,113.91117786,22.75726862,113.91116444,22.75727357,113.91115103,22.75727852,113.91113761,22.75728471,113.91112420,22.75728966,113.91111078,22.75729585,113.91109737,22.75730204,113.91108530,22.75730946,113.91107322,22.75731565,113.91105981,22.75732307,113.91104773,22.75733049,113.91103700,22.75733791,113.91102493,22.75734534,113.91101285,22.75735276,113.91100212,22.75736142,113.91099139,22.75737008,113.91098066,22.75737750,113.91096992,22.75738616,113.91095919,22.75739605,113.91094846,22.75740471,113.91093907,22.75741337,113.91092968,22.75742327,113.91091895,22.75743192,113.91091090,22.75744182,113.91090150,22.75745171,113.91089211,22.75746161,113.91088406,22.75747150,113.91087467,22.75748140,113.91086662,22.75749129,113.91085857,22.75750118,113.91085052,22.75751108,113.91084382,22.75752221,113.91083577,22.75753210,113.91082906,22.75754323,113.91082235,22.75755312,113.91081564,22.75756425,113.91080893,22.75757415,113.91080223,22.75758528,113.91079552,22.75759641,113.91079015,22.75760630,113.91078479,22.75761743,113.91077942,22.75762856,113.91077405,22.75763969,113.91076869,22.75764958,113.91076332,22.75766071,113.91075795,22.75767184,113.91075393,22.75768297,113.91074856,22.75769410,113.91074454,22.75770522,113.91074051,22.75771635,113.91073649,22.75772748,113.91073246,22.75773861,113.91072978,22.75774974,113.91072575,22.75776087,113.91072173,22.75777200,113.91071904,22.75778189,113.91071636,22.75779302,113.91071367,22.75780414,113.91071099,22.75781527,113.91070831,22.75782640,113.91070562,22.75783753,113.91070294,22.75784866,113.91070160,22.75785978,113.91069891,22.75787091,113.91069757,22.75788204,113.91069623,22.75789317,113.91069489,22.75790429,113.91069355,22.75791542,113.91069220,22.75792655,113.91069086,22.75793768,113.91068952,22.75794880,113.91068952,22.75795993,113.91068818,22.75797106,113.91068818,22.75798219,113.91068817,22.75799331,113.91068817,22.75800444,113.91068817,22.75801557",
            //甲子塘老旧房片区
            "113.91253397,22.73700765,113.91253397,22.73701507,113.91253397,22.73702126,113.91253397,22.73702744,113.91253397,22.73703362,113.91253397,22.73703980,113.91253531,22.73704599,113.91253531,22.73705341,113.91253531,22.73705959,113.91253531,22.73706577,113.91253665,22.73707195,113.91253665,22.73707937,113.91253665,22.73708555,113.91253799,22.73709174,113.91253799,22.73709792,113.91253933,22.73710534,113.91253933,22.73711152,113.91254067,22.73711770,113.91254067,22.73712512,113.91254202,22.73713130,113.91254336,22.73713872,113.91254336,22.73714491,113.91254470,22.73715232,113.91254604,22.73715851,113.91254738,22.73716593,113.91254872,22.73717211,113.91255006,22.73717953,113.91255140,22.73718694,113.91255274,22.73719313,113.91255408,22.73720055,113.91255542,22.73720796,113.91255676,22.73721538,113.91255811,22.73722280,113.91256079,22.73723022,113.91256213,22.73723764,113.91256481,22.73724506,113.91256615,22.73725247,113.91256883,22.73725989,113.91257018,22.73726731,113.91257286,22.73727473,113.91257554,22.73728338,113.91257822,22.73729080,113.91258090,22.73729822,113.91258359,22.73730687,113.91258627,22.73731429,113.91258895,22.73732295,113.91259297,22.73733036,113.91259566,22.73733902,113.91259968,22.73734767,113.91260236,22.73735509,113.91260639,22.73736374,113.91261041,22.73737240,113.91261443,22.73738105,113.91261846,22.73738971,113.91262382,22.73739836,113.91262785,22.73740701,113.91263321,22.73741567,113.91263723,22.73742432,113.91264260,22.73743297,113.91264796,22.73744163,113.91265333,22.73745028,113.91266003,22.73745893,113.91266540,22.73746759,113.91267211,22.73747624,113.91267881,22.73748365,113.91268552,22.73749231,113.91269222,22.73750096,113.91269893,22.73750961,113.91270698,22.73751703,113.91271503,22.73752568,113.91272307,22.73753309,113.91273112,22.73754175,113.91273917,22.73754916,113.91274856,22.73755658,113.91275795,22.73756275,113.91276734,22.73757017,113.91277673,22.73757635,113.91278611,22.73758253,113.91279684,22.73758870,113.91280623,22.73759364,113.91281696,22.73759859,113.91282769,22.73760353,113.91283843,22.73760847,113.91285050,22.73761217,113.91286123,22.73761464,113.91287196,22.73761834,113.91288403,22.73762081,113.91289476,22.73762204,113.91290683,22.73762327,113.91291891,22.73762450,113.91293098,22.73762450,113.91294171,22.73762449,113.91295378,22.73762325,113.91296585,22.73762201,113.91297658,22.73762076,113.91298866,22.73761829,113.91299939,22.73761457,113.91301012,22.73761209,113.91302219,22.73760838,113.91303292,22.73760342,113.91304365,22.73759847,113.91305438,22.73759352,113.91306377,22.73758857,113.91307450,22.73758238,113.91308389,22.73757620,113.91309328,22.73757001,113.91310267,22.73756258,113.91311206,22.73755640,113.91312145,22.73754897,113.91312950,22.73754155,113.91313755,22.73753289,113.91314560,22.73752547,113.91315365,22.73751681,113.91316169,22.73750938,113.91316840,22.73750072,113.91317511,22.73749206,113.91318182,22.73748341,113.91318852,22.73747598,113.91319523,22.73746732,113.91320060,22.73745867,113.91320730,22.73745001,113.91321267,22.73744135,113.91321803,22.73743269,113.91322340,22.73742403,113.91322743,22.73741537,113.91323279,22.73740671,113.91323682,22.73739806,113.91324218,22.73738940,113.91324621,22.73738074,113.91325023,22.73737208,113.91325426,22.73736342,113.91325828,22.73735477,113.91326096,22.73734735,113.91326499,22.73733869,113.91326767,22.73733003,113.91327169,22.73732261,113.91327438,22.73731395,113.91327706,22.73730653,113.91327974,22.73729787,113.91328243,22.73729045,113.91328511,22.73728303,113.91328779,22.73727438,113.91329048,22.73726696,113.91329182,22.73725954,113.91329450,22.73725211,113.91329584,22.73724469,113.91329853,22.73723727,113.91329987,22.73722985,113.91330255,22.73722243,113.91330389,22.73721501,113.91330524,22.73720759,113.91330658,22.73720017,113.91330792,22.73719275,113.91330926,22.73718657,113.91331060,22.73717915,113.91331194,22.73717173,113.91331329,22.73716555,113.91331463,22.73715813,113.91331597,22.73715194,113.91331731,22.73714452,113.91331731,22.73713834,113.91331865,22.73713092,113.91331999,22.73712474,113.91332000,22.73711732,113.91332134,22.73711113,113.91332134,22.73710495,113.91332268,22.73709753,113.91332268,22.73709135,113.91332402,22.73708517,113.91332402,22.73707898,113.91332402,22.73707156,113.91332536,22.73706538,113.91332536,22.73705920,113.91332536,22.73705302,113.91332537,22.73704560,113.91332671,22.73703941,113.91332671,22.73703323,113.91332671,22.73702705,113.91332671,22.73702086,113.91332671,22.73701468,113.91332671,22.73700726,113.91332671,22.73700108,113.91332671,22.73699490,113.91332671,22.73698871,113.91332671,22.73698253,113.91332671,22.73697635,113.91332537,22.73697017,113.91332537,22.73696275,113.91332537,22.73695656,113.91332537,22.73695038,113.91332403,22.73694420,113.91332403,22.73693678,113.91332403,22.73693060,113.91332269,22.73692442,113.91332269,22.73691823,113.91332135,22.73691081,113.91332135,22.73690463,113.91332001,22.73689845,113.91332001,22.73689103,113.91331867,22.73688485,113.91331733,22.73687743,113.91331733,22.73687125,113.91331599,22.73686383,113.91331465,22.73685765,113.91331331,22.73685023,113.91331196,22.73684405,113.91331062,22.73683663,113.91330928,22.73682921,113.91330794,22.73682303,113.91330660,22.73681561,113.91330526,22.73680819,113.91330392,22.73680077,113.91330258,22.73679335,113.91329990,22.73678593,113.91329856,22.73677852,113.91329587,22.73677110,113.91329453,22.73676368,113.91329185,22.73675626,113.91329051,22.73674884,113.91328783,22.73674142,113.91328515,22.73673277,113.91328246,22.73672535,113.91327978,22.73671793,113.91327710,22.73670928,113.91327442,22.73670186,113.91327173,22.73669321,113.91326771,22.73668579,113.91326503,22.73667714,113.91326101,22.73666848,113.91325832,22.73666106,113.91325430,22.73665241,113.91325028,22.73664376,113.91324625,22.73663510,113.91324223,22.73662645,113.91323687,22.73661780,113.91323284,22.73660914,113.91322748,22.73660049,113.91322345,22.73659183,113.91321809,22.73658318,113.91321272,22.73657453,113.91320736,22.73656587,113.91320065,22.73655722,113.91319529,22.73654857,113.91318858,22.73653992,113.91318188,22.73653250,113.91317517,22.73652385,113.91316846,22.73651520,113.91316176,22.73650654,113.91315371,22.73649913,113.91314566,22.73649048,113.91313762,22.73648306,113.91312957,22.73647441,113.91312152,22.73646699,113.91311213,22.73645958,113.91310274,22.73645340,113.91309335,22.73644599,113.91308397,22.73643981,113.91307458,22.73643363,113.91306385,22.73642745,113.91305446,22.73642251,113.91304373,22.73641757,113.91303300,22.73641263,113.91302227,22.73640769,113.91301019,22.73640399,113.91299946,22.73640152,113.91298873,22.73639781,113.91297666,22.73639535,113.91296593,22.73639411,113.91295386,22.73639288,113.91294179,22.73639165,113.91293106,22.73639166,113.91291898,22.73639167,113.91290691,22.73639291,113.91289484,22.73639415,113.91288411,22.73639539,113.91287204,22.73639787,113.91286130,22.73640159,113.91285057,22.73640406,113.91283850,22.73640778,113.91282777,22.73641273,113.91281704,22.73641768,113.91280631,22.73642263,113.91279692,22.73642758,113.91278619,22.73643377,113.91277680,22.73643996,113.91276741,22.73644615,113.91275802,22.73645357,113.91274863,22.73645976,113.91273924,22.73646718,113.91273119,22.73647461,113.91272314,22.73648327,113.91271509,22.73649069,113.91270704,22.73649935,113.91269899,22.73650677,113.91269229,22.73651543,113.91268558,22.73652409,113.91267887,22.73653275,113.91267217,22.73654017,113.91266546,22.73654883,113.91266009,22.73655749,113.91265338,22.73656615,113.91264802,22.73657481,113.91264265,22.73658347,113.91263729,22.73659212,113.91263326,22.73660078,113.91262790,22.73660944,113.91262387,22.73661810,113.91261851,22.73662676,113.91261448,22.73663541,113.91261046,22.73664407,113.91260643,22.73665273,113.91260241,22.73666139,113.91259972,22.73666881,113.91259570,22.73667747,113.91259302,22.73668612,113.91258899,22.73669354,113.91258631,22.73670220,113.91258362,22.73670962,113.91258094,22.73671828,113.91257826,22.73672570,113.91257557,22.73673312,113.91257289,22.73674178,113.91257021,22.73674920,113.91256887,22.73675662,113.91256618,22.73676404,113.91256484,22.73677146,113.91256216,22.73677888,113.91256082,22.73678630,113.91255813,22.73679372,113.91255679,22.73680114,113.91255545,22.73680856,113.91255411,22.73681598,113.91255277,22.73682340,113.91255142,22.73682958,113.91255008,22.73683700,113.91254874,22.73684442,113.91254740,22.73685061,113.91254606,22.73685803,113.91254471,22.73686421,113.91254337,22.73687163,113.91254337,22.73687781,113.91254203,22.73688523,113.91254069,22.73689141,113.91254069,22.73689883,113.91253935,22.73690502,113.91253935,22.73691120,113.91253800,22.73691862,113.91253800,22.73692480,113.91253666,22.73693099,113.91253666,22.73693717,113.91253666,22.73694459,113.91253532,22.73695077,113.91253532,22.73695695,113.91253532,22.73696314,113.91253532,22.73697056,113.91253398,22.73697674,113.91253398,22.73698292,113.91253398,22.73698911,113.91253398,22.73699529,113.91253398,22.73700147,113.91253397,22.73700765",
            //塘尾社区旧村老旧房片区
            "113.89676707,22.75629450,113.89676707,22.75630933,113.89676707,22.75632293,113.89676841,22.75633777,113.89676841,22.75635137,113.89676841,22.75636497,113.89676975,22.75637981,113.89677109,22.75639341,113.89677244,22.75640701,113.89677378,22.75642184,113.89677512,22.75643544,113.89677646,22.75645028,113.89677914,22.75646388,113.89678049,22.75647748,113.89678317,22.75649232,113.89678451,22.75650592,113.89678719,22.75652075,113.89678988,22.75653435,113.89679256,22.75654919,113.89679659,22.75656279,113.89679927,22.75657639,113.89680330,22.75659123,113.89680598,22.75660483,113.89681001,22.75661967,113.89681404,22.75663327,113.89681806,22.75664810,113.89682343,22.75666170,113.89682746,22.75667654,113.89683282,22.75669138,113.89683685,22.75670498,113.89684222,22.75671982,113.89684759,22.75673342,113.89685296,22.75674826,113.89685967,22.75676186,113.89686504,22.75677669,113.89687175,22.75679030,113.89687846,22.75680513,113.89688517,22.75681873,113.89689188,22.75683357,113.89689993,22.75684717,113.89690664,22.75686201,113.89691470,22.75687561,113.89692275,22.75689045,113.89693080,22.75690405,113.89694020,22.75691765,113.89694825,22.75693249,113.89695765,22.75694609,113.89696704,22.75695969,113.89697644,22.75697330,113.89698718,22.75698690,113.89699657,22.75700050,113.89700731,22.75701410,113.89701805,22.75702770,113.89703013,22.75704131,113.89704087,22.75705491,113.89705295,22.75706727,113.89706503,22.75708088,113.89707711,22.75709324,113.89709053,22.75710561,113.89710395,22.75711798,113.89711738,22.75713034,113.89713080,22.75714271,113.89714422,22.75715384,113.89715899,22.75716620,113.89717375,22.75717733,113.89718852,22.75718846,113.89720328,22.75719836,113.89721939,22.75720949,113.89723415,22.75721938,113.89725026,22.75722928,113.89726771,22.75723917,113.89728382,22.75724783,113.89730127,22.75725649,113.89731872,22.75726514,113.89733617,22.75727380,113.89735362,22.75728122,113.89737107,22.75728864,113.89738986,22.75729483,113.89740865,22.75730101,113.89742610,22.75730720,113.89744489,22.75731215,113.89746503,22.75731710,113.89748382,22.75732205,113.89750261,22.75732576,113.89752275,22.75732947,113.89754154,22.75733195,113.89756168,22.75733443,113.89758047,22.75733690,113.89760060,22.75733814,113.89762074,22.75733815,113.89764087,22.75733815,113.89765966,22.75733815,113.89767980,22.75733816,113.89769993,22.75733692,113.89771873,22.75733445,113.89773886,22.75733198,113.89775765,22.75732952,113.89777779,22.75732581,113.89779658,22.75732210,113.89781537,22.75731716,113.89783551,22.75731222,113.89785430,22.75730728,113.89787175,22.75730110,113.89789054,22.75729492,113.89790934,22.75728874,113.89792679,22.75728133,113.89794424,22.75727391,113.89796169,22.75726526,113.89797914,22.75725661,113.89799659,22.75724796,113.89801270,22.75723931,113.89803015,22.75722942,113.89804625,22.75721953,113.89806102,22.75720964,113.89807713,22.75719852,113.89809189,22.75718863,113.89810666,22.75717751,113.89812143,22.75716638,113.89813619,22.75715402,113.89814962,22.75714290,113.89816304,22.75713053,113.89817646,22.75711817,113.89818989,22.75710581,113.89820331,22.75709345,113.89821539,22.75708109,113.89822747,22.75706749,113.89823955,22.75705513,113.89825029,22.75704153,113.89826237,22.75702793,113.89827311,22.75701434,113.89828385,22.75700074,113.89829325,22.75698714,113.89830399,22.75697354,113.89831339,22.75695994,113.89832278,22.75694634,113.89833218,22.75693275,113.89834023,22.75691791,113.89834963,22.75690431,113.89835768,22.75689071,113.89836574,22.75687588,113.89837379,22.75686228,113.89838051,22.75684745,113.89838856,22.75683385,113.89839527,22.75681901,113.89840199,22.75680541,113.89840870,22.75679058,113.89841541,22.75677698,113.89842078,22.75676214,113.89842749,22.75674854,113.89843286,22.75673371,113.89843823,22.75672011,113.89844360,22.75670528,113.89844763,22.75669168,113.89845300,22.75667684,113.89845703,22.75666200,113.89846240,22.75664841,113.89846642,22.75663357,113.89847045,22.75661997,113.89847448,22.75660514,113.89847717,22.75659154,113.89848119,22.75657670,113.89848388,22.75656310,113.89848791,22.75654950,113.89849059,22.75653467,113.89849328,22.75652107,113.89849596,22.75650623,113.89849731,22.75649263,113.89849999,22.75647780,113.89850133,22.75646420,113.89850402,22.75645060,113.89850536,22.75643576,113.89850671,22.75642216,113.89850805,22.75640732,113.89850939,22.75639373,113.89851074,22.75638013,113.89851208,22.75636529,113.89851208,22.75635169,113.89851208,22.75633809,113.89851342,22.75632325,113.89851342,22.75630965,113.89851343,22.75629482,113.89851343,22.75628122,113.89851343,22.75626762,113.89851209,22.75625278,113.89851209,22.75623918,113.89851209,22.75622558,113.89851075,22.75621075,113.89850941,22.75619715,113.89850806,22.75618355,113.89850672,22.75616871,113.89850538,22.75615511,113.89850404,22.75614027,113.89850136,22.75612667,113.89850001,22.75611307,113.89849733,22.75609824,113.89849599,22.75608464,113.89849331,22.75606980,113.89849062,22.75605620,113.89848794,22.75604136,113.89848391,22.75602776,113.89848123,22.75601416,113.89847720,22.75599932,113.89847452,22.75598572,113.89847049,22.75597089,113.89846647,22.75595729,113.89846244,22.75594245,113.89845707,22.75592885,113.89845305,22.75591401,113.89844768,22.75589918,113.89844365,22.75588557,113.89843829,22.75587074,113.89843292,22.75585714,113.89842755,22.75584230,113.89842084,22.75582870,113.89841547,22.75581386,113.89840876,22.75580026,113.89840205,22.75578542,113.89839534,22.75577182,113.89838863,22.75575699,113.89838058,22.75574338,113.89837387,22.75572855,113.89836581,22.75571495,113.89835776,22.75570011,113.89834971,22.75568651,113.89834031,22.75567291,113.89833226,22.75565807,113.89832287,22.75564447,113.89831347,22.75563087,113.89830408,22.75561726,113.89829334,22.75560366,113.89828394,22.75559006,113.89827321,22.75557646,113.89826247,22.75556286,113.89825039,22.75554926,113.89823965,22.75553565,113.89822757,22.75552329,113.89821549,22.75550969,113.89820341,22.75549732,113.89818999,22.75548496,113.89817657,22.75547259,113.89816315,22.75546022,113.89814972,22.75544786,113.89813630,22.75543673,113.89812154,22.75542436,113.89810677,22.75541323,113.89809201,22.75540210,113.89807724,22.75539221,113.89806114,22.75538108,113.89804637,22.75537119,113.89803027,22.75536129,113.89801282,22.75535140,113.89799671,22.75534274,113.89797926,22.75533409,113.89796181,22.75532543,113.89794436,22.75531677,113.89792691,22.75530935,113.89790946,22.75530193,113.89789067,22.75529574,113.89787188,22.75528956,113.89785443,22.75528337,113.89783564,22.75527842,113.89781550,22.75527348,113.89779671,22.75526853,113.89777792,22.75526481,113.89775779,22.75526110,113.89773899,22.75525863,113.89771886,22.75525615,113.89770007,22.75525367,113.89767993,22.75525243,113.89765980,22.75525243,113.89764100,22.75525119,113.89762087,22.75525242,113.89760074,22.75525242,113.89758060,22.75525365,113.89756181,22.75525612,113.89754167,22.75525859,113.89752288,22.75526106,113.89750275,22.75526476,113.89748395,22.75526847,113.89746516,22.75527341,113.89744502,22.75527835,113.89742623,22.75528329,113.89740878,22.75528947,113.89738999,22.75529565,113.89737119,22.75530183,113.89735374,22.75530924,113.89733629,22.75531666,113.89731884,22.75532531,113.89730139,22.75533396,113.89728394,22.75534261,113.89726783,22.75535126,113.89725038,22.75536115,113.89723427,22.75537104,113.89721951,22.75538093,113.89720340,22.75539205,113.89718863,22.75540194,113.89717386,22.75541306,113.89715910,22.75542419,113.89714433,22.75543655,113.89713091,22.75544767,113.89711748,22.75546003,113.89710406,22.75547239,113.89709063,22.75548475,113.89707721,22.75549711,113.89706513,22.75550948,113.89705305,22.75552307,113.89704096,22.75553543,113.89703022,22.75554903,113.89701814,22.75556263,113.89700740,22.75557623,113.89699666,22.75558982,113.89698727,22.75560342,113.89697653,22.75561702,113.89696713,22.75563062,113.89695773,22.75564422,113.89694833,22.75565781,113.89694028,22.75567265,113.89693088,22.75568625,113.89692283,22.75569984,113.89691477,22.75571468,113.89690672,22.75572828,113.89690000,22.75574311,113.89689195,22.75575671,113.89688524,22.75577155,113.89687852,22.75578514,113.89687181,22.75579998,113.89686510,22.75581358,113.89685973,22.75582841,113.89685301,22.75584201,113.89684764,22.75585685,113.89684227,22.75587044,113.89683690,22.75588528,113.89683287,22.75589888,113.89682750,22.75591371,113.89682348,22.75592855,113.89681811,22.75594215,113.89681408,22.75595698,113.89681005,22.75597058,113.89680602,22.75598542,113.89680334,22.75599902,113.89679931,22.75601385,113.89679662,22.75602745,113.89679259,22.75604105,113.89678991,22.75605589,113.89678722,22.75606949,113.89678454,22.75608432,113.89678319,22.75609792,113.89678051,22.75611276,113.89677917,22.75612636,113.89677648,22.75613995,113.89677514,22.75615479,113.89677379,22.75616839,113.89677245,22.75618323,113.89677111,22.75619683,113.89676976,22.75621043,113.89676842,22.75622526,113.89676842,22.75623886,113.89676842,22.75625246,113.89676708,22.75626730,113.89676707,22.75628090,113.89676707,22.75629450",
            //塘尾社区工业区老旧房片区
            "113.90628017,22.74859069,113.90628017,22.74860306,113.90628017,22.74861419,113.90628151,22.74862531,113.90628151,22.74863644,113.90628285,22.74864757,113.90628420,22.74865870,113.90628420,22.74866982,113.90628554,22.74868095,113.90628822,22.74869208,113.90628956,22.74870321,113.90629090,22.74871433,113.90629358,22.74872546,113.90629627,22.74873659,113.90629895,22.74874772,113.90630163,22.74875884,113.90630432,22.74876997,113.90630700,22.74878110,113.90631102,22.74879223,113.90631371,22.74880335,113.90631773,22.74881324,113.90632175,22.74882437,113.90632578,22.74883550,113.90632980,22.74884662,113.90633383,22.74885652,113.90633919,22.74886764,113.90634322,22.74887753,113.90634859,22.74888866,113.90635395,22.74889855,113.90635932,22.74890844,113.90636469,22.74891957,113.90637005,22.74892946,113.90637676,22.74893935,113.90638213,22.74894924,113.90638883,22.74895913,113.90639554,22.74896902,113.90640225,22.74897891,113.90640896,22.74898880,113.90641567,22.74899869,113.90642372,22.74900734,113.90643043,22.74901723,113.90643848,22.74902588,113.90644518,22.74903577,113.90645323,22.74904443,113.90646128,22.74905308,113.90647068,22.74906173,113.90647873,22.74907039,113.90648678,22.74907904,113.90649617,22.74908769,113.90650556,22.74909635,113.90651361,22.74910376,113.90652300,22.74911242,113.90653239,22.74911983,113.90654178,22.74912849,113.90655252,22.74913590,113.90656191,22.74914332,113.90657264,22.74914950,113.90658204,22.74915692,113.90659277,22.74916433,113.90660350,22.74917051,113.90661424,22.74917793,113.90662497,22.74918411,113.90663570,22.74919029,113.90664644,22.74919647,113.90665851,22.74920141,113.90666925,22.74920759,113.90668132,22.74921253,113.90669205,22.74921871,113.90670413,22.74922366,113.90671621,22.74922860,113.90672828,22.74923231,113.90674036,22.74923725,113.90675243,22.74924096,113.90676451,22.74924590,113.90677658,22.74924961,113.90678866,22.74925331,113.90680073,22.74925578,113.90681415,22.74925949,113.90682623,22.74926196,113.90683964,22.74926443,113.90685172,22.74926690,113.90686514,22.74926937,113.90687721,22.74927184,113.90689063,22.74927307,113.90690270,22.74927554,113.90691612,22.74927678,113.90692954,22.74927677,113.90694161,22.74927801,113.90695503,22.74927924,113.90696845,22.74927924,113.90698187,22.74927924,113.90699394,22.74927923,113.90700736,22.74927923,113.90702078,22.74927799,113.90703285,22.74927675,113.90704627,22.74927675,113.90705969,22.74927551,113.90707176,22.74927303,113.90708518,22.74927179,113.90709725,22.74926932,113.90711067,22.74926684,113.90712275,22.74926437,113.90713617,22.74926189,113.90714824,22.74925941,113.90716166,22.74925570,113.90717373,22.74925323,113.90718581,22.74924951,113.90719789,22.74924580,113.90720996,22.74924085,113.90722204,22.74923714,113.90723411,22.74923219,113.90724619,22.74922848,113.90725826,22.74922353,113.90727034,22.74921858,113.90728107,22.74921240,113.90729315,22.74920745,113.90730388,22.74920127,113.90731596,22.74919632,113.90732669,22.74919013,113.90733743,22.74918395,113.90734816,22.74917776,113.90735889,22.74917034,113.90736963,22.74916416,113.90738036,22.74915674,113.90738976,22.74914931,113.90740049,22.74914313,113.90740988,22.74913571,113.90742062,22.74912829,113.90743001,22.74911963,113.90743940,22.74911221,113.90744879,22.74910355,113.90745684,22.74909613,113.90746624,22.74908747,113.90747563,22.74907882,113.90748368,22.74907016,113.90749173,22.74906150,113.90750112,22.74905285,113.90750917,22.74904419,113.90751722,22.74903553,113.90752393,22.74902564,113.90753198,22.74901698,113.90753869,22.74900709,113.90754674,22.74899843,113.90755345,22.74898854,113.90756016,22.74897865,113.90756687,22.74896875,113.90757358,22.74895886,113.90758029,22.74894897,113.90758566,22.74893907,113.90759237,22.74892918,113.90759773,22.74891929,113.90760310,22.74890816,113.90760847,22.74889827,113.90761383,22.74888837,113.90761920,22.74887724,113.90762323,22.74886735,113.90762860,22.74885622,113.90763262,22.74884633,113.90763665,22.74883520,113.90764067,22.74882407,113.90764470,22.74881294,113.90764872,22.74880305,113.90765141,22.74879192,113.90765543,22.74878079,113.90765812,22.74876967,113.90766080,22.74875854,113.90766349,22.74874741,113.90766617,22.74873628,113.90766885,22.74872515,113.90767154,22.74871402,113.90767288,22.74870289,113.90767422,22.74869177,113.90767691,22.74868064,113.90767825,22.74866951,113.90767825,22.74865838,113.90767959,22.74864725,113.90768094,22.74863613,113.90768094,22.74862500,113.90768228,22.74861387,113.90768228,22.74860274,113.90768228,22.74859038,113.90768228,22.74857925,113.90768228,22.74856812,113.90768094,22.74855699,113.90768094,22.74854587,113.90767960,22.74853474,113.90767826,22.74852361,113.90767826,22.74851248,113.90767692,22.74850136,113.90767424,22.74849023,113.90767290,22.74847910,113.90767155,22.74846797,113.90766887,22.74845685,113.90766619,22.74844572,113.90766351,22.74843459,113.90766082,22.74842346,113.90765814,22.74841234,113.90765546,22.74840121,113.90765143,22.74839008,113.90764875,22.74837896,113.90764473,22.74836907,113.90764070,22.74835794,113.90763668,22.74834681,113.90763265,22.74833568,113.90762863,22.74832579,113.90762326,22.74831467,113.90761924,22.74830478,113.90761387,22.74829365,113.90760851,22.74828376,113.90760314,22.74827387,113.90759777,22.74826274,113.90759241,22.74825285,113.90758570,22.74824296,113.90758033,22.74823307,113.90757363,22.74822318,113.90756692,22.74821329,113.90756021,22.74820341,113.90755350,22.74819352,113.90754680,22.74818363,113.90753875,22.74817497,113.90753204,22.74816508,113.90752399,22.74815643,113.90751728,22.74814654,113.90750923,22.74813789,113.90750118,22.74812923,113.90749179,22.74812058,113.90748374,22.74811193,113.90747569,22.74810328,113.90746630,22.74809462,113.90745691,22.74808597,113.90744886,22.74807855,113.90743947,22.74806990,113.90743008,22.74806248,113.90742068,22.74805383,113.90740995,22.74804642,113.90740056,22.74803900,113.90738983,22.74803282,113.90738043,22.74802540,113.90736970,22.74801799,113.90735897,22.74801181,113.90734824,22.74800439,113.90733750,22.74799821,113.90732677,22.74799203,113.90731604,22.74798585,113.90730396,22.74798091,113.90729323,22.74797473,113.90728115,22.74796979,113.90727042,22.74796361,113.90725834,22.74795867,113.90724627,22.74795372,113.90723419,22.74795002,113.90722212,22.74794507,113.90721004,22.74794137,113.90719797,22.74793642,113.90718589,22.74793272,113.90717382,22.74792901,113.90716174,22.74792654,113.90714833,22.74792283,113.90713625,22.74792036,113.90712283,22.74791789,113.90711076,22.74791542,113.90709734,22.74791295,113.90708527,22.74791048,113.90707185,22.74790925,113.90705977,22.74790678,113.90704636,22.74790555,113.90703294,22.74790555,113.90702086,22.74790432,113.90700745,22.74790308,113.90699403,22.74790309,113.90698195,22.74790309,113.90696854,22.74790309,113.90695512,22.74790310,113.90694170,22.74790434,113.90692963,22.74790557,113.90691621,22.74790558,113.90690279,22.74790682,113.90689072,22.74790929,113.90687730,22.74791053,113.90686522,22.74791301,113.90685180,22.74791548,113.90683973,22.74791796,113.90682631,22.74792043,113.90681424,22.74792291,113.90680082,22.74792662,113.90678874,22.74792910,113.90677667,22.74793281,113.90676459,22.74793652,113.90675251,22.74794147,113.90674044,22.74794518,113.90672836,22.74795013,113.90671629,22.74795384,113.90670421,22.74795879,113.90669213,22.74796374,113.90668140,22.74796992,113.90666932,22.74797487,113.90665859,22.74798106,113.90664651,22.74798600,113.90663578,22.74799219,113.90662505,22.74799837,113.90661431,22.74800456,113.90660358,22.74801198,113.90659284,22.74801816,113.90658211,22.74802558,113.90657271,22.74803300,113.90656198,22.74803919,113.90655259,22.74804661,113.90654185,22.74805403,113.90653246,22.74806269,113.90652307,22.74807011,113.90651367,22.74807876,113.90650562,22.74808618,113.90649623,22.74809484,113.90648684,22.74810350,113.90647879,22.74811216,113.90647074,22.74812081,113.90646134,22.74812947,113.90645329,22.74813813,113.90644524,22.74814678,113.90643853,22.74815668,113.90643048,22.74816533,113.90642377,22.74817522,113.90641572,22.74818388,113.90640901,22.74819377,113.90640230,22.74820367,113.90639559,22.74821356,113.90638888,22.74822345,113.90638217,22.74823335,113.90637680,22.74824324,113.90637009,22.74825313,113.90636473,22.74826302,113.90635936,22.74827415,113.90635399,22.74828404,113.90634862,22.74829394,113.90634326,22.74830507,113.90633923,22.74831496,113.90633386,22.74832609,113.90632984,22.74833598,113.90632581,22.74834711,113.90632178,22.74835824,113.90631776,22.74836937,113.90631373,22.74837926,113.90631105,22.74839039,113.90630702,22.74840151,113.90630434,22.74841264,113.90630165,22.74842377,113.90629897,22.74843490,113.90629629,22.74844603,113.90629360,22.74845716,113.90629092,22.74846829,113.90628957,22.74847941,113.90628823,22.74849054,113.90628555,22.74850167,113.90628421,22.74851280,113.90628420,22.74852393,113.90628286,22.74853505,113.90628152,22.74854618,113.90628152,22.74855731,113.90628018,22.74856844,113.90628018,22.74857957,113.90628017,22.74859069"
    };
    private String BJ[]=new String[]{
            "113.91286413,22.76387936,113.91286278,22.76332713,113.91144258,22.76318607,113.91147009,22.76255608,113.91038948,22.76227469000001,113.90980873,22.76256376,113.90965027,22.76298199,113.9092657,22.76226578,113.90889456,22.76200304,113.90837066,22.76155295,113.90784778,22.76145735,113.90485827,22.76065728,113.90526869,22.76000492,113.90325513,22.7595249,113.90344515,22.75872768,113.90176223,22.75815899,113.90073154,22.7579276,113.90038382,22.75899971,113.90013488,22.75885049,113.9002161,22.75856601,113.89993908,22.75848677,113.8997864,22.75862914,113.89966951,22.7588674,113.89957798,22.75882431,113.89950373,22.75897584,113.89922569,22.75888702,113.89930703,22.75873191000001,113.89908036,22.75853945,113.89949423,22.75776788,113.89924939,22.75767339,113.89874009,22.75812584,113.8985736,22.75777278,113.89790806,22.75733997,113.89801877,22.75717431,113.89776675,22.75705035,113.89780698,22.75697897,113.89742955,22.75673519,113.89688856,22.75623217,113.89593615,22.75542773,113.89426986,22.75420887,113.89312547,22.75298059,113.89307498,22.75301061,113.89296648,22.75289007,113.89251503,22.75293882,113.89178984,22.75206799,113.89210808,22.75179393,113.89179561,22.75135475,113.89185684,22.75123549,113.89116058,22.75085624,113.89061786,22.75025656,113.89069498,22.74950882,113.89158856,22.74905099,113.89245256,22.74883178,113.89304344,22.74863281,113.89365491,22.74830485,113.89335808,22.74783288,113.89308,22.74756275,113.89317518,22.7468717,113.89281496,22.74659036,113.89217933,22.74732231,113.89150528,22.74704347,113.89049297,22.74655128,113.8898462,22.74546502000001,113.88921068,22.7446069,113.88890651,22.74398051,113.88964446,22.74360801,113.889435,22.74298027,113.88926516,22.7426751,113.8882166,22.74244332,113.88759669,22.74119547,113.88714822,22.74066123,113.88707948,22.73999058,113.88638609,22.73919011,113.8856114,22.73919176,113.88555705,22.73869128000001,113.88787458,22.7387315,113.88980296,22.7387656,113.89436709,22.73942997,113.89480453,22.73905267,113.89562022,22.73890881,113.89594884,22.73851116,113.89663977,22.73812835,113.89711375,22.7379234,113.89744546,22.73768204,113.89792263,22.73783925,113.89838583,22.73867639,113.89859239,22.73905417,113.89916503,22.73898467,113.89950285,22.73891688,113.89969866,22.73903872,113.89980372,22.73937287,113.90093191,22.73938492,113.90110992,22.73937623,113.90130299,22.73937547,113.90156821,22.73925291,113.90190434,22.73915929,113.90235292,22.73898411,113.90265275,22.73890487,113.90303152,22.73873779,113.90501863,22.73831344,113.90493093,22.73808465,113.90457036,22.73777895,113.90456029,22.73732821,113.90476266,22.73713045,113.90584754,22.7362955,113.90655166,22.7369453,113.90711524,22.73731794,113.90689359,22.73777146,113.90732586,22.73840091,113.90827756,22.73808468,113.9103998,22.73706912,113.90976889,22.73583432,113.91063919,22.73547312,113.91148975,22.73534427,113.91178291,22.73558476,113.91249103,22.73481512,113.91325432,22.73382871,113.91351173,22.73307454,113.91372165,22.73168555,113.91444273,22.73110357,113.91479321,22.73041198,113.91489449,22.73017976,113.91582673,22.73025805,113.91631548,22.73098166,113.91744177,22.73155494,113.91780647,22.7318326,113.91838984,22.73174275,113.91902845,22.7312506,113.91941265,22.73143369,113.91993316,22.73090578,113.9204776,22.73096357,113.92240888,22.72775128,113.92320654,22.72771498,113.92575763,22.72806575,113.92708166,22.72810946,113.92748082,22.72329297,113.92859899,22.72243014,113.92944606,22.72092224,113.93015923,22.71953598,113.93181502,22.71812091,113.93244097,22.71743344,113.93326618,22.71593592,113.9334153,22.7154298,113.93380421,22.71434684,113.9340574,22.71068052,113.93512816,22.71051947,113.93854783,22.71323675,113.94162677,22.71304038,113.94389658,22.71561811000001,113.94595435,22.71679733,113.94827922,22.721073,113.9518749,22.72008323,113.9532995,22.72044267,113.95366691,22.71929226,113.9548427,22.71727183,113.95617779,22.71702582,113.95781688,22.71777881,113.95873475,22.71672792,113.96045736,22.71584059,113.96166885,22.71533567,113.96649657,22.71426444,113.96836611,22.71403212,113.97149512,22.7152604,113.97447211,22.71698851,113.97647231,22.71907118,113.97393104,22.72401032000001,113.97047366,22.72801137,113.96952117,22.728364,113.96926642,22.72907306,113.96912395,22.73000195,113.96907069,22.73065272,113.96885815,22.73150923,113.96854008,22.73358768,113.96839121,22.73464672,113.96593863,22.73427019,113.96391565,22.73393185,113.9616032,22.73410046,113.96021841,22.73437027,113.95852106,22.73512123,113.95635227,22.73634165,113.95415181,22.7374566,113.95129332,22.73900141,113.94914349,22.74000556,113.94760511,22.74062925,113.94554695,22.74112495,113.94338018,22.74143459,113.94184669,22.74180801,113.93964821,22.74242777,113.93816926,22.7430578,113.93660929,22.74378965,113.93504486,22.74489598,113.93321615,22.74653404,113.93110905,22.74881782,113.92949711,22.75063766,113.92767871,22.75287689,113.92644592,22.75407347,113.92495071,22.75460796000001,113.92297161,22.75468889,113.92183023,22.75515858,113.92061789,22.75652436,113.91984508,22.7576256,113.91831927,22.75951265,113.91745392,22.76045917,113.91569349,22.76221059,113.91428037,22.76422167,113.91286413,22.76387936"
           //"113.91286413,22.76387936,113.91286278,22.76332713,113.91144258,22.76318607,113.91147009,22.76255608,113.91038948,22.76227469,113.90980873,22.76256376,113.90965027,22.76298199,113.90926570,22.76226578,113.90889456,22.76200304,113.90837066,22.76155295,113.90784778,22.76145735,113.90485827,22.76065728,113.90526869,22.76000492,113.90325513,22.75952490,113.90344515,22.75872768,113.90176223,22.75815899,113.90073154,22.75792760,113.90038382,22.75899971,113.90013488,22.75885049,113.90021610,22.75856601,113.89993908,22.75848677,113.89978640,22.75862914,113.89966951,22.75886740,113.89957798,22.75882431,113.89950373,22.75897584,113.89922569,22.75888702,113.89930703,22.75873191,113.89908036,22.75853945,113.89949423,22.75776788,113.89924939,22.75767339,113.89874009,22.75812584,113.89857360,22.75777278,113.89790806,22.75733997,113.89801877,22.75717431,113.89776675,22.75705035,113.89780698,22.75697897,113.89742955,22.75673519,113.89688856,22.75623217,113.89593615,22.75542773,113.89426986,22.75420887,113.89312547,22.75298059,113.89307498,22.75301061,113.89296648,22.75289007,113.89251503,22.75293882,113.89178984,22.75206799,113.89210808,22.75179393,113.89179561,22.75135475,113.89185684,22.75123549,113.89116058,22.75085624,113.89061786,22.75025656,113.89069498,22.74950882,113.89158856,22.74905099,113.89245256,22.74883178,113.89304344,22.74863281,113.89365491,22.74830485,113.89335808,22.74783288,113.89308000,22.74756275,113.89317518,22.74687170,113.89281496,22.74659036,113.89217933,22.74732231,113.89150528,22.74704347,113.89049297,22.74655128,113.88984620,22.74546502,113.88921068,22.74460690,113.88890651,22.74398051,113.88964446,22.74360801,113.88943500,22.74298027,113.88926516,22.74267510,113.88821660,22.74244332,113.88759669,22.74119547,113.88714822,22.74066123,113.88707948,22.73999058,113.88638609,22.73919011,113.88561140,22.73919176,113.88555705,22.73869128,113.88787458,22.73873150,113.88980296,22.73876560,113.89436709,22.73942997,113.89480453,22.73905267,113.89562022,22.73890881,113.89594884,22.73851116,113.89663977,22.73812835,113.89711375,22.73792340,113.89744546,22.73768204,113.89792263,22.73783925,113.89838583,22.73867639,113.89859239,22.73905417,113.89916503,22.73898467,113.89950285,22.73891688,113.89969866,22.73903872,113.89980372,22.73937287,113.90093191,22.73938492,113.90110992,22.73937623,113.90130299,22.73937547,113.90156821,22.73925291,113.90190434,22.73915929,113.90235292,22.73898411,113.90265275,22.73890487,113.90303152,22.73873779,113.90501863,22.73831344,113.90493093,22.73808465,113.90457036,22.73777895,113.90456029,22.73732821,113.90476266,22.73713045,113.90584754,22.73629550,113.90655166,22.73694530,113.90711524,22.73731794,113.90689359,22.73777146,113.90732586,22.73840091,113.90827756,22.73808468,113.91039980,22.73706912,113.90976889,22.73583432,113.91063919,22.73547312,113.91148975,22.73534427,113.91178291,22.73558476,113.91249103,22.73481512,113.91325432,22.73382871,113.91351173,22.73307454,113.91372165,22.73168555,113.91444273,22.73110357,113.91479321,22.73041198,113.91489449,22.73017976,113.91582673,22.73025805,113.91631548,22.73098166,113.91744177,22.73155494,113.91780647,22.73183260,113.91838984,22.73174275,113.91902845,22.73125060,113.91941265,22.73143369,113.91993316,22.73090578,113.92047760,22.73096357,113.92240888,22.72775128,113.92320654,22.72771498,113.92575763,22.72806575,113.92708166,22.72810946,113.92748082,22.72329297,113.92859899,22.72243014,113.92944606,22.72092224,113.93015923,22.71953598,113.93181502,22.71812091,113.93244097,22.71743344,113.93326618,22.71593592,113.93341530,22.71542980,113.93380421,22.71434684,113.93405740,22.71068052,113.93512816,22.71051947,113.93854783,22.71323675,113.94162677,22.71304038,113.94389658,22.71561811,113.94595435,22.71679733,113.94827922,22.72107300,113.95187490,22.72008323,113.95329950,22.72044267,113.95366691,22.71929226,113.95484270,22.71727183,113.95617779,22.71702582,113.95781688,22.71777881,113.95873475,22.71672792,113.96045736,22.71584059,113.96166885,22.71533567,113.96649657,22.71426444,113.96836611,22.71403212,113.97149512,22.71526040,113.97447211,22.71698851,113.97647231,22.71907118,113.97393104,22.72401032,113.97047366,22.72801137,113.96952117,22.72836400,113.96926642,22.72907306,113.96912395,22.73000195,113.96907069,22.73065272,113.96885815,22.73150923,113.96854008,22.73358768,113.96839121,22.73464672,113.96593863,22.73427019,113.96391565,22.73393185,113.96160320,22.73410046,113.96021841,22.73437027,113.95852106,22.73512123,113.95635227,22.73634165,113.95415181,22.73745660,113.95129332,22.73900141,113.94914349,22.74000556,113.94760511,22.74062925,113.94554695,22.74112495,113.94338018,22.74143459,113.94184669,22.74180801,113.93964821,22.74242777,113.93816926,22.74305780,113.93660929,22.74378965,113.93504486,22.74489598,113.93321615,22.74653404,113.93110905,22.74881782,113.92949711,22.75063766,113.92767871,22.75287689,113.92644592,22.75407347,113.92495071,22.75460796,113.92297161,22.75468889,113.92183023,22.75515858,113.92061789,22.75652436,113.91984508,22.75762560,113.91831927,22.75951265,113.91745392,22.76045917,113.91569349,22.76221059,113.91428037,22.76422167,113.91286413,22.76387936"
};

    int x= 0 ;
    public void testSocket(){
       x = x+1;
       if(x>50){
           return;
       }
        handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    WebSocketService.closeWebsocket(false);
                    stopService(websocketServiceIntent);
                    startService(websocketServiceIntent);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            testSocket();
                        }
                    }, 2000);
                    initData();
                }
            },3000);
        }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i("zxy", "onClick: view==");

        }
    };
    private int currentPage;//当前页
    private int totalPage = 1;//总页
    private  final int INDEX = 1;//首页
    private int currentWfs;//当前区
    private boolean isLoadMore = false;
    private List<WeiFang> datap = new ArrayList<>();
    SwipeRefreshLayout.OnRefreshListener onRefreshListener =  new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            currentPage = INDEX;
            isLoadMore = false;
            //刷新
            getPresenter().requesWFData(LocationdrawActivity.this,INDEX, currentWfs);
        }
    };

    NestedScrollView.OnScrollChangeListener onScrollChangeListener =  new NestedScrollView.OnScrollChangeListener(){
        @Override
        public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                if(currentPage<totalPage) {
                    loadMore();
                }else{
                    Toast.makeText(LocationdrawActivity.this,getResources().getString(R.string.full_text),Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    OnRecyclerViewItemOnClickListener listener  = new OnRecyclerViewItemOnClickListener() {
        @Override
        public void onClick(View view, int position) {
            Log.i(TAG, "onClick: datap.get(position)=="+datap.get(position).getId());
            popuWf.dismiss();
            showPopupWindow(datap.get(position),2);
        }
    };

    private void loadMore(){
        boolean isNetworkAvailable = NetworkUtil.isNetworkAvailable(LocationdrawActivity.this);
        if (isNetworkAvailable){
            currentPage+=1;
            isLoadMore = true;
            getPresenter().requesWFData(LocationdrawActivity.this,currentPage, currentWfs);
        }else {
            Toast.makeText(LocationdrawActivity.this,R.string.network_error,Toast.LENGTH_LONG).show();
        }

    }
}
