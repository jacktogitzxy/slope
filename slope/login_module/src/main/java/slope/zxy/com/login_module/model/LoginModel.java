package slope.zxy.com.login_module.model;

import android.content.Context;
import android.util.Log;


import com.google.gson.Gson;
import com.zig.slope.common.base.bean.BaseResponseBean;
import com.zig.slope.common.base.bean.LoginBean;
import com.zig.slope.common.base.bean.LoginMsg;
import com.zig.slope.common.base.bean.SlopeBean;
import com.zig.slope.common.http.RxObserver;
import com.zig.slope.common.http.RxRetrofitManager;
import com.zig.slope.common.http.cancle.ApiCancleManager;


import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import slope.zxy.com.login_module.BannerBean;
import slope.zxy.com.login_module.api.LoginApi;
import slope.zxy.com.login_module.contract.LoginContract;
import io.reactivex.functions.Consumer;
/**
 * Author：CHENHAO
 * date：2018/5/3
 * desc：
 */

public class LoginModel implements LoginContract.LoginModel {
    /**
     * 请求banner图片以及文章列表数据
     */
    @Override
    public void getLoginDatas(Context context, String userName,String pwd, final LoginContract.ILoginModelCallback callback) {
        Log.i("zxy", "-----------------getLoginDatas: ");
        RxRetrofitManager.getInstance()
                .setTag("login")
                .getApiService(LoginApi.class)
                .getLoginSolpe(userName,pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<BaseResponseBean<LoginBean>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(BaseResponseBean<LoginBean> loginBeanBaseResponseBean) {
//                        Log.i("zxy", "onNext:loginBeanBaseResponseBean= "+loginBeanBaseResponseBean.getLogin().getOperatorName());
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//                .doOnNext(new Consumer<BaseResponseBean<LoginBean>>() {
//                    @Override
//                    public void accept(BaseResponseBean<LoginBean> loginBeanBaseResponseBean) throws Exception {
//                        if (loginBeanBaseResponseBean.getErrorCode() >= 0){
//                                if(callback!=null){
//                                    Log.i("zxy", "accept: loginBeanBaseResponseBean="+loginBeanBaseResponseBean.getData());
//                                    callback.onSuccess(loginBeanBaseResponseBean);
//                                }
//                        }else{
//                            if(callback!=null){
//                                callback.onFail(loginBeanBaseResponseBean.getErrorMsg());
//                            }
//                        }
//                    }
//                })
//
                .subscribe( new RxObserver<BaseResponseBean<LoginMsg>>(context, true) {
                    @Override
                    public void onSuccess(BaseResponseBean<LoginMsg> listBaseResponseBean) {
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
        ApiCancleManager.getInstance().cancel("login");
    }
}
