package com.zig.slope.api;


import com.zig.slope.bean.GongDi;
import com.zig.slope.common.base.bean.BaseResponseBean;
import java.util.List;
import io.reactivex.Observable;
import retrofit2.http.POST;
/**
 * Author：CHENHAO
 * date：2018/5/4
 * desc：
 */

public interface ConstructionApi {
    /**
     *工地列表
     */
    @POST("queryConstructionInfoApp")
    Observable<BaseResponseBean<List<GongDi>>> ConstructionApp();
}
