package slope.zxy.com.login_module.contract;

import android.content.Context;

import com.zig.slope.common.base.bean.BaseResponseBean;
import com.zig.slope.common.base.bean.LoginBean;
import com.zig.slope.common.base.bean.LoginMsg;
import com.zig.slope.common.base.bean.SlopeBean;

import java.util.List;

/**
 * Author：CHENHAO
 * date：2018/5/3
 * desc：契约类
 */

public class LoginContract {
    public interface LoginView{
        void onLoginSuccess(LoginMsg data);
        void onLoginFail(String msg);
    }

    public interface LoginModel{
        //请求数据，回调
        void getLoginDatas(Context context,String userName,String pwd, ILoginModelCallback callback);
        //取消请求
        void cancleHttpRequest();
    }

    public interface ILoginModelCallback{
        void onSuccess(BaseResponseBean<LoginMsg> response);
        void onFail(String msg);
    }

}
