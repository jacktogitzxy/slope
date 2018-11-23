package com.zig.slope.api;


import com.zig.slope.bean.SanFan;
import com.zig.slope.common.base.bean.BaseResponseBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.POST;

/**
 * Author：CHENHAO
 * date：2018/5/4
 * desc：
 */

public interface ThreeDefenseApi {
    /**
     *三防列表
     */
    @POST("queryThreeDefensePageApp")
    Observable<BaseResponseBean<List<SanFan>>> ThreeDefenseApp();
}
