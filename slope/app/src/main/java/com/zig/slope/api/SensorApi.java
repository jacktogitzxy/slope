package com.zig.slope.api;


import com.zig.slope.bean.DataWarnBean;
import com.zig.slope.common.base.bean.BaseResponseBean;
import com.zig.slope.common.base.bean.DataBean;
import java.util.List;
import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Author：zxy
 * date：2018/5/4
 * desc：
 */

public interface SensorApi {
    /**
     *传感器api
     */
    @POST("querySlopeSensorDataByNewNameApp")
    Observable<BaseResponseBean<List<DataBean>>> getSensorData(@Query("newName") String id);
    @POST("querySlopeForcastLastDataLogApp")
    Observable<BaseResponseBean<List<DataWarnBean>>> getSensorForcastData(@Query("newName") String id);
}
