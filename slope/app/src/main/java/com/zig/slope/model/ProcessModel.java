package com.zig.slope.model;

import android.content.Context;
import android.util.Log;

import com.zig.slope.api.HisApi;
import com.zig.slope.api.ProcessApi;
import com.zig.slope.common.base.bean.BaseResponseBean;
import com.zig.slope.common.base.bean.HisBean;
import com.zig.slope.common.base.bean.ProcessBean;
import com.zig.slope.common.http.RxObserver;
import com.zig.slope.common.http.RxRetrofitManager;
import com.zig.slope.common.http.cancle.ApiCancleManager;
import com.zig.slope.contract.HisContract;
import com.zig.slope.contract.ProcessContract;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Author：CHENHAO
 * date：2018/5/3
 * desc：
 */

public class ProcessModel implements ProcessContract.ProcessModel {
    /**
     * 请求process列表
     */
    @Override
    public void getProcessDatas(Context context,  final ProcessContract.IProcessModelCallback callback) {

        RxRetrofitManager.getInstance()
                .setTag("getProcessDatas")
                .getApiService(ProcessApi.class)
                 .SlopProgressApp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( new RxObserver<BaseResponseBean<List<ProcessBean>>>(context, true) {
                    @Override
                    public void onSuccess(BaseResponseBean<List<ProcessBean>> BaseResponseBean) {
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
