package com.zig.slope.presenter;

import android.content.Context;
import android.util.Log;

import com.zig.slope.bean.DiXian;
import com.zig.slope.bean.GongDi;
import com.zig.slope.bean.PaiWu;
import com.zig.slope.bean.SanFan;
import com.zig.slope.common.base.BasePresenter;
import com.zig.slope.common.base.bean.BaseResponseBean;
import com.zig.slope.common.base.bean.ProcessBean;
import com.zig.slope.contract.ProcessContract;
import com.zig.slope.model.ProcessModel;

import java.util.List;

/**
 * Author：CHENHAO
 * date：2018/5/3
 * desc：
 */

public class ProcessPresenterImpl extends BasePresenter<ProcessContract.ProcessView> {
    private ProcessModel model;

    public ProcessPresenterImpl(){
        model = new ProcessModel();
    }

    /**
     * 请求巡查历史
     */
    public void requestProcessData(Context context){
        model.getProcessDatas(context,new ProcessContract.IProcessModelCallback() {
            @Override
            public void onSuccess(BaseResponseBean<List<ProcessBean>> response) {
                if (getMvpView() != null) {
                    getMvpView().onProcessSucess(response.getData());
                }
            }

            @Override
            public void onFail(String msg) {
                if (getMvpView() != null) {
                    getMvpView().onProcessFail(msg);
                }
            }
        });
    }
    /**
     * 请求三防
     */
    public void requestSFData(Context context){
        model.getThreeDefenseDatas(context,new ProcessContract.IProcessModelCallbacksf() {
            @Override
            public void onSuccess(BaseResponseBean<List<SanFan>> response) {
                if (getMvpView() != null) {
                    getMvpView().onThreeDefenseSucess(response.getData());
                }
            }

            @Override
            public void onFail(String msg) {
                if (getMvpView() != null) {
                    getMvpView().onThreeDefenseFail(msg);
                }
            }
        });
    }
    /**
     * 请求工地
     */
    public void requesGDData(Context context){
        model.getConstructionDatas(context,new ProcessContract.IProcessModelCallbackgd() {
            @Override
            public void onSuccess(BaseResponseBean<List<GongDi>> response) {
                if (getMvpView() != null) {
                    getMvpView().onConstructionSucess(response.getData());
                }
            }

            @Override
            public void onFail(String msg) {
                if (getMvpView() != null) {
                    getMvpView().onConstructionFail(msg);
                }
            }
        });
    }

    /**
     * 请求地陷
     */
    public void requesDXData(Context context){
        model.getSubsidenceDatas(context,new ProcessContract.IProcessModelCallbackdx() {
            @Override
            public void onSuccess(BaseResponseBean<List<DiXian>> response) {
                if (getMvpView() != null) {
                    getMvpView().onSubsidenceSucess(response.getData());
                }
            }

            @Override
            public void onFail(String msg) {
                if (getMvpView() != null) {
                    getMvpView().onSubsidenceFail(msg);
                }
            }
        });
    }

    /**
     * 请求排污
     */
    public void requesHDData(Context context){
        model.getSewageDatas(context,new ProcessContract.IProcessModelCallbackpw() {
            @Override
            public void onSuccess(BaseResponseBean<List<PaiWu>> response) {
                if (getMvpView() != null) {
                    getMvpView().onSewageSucess(response.getData());
                }
            }

            @Override
            public void onFail(String msg) {
                if (getMvpView() != null) {
                    getMvpView().onSewageFail(msg);
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
