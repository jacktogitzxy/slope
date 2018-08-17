package com.zig.slope.common.http.api;

import com.zig.slope.common.base.bean.BaseResponseBean;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Author：CHENHAO
 * date：2018/5/3
 * desc：
 */

public interface RetrofitServer {
    /**
     * 首页banner图
     */
    @GET("banner/json")
    Observable<BaseResponseBean> getBannerImgs();
}
