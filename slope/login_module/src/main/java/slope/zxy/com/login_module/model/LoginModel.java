package slope.zxy.com.login_module.model;

import android.content.Context;
import android.util.Log;


import com.zig.slope.common.base.bean.BaseResponseBean;
import com.zig.slope.common.base.bean.LoginMsg;
import com.zig.slope.common.http.RxObserver;
import com.zig.slope.common.http.RxRetrofitManager;
import com.zig.slope.common.http.cancle.ApiCancleManager;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import slope.zxy.com.login_module.api.LoginApi;
import slope.zxy.com.login_module.contract.LoginContract;

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
        RxRetrofitManager.getInstance()
                .setTag("login")
                .getApiService(LoginApi.class)
                .getLoginSolpe(userName,pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
