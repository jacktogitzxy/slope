package slope.zxy.com.weather_moudle;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.util.Util;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import slope.zxy.com.weather_moudle.adapter.HourDataListAdapter;
import slope.zxy.com.weather_moudle.bean.FutureWeatherBean;
import slope.zxy.com.weather_moudle.bean.WeatherBean;
import slope.zxy.com.weather_moudle.utils.HttpUtil;
import slope.zxy.com.weather_moudle.utils.SpUtils;
import slope.zxy.com.weather_moudle.utils.SystemUtils;
import slope.zxy.com.weather_moudle.utils.Utility;
@Route(path = "/weather/index")
public class WeatherActivity extends SlidingActivity {
    /**
     * 主控件初始化
     **/
    //下拉刷新控件
    SwipeRefreshLayout mSwipeRefresh;
    //侧滑控件
    DrawerLayout mDrawerLayout;
    //当前天气的RelativeLayout
    public RelativeLayout mNowWeatherRelativeLayout;
    //整体布局的ScrollView
    public ScrollView mScrollView;
    //半小时更新的RecyclerView
    public RecyclerView mRecyclerView;
    //未来天气的Linearlayout
    public LinearLayout mForecastLayout;
    //背景的图片
    public LinearLayout mLinearLayoutLeftMenu;

    /**
     * NowWeather 控件初始化
     **/
    //最后一次更新时间
    TextView mLastUpateText;
    //空气质量
    TextView mNowWeatherAirQuality;
    //空气指数
    TextView mNowWeatherAirIndex;
    //当天最高气温
    TextView mNowWeatherHightTempture;
    //当天最低气温
    TextView mNowWeatherLowTempture;
    //当前气温
    TextView mNowWeatherTempeture;
    //当天天气状态
    TextView mNowWeatherCondition;
    //当前天气图标
    ImageView mNowWeather;

    /**右侧菜单控件初始化**/


    /**
     * 空气质量控件初始化
     **/
  /*  @BindView(R.id.air_linearlayout)
  public   LinearLayout mAirLinearLayout;*/
    TextView mAirWeatherCondition;
    TextView mAirPm25Index;
    TextView mAirCoIndex;
    TextView mAirPm10Index;
    TextView mAirSo2Index;
    TextView mAirO3Index;
    TextView mAirNo2Index;
    TextView mAqiPrimaryPollutiant;
    /**
     * 生活指数控件初始化
     **/
    TextView mIndexClothBrief;
    TextView mIndexClothTxt;
    TextView mIndexFluBrief;
    TextView mIndexFluTxt;
    TextView mIndexSportBrief;
    TextView mIndexSportTxt;
    TextView mIndexTravelBrief;
    TextView mIndexTravelTxt;
    //今日天气状态
    TextView mRightTodayWeatherText;
    //气压
    TextView mRightAirPressText;
    //降水概率
    TextView mRightRainText;
    TextView mRightUv;
    //天气图标
    ImageView mRightTodayWeatherImg;
    //白天天气
    TextView mRightDayWeatherText;
    //夜晚天气
    TextView mRightNightWeatherText;
    //白天气温
    TextView mRightDayTempText;
    //夜晚气温
    TextView mRightNightTempText;
    //日出时间
    TextView mRightSunBeginText;
    //日落时间
    TextView mRightSunEnd;
    //白天风力
    TextView mRightDayWind;
    //夜晚风力
    TextView mRightNightWind;
    //白天风向
    TextView mRightDayWindDiretion;
    //夜晚风向
    TextView mRightNightWindDiretion;

    TextView mRightCityNameText;
    TextView mRightAreaCodeText;
    TextView mRightAreaNumText;
    TextView mRightAltitudeText;
    TextView mRightLatitudeText;
    TextView mRightLongtitudeText;

    private int mNowWeatherHeight = -1;
    private int DisplayHeight;
    private int DisplayWideth;
    private TextView main_title_weather;
    private Context mContext = WeatherActivity.this;
    private static final int REQUEST_CODE_PICK_CITY = 0;
    private static final int REQUEST_CODE_EDIT_CITY = 1;
    public SQLiteDatabase db;
    private String dataCity ;
    private TextView title_add_city;
    private ImageView mBackImageView,weather_gif;
    private Handler mHandler = new Handler() {
        @Override
        //当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int type = msg.what;
           if(type==2) {
               requestWeather(dataCity);//请求天气
               mSwipeRefresh.setRefreshing(false);//请求后下拉刷新停止
           }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        dataCity = intent.getStringExtra("city");
        setContentView(R.layout.activity_weather);
        initView();
        if(dataCity==null){
            dataCity="深圳";
        }
        requestWeather(dataCity);
    }

