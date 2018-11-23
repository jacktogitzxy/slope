package com.zig.slope.model;

import android.content.Context;
import android.util.Log;

import com.zig.slope.api.HisApi;
import com.zig.slope.common.base.bean.BaseResponseBean;
import com.zig.slope.common.base.bean.HisBean;
import com.zig.slope.common.base.bean.HisReport;
import com.zig.slope.common.base.bean.LoginMsg;
import com.zig.slope.common.http.RxObserver;
import com.zig.slope.common.http.RxRetrofitManager;
import com.zig.slope.common.http.cancle.ApiCancleManager;
import com.zig.slope.contract.HisContract;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import slope.zxy.com.login_module.api.LoginApi;

/**
 * Author：CHENHAO
 * date：2018/5/3
 * desc：
 */

public class HisModel implements HisContract.HisModel {
    /**
     * 请求banner图片以及文章列表数据
     */
    @Override
    public void getHisDatas(Context context, String admin,int page,int types, final HisContract.IHisModelCallback callback,String meths) {

        RxRetrofitManager.getInstance()
                .setTag(meths)
                .getApiService(HisApi.class)
                .getHisReport(meths,page,admin,types)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( new RxObserver<BaseResponseBean<HisBean>>(context, true) {
                    @Override
                    public void onSuccess(BaseResponseBean<HisBean> BaseResponseBean) {
                        if (BaseResponseBean.getCode() >= 0&&BaseResponseBean.getData()!=null){
                            if (callback != null){
                                callback.onSuccess(BaseResponseBean);
                            }
                        }else {
                            if (callback != null){
                                callback.onFail(BaseResponseBean.getInfo());
                            }
                        }
                    }
                });
    }

    @Override
    public void cancleHttpRequest() {
        ApiCancleManager.getInstance().cancel("hisReport");
    }


}
