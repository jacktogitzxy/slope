package com.zig.slope.model;

import android.content.Context;

import com.zig.slope.api.SensorApi;
import com.zig.slope.common.base.bean.BaseResponseBean;
import com.zig.slope.common.base.bean.DataBean;
import com.zig.slope.common.http.RxObserver;
import com.zig.slope.common.http.RxRetrofitManager;
import com.zig.slope.common.http.cancle.ApiCancleManager;
import com.zig.slope.contract.SensorContract;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Author：CHENHAO
 * date：2018/5/3
 * desc：
 */

public class SensorModel implements SensorContract.SensorModel {
    /**
     * 请求Sensor列表
     */
    @Override
    public void getSensorDatas(Context context,String newName,  final SensorContract.ISensorModelCallback callback) {

        RxRetrofitManager.getInstance()
                .setTag("getSensorDatas")
                .getApiService(SensorApi.class)
                .getSensorData(newName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( new RxObserver<BaseResponseBean<List<DataBean>>>(context, true) {
                    @Override
                    public void onSuccess(BaseResponseBean<List<DataBean>> BaseResponseBean) {
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
    public void getSensorForcastDatas(Context context, String newName, final SensorContract.ISensorModelCallback callback) {
        RxRetrofitManager.getInstance()
                .setTag("getSensorDatas")
                .getApiService(SensorApi.class)
                .getSensorForcastData(newName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( new RxObserver<BaseResponseBean<List<DataBean>>>(context, true) {
                    @Override
                    public void onSuccess(BaseResponseBean<List<DataBean>> BaseResponseBean) {
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
        ApiCancleManager.getInstance().cancel("getSensorDatas");
    }


}
