package com.zig.slope.model;

import android.content.Context;
import android.util.Log;

import com.zig.slope.api.HisApi;
import com.zig.slope.common.base.bean.BaseResponseBean;
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
    public void getHisDatas(Context context, String admin, final HisContract.IHisModelCallback callback) {
        Log.i("zxy", "-----------------getHisDatas: ");
        RxRetrofitManager.getInstance()
                .setTag("hisReport")
                .getApiService(HisApi.class)
                .getHisReport(admin)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( new RxObserver<BaseResponseBean<List<HisReport>>>(context, true) {
                    @Override
                    public void onSuccess(BaseResponseBean<List<HisReport>> listBaseResponseBean) {
                        if (listBaseResponseBean.getCode() >= 0&&listBaseResponseBean.getData()!=null){
                            if (callback != null){
                                Log.i("zxy", "accept: listBaseResponseBean=="+listBaseResponseBean.getInfo());
                                callback.onSuccess(listBaseResponseBean);
                            }
                        }else {
                            if (callback != null){
                                Log.i("zxy", "accept: listBaseResponseBean=="+listBaseResponseBean.getInfo());
                                callback.onFail(listBaseResponseBean.getInfo());
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
