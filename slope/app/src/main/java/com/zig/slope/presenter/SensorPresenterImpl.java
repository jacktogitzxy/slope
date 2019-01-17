package com.zig.slope.presenter;

import android.content.Context;

import com.zig.slope.bean.DataWarnBean;
import com.zig.slope.common.base.BasePresenter;
import com.zig.slope.common.base.bean.BaseResponseBean;
import com.zig.slope.common.base.bean.DataBean;
import com.zig.slope.contract.SensorContract;
import com.zig.slope.model.SensorModel;

import java.util.List;

/**
 * Author：CHENHAO
 * date：2018/5/3
 * desc：
 */

public class SensorPresenterImpl extends BasePresenter<SensorContract.SensorView> {
    private SensorModel model;

    public SensorPresenterImpl(){
        model = new SensorModel();
    }

    /**
     * 请求监测点数据
     */
    public void requestSensorData(Context context,String newName){
        model.getSensorDatas(context,newName,new SensorContract.ISensorModelCallback() {
            @Override
            public void onSuccess(BaseResponseBean<List<DataBean>> response) {
                if (getMvpView() != null) {
                    getMvpView().onSensorSucess(response);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getMvpView() != null) {
                    getMvpView().onSensorFail(msg);
                }
            }
        });
    }
    /**
     * 请求监测点预警数据
     */
    public void requestSensorForcastData(Context context,String newName){
        model.getSensorForcastDatas(context,newName,new SensorContract.ISensorModelCallbacks() {
            @Override
            public void onSuccess(BaseResponseBean<List<DataWarnBean>> response) {
                if (getMvpView() != null) {
                    getMvpView().onSensorSucessw(response);
                }
            }

            @Override
            public void onFail(String msg) {
                if (getMvpView() != null) {
                    getMvpView().onSensorFailw(msg);
                }
            }
        });
    }

    /**
     * 取消请求
     */
    public void cancleHttpRequest(){
        model.cancleHttpRequest();
    }
}
