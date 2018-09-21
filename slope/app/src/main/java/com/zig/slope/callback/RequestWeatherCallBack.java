package com.zig.slope.callback;

import com.zig.slope.bean.UserLoacl;

import java.util.List;

import slope.zxy.com.weather_moudle.bean.WeatherBean;

/**
 * Created by 17120 on 2018/8/31.
 */

public interface RequestWeatherCallBack {
    void onSuccess(WeatherBean response);
    void onFail(String msg);
}
