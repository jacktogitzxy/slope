package com.zig.slope.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

/**
 * Created by 17120 on 2018/10/8.
 */

public  class ToolUtils {
    public static final String APP_BAIDU_MAP = "com.baidu.BaiduMap";
    public static final String APP_AMAP = "com.autonavi.minimap";
    /**
     * 检测是否有某个应用
     * */
    public static boolean hasApp(Context ctx, String packageName) {
        PackageManager manager = ctx.getPackageManager();
        List<PackageInfo> apps = manager.getInstalledPackages(0);
        if (apps != null) {
            for (int i = 0; i < apps.size(); i++) {
                if (apps.get(i).packageName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static  String exchangeString(String old){
        //000000---00:00:00
        StringBuffer stringBuffer = new StringBuffer(old);
        stringBuffer.insert(2, ":");
        stringBuffer.insert(5, ":");
        return stringBuffer.toString();
    }
}
