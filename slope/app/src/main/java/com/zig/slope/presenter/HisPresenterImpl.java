package com.zig.slope.presenter;

import android.content.Context;
import android.util.Log;

import com.zig.slope.common.base.BasePresenter;
import com.zig.slope.common.base.bean.BaseResponseBean;
import com.zig.slope.common.base.bean.HisReport;
import com.zig.slope.common.base.bean.LoginMsg;
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
    public void requestLoginData(Context context,String admin){
        model.getHisDatas(context,admin,new HisContract.IHisModelCallback() {
            @Override
            public void onSuccess(BaseResponseBean<List<HisReport>> response) {
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
        });
    }

    /**
     * 取消请求
     */
    public void cancleHttpRequest(){
        model.cancleHttpRequest();
    }
}
