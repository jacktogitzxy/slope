package com.zig.slope.api;


import com.zig.slope.common.base.bean.BaseResponseBean;
import com.zig.slope.common.base.bean.DataBean;
import java.util.List;
import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Author：CHENHAO
 * date：2018/5/4
 * desc：
 */

public interface SensorApi {
    /**
     *治理进度列表
     */
    @POST("querySlopeSensorDataByNewNameApp")
    Observable<BaseResponseBean<List<DataBean>>> getSensorData(@Query("newName") String id);
}
