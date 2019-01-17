package com.zig.slope.api;


import com.zig.slope.bean.WeiFang;
import com.zig.slope.bean.WeiFangBean;
import com.zig.slope.common.base.bean.BaseResponseBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Author：CHENHAO
 * date：2018/5/4
 * desc：
 */

public interface DangerousApi {
    /**
     *危房列表
     */
    @POST("queryDangerousApp")
    Observable<BaseResponseBean<WeiFangBean>> DangerousApp(@Query("currentPage") int page, @Query("community") int community);
}
