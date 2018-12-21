package com.zig.slope.App;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.zig.slope.LocationdrawActivity;
import com.zig.slope.R;
import com.zig.slope.common.base.BaseApplication;
import com.zig.slope.common.utils.Utils;

//import org.easydarwin.easyplayer.data.EasyDBHelper;
import org.xutils.x;

import cn.jpush.android.api.JPushInterface;

/**
 * Author：CHENHAO
 * date：2018/5/7
 * desc：
 */

public class MyApplication extends BaseApplication {
    public static String sPicturePath;
    public static String sMoviePath;

    @SuppressLint("NewApi")
    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        // for compatibility
        Bugly.init(getApplicationContext(), "c5379a7e5e", false);
        Beta.checkUpgrade();
//        Beta.autoInit = true;//自动初始化开关,true表示app启动自动初始化升级模块; false不会自动初始化; 开发者如果担心sdk初始化影响app启动速度，可以设置为false，在后面某个时刻手动调用Beta.init(getApplicationContext(),false);
//        Beta.autoCheckUpgrade = true;//true表示初始化时自动检查升级; false表示不会自动检查升级,需要手动调用Beta.checkUpgrade()方法;
//        Beta.upgradeCheckPeriod = 60 * 1000;//设置升级检查周期为60s(默认检查周期为0s)，60s内SDK不重复向后台请求策略);
//        Beta.initDelay = 1 * 1000;//设置启动延时为1s（默认延时3s），APP启动1s后初始化SDK，避免影响APP启动速度;
//        Beta.largeIconId = R.drawable.ic_launcher;//设置通知栏大图标,largeIconId为项目中的图片资源;
//        Beta.smallIconId = R.drawable.ic_launcher;//设置状态栏小图标
//        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);//设置sd卡的Download为更新资源存储目录
//        Beta.canShowUpgradeActs.add(LocationdrawActivity.class);//添加可显示弹窗的Activity,例如，只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗; 如果不设置默认所有activity都可以显示弹窗。
//        Beta.autoDownloadOnWifi = true;//设置Wifi下自动下载,默认false
//        Beta.enableHotfix = true;//升级SDK默认是开启热更新能力的，如果你不需要使用热更新，可以将这个接口设置为false。
     //   Beta.checkUpgrade(false,false);
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
//        TypefaceProvider.registerDefaultIconSets();
        //突破65535的限制
        MultiDex.install(this);
        //ARouter配置
        if (Utils.isDebug()) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
        x.Ext.init(this);

        sPicturePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/EasyPlayer";
        sMoviePath = getExternalFilesDir(Environment.DIRECTORY_MOVIES) + "/EasyPlayer";

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
