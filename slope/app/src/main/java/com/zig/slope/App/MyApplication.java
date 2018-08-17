package com.zig.slope.App;

import android.support.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.zig.slope.common.base.BaseApplication;
import com.zig.slope.common.utils.Utils;

import org.xutils.x;

/**
 * Author：CHENHAO
 * date：2018/5/7
 * desc：
 */

public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        // for compatibility
        Bugly.init(getApplicationContext(), "9c4b0e3ce3", false);
        Beta.checkUpgrade(false,false);
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
    }


}
