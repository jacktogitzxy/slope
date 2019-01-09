package com.zig.slope.api;


import com.zig.slope.bean.DiXian;
import com.zig.slope.common.base.bean.BaseResponseBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.POST;

/**
 * Author：CHENHAO
 * date：2018/5/4
 * desc：
 */

public interface SubsidenceApi {
    /**
     *地陷列表
     */
    @POST("querySubsidenceApp")
    Observable<BaseResponseBean<List<DiXian>>> SubsidenceApp();
}
