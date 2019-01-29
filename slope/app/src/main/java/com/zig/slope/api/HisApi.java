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

public interface HisApi {
    /**
     *巡查数据
     */
    @POST("{app}")
    Observable<BaseResponseBean<HisBean>> getHisReport(@Path("app")String meths, @Query("currentPage") int page, @Query("operatorId") String admin,@Query("type_s") int type_s,@Query("flag") int flag);

    /**
     *管理审核
     */
    @POST("adminShenHeApp ")
    Observable<BaseResponseBean> adminShenHeApp( @Query("id") String id,@Query("operatorName") String operatorName,@Query("contents") String contents,@Query("type_s") int type_s);

    /**
     *领导审核
     */
    @POST("LeaderShenHeApp ")
    Observable<BaseResponseBean> LeaderShenHeApp( @Query("id") String id,@Query("operatorName") String operatorName,@Query("contents") String contents,@Query("type_s") int type_s);


}
