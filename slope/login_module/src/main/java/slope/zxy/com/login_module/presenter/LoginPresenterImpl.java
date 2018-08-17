package slope.zxy.com.login_module.presenter;

import android.content.Context;
import android.util.Log;


import com.zig.slope.common.base.BasePresenter;
import com.zig.slope.common.base.bean.BaseResponseBean;
import com.zig.slope.common.base.bean.LoginBean;
import com.zig.slope.common.base.bean.LoginMsg;
import com.zig.slope.common.base.bean.SlopeBean;

import java.util.List;

import slope.zxy.com.login_module.contract.LoginContract;
import slope.zxy.com.login_module.model.LoginModel;

/**
 * Author：CHENHAO
 * date：2018/5/3
 * desc：
 */

public class LoginPresenterImpl extends BasePresenter<LoginContract.LoginView> {
    private LoginModel model;

    public LoginPresenterImpl(){
        model = new LoginModel();
    }

    /**
     * 请求banner图片、文章列表数据
     */
    public void requestLoginData(Context context,String userName,String pwd){
        model.getLoginDatas(context,userName,pwd, new LoginContract.ILoginModelCallback() {
            @Override
            public void onSuccess(BaseResponseBean<LoginMsg> response) {
                //负责的边坡
                if (getMvpView() != null) {
                    Log.i("zxy", "onSuccess: "+response.getData());
                    getMvpView().onLoginSuccess(response.getData());
                }
            }

            @Override
            public void onFail(String msg) {
                //登录失败
                if (getMvpView() != null) {
                    getMvpView().onLoginFail(msg);
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
