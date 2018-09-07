package com.zig.slope.api;


import com.zig.slope.common.base.bean.BaseResponseBean;
import com.zig.slope.common.base.bean.HisBean;
import com.zig.slope.common.base.bean.ProcessBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Author：CHENHAO
 * date：2018/5/4
 * desc：
 */

public interface ProcessApi {
    /**
     *治理进度列表
     */
    @POST("querySlopProgressApp")
    Observable<BaseResponseBean<List<ProcessBean>>> SlopProgressApp();

}