    private void initView() {
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNowWeatherRelativeLayout  = (RelativeLayout) findViewById(R.id.main_now_weather);
        mScrollView  = (ScrollView) findViewById(R.id.weather_scrollview_layout);
        mRecyclerView  = (RecyclerView) findViewById(R.id.hourdata_recyclerview);
        mForecastLayout  = (LinearLayout) findViewById(R.id.forecast_layout);
        mLastUpateText  = (TextView) findViewById(R.id.last_upate_text);
        mNowWeatherAirQuality  = (TextView) findViewById(R.id.now_weather_air_quality);
        weather_gif = (ImageView) findViewById(R.id.weather_gif);
        Glide.with(this).load(R.drawable.sun).into(weather_gif);
        mNowWeatherAirIndex = (TextView) findViewById(R.id.now_weather_air_index);
        mNowWeatherHightTempture = (TextView) findViewById(R.id.now_weather_hight_tempture);
        mNowWeatherLowTempture  = (TextView) findViewById(R.id.now_weather_low_tempture);
        mNowWeatherTempeture  = (TextView) findViewById(R.id.now_weather_tempeture);
        mNowWeatherCondition  = (TextView) findViewById(R.id.now_weather_condition_tv);
        mNowWeather  = (ImageView) findViewById(R.id.now_weather_img);
        mAirWeatherCondition  = (TextView) findViewById(R.id.air_weather_condition);
        mAirPm25Index  = (TextView) findViewById(R.id.air_pm2_5_index);
        mBackImageView = (ImageView) findViewById(R.id.back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WeatherActivity.this.finish();
            }
        });
        mAirCoIndex = (TextView) findViewById(R.id.air_co_index);
        mAirPm10Index = (TextView) findViewById(R.id.air_pm10_index);
        mAirSo2Index  = (TextView) findViewById(R.id.air_so2_index);
        mAirO3Index  = (TextView) findViewById(R.id.air_o3_index);
        mAirNo2Index  = (TextView) findViewById(R.id.air_no2_index);
        mAqiPrimaryPollutiant  = (TextView) findViewById(R.id.aqi_primary_pollutant);


        mIndexClothBrief = (TextView) findViewById(R.id.index_cloth_brief);
        mIndexClothTxt = (TextView) findViewById(R.id.index_cloth_txt);
        mIndexFluBrief  = (TextView) findViewById(R.id.index_flu_brief);
        mIndexFluTxt  = (TextView) findViewById(R.id.index_flu_txt);
        mIndexSportBrief  = (TextView) findViewById(R.id.index_sport_brief);
        mIndexSportTxt  = (TextView) findViewById(R.id.index_sport_txt);
        mIndexTravelBrief  = (TextView) findViewById(R.id.index_travel_brief);
        mIndexTravelTxt  = (TextView) findViewById(R.id.index_travel_txt);
        main_title_weather = (TextView) findViewById(R.id.main_title_weather);
        main_title_weather.setText(getResources().getString(R.string.weatherCity));
        title_add_city = (TextView) findViewById(R.id.title_add_city);
        title_add_city.setVisibility(View.VISIBLE);
        title_add_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeatherActivity.this, CitySelectActivity.class);
                intent.putExtra("city",dataCity);
                startActivityForResult(intent, REQUEST_CODE_PICK_CITY);
            }
        });
        SlidingMenu mRightMenu = getSlidingMenu();
        //右侧菜单布局
        setBehindContentView(R.layout.main_right_menu);
        mRightMenu.setMode(SlidingMenu.RIGHT);
        //滑出距离
        mRightMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        mRightMenu.setFadeDegree(0.35f);
        //右侧控件初始化
        mRightTodayWeatherImg = (ImageView) findViewById(R.id.right_today_weather_img);
        mRightTodayWeatherText = (TextView) findViewById(R.id.right_today_weather);
        Log.i("zxy", "initView: mRightTodayWeatherText==="+mRightTodayWeatherText);
        mRightAirPressText = (TextView) findViewById(R.id.right_air_press);
        mRightRainText = (TextView) findViewById(R.id.right_rain);
        mRightDayWeatherText = (TextView) findViewById(R.id.right_day_weather);
        mRightNightWeatherText = (TextView) findViewById(R.id.right_night_weather);
        mRightSunBeginText = (TextView) findViewById(R.id.right_sunbegin);
        mRightSunEnd = (TextView) findViewById(R.id.right_sunend);
        mRightNightWind = (TextView) findViewById(R.id.right_night_wind);
        mRightDayWind = (TextView) findViewById(R.id.right_day_wind);
        mRightDayWindDiretion = (TextView) findViewById(R.id.right_day_wind_diretion);
        mRightNightWindDiretion = (TextView) findViewById(R.id.right_night_wind_diretion);
        mRightDayTempText = (TextView) findViewById(R.id.right_day_temp);
        mRightNightTempText = (TextView) findViewById(R.id.right_night_temp);
        mRightNightWindDiretion = (TextView) findViewById(R.id.right_night_wind_diretion);
        mRightUv = (TextView) findViewById(R.id.right_uv);

        mRightCityNameText = (TextView) findViewById(R.id.right_menu_cityname);
        mRightAreaCodeText = (TextView) findViewById(R.id.right_menu_areacode);
        mRightAltitudeText = (TextView) findViewById(R.id.right_menu_altitude);
        mRightLongtitudeText = (TextView) findViewById(R.id.right_menu_longtitude);
        mRightAreaNumText = (TextView) findViewById(R.id.right_menu_postcode);
        mRightLatitudeText = (TextView) findViewById(R.id.right_menu_latitude);
        //滑动至最上
        mScrollView.smoothScrollTo(0, 0);
        //下拉刷新控件
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Utility.isNetworkConnected(getApplicationContext())){
                    Message message = Message.obtain();
                    message.what =2;
                    requestWeather(dataCity);
                    //延时2秒
                    mHandler.sendMessageDelayed(message,2000);
                }else{
                    Toast.makeText(getApplicationContext(),"请求失败,请检查网络状况",Toast.LENGTH_SHORT).show();
                    if(mSwipeRefresh.isRefreshing()){
                        mSwipeRefresh.setRefreshing(false);
                    }
                }
            }
        });
        //下拉刷新颜色设置
        mSwipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        //控件拉动是放大放小，起始位置，结束位置
        mSwipeRefresh.setProgressViewOffset(false, 150, 300);
        // mNowWeatherHeight高度=屏幕高度-标题栏高度-状态栏高度
        mNowWeatherHeight = SystemUtils.getDisplayHeight(mContext) - SystemUtils.getActionBarSize(mContext) - SystemUtils.getStatusBarHeight(mContext);
        DisplayHeight = SystemUtils.getDisplayHeight(mContext);
        DisplayWideth = SystemUtils.getDisplayWidth(mContext);
        //设置当前天气信息RelativeLayout的高度
        mNowWeatherRelativeLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, mNowWeatherHeight));
    }
    public void requestWeather(final String cityName) {
        String u = "http://120.79.174.224:8081/fx/getWeather?cityName="+cityName;
        HttpUtil.sendOkHttpRequest(u, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("zxy", "onFailure: ");
                runOnUiThread(new Runnable() {
                       @Override
                        public void run() {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                            setLocalData();
                            mSwipeRefresh.setRefreshing(false);
                        }
                    });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("zxy", "onResponse: ");
                final WeatherBean  weather = Utility.handleWeatherResponse(response.body().string());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (weather != null && "0".equals(weather.mShowapi_Res_Code)) {
                                showWeatherInfo(weather);
                            } else {
                                Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                            }
                            mSwipeRefresh.setRefreshing(false);
                        }
                    });
            }
        });

    }

    private void showWeatherInfo(WeatherBean weather) {
        main_title_weather.setText(weather.getmCityInfoBean().getmCityName_C5());
        //NowWeather数据显示
        mLastUpateText.setText(weather.getmNowWeatherBean().getmTemperature_Time() + "更新");
        mNowWeatherAirQuality.setText("空气" + weather.getmAqiDetailBean().getmQuality());
        mNowWeatherAirIndex.setText("指数" + weather.getmAqiDetailBean().getmAqi());
        mNowWeatherHightTempture.setText(weather.getmTodayWeatherBean().getmDay_Air_Temperature() + "°");
        mNowWeatherLowTempture.setText(weather.getmTodayWeatherBean().getmNight_Air_Temperature() + "°");
        mNowWeatherTempeture.setText(weather.getmNowWeatherBean().getmTemperature());
        String w = weather.getmNowWeatherBean().getmWeather();
        mNowWeatherCondition.setText(w);
        Glide.with(this).load(weather.getmNowWeatherBean().getmWeather_Pic()).diskCacheStrategy(DiskCacheStrategy.ALL).into(mNowWeather);
        if(w!=null){
            if(w.contains("雨")){
                Glide.with(this).load(R.drawable.xiaoyu).into(weather_gif);
            }
            if(w.contains("雪")){
                Glide.with(this).load(R.drawable.xue).into(weather_gif);
            }
            if(w.contains("风")){
                Glide.with(this).load(R.drawable.feng).into(weather_gif);
            }
            if(w.contains("雷")){
                Glide.with(this).load(R.drawable.leiyu).into(weather_gif);
            }else{
                Glide.with(this).load(R.drawable.sun).into(weather_gif);
            }

        }
        //hourlist数据显示
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        mRecyclerView.setAdapter(new HourDataListAdapter(mContext, weather.getmHourDataList()));
        mRecyclerView.scrollToPosition(weather.getmHourDataList().size() - 1);
        //天气view动态添加
        mForecastLayout.removeAllViews();
        for (FutureWeatherBean futureWeatherBean : weather.getmFutureWeatherBeen()) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, mForecastLayout, false);
            view.setLayoutParams(new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayHeight / 12));
            TextView dataText = (TextView) view.findViewById(R.id.forecast_week_tv);
            ImageView weatherImg = (ImageView) view.findViewById(R.id.forecast_icon);
            TextView lowTempText = (TextView) view.findViewById(R.id.forecast_low_temp_tv);
            TextView hightTempText = (TextView) view.findViewById(R.id.forecast_high_temp_tv);
            dataText.setText(Utility.weakDayInfliter(futureWeatherBean.getmWeekDay()));
            Glide.with(this).load(futureWeatherBean.getmDay_Weather_Pic()).into(weatherImg);
            lowTempText.setText(futureWeatherBean.getmDay_Air_Temperature() + "°");
            hightTempText.setText(futureWeatherBean.getmNight_Air_Temperature() + "°");
            mForecastLayout.addView(view);
        }
        mForecastLayout.setLayoutParams((new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DisplayHeight / 2)));
        //空气质量UI更新
        //  mAirLinearLayout.setLayoutParams((new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,DisplayHeight/2)));
        mAirWeatherCondition.setText(weather.getmAqiDetailBean().mQuality);
        mAirPm25Index.setText(weather.getmAqiDetailBean().getmPm2_5());
        mAirCoIndex.setText(weather.getmAqiDetailBean().getmCo());
        mAirNo2Index.setText(weather.getmAqiDetailBean().getmNo2());
        mAirO3Index.setText(weather.getmAqiDetailBean().getmO3());
        mAirPm10Index.setText(weather.getmAqiDetailBean().getmPm10());
        if(weather.getmAqiDetailBean().getmPrimary_Pollutant()!=null&&!"".equals(weather.getmAqiDetailBean().getmPrimary_Pollutant())){
            mAqiPrimaryPollutiant.setVisibility(View.VISIBLE);
            mAqiPrimaryPollutiant.setText("主要污染物：    " + weather.getmAqiDetailBean().getmPrimary_Pollutant());
        }else{
            mAqiPrimaryPollutiant.setVisibility(View.GONE);
        }
        mAirSo2Index.setText(weather.getmAqiDetailBean().getmSo2());
        //生活指数UI更新
        mIndexClothBrief.setText("穿衣指数：" + weather.getIndexBean().getmClothesTitle());
        mIndexClothTxt.setText(weather.getIndexBean().getmClothesDesc());
        mIndexFluBrief.setText("感冒指数：" + weather.getIndexBean().getmColdTitle());
        mIndexFluTxt.setText(weather.getIndexBean().getmColdDesc());
        mIndexSportBrief.setText("运动指数：" + weather.getIndexBean().getmSportsTitle());
        mIndexSportTxt.setText(weather.getIndexBean().getmSportsDesc());
        mIndexTravelBrief.setText("旅游指数：" + weather.getIndexBean().getmTravelTitle());
        mIndexTravelTxt.setText(weather.getIndexBean().getmTravelDesc());
        //右侧菜单数据
        Log.i("zxy", "showWeatherInfo:=====getmTodayWeatherBean=="+mRightTodayWeatherText);
        mRightTodayWeatherText.setText(weather.getmTodayWeatherBean().getmDay_Weather());
        mRightAirPressText.setText("气压" + weather.getmTodayWeatherBean().getmAir_Press());
        mRightRainText.setText("降水概率" + weather.getmTodayWeatherBean().getmJiangShui());
        Glide.with(this).load(weather.getmTodayWeatherBean().getmDay_Weather_Pic()).into(mRightTodayWeatherImg);
        mRightDayWeatherText.setText(weather.getmTodayWeatherBean().getmDay_Weather());
        mRightNightWeatherText.setText(weather.getmTodayWeatherBean().getmNight_Weather());
        mRightDayWind.setText(weather.getmTodayWeatherBean().getmDay_Wind_Power());
        mRightNightWind.setText(weather.getmTodayWeatherBean().getmNight_Wind_Power());
        mRightDayTempText.setText(weather.getmTodayWeatherBean().getmDay_Air_Temperature() + "°");
        mRightNightTempText.setText(weather.getmTodayWeatherBean().getmNight_Air_Temperature() + "°");
        mRightDayWindDiretion.setText(weather.getmTodayWeatherBean().getmDay_Wind_Direction());
        mRightNightWindDiretion.setText(weather.getmTodayWeatherBean().getmNight_Wind_Direction());
        mRightSunBeginText.setText(weather.getmTodayWeatherBean().getmSun_Begin());
        mRightSunEnd.setText(weather.getmTodayWeatherBean().getmSun_End());
        mRightUv.setText("紫外线" + weather.getmTodayWeatherBean().getmZiWaiXian());

        mRightCityNameText.setText("城市名：" + weather.getmCityInfoBean().getmCityName_C5());
        mRightAreaNumText.setText("邮编：" + weather.getmCityInfoBean().getmPostCode_C12());
        mRightLatitudeText.setText("经度：" + weather.getmCityInfoBean().getmLatitude());
        mRightLongtitudeText.setText("纬度：" + weather.getmCityInfoBean().getmLongitude());
        mRightAreaCodeText.setText("区号：" + weather.getmCityInfoBean().getmAreaCode_C11());
        mRightAltitudeText.setText("海拔：" + weather.getmCityInfoBean().getmAltitude_C15() + "米");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //城市选择的返回
        if (requestCode == REQUEST_CODE_PICK_CITY && resultCode == RESULT_OK) {
            if (data != null) {
                String city = data.getStringExtra("picked_city");
                 requestWeather(city);
            }
        }
    }


    @Override
    protected void onDestroy() {
        if (Util.isOnMainThread()) {
            if(mContext!=null) {
                Glide.with(mContext).pauseRequests();
            }
        }
        super.onDestroy();


    }

    private void setLocalData(){
        String res2 = "{\"showapi_res_error\":\"\",\"showapi_res_id\":\"19098a7c95ff4311b6a4d6dd40f4688c\",\"showapi_res_code\":0,\"showapi_res_body\":{\"f4\":{\"day_wind_power\":\"0-3级 <5.4m/s\",\"night_wind_power\":\"0-3级 <5.4m/s\",\"night_weather_code\":\"03\",\"day_weather\":\"阵雨\",\"sun_begin_end\":\"06:12|18:20\",\"air_press\":\"1012 hPa\",\"index\":{\"yh\":{\"desc\":\"建议尽量不要去室外约会。\",\"title\":\"较不适宜\"},\"zs\":{\"desc\":\"气温较高，易中暑人群注意阴凉休息。\",\"title\":\"可能中暑\"},\"cl\":{\"desc\":\"有降水,推荐您在室内进行休闲运动。\",\"title\":\"较不宜\"},\"travel\":{\"desc\":\"有降水气温高，注意防暑降温携带雨具。\",\"title\":\"较不宜\"},\"ag\":{\"desc\":\"天气条件易诱发过敏，易过敏人群应减少外出，外出宜穿长衣长裤并佩戴好眼镜和口罩，外出归来时及时清洁手和口鼻。\",\"title\":\"易发\"},\"beauty\":{\"desc\":\"请选用保湿型霜类化妆品。\",\"title\":\"保湿\"},\"pj\":{\"desc\":\"天热潮湿，可饮用清凉的啤酒，不要贪杯。\",\"title\":\"适宜\"},\"dy\":{\"desc\":\"天气不好，不适合垂钓\",\"title\":\"不适宜钓鱼\"},\"nl\":{\"desc\":\"天气较好，虽然有点风，但仍比较适宜夜生活，您可以放心外出。\",\"title\":\"较适宜\"},\"pk\":{\"desc\":\"天气酷热，不适宜放风筝。\",\"title\":\"不宜\"},\"uv\":{\"desc\":\"涂擦SPF大于15、PA+防晒护肤品\",\"title\":\"中等\"},\"aqi\":{\"desc\":\"空气很好，可以外出活动，呼吸新鲜空气\",\"title\":\"优质\"},\"gj\":{\"desc\":\"有降水，出门带雨具并注意防雷。\",\"title\":\"不适宜\"},\"ac\":{\"desc\":\"天气热，到中午的时候您将会感到有点热，因此建议在午后较热时开启制冷空调。\",\"title\":\"部分时间开启\"},\"mf\":{\"desc\":\"头发需要保持清洁，同时要注意防晒，含防晒成分洗发护发品是很好的选择。\",\"title\":\"一般\"},\"ls\":{\"desc\":\"有降水会淋湿衣物，不适宜晾晒。\",\"title\":\"不适宜\"},\"glass\":{\"desc\":\"需要佩戴\",\"title\":\"需要\"},\"xq\":{\"desc\":\"天气热，容易烦躁\",\"title\":\"较差\"},\"clothes\":{\"desc\":\"适合穿T恤、短薄外套等夏季服装\",\"title\":\"热\"},\"sports\":{\"desc\":\"有降水,推荐您在室内进行休闲运动。\",\"title\":\"较不宜\"},\"hc\":{\"desc\":\"天气不好，建议选择别的娱乐方式。\",\"title\":\"不适宜\"},\"comfort\":{\"desc\":\"偏热或较热，部分人感觉不舒适\",\"title\":\"较差\"},\"wash_car\":{\"desc\":\"有雨，雨水和泥水会弄脏爱车\",\"title\":\"不宜\"},\"cold\":{\"desc\":\"感冒机率较低，避免长期处于空调屋中。\",\"title\":\"少发\"}},\"day_weather_code\":\"03\",\"night_weather\":\"阵雨\",\"night_weather_pic\":\"http://app1.showapi.com/weather/icon/night/03.png\",\"day\":\"20180923\",\"day_wind_direction\":\"无持续风向\",\"jiangshui\":\"72%\",\"day_air_temperature\":\"30\",\"night_wind_direction\":\"无持续风向\",\"weekday\":7,\"ziwaixian\":\"中等\",\"night_air_temperature\":\"24\",\"day_weather_pic\":\"http://app1.showapi.com/weather/icon/day/03.png\",\"3hourForcast\":[{\"temperature\":\"26\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"25\",\"hour\":\"8时-14时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/01.png\",\"temperature_max\":\"29\",\"wind_power\":\"<3级,1\",\"weather\":\"多云\"},{\"temperature\":\"29\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"26\",\"hour\":\"14时-20时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/03.png\",\"temperature_max\":\"29\",\"wind_power\":\"<3级,3\",\"weather\":\"阵雨\"},{\"temperature\":\"27\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"25\",\"hour\":\"20时-2时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/night/03.png\",\"temperature_max\":\"29\",\"wind_power\":\"<3级,0\",\"weather\":\"阵雨\"},{\"temperature\":\"25\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"24\",\"hour\":\"2时-8时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/night/03.png\",\"temperature_max\":\"27\",\"wind_power\":\"<3级,0\",\"weather\":\"阵雨\"}]},\"f5\":{\"day_wind_power\":\"0-3级 <5.4m/s\",\"night_wind_power\":\"0-3级 <5.4m/s\",\"night_weather_code\":\"03\",\"day_weather\":\"阵雨\",\"sun_begin_end\":\"06:13|18:19\",\"air_press\":\"1012 hPa\",\"index\":{\"yh\":{\"desc\":\"做好防雨工作，仍可有一个愉快的约会。\",\"title\":\"较适宜\"},\"zs\":{\"desc\":\"气温不高，中暑几率极低。\",\"title\":\"不容易中暑\"},\"cl\":{\"desc\":\"有降水,推荐您在室内进行休闲运动。\",\"title\":\"较不宜\"},\"travel\":{\"desc\":\"有降水气温高，注意防暑降温携带雨具。\",\"title\":\"较不宜\"},\"ag\":{\"desc\":\"天气条件易诱发过敏，易过敏人群应减少外出，外出宜穿长衣长裤并佩戴好眼镜和口罩，外出归来时及时清洁手和口鼻。\",\"title\":\"易发\"},\"beauty\":{\"desc\":\"请选用保湿型霜类化妆品。\",\"title\":\"保湿\"},\"pj\":{\"desc\":\"天气炎热，可适量饮用啤酒，不要过量。\",\"title\":\"适宜\"},\"dy\":{\"desc\":\"天气不好，不适合垂钓\",\"title\":\"不适宜钓鱼\"},\"nl\":{\"desc\":\"天气较好，虽然有点风，但仍比较适宜夜生活，您可以放心外出。\",\"title\":\"较适宜\"},\"pk\":{\"desc\":\"有雨，天气不好，不适宜放风筝。\",\"title\":\"不适宜\"},\"uv\":{\"desc\":\"辐射较弱，涂擦SPF12-15、PA+护肤品\",\"title\":\"弱\"},\"aqi\":{\"desc\":\"空气很好，可以外出活动，呼吸新鲜空气\",\"title\":\"优质\"},\"gj\":{\"desc\":\"有降水，出门带雨具并注意防雷。\",\"title\":\"不适宜\"},\"ac\":{\"desc\":\"天气热，到中午的时候您将会感到有点热，因此建议在午后较热时开启制冷空调。\",\"title\":\"部分时间开启\"},\"mf\":{\"desc\":\"头发需要保持清洁，同时要注意防晒，含防晒成分洗发护发品是很好的选择。\",\"title\":\"一般\"},\"ls\":{\"desc\":\"有降水会淋湿衣物，不适宜晾晒。\",\"title\":\"不适宜\"},\"glass\":{\"desc\":\"不需要佩戴\",\"title\":\"不需要\"},\"xq\":{\"desc\":\"差湿嗒塔的环境让人的心情难以开朗\",\"title\":\"较差\"},\"clothes\":{\"desc\":\"适合穿T恤、短薄外套等夏季服装\",\"title\":\"热\"},\"sports\":{\"desc\":\"有降水,推荐您在室内进行休闲运动。\",\"title\":\"较不宜\"},\"hc\":{\"desc\":\"天气不好，建议选择别的娱乐方式。\",\"title\":\"不适宜\"},\"comfort\":{\"desc\":\"热，感觉不舒适\",\"title\":\"较差\"},\"wash_car\":{\"desc\":\"有雨，雨水和泥水会弄脏爱车\",\"title\":\"不宜\"},\"cold\":{\"desc\":\"感冒机率较低，避免长期处于空调屋中。\",\"title\":\"少发\"}},\"day_weather_code\":\"03\",\"night_weather\":\"阵雨\",\"night_weather_pic\":\"http://app1.showapi.com/weather/icon/night/03.png\",\"day\":\"20180924\",\"day_wind_direction\":\"无持续风向\",\"jiangshui\":\"70%\",\"day_air_temperature\":\"29\",\"night_wind_direction\":\"无持续风向\",\"weekday\":1,\"ziwaixian\":\"弱\",\"night_air_temperature\":\"25\",\"day_weather_pic\":\"http://app1.showapi.com/weather/icon/day/03.png\",\"3hourForcast\":[{\"temperature\":\"24\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"24\",\"hour\":\"8时-14时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/03.png\",\"temperature_max\":\"28\",\"wind_power\":\"<3级,3\",\"weather\":\"阵雨\"},{\"temperature\":\"28\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"24\",\"hour\":\"14时-20时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/03.png\",\"temperature_max\":\"28\",\"wind_power\":\"<3级,3\",\"weather\":\"阵雨\"},{\"temperature\":\"27\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"25\",\"hour\":\"20时-2时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/night/03.png\",\"temperature_max\":\"28\",\"wind_power\":\"<3级,0\",\"weather\":\"阵雨\"},{\"temperature\":\"25\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"25\",\"hour\":\"2时-8时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/night/03.png\",\"temperature_max\":\"27\",\"wind_power\":\"<3级,0\",\"weather\":\"阵雨\"}]},\"hourDataList\":[{\"wind_direction\":\"东南风 \",\"aqi\":\"57\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/night/01.png\",\"wind_power\":\"1级\",\"temperature_time\":\"00:00\",\"weather_code\":\"01\",\"temperature\":\"27\",\"sd\":\"85%\",\"aqiDetail\":{\"quality\":\"良好\",\"aqi\":\"57\",\"pm10\":\"63\",\"area\":\"深圳\",\"co\":\"0.708\",\"o3\":\"37\",\"so2\":\"10\",\"no2\":\"62\",\"primary_pollutant\":\"臭氧8小时\",\"o3_8h\":\"112\",\"num\":\"276\",\"pm2_5\":\"38\"},\"weather\":\"多云\"},{\"wind_direction\":\"东南风 \",\"aqi\":\"56\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/night/01.png\",\"wind_power\":\"2级\",\"temperature_time\":\"00:30\",\"weather_code\":\"01\",\"temperature\":\"27\",\"sd\":\"87%\",\"aqiDetail\":{\"quality\":\"良好\",\"aqi\":\"56\",\"pm10\":\"62\",\"area\":\"深圳\",\"co\":\"0.7\",\"o3\":\"32\",\"so2\":\"11\",\"no2\":\"58\",\"primary_pollutant\":\"颗粒物(PM10)\",\"o3_8h\":\"96\",\"num\":\"269\",\"pm2_5\":\"38\"},\"weather\":\"多云\"},{\"wind_direction\":\"南风\",\"aqi\":\"56\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/night/01.png\",\"wind_power\":\"2级\",\"temperature_time\":\"01:00\",\"weather_code\":\"01\",\"temperature\":\"27\",\"sd\":\"88%\",\"aqiDetail\":{\"quality\":\"良好\",\"aqi\":\"56\",\"pm10\":\"62\",\"area\":\"深圳\",\"co\":\"0.7\",\"o3\":\"32\",\"so2\":\"11\",\"no2\":\"58\",\"primary_pollutant\":\"颗粒物(PM10)\",\"o3_8h\":\"96\",\"num\":\"269\",\"pm2_5\":\"38\"},\"weather\":\"多云\"},{\"wind_direction\":\"西南风\",\"aqi\":\"56\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/night/01.png\",\"wind_power\":\"1级\",\"temperature_time\":\"01:30\",\"weather_code\":\"01\",\"temperature\":\"26\",\"sd\":\"88%\",\"aqiDetail\":{\"quality\":\"良好\",\"aqi\":\"56\",\"pm10\":\"62\",\"area\":\"深圳\",\"co\":\"0.692\",\"o3\":\"27\",\"so2\":\"11\",\"no2\":\"58\",\"primary_pollutant\":\"颗粒物(PM10)\",\"o3_8h\":\"79\",\"num\":\"263\",\"pm2_5\":\"36\"},\"weather\":\"多云\"},{\"wind_direction\":\"西南风\",\"aqi\":\"56\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/night/01.png\",\"wind_power\":\"1级\",\"temperature_time\":\"02:00\",\"weather_code\":\"01\",\"temperature\":\"26\",\"sd\":\"90%\",\"aqiDetail\":{\"quality\":\"良好\",\"aqi\":\"56\",\"pm10\":\"62\",\"area\":\"深圳\",\"co\":\"0.692\",\"o3\":\"27\",\"so2\":\"11\",\"no2\":\"58\",\"primary_pollutant\":\"颗粒物(PM10)\",\"o3_8h\":\"79\",\"num\":\"263\",\"pm2_5\":\"36\"},\"weather\":\"多云\"},{\"wind_direction\":\"东南风 \",\"aqi\":\"58\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/night/01.png\",\"wind_power\":\"1级\",\"temperature_time\":\"02:30\",\"weather_code\":\"01\",\"temperature\":\"26\",\"sd\":\"91%\",\"aqiDetail\":{\"quality\":\"良好\",\"aqi\":\"58\",\"pm10\":\"65\",\"area\":\"深圳\",\"co\":\"0.7\",\"o3\":\"25\",\"so2\":\"11\",\"no2\":\"56\",\"primary_pollutant\":\"颗粒物(PM10)\",\"o3_8h\":\"25\",\"num\":\"286\",\"pm2_5\":\"38\"},\"weather\":\"多云\"},{\"wind_direction\":\"东南风 \",\"aqi\":\"58\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/night/01.png\",\"wind_power\":\"1级\",\"temperature_time\":\"03:00\",\"weather_code\":\"01\",\"temperature\":\"26\",\"sd\":\"90%\",\"aqiDetail\":{\"quality\":\"良好\",\"aqi\":\"58\",\"pm10\":\"65\",\"area\":\"深圳\",\"co\":\"0.7\",\"o3\":\"25\",\"so2\":\"11\",\"no2\":\"56\",\"primary_pollutant\":\"颗粒物(PM10)\",\"o3_8h\":\"25\",\"num\":\"286\",\"pm2_5\":\"38\"},\"weather\":\"多云\"},{\"wind_direction\":\"西南风\",\"aqi\":\"58\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/night/01.png\",\"wind_power\":\"1级\",\"temperature_time\":\"03:30\",\"weather_code\":\"01\",\"temperature\":\"26\",\"sd\":\"90%\",\"aqiDetail\":{\"quality\":\"良好\",\"aqi\":\"58\",\"pm10\":\"65\",\"area\":\"深圳\",\"co\":\"0.736\",\"o3\":\"22\",\"so2\":\"11\",\"no2\":\"58\",\"primary_pollutant\":\"颗粒物(PM10)\",\"o3_8h\":\"24\",\"num\":\"294\",\"pm2_5\":\"40\"},\"weather\":\"多云\"},{\"wind_direction\":\"东风\",\"aqi\":\"58\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/night/01.png\",\"wind_power\":\"1级\",\"temperature_time\":\"04:00\",\"weather_code\":\"01\",\"temperature\":\"26\",\"sd\":\"92%\",\"aqiDetail\":{\"quality\":\"良好\",\"aqi\":\"58\",\"pm10\":\"65\",\"area\":\"深圳\",\"co\":\"0.736\",\"o3\":\"22\",\"so2\":\"11\",\"no2\":\"58\",\"primary_pollutant\":\"颗粒物(PM10)\",\"o3_8h\":\"24\",\"num\":\"294\",\"pm2_5\":\"40\"},\"weather\":\"多云\"},{\"wind_direction\":\"东南风 \",\"aqi\":\"58\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/night/01.png\",\"wind_power\":\"1级\",\"temperature_time\":\"04:30\",\"weather_code\":\"01\",\"temperature\":\"26\",\"sd\":\"91%\",\"aqiDetail\":{\"quality\":\"良好\",\"aqi\":\"58\",\"pm10\":\"65\",\"area\":\"深圳\",\"co\":\"0.727\",\"o3\":\"22\",\"so2\":\"11\",\"no2\":\"54\",\"primary_pollutant\":\"颗粒物(PM10)\",\"o3_8h\":\"24\",\"num\":\"293\",\"pm2_5\":\"40\"},\"weather\":\"多云\"},{\"wind_direction\":\"静风\",\"aqi\":\"58\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/night/01.png\",\"wind_power\":\"0级\",\"temperature_time\":\"05:00\",\"weather_code\":\"01\",\"temperature\":\"26\",\"sd\":\"90%\",\"aqiDetail\":{\"quality\":\"良好\",\"aqi\":\"58\",\"pm10\":\"65\",\"area\":\"深圳\",\"co\":\"0.727\",\"o3\":\"22\",\"so2\":\"11\",\"no2\":\"54\",\"primary_pollutant\":\"颗粒物(PM10)\",\"o3_8h\":\"24\",\"num\":\"293\",\"pm2_5\":\"40\"},\"weather\":\"多云\"},{\"wind_direction\":\"西南风\",\"aqi\":\"58\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/night/01.png\",\"wind_power\":\"1级\",\"temperature_time\":\"05:30\",\"weather_code\":\"01\",\"temperature\":\"26\",\"sd\":\"91%\",\"aqiDetail\":{\"quality\":\"良好\",\"aqi\":\"58\",\"pm10\":\"65\",\"area\":\"深圳\",\"co\":\"0.718\",\"o3\":\"21\",\"so2\":\"10\",\"no2\":\"49\",\"primary_pollutant\":\"颗粒物(PM10)\",\"o3_8h\":\"23\",\"num\":\"299\",\"pm2_5\":\"39\"},\"weather\":\"多云\"},{\"wind_direction\":\"西北风\",\"aqi\":\"58\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/night/01.png\",\"wind_power\":\"1级\",\"temperature_time\":\"06:00\",\"weather_code\":\"01\",\"temperature\":\"26\",\"sd\":\"89%\",\"aqiDetail\":{\"quality\":\"良好\",\"aqi\":\"58\",\"pm10\":\"65\",\"area\":\"深圳\",\"co\":\"0.718\",\"o3\":\"21\",\"so2\":\"10\",\"no2\":\"49\",\"primary_pollutant\":\"颗粒物(PM10)\",\"o3_8h\":\"23\",\"num\":\"299\",\"pm2_5\":\"39\"},\"weather\":\"多云\"},{\"wind_direction\":\"西南风\",\"aqi\":\"55\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/night/01.png\",\"wind_power\":\"1级\",\"temperature_time\":\"06:30\",\"weather_code\":\"01\",\"temperature\":\"26\",\"sd\":\"90%\",\"aqiDetail\":{\"quality\":\"良好\",\"aqi\":\"55\",\"pm10\":\"60\",\"area\":\"深圳\",\"co\":\"0.682\",\"o3\":\"26\",\"so2\":\"10\",\"no2\":\"43\",\"primary_pollutant\":\"颗粒物(PM10)\",\"o3_8h\":\"24\",\"num\":\"287\",\"pm2_5\":\"36\"},\"weather\":\"多云\"},{\"wind_direction\":\"西南风\",\"aqi\":\"55\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/01.png\",\"wind_power\":\"1级\",\"temperature_time\":\"07:00\",\"weather_code\":\"01\",\"temperature\":\"26\",\"sd\":\"88%\",\"aqiDetail\":{\"quality\":\"良好\",\"aqi\":\"55\",\"pm10\":\"60\",\"area\":\"深圳\",\"co\":\"0.682\",\"o3\":\"26\",\"so2\":\"10\",\"no2\":\"43\",\"primary_pollutant\":\"颗粒物(PM10)\",\"o3_8h\":\"24\",\"num\":\"287\",\"pm2_5\":\"36\"},\"weather\":\"多云\"},{\"wind_direction\":\"西南风\",\"aqi\":\"54\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/01.png\",\"wind_power\":\"1级\",\"temperature_time\":\"07:31\",\"weather_code\":\"01\",\"temperature\":\"26\",\"sd\":\"90%\",\"aqiDetail\":{\"quality\":\"良好\",\"aqi\":\"54\",\"pm10\":\"58\",\"area\":\"深圳\",\"co\":\"0.65\",\"o3\":\"25\",\"so2\":\"11\",\"no2\":\"41\",\"primary_pollutant\":\"颗粒物(PM2.5)颗粒物(PM10)\",\"o3_8h\":\"23\",\"num\":\"286\",\"pm2_5\":\"37\"},\"weather\":\"多云\"},{\"wind_direction\":\"西南风\",\"aqi\":\"56\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/01.png\",\"wind_power\":\"1级\",\"temperature_time\":\"08:04\",\"weather_code\":\"01\",\"temperature\":\"27\",\"sd\":\"87%\",\"aqiDetail\":{\"quality\":\"良好\",\"aqi\":\"56\",\"pm10\":\"62\",\"area\":\"深圳\",\"co\":\"0.692\",\"o3\":\"23\",\"so2\":\"9\",\"no2\":\"39\",\"primary_pollutant\":\"颗粒物(PM10)\",\"o3_8h\":\"23\",\"num\":\"290\",\"pm2_5\":\"38\"},\"weather\":\"多云\"},{\"wind_direction\":\"南风\",\"aqi\":\"56\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/01.png\",\"wind_power\":\"1级\",\"temperature_time\":\"08:30\",\"weather_code\":\"01\",\"temperature\":\"28\",\"sd\":\"83%\",\"aqiDetail\":{\"quality\":\"良好\",\"aqi\":\"56\",\"pm10\":\"62\",\"area\":\"深圳\",\"co\":\"0.692\",\"o3\":\"23\",\"so2\":\"9\",\"no2\":\"39\",\"primary_pollutant\":\"颗粒物(PM10)\",\"o3_8h\":\"23\",\"num\":\"290\",\"pm2_5\":\"38\"},\"weather\":\"多云\"},{\"wind_direction\":\"东风\",\"aqi\":\"56\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/01.png\",\"wind_power\":\"1级\",\"temperature_time\":\"09:00\",\"weather_code\":\"01\",\"temperature\":\"28\",\"sd\":\"79%\",\"aqiDetail\":{\"quality\":\"良好\",\"aqi\":\"56\",\"pm10\":\"62\",\"area\":\"深圳\",\"co\":\"0.692\",\"o3\":\"23\",\"so2\":\"9\",\"no2\":\"39\",\"primary_pollutant\":\"颗粒物(PM10)\",\"o3_8h\":\"23\",\"num\":\"290\",\"pm2_5\":\"38\"},\"weather\":\"多云\"},{\"wind_direction\":\"西南风\",\"aqi\":\"53\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/01.png\",\"wind_power\":\"2级\",\"temperature_time\":\"09:30\",\"weather_code\":\"01\",\"temperature\":\"28\",\"sd\":\"75%\",\"aqiDetail\":{\"quality\":\"良好\",\"aqi\":\"53\",\"pm10\":\"57\",\"area\":\"深圳\",\"co\":\"0.733\",\"o3\":\"25\",\"so2\":\"10\",\"no2\":\"39\",\"primary_pollutant\":\"颗粒物(PM10)\",\"o3_8h\":\"23\",\"num\":\"266\",\"pm2_5\":\"35\"},\"weather\":\"多云\"},{\"wind_direction\":\"南风\",\"aqi\":\"53\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/01.png\",\"wind_power\":\"2级\",\"temperature_time\":\"10:00\",\"weather_code\":\"01\",\"temperature\":\"29\",\"sd\":\"75%\",\"aqiDetail\":{\"quality\":\"良好\",\"aqi\":\"53\",\"pm10\":\"57\",\"area\":\"深圳\",\"co\":\"0.733\",\"o3\":\"25\",\"so2\":\"10\",\"no2\":\"39\",\"primary_pollutant\":\"颗粒物(PM10)\",\"o3_8h\":\"23\",\"num\":\"266\",\"pm2_5\":\"35\"},\"weather\":\"多云\"},{\"wind_direction\":\"南风\",\"aqi\":\"49\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/01.png\",\"wind_power\":\"1级\",\"temperature_time\":\"10:31\",\"weather_code\":\"01\",\"temperature\":\"29\",\"sd\":\"74%\",\"aqiDetail\":{\"quality\":\"良好\",\"aqi\":\"49\",\"pm10\":\"51\",\"area\":\"深圳\",\"co\":\"0.667\",\"o3\":\"37\",\"so2\":\"13\",\"no2\":\"38\",\"primary_pollutant\":\"颗粒物(PM10)\",\"o3_8h\":\"24\",\"num\":\"214\",\"pm2_5\":\"30\"},\"weather\":\"多云\"},{\"wind_direction\":\"西南风\",\"aqi\":\"49\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/01.png\",\"wind_power\":\"2级\",\"temperature_time\":\"11:01\",\"weather_code\":\"01\",\"temperature\":\"30\",\"sd\":\"75%\",\"aqiDetail\":{\"quality\":\"良好\",\"aqi\":\"49\",\"pm10\":\"51\",\"area\":\"深圳\",\"co\":\"0.667\",\"o3\":\"37\",\"so2\":\"13\",\"no2\":\"38\",\"primary_pollutant\":\"颗粒物(PM10)\",\"o3_8h\":\"24\",\"num\":\"214\",\"pm2_5\":\"30\"},\"weather\":\"多云\"},{\"wind_direction\":\"西南风\",\"aqi\":\"45\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/01.png\",\"wind_power\":\"2级\",\"temperature_time\":\"11:31\",\"weather_code\":\"01\",\"temperature\":\"30\",\"sd\":\"73%\",\"aqiDetail\":{\"quality\":\"优质\",\"aqi\":\"45\",\"pm10\":\"48\",\"area\":\"深圳\",\"co\":\"0.642\",\"o3\":\"52\",\"so2\":\"12\",\"no2\":\"35\",\"primary_pollutant\":\"\",\"o3_8h\":\"28\",\"num\":\"181\",\"pm2_5\":\"28\"},\"weather\":\"多云\"},{\"wind_direction\":\"西南风\",\"aqi\":\"45\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/01.png\",\"wind_power\":\"2级\",\"temperature_time\":\"12:06\",\"weather_code\":\"01\",\"temperature\":\"31\",\"sd\":\"68%\",\"aqiDetail\":{\"quality\":\"优质\",\"aqi\":\"45\",\"pm10\":\"49\",\"area\":\"深圳\",\"co\":\"0.636\",\"o3\":\"60\",\"so2\":\"12\",\"no2\":\"33\",\"primary_pollutant\":\"颗粒物(PM10)\",\"o3_8h\":\"31\",\"num\":\"182\",\"pm2_5\":\"29\"},\"weather\":\"多云\"},{\"wind_direction\":\"西南风\",\"aqi\":\"45\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/01.png\",\"wind_power\":\"2级\",\"temperature_time\":\"12:30\",\"weather_code\":\"01\",\"temperature\":\"31\",\"sd\":\"69%\",\"aqiDetail\":{\"quality\":\"优质\",\"aqi\":\"45\",\"pm10\":\"49\",\"area\":\"深圳\",\"co\":\"0.636\",\"o3\":\"60\",\"so2\":\"12\",\"no2\":\"33\",\"primary_pollutant\":\"颗粒物(PM10)\",\"o3_8h\":\"31\",\"num\":\"182\",\"pm2_5\":\"29\"},\"weather\":\"多云\"},{\"wind_direction\":\"西南风\",\"aqi\":\"45\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/01.png\",\"wind_power\":\"2级\",\"temperature_time\":\"13:00\",\"weather_code\":\"01\",\"temperature\":\"31\",\"sd\":\"69%\",\"aqiDetail\":{\"quality\":\"优质\",\"aqi\":\"45\",\"pm10\":\"49\",\"area\":\"深圳\",\"co\":\"0.636\",\"o3\":\"60\",\"so2\":\"12\",\"no2\":\"33\",\"primary_pollutant\":\"颗粒物(PM10)\",\"o3_8h\":\"31\",\"num\":\"182\",\"pm2_5\":\"29\"},\"weather\":\"多云\"},{\"wind_direction\":\"西南风\",\"aqi\":\"46\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/01.png\",\"wind_power\":\"2级\",\"temperature_time\":\"13:30\",\"weather_code\":\"01\",\"temperature\":\"31\",\"sd\":\"69%\",\"aqiDetail\":{\"quality\":\"优质\",\"aqi\":\"46\",\"pm10\":\"49\",\"area\":\"深圳\",\"co\":\"0.62\",\"o3\":\"87\",\"so2\":\"12\",\"no2\":\"29\",\"primary_pollutant\":\"颗粒物(PM10)\",\"o3_8h\":\"40\",\"num\":\"208\",\"pm2_5\":\"30\"},\"weather\":\"多云\"},{\"wind_direction\":\"西南风\",\"aqi\":\"46\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/01.png\",\"wind_power\":\"2级\",\"temperature_time\":\"14:00\",\"weather_code\":\"01\",\"temperature\":\"31\",\"sd\":\"69%\",\"aqiDetail\":{\"quality\":\"优质\",\"aqi\":\"46\",\"pm10\":\"49\",\"area\":\"深圳\",\"co\":\"0.62\",\"o3\":\"87\",\"so2\":\"12\",\"no2\":\"29\",\"primary_pollutant\":\"颗粒物(PM10)\",\"o3_8h\":\"40\",\"num\":\"208\",\"pm2_5\":\"30\"},\"weather\":\"多云\"},{\"wind_direction\":\"西南风\",\"aqi\":\"43\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/01.png\",\"wind_power\":\"2级\",\"temperature_time\":\"14:30\",\"weather_code\":\"01\",\"temperature\":\"31\",\"sd\":\"69%\",\"aqiDetail\":{\"quality\":\"优质\",\"aqi\":\"43\",\"pm10\":\"48\",\"area\":\"深圳\",\"co\":\"0.667\",\"o3\":\"106\",\"so2\":\"10\",\"no2\":\"23\",\"primary_pollutant\":\"\",\"o3_8h\":\"50\",\"num\":\"196\",\"pm2_5\":\"29\"},\"weather\":\"多云\"},{\"wind_direction\":\"西南风\",\"aqi\":\"43\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/01.png\",\"wind_power\":\"2级\",\"temperature_time\":\"15:00\",\"weather_code\":\"01\",\"temperature\":\"31\",\"sd\":\"69%\",\"aqiDetail\":{\"quality\":\"优质\",\"aqi\":\"43\",\"pm10\":\"48\",\"area\":\"深圳\",\"co\":\"0.667\",\"o3\":\"106\",\"so2\":\"10\",\"no2\":\"23\",\"primary_pollutant\":\"\",\"o3_8h\":\"50\",\"num\":\"196\",\"pm2_5\":\"29\"},\"weather\":\"多云\"},{\"wind_direction\":\"西南风\",\"aqi\":\"45\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/01.png\",\"wind_power\":\"2级\",\"temperature_time\":\"15:30\",\"weather_code\":\"01\",\"temperature\":\"31\",\"sd\":\"69%\",\"aqiDetail\":{\"quality\":\"优质\",\"aqi\":\"45\",\"pm10\":\"48\",\"area\":\"深圳\",\"co\":\"0.642\",\"o3\":\"124\",\"so2\":\"9\",\"no2\":\"21\",\"primary_pollutant\":\"\",\"o3_8h\":\"64\",\"num\":\"231\",\"pm2_5\":\"30\"},\"weather\":\"多云\"},{\"wind_direction\":\"西南风\",\"aqi\":\"45\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/01.png\",\"wind_power\":\"2级\",\"temperature_time\":\"16:00\",\"weather_code\":\"01\",\"temperature\":\"31\",\"sd\":\"69%\",\"aqiDetail\":{\"quality\":\"优质\",\"aqi\":\"45\",\"pm10\":\"48\",\"area\":\"深圳\",\"co\":\"0.642\",\"o3\":\"124\",\"so2\":\"9\",\"no2\":\"21\",\"primary_pollutant\":\"\",\"o3_8h\":\"64\",\"num\":\"231\",\"pm2_5\":\"30\"},\"weather\":\"多云\"}],\"f2\":{\"day_wind_power\":\"0-3级 <5.4m/s\",\"night_wind_power\":\"0-3级 <5.4m/s\",\"night_weather_code\":\"03\",\"day_weather\":\"阵雨\",\"sun_begin_end\":\"06:12|18:22\",\"air_press\":\"1012 hPa\",\"index\":{\"yh\":{\"desc\":\"建议尽量不要去室外约会。\",\"title\":\"较不适宜\"},\"zs\":{\"desc\":\"气温较高，易中暑人群注意阴凉休息。\",\"title\":\"可能中暑\"},\"cl\":{\"desc\":\"有降水,推荐您在室内进行休闲运动。\",\"title\":\"较不宜\"},\"travel\":{\"desc\":\"有降水气温高，注意防暑降温携带雨具。\",\"title\":\"较不宜\"},\"ag\":{\"desc\":\"天气条件易诱发过敏，易过敏人群应减少外出，外出宜穿长衣长裤并佩戴好眼镜和口罩，外出归来时及时清洁手和口鼻。\",\"title\":\"易发\"},\"beauty\":{\"desc\":\"请选用保湿型霜类化妆品。\",\"title\":\"保湿\"},\"pj\":{\"desc\":\"天热潮湿，可饮用清凉的啤酒，不要贪杯。\",\"title\":\"适宜\"},\"dy\":{\"desc\":\"天气不好，不适合垂钓\",\"title\":\"不适宜钓鱼\"},\"nl\":{\"desc\":\"天气较好，虽然有点风，但仍比较适宜夜生活，您可以放心外出。\",\"title\":\"较适宜\"},\"pk\":{\"desc\":\"天气酷热，不适宜放风筝。\",\"title\":\"不宜\"},\"uv\":{\"desc\":\"涂擦SPF大于15、PA+防晒护肤品\",\"title\":\"中等\"},\"aqi\":{\"desc\":\"空气很好，可以外出活动，呼吸新鲜空气\",\"title\":\"优质\"},\"gj\":{\"desc\":\"有降水，出门带雨具并注意防雷。\",\"title\":\"不适宜\"},\"ac\":{\"desc\":\"天气热，到中午的时候您将会感到有点热，因此建议在午后较热时开启制冷空调。\",\"title\":\"部分时间开启\"},\"mf\":{\"desc\":\"头发需要保持清洁，同时要注意防晒，含防晒成分洗发护发品是很好的选择。\",\"title\":\"一般\"},\"ls\":{\"desc\":\"有降水会淋湿衣物，不适宜晾晒。\",\"title\":\"不适宜\"},\"glass\":{\"desc\":\"需要佩戴\",\"title\":\"需要\"},\"xq\":{\"desc\":\"天气热，容易烦躁\",\"title\":\"较差\"},\"clothes\":{\"desc\":\"适合穿T恤、短薄外套等夏季服装\",\"title\":\"热\"},\"sports\":{\"desc\":\"有降水,推荐您在室内进行休闲运动。\",\"title\":\"较不宜\"},\"hc\":{\"desc\":\"天气不好，建议选择别的娱乐方式。\",\"title\":\"不适宜\"},\"comfort\":{\"desc\":\"偏热或较热，部分人感觉不舒适\",\"title\":\"较差\"},\"wash_car\":{\"desc\":\"有雨，雨水和泥水会弄脏爱车\",\"title\":\"不宜\"},\"cold\":{\"desc\":\"感冒机率较低，避免长期处于空调屋中。\",\"title\":\"少发\"}},\"day_weather_code\":\"03\",\"night_weather\":\"阵雨\",\"night_weather_pic\":\"http://app1.showapi.com/weather/icon/night/03.png\",\"day\":\"20180921\",\"day_wind_direction\":\"无持续风向\",\"jiangshui\":\"79%\",\"day_air_temperature\":\"32\",\"night_wind_direction\":\"无持续风向\",\"weekday\":5,\"ziwaixian\":\"中等\",\"night_air_temperature\":\"26\",\"day_weather_pic\":\"http://app1.showapi.com/weather/icon/day/03.png\",\"3hourForcast\":[{\"temperature\":\"28\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"26\",\"hour\":\"8时-11时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/01.png\",\"temperature_max\":\"31\",\"wind_power\":\"<3级,1\",\"weather\":\"多云\"},{\"temperature\":\"31\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"28\",\"hour\":\"11时-14时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/03.png\",\"temperature_max\":\"31\",\"wind_power\":\"<3级,3\",\"weather\":\"阵雨\"},{\"temperature\":\"31\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"30\",\"hour\":\"14时-17时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/03.png\",\"temperature_max\":\"31\",\"wind_power\":\"<3级,3\",\"weather\":\"阵雨\"},{\"temperature\":\"30\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"26\",\"hour\":\"17时-20时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/03.png\",\"temperature_max\":\"31\",\"wind_power\":\"<3级,3\",\"weather\":\"阵雨\"},{\"temperature\":\"26\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"26\",\"hour\":\"20时-23时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/night/03.png\",\"temperature_max\":\"30\",\"wind_power\":\"<3级,0\",\"weather\":\"阵雨\"},{\"temperature\":\"26\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"26\",\"hour\":\"23时-2时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/night/03.png\",\"temperature_max\":\"26\",\"wind_power\":\"<3级,0\",\"weather\":\"阵雨\"},{\"temperature\":\"26\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"26\",\"hour\":\"2时-5时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/night/03.png\",\"temperature_max\":\"26\",\"wind_power\":\"<3级,0\",\"weather\":\"阵雨\"},{\"temperature\":\"26\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"26\",\"hour\":\"5时-8时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/night/03.png\",\"temperature_max\":\"27\",\"wind_power\":\"<3级,0\",\"weather\":\"阵雨\"}]},\"f6\":{\"day_wind_power\":\"0-3级 <5.4m/s\",\"night_wind_power\":\"0-3级 <5.4m/s\",\"night_weather_code\":\"01\",\"day_weather\":\"阵雨\",\"sun_begin_end\":\"06:13|18:18\",\"air_press\":\"1012 hPa\",\"index\":{\"yh\":{\"desc\":\"建议尽量不要去室外约会。\",\"title\":\"较不适宜\"},\"zs\":{\"desc\":\"气温较高，易中暑人群注意阴凉休息。\",\"title\":\"可能中暑\"},\"cl\":{\"desc\":\"有降水,推荐您在室内进行休闲运动。\",\"title\":\"较不宜\"},\"travel\":{\"desc\":\"有降水气温高，注意防暑降温携带雨具。\",\"title\":\"较不宜\"},\"ag\":{\"desc\":\"天气条件易诱发过敏，易过敏人群应减少外出，外出宜穿长衣长裤并佩戴好眼镜和口罩，外出归来时及时清洁手和口鼻。\",\"title\":\"易发\"},\"beauty\":{\"desc\":\"请选用保湿型霜类化妆品。\",\"title\":\"保湿\"},\"pj\":{\"desc\":\"天热潮湿，可饮用清凉的啤酒，不要贪杯。\",\"title\":\"适宜\"},\"dy\":{\"desc\":\"天气不好，不适合垂钓\",\"title\":\"不适宜钓鱼\"},\"nl\":{\"desc\":\"天气较好，虽然有点风，但仍比较适宜夜生活，您可以放心外出。\",\"title\":\"较适宜\"},\"pk\":{\"desc\":\"天气酷热，不适宜放风筝。\",\"title\":\"不宜\"},\"uv\":{\"desc\":\"涂擦SPF大于15、PA+防晒护肤品\",\"title\":\"中等\"},\"aqi\":{\"desc\":\"空气很好，可以外出活动，呼吸新鲜空气\",\"title\":\"优质\"},\"gj\":{\"desc\":\"有降水，出门带雨具并注意防雷。\",\"title\":\"不适宜\"},\"ac\":{\"desc\":\"天气热，到中午的时候您将会感到有点热，因此建议在午后较热时开启制冷空调。\",\"title\":\"部分时间开启\"},\"mf\":{\"desc\":\"头发需要保持清洁，同时要注意防晒，含防晒成分洗发护发品是很好的选择。\",\"title\":\"一般\"},\"ls\":{\"desc\":\"有降水会淋湿衣物，不适宜晾晒。\",\"title\":\"不适宜\"},\"glass\":{\"desc\":\"需要佩戴\",\"title\":\"需要\"},\"xq\":{\"desc\":\"天气热，容易烦躁\",\"title\":\"较差\"},\"clothes\":{\"desc\":\"适合穿T恤、短薄外套等夏季服装\",\"title\":\"热\"},\"sports\":{\"desc\":\"有降水,推荐您在室内进行休闲运动。\",\"title\":\"较不宜\"},\"hc\":{\"desc\":\"天气不好，建议选择别的娱乐方式。\",\"title\":\"不适宜\"},\"comfort\":{\"desc\":\"热，感觉不舒适\",\"title\":\"较差\"},\"wash_car\":{\"desc\":\"有雨，雨水和泥水会弄脏爱车\",\"title\":\"不宜\"},\"cold\":{\"desc\":\"感冒机率较低，避免长期处于空调屋中。\",\"title\":\"少发\"}},\"day_weather_code\":\"03\",\"night_weather\":\"多云\",\"night_weather_pic\":\"http://app1.showapi.com/weather/icon/night/01.png\",\"day\":\"20180925\",\"day_wind_direction\":\"无持续风向\",\"jiangshui\":\"79%\",\"day_air_temperature\":\"30\",\"night_wind_direction\":\"无持续风向\",\"weekday\":2,\"ziwaixian\":\"中等\",\"night_air_temperature\":\"25\",\"day_weather_pic\":\"http://app1.showapi.com/weather/icon/day/03.png\",\"3hourForcast\":[{\"temperature\":\"25\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"25\",\"hour\":\"8时-14时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/03.png\",\"temperature_max\":\"29\",\"wind_power\":\"<3级,3\",\"weather\":\"阵雨\"},{\"temperature\":\"29\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"25\",\"hour\":\"14时-20时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/03.png\",\"temperature_max\":\"29\",\"wind_power\":\"<3级,3\",\"weather\":\"阵雨\"},{\"temperature\":\"27\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"25\",\"hour\":\"20时-2时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/night/03.png\",\"temperature_max\":\"29\",\"wind_power\":\"<3级,0\",\"weather\":\"阵雨\"},{\"temperature\":\"25\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"25\",\"hour\":\"2时-8时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/night/01.png\",\"temperature_max\":\"27\",\"wind_power\":\"<3级,0\",\"weather\":\"多云\"}]},\"time\":\"20180920113000\",\"cityInfo\":{\"c4\":\"shenzhen\",\"c17\":\"+8\",\"c5\":\"深圳\",\"c6\":\"guangdong\",\"latitude\":22.544,\"c7\":\"广东\",\"c1\":\"101280601\",\"c12\":\"518001\",\"c2\":\"shenzhen\",\"c15\":\"40\",\"c8\":\"china\",\"c9\":\"中国\",\"longitude\":114.109,\"c3\":\"深圳\",\"c11\":\"0755\",\"c16\":\"AZ9755\",\"c10\":\"2\"},\"f7\":{\"day_wind_power\":\"0-3级 <5.4m/s\",\"night_wind_power\":\"0-3级 <5.4m/s\",\"night_weather_code\":\"07\",\"day_weather\":\"多云\",\"sun_begin_end\":\"06:13|18:17\",\"air_press\":\"1012 hPa\",\"index\":{\"yh\":{\"desc\":\"天气较好，适宜约会\",\"title\":\"较适宜\"},\"zs\":{\"desc\":\"气温较高，易中暑人群注意阴凉休息。\",\"title\":\"可能中暑\"},\"cl\":{\"desc\":\"天气炎热，建议停止户外运动。\",\"title\":\"较不宜\"},\"travel\":{\"desc\":\"天气很热，如外出可选择水上娱乐项目。\",\"title\":\"较不宜\"},\"ag\":{\"desc\":\"天气条件易诱发过敏，易过敏人群应减少外出，外出宜穿长衣长裤并佩戴好眼镜和口罩，外出归来时及时清洁手和口鼻。\",\"title\":\"易发\"},\"beauty\":{\"desc\":\"请选用露质面霜打底，水质无油粉底霜。\",\"title\":\"去油\"},\"pj\":{\"desc\":\"炎热干燥，适宜饮用冰镇啤酒，不要贪杯。\",\"title\":\"适宜\"},\"dy\":{\"desc\":\"天气稍热会对垂钓产生一定影响。\",\"title\":\"较适宜\"},\"nl\":{\"desc\":\"天气较好，虽然有点风，但仍比较适宜夜生活，您可以放心外出。\",\"title\":\"较适宜\"},\"pk\":{\"desc\":\"天气酷热，不适宜放风筝。\",\"title\":\"不宜\"},\"uv\":{\"desc\":\"辐射较弱，涂擦SPF12-15、PA+护肤品\",\"title\":\"弱\"},\"aqi\":{\"desc\":\"空气很好，可以外出活动，呼吸新鲜空气\",\"title\":\"优质\"},\"gj\":{\"desc\":\"这种好天气去逛街可使身心畅快放松。\",\"title\":\"适宜\"},\"ac\":{\"desc\":\"天气热，到中午的时候您将会感到有点热，因此建议在午后较热时开启制冷空调。\",\"title\":\"部分时间开启\"},\"mf\":{\"desc\":\"头发需要保持清洁，同时要注意防晒，含防晒成分洗发护发品是很好的选择。\",\"title\":\"一般\"},\"ls\":{\"desc\":\"天气阴沉，不太适宜晾晒。\",\"title\":\"不适宜\"},\"glass\":{\"desc\":\"不需要佩戴\",\"title\":\"不需要\"},\"xq\":{\"desc\":\"天气热，容易烦躁\",\"title\":\"较差\"},\"clothes\":{\"desc\":\"适合穿T恤、短薄外套等夏季服装\",\"title\":\"热\"},\"sports\":{\"desc\":\"天气炎热，建议停止户外运动。\",\"title\":\"较不宜\"},\"hc\":{\"desc\":\"天气较好，适宜划船及嬉玩水上运动。\",\"title\":\"适宜\"},\"comfort\":{\"desc\":\"偏热或较热，部分人感觉不舒适\",\"title\":\"较差\"},\"wash_car\":{\"desc\":\"有雨，雨水和泥水会弄脏爱车\",\"title\":\"不宜\"},\"cold\":{\"desc\":\"感冒机率较低，避免长期处于空调屋中。\",\"title\":\"少发\"}},\"day_weather_code\":\"01\",\"night_weather\":\"小雨\",\"night_weather_pic\":\"http://app1.showapi.com/weather/icon/night/07.png\",\"day\":\"20180926\",\"day_wind_direction\":\"无持续风向\",\"jiangshui\":\"6%\",\"day_air_temperature\":\"30\",\"night_wind_direction\":\"无持续风向\",\"weekday\":3,\"ziwaixian\":\"弱\",\"night_air_temperature\":\"26\",\"day_weather_pic\":\"http://app1.showapi.com/weather/icon/day/01.png\",\"3hourForcast\":[{\"temperature\":\"25\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"25\",\"hour\":\"8时-14时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/01.png\",\"temperature_max\":\"29\",\"wind_power\":\"<3级,2\",\"weather\":\"多云\"},{\"temperature\":\"29\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"25\",\"hour\":\"14时-20时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/01.png\",\"temperature_max\":\"29\",\"wind_power\":\"<3级,2\",\"weather\":\"多云\"},{\"temperature\":\"27\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"27\",\"hour\":\"20时-2时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/night/01.png\",\"temperature_max\":\"29\",\"wind_power\":\"<3级,0\",\"weather\":\"多云\"},{\"temperature\":\"27\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"27\",\"hour\":\"2时-8时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/night/01.png\",\"temperature_max\":\"27\",\"wind_power\":\"<3级,0\",\"weather\":\"多云\"}]},\"now\":{\"wind_direction\":\"西南风\",\"aqi\":\"45\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/01.png\",\"wind_power\":\"2级\",\"temperature_time\":\"16:00\",\"weather_code\":\"01\",\"temperature\":\"31\",\"sd\":\"69%\",\"aqiDetail\":{\"quality\":\"优质\",\"aqi\":\"45\",\"pm10\":\"48\",\"area\":\"深圳\",\"co\":\"0.642\",\"o3\":\"124\",\"so2\":\"9\",\"no2\":\"21\",\"primary_pollutant\":\"\",\"o3_8h\":\"64\",\"num\":\"231\",\"pm2_5\":\"30\"},\"weather\":\"多云\"},\"alarmList\":[{\"issueTime\":\"2018-09-19 18:26:18\",\"city\":\"深圳市\",\"issueContent\":\"深圳市气象局于09月19日18时26分发布深圳市暴雨橙色预警信号，请注意防御。（预警信息来源：国家预警信息发布中心）\",\"province\":\"广东省\",\"signalLevel\":\"橙色\",\"signalType\":\"暴雨\"}],\"f3\":{\"day_wind_power\":\"0-3级 <5.4m/s\",\"night_wind_power\":\"0-3级 <5.4m/s\",\"night_weather_code\":\"03\",\"day_weather\":\"阵雨\",\"sun_begin_end\":\"06:12|18:21\",\"air_press\":\"1012 hPa\",\"index\":{\"yh\":{\"desc\":\"建议尽量不要去室外约会。\",\"title\":\"较不适宜\"},\"zs\":{\"desc\":\"气温较高，易中暑人群注意阴凉休息。\",\"title\":\"可能中暑\"},\"cl\":{\"desc\":\"有降水,推荐您在室内进行休闲运动。\",\"title\":\"较不宜\"},\"travel\":{\"desc\":\"有降水气温高，注意防暑降温携带雨具。\",\"title\":\"较不宜\"},\"ag\":{\"desc\":\"天气条件易诱发过敏，易过敏人群应减少外出，外出宜穿长衣长裤并佩戴好眼镜和口罩，外出归来时及时清洁手和口鼻。\",\"title\":\"易发\"},\"beauty\":{\"desc\":\"请选用保湿型霜类化妆品。\",\"title\":\"保湿\"},\"pj\":{\"desc\":\"天热潮湿，可饮用清凉的啤酒，不要贪杯。\",\"title\":\"适宜\"},\"dy\":{\"desc\":\"天气不好，不适合垂钓\",\"title\":\"不适宜钓鱼\"},\"nl\":{\"desc\":\"天气较好，虽然有点风，但仍比较适宜夜生活，您可以放心外出。\",\"title\":\"较适宜\"},\"pk\":{\"desc\":\"天气酷热，不适宜放风筝。\",\"title\":\"不宜\"},\"uv\":{\"desc\":\"涂擦SPF大于15、PA+防晒护肤品\",\"title\":\"中等\"},\"aqi\":{\"desc\":\"空气很好，可以外出活动，呼吸新鲜空气\",\"title\":\"优质\"},\"gj\":{\"desc\":\"有降水，出门带雨具并注意防雷。\",\"title\":\"不适宜\"},\"ac\":{\"desc\":\"天气热，到中午的时候您将会感到有点热，因此建议在午后较热时开启制冷空调。\",\"title\":\"部分时间开启\"},\"mf\":{\"desc\":\"头发需要保持清洁，同时要注意防晒，含防晒成分洗发护发品是很好的选择。\",\"title\":\"一般\"},\"ls\":{\"desc\":\"有降水会淋湿衣物，不适宜晾晒。\",\"title\":\"不适宜\"},\"glass\":{\"desc\":\"需要佩戴\",\"title\":\"需要\"},\"xq\":{\"desc\":\"天气热，容易烦躁\",\"title\":\"较差\"},\"clothes\":{\"desc\":\"适合穿T恤、短薄外套等夏季服装\",\"title\":\"热\"},\"sports\":{\"desc\":\"有降水,推荐您在室内进行休闲运动。\",\"title\":\"较不宜\"},\"hc\":{\"desc\":\"天气不好，建议选择别的娱乐方式。\",\"title\":\"不适宜\"},\"comfort\":{\"desc\":\"偏热或较热，部分人感觉不舒适\",\"title\":\"较差\"},\"wash_car\":{\"desc\":\"有雨，雨水和泥水会弄脏爱车\",\"title\":\"不宜\"},\"cold\":{\"desc\":\"感冒机率较低，避免长期处于空调屋中。\",\"title\":\"少发\"}},\"day_weather_code\":\"03\",\"night_weather\":\"阵雨\",\"night_weather_pic\":\"http://app1.showapi.com/weather/icon/night/03.png\",\"day\":\"20180922\",\"day_wind_direction\":\"无持续风向\",\"jiangshui\":\"76%\",\"day_air_temperature\":\"31\",\"night_wind_direction\":\"无持续风向\",\"weekday\":6,\"ziwaixian\":\"中等\",\"night_air_temperature\":\"25\",\"day_weather_pic\":\"http://app1.showapi.com/weather/icon/day/03.png\",\"3hourForcast\":[{\"temperature\":\"27\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"26\",\"hour\":\"8时-11时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/03.png\",\"temperature_max\":\"29\",\"wind_power\":\"<3级,3\",\"weather\":\"阵雨\"},{\"temperature\":\"29\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"27\",\"hour\":\"11时-14时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/02.png\",\"temperature_max\":\"30\",\"wind_power\":\"<3级,3\",\"weather\":\"阴\"},{\"temperature\":\"30\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"29\",\"hour\":\"14时-17时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/01.png\",\"temperature_max\":\"30\",\"wind_power\":\"<3级,2\",\"weather\":\"多云\"},{\"temperature\":\"29\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"26\",\"hour\":\"17时-20时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/03.png\",\"temperature_max\":\"30\",\"wind_power\":\"<3级,3\",\"weather\":\"阵雨\"},{\"temperature\":\"26\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"25\",\"hour\":\"20时-23时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/night/01.png\",\"temperature_max\":\"29\",\"wind_power\":\"<3级,0\",\"weather\":\"多云\"},{\"temperature\":\"25\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"25\",\"hour\":\"23时-2时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/night/01.png\",\"temperature_max\":\"26\",\"wind_power\":\"<3级,0\",\"weather\":\"多云\"},{\"temperature\":\"25\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"25\",\"hour\":\"2时-5时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/night/01.png\",\"temperature_max\":\"25\",\"wind_power\":\"<3级,0\",\"weather\":\"多云\"},{\"temperature\":\"25\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"25\",\"hour\":\"5时-8时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/night/03.png\",\"temperature_max\":\"26\",\"wind_power\":\"<3级,0\",\"weather\":\"阵雨\"}]},\"f1\":{\"day_wind_power\":\"0-3级 <5.4m/s\",\"night_wind_power\":\"0-3级 <5.4m/s\",\"night_weather_code\":\"01\",\"day_weather\":\"多云\",\"sun_begin_end\":\"06:11|18:23\",\"air_press\":\"1012 hPa\",\"index\":{\"yh\":{\"desc\":\"天气较好，适宜约会\",\"title\":\"较适宜\"},\"zs\":{\"desc\":\"气温较高，易中暑人群注意阴凉休息。\",\"title\":\"可能中暑\"},\"cl\":{\"desc\":\"天气炎热，建议停止户外运动。\",\"title\":\"较不宜\"},\"travel\":{\"desc\":\"天气很热，如外出可选择水上娱乐项目。\",\"title\":\"较不宜\"},\"ag\":{\"desc\":\"天气条件易诱发过敏，易过敏人群应减少外出，外出宜穿长衣长裤并佩戴好眼镜和口罩，外出归来时及时清洁手和口鼻。\",\"title\":\"易发\"},\"beauty\":{\"desc\":\"请选用露质面霜打底，水质无油粉底霜。\",\"title\":\"去油\"},\"pj\":{\"desc\":\"炎热干燥，适宜饮用冰镇啤酒，不要贪杯。\",\"title\":\"适宜\"},\"dy\":{\"desc\":\"天气太热,不适宜垂钓。\",\"title\":\"不适宜钓鱼\"},\"nl\":{\"desc\":\"天气较好，虽然有点风，但仍比较适宜夜生活，您可以放心外出。\",\"title\":\"较适宜\"},\"pk\":{\"desc\":\"天气酷热，不适宜放风筝。\",\"title\":\"不宜\"},\"uv\":{\"desc\":\"涂擦SPF大于15、PA+防晒护肤品\",\"title\":\"中等\"},\"aqi\":{\"desc\":\"空气很好，可以外出活动，呼吸新鲜空气\",\"title\":\"优质\"},\"gj\":{\"desc\":\"这种好天气去逛街可使身心畅快放松。\",\"title\":\"适宜\"},\"ac\":{\"desc\":\"天气热，到中午的时候您将会感到有点热，因此建议在午后较热时开启制冷空调。\",\"title\":\"部分时间开启\"},\"mf\":{\"desc\":\"头发需要保持清洁，同时要注意防晒，含防晒成分洗发护发品是很好的选择。\",\"title\":\"一般\"},\"ls\":{\"desc\":\"天气阴沉，不太适宜晾晒。\",\"title\":\"不适宜\"},\"glass\":{\"desc\":\"需要佩戴\",\"title\":\"需要\"},\"xq\":{\"desc\":\"天气热，容易烦躁\",\"title\":\"较差\"},\"clothes\":{\"desc\":\"建议穿短衫、短裤等清凉夏季服装\",\"title\":\"炎热\"},\"sports\":{\"desc\":\"天气炎热，建议停止户外运动。\",\"title\":\"较不宜\"},\"hc\":{\"desc\":\"天气较好，适宜划船及嬉玩水上运动。\",\"title\":\"适宜\"},\"comfort\":{\"desc\":\"偏热或较热，部分人感觉不舒适\",\"title\":\"较差\"},\"wash_car\":{\"desc\":\"无雨且风力较小，易保持清洁度\",\"title\":\"较适宜\"},\"cold\":{\"desc\":\"感冒机率较低，避免长期处于空调屋中。\",\"title\":\"少发\"}},\"day_weather_code\":\"01\",\"night_weather\":\"多云\",\"night_weather_pic\":\"http://app1.showapi.com/weather/icon/night/01.png\",\"day\":\"20180920\",\"day_wind_direction\":\"无持续风向\",\"jiangshui\":\"15%\",\"day_air_temperature\":\"32\",\"night_wind_direction\":\"无持续风向\",\"weekday\":4,\"ziwaixian\":\"中等\",\"night_air_temperature\":\"25\",\"day_weather_pic\":\"http://app1.showapi.com/weather/icon/day/01.png\",\"3hourForcast\":[{\"temperature\":\"28\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"28\",\"hour\":\"8时-11时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/07.png\",\"temperature_max\":\"31\",\"wind_power\":\"<3级,3\",\"weather\":\"小雨\"},{\"temperature\":\"31\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"28\",\"hour\":\"11时-14时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/01.png\",\"temperature_max\":\"31\",\"wind_power\":\"<3级,2\",\"weather\":\"多云\"},{\"temperature\":\"31\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"30\",\"hour\":\"14时-17时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/01.png\",\"temperature_max\":\"31\",\"wind_power\":\"<3级,2\",\"weather\":\"多云\"},{\"temperature\":\"30\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"28\",\"hour\":\"17时-20时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/day/01.png\",\"temperature_max\":\"31\",\"wind_power\":\"<3级,2\",\"weather\":\"多云\"},{\"temperature\":\"28\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"27\",\"hour\":\"20时-23时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/night/01.png\",\"temperature_max\":\"30\",\"wind_power\":\"<3级,0\",\"weather\":\"多云\"},{\"temperature\":\"27\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"26\",\"hour\":\"23时-2时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/night/01.png\",\"temperature_max\":\"28\",\"wind_power\":\"<3级,0\",\"weather\":\"多云\"},{\"temperature\":\"26\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"26\",\"hour\":\"2时-5时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/night/01.png\",\"temperature_max\":\"27\",\"wind_power\":\"<3级,0\",\"weather\":\"多云\"},{\"temperature\":\"26\",\"wind_direction\":\"无持续风向\",\"temperature_min\":\"26\",\"hour\":\"5时-8时\",\"weather_pic\":\"http://app1.showapi.com/weather/icon/night/01.png\",\"temperature_max\":\"28\",\"wind_power\":\"<3级,0\",\"weather\":\"多云\"}]},\"ret_code\":0}}";
         WeatherBean weather = Utility.handleWeatherResponse(res2);
         showWeatherInfo(weather);
    }
}
