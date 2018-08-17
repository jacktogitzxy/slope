package com.zig.slope.api;


import com.zig.slope.common.base.bean.BaseResponseBean;
import com.zig.slope.common.base.bean.HisReport;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Author：CHENHAO
 * date：2018/5/4
 * desc：
 */

public interface HisApi {
    /**
     *登录数据
     */
    @POST("queryInspectionResultsApp")
    Observable<BaseResponseBean<List<HisReport>>> getHisReport(@Query("patrollerID") String admin);

}
