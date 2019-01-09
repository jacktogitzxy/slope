package com.zig.slope.model;

import android.content.Context;
import android.util.Log;

import com.zig.slope.api.ConstructionApi;
import com.zig.slope.api.HisApi;
import com.zig.slope.api.ProcessApi;
import com.zig.slope.api.SewageApi;
import com.zig.slope.api.SubsidenceApi;
import com.zig.slope.api.ThreeDefenseApi;
import com.zig.slope.bean.DiXian;
import com.zig.slope.bean.GongDi;
import com.zig.slope.bean.PaiWu;
import com.zig.slope.bean.SanFan;
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

    /**
     * 请求三防列表
     */
    @Override
    public void getThreeDefenseDatas(Context context,  final ProcessContract.IProcessModelCallbacksf callback) {

        RxRetrofitManager.getInstance()
                .setTag("ThreeDefense")
                .getApiService(ThreeDefenseApi.class)
                .ThreeDefenseApp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( new RxObserver<BaseResponseBean<List<SanFan>>>(context, true) {
                    @Override
                    public void onSuccess(BaseResponseBean<List<SanFan>> BaseResponseBean) {
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

    /**
     * 请求工地列表
     */
    @Override
    public void getConstructionDatas(Context context,  final ProcessContract.IProcessModelCallbackgd callback) {

        RxRetrofitManager.getInstance()
                .setTag("Construction")
                .getApiService(ConstructionApi.class)
                .ConstructionApp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( new RxObserver<BaseResponseBean<List<GongDi>>>(context, true) {
                    @Override
                    public void onSuccess(BaseResponseBean<List<GongDi>> BaseResponseBean) {
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
    public void getSubsidenceDatas(Context context, final ProcessContract.IProcessModelCallbackdx callback) {
        RxRetrofitManager.getInstance()
                .setTag("Subsidence")
                .getApiService(SubsidenceApi.class)
                .SubsidenceApp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( new RxObserver<BaseResponseBean<List<DiXian>>>(context, true) {
                    @Override
                    public void onSuccess(BaseResponseBean<List<DiXian>> BaseResponseBean) {
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
    public void getSewageDatas(Context context, final ProcessContract.IProcessModelCallbackpw callback) {
        RxRetrofitManager.getInstance()
                .setTag("Sewage")
                .getApiService(SewageApi.class)
                .SewageApp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( new RxObserver<BaseResponseBean<List<PaiWu>>>(context, true) {
                    @Override
                    public void onSuccess(BaseResponseBean<List<PaiWu>> BaseResponseBean) {
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
        ApiCancleManager.getInstance().cancel("getProcessDatas");
        ApiCancleManager.getInstance().cancel("ThreeDefense");
        ApiCancleManager.getInstance().cancel("Construction");
        ApiCancleManager.getInstance().cancel("Subsidence");
        ApiCancleManager.getInstance().cancel("Sewage");
    }


}
