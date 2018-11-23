package com.zig.slope.presenter;

import android.content.Context;
import android.util.Log;

import com.zig.slope.common.base.BasePresenter;
import com.zig.slope.common.base.bean.BaseResponseBean;
import com.zig.slope.common.base.bean.HisBean;
import com.zig.slope.contract.HisContract;
import com.zig.slope.model.HisModel;

import java.util.List;

/**
 * Author：CHENHAO
 * date：2018/5/3
 * desc：
 */

public class HisPresenterImpl extends BasePresenter<HisContract.HisReportView> {
    private HisModel model;

    public HisPresenterImpl(){
        model = new HisModel();
    }

    /**
     * 请求巡查历史
     */
    public void requestHisData(Context context,String admin,int page,String meths,int types){
        model.getHisDatas(context,admin,page,types,new HisContract.IHisModelCallback() {
            @Override
            public void onSuccess(BaseResponseBean<HisBean> response) {
                if (getMvpView() != null) {
                    Log.i("zxy", "onSuccess: "+response.getData());
                    getMvpView().onHisSucess(response.getData());
                }
            }

            @Override
            public void onFail(String msg) {
                if (getMvpView() != null) {
                    getMvpView().onHisFail(msg);
                }
            }
        },meths);
    }

    /**
     * 取消请求
     */
    public void cancleHttpRequest(){
        model.cancleHttpRequest();
    }
}
