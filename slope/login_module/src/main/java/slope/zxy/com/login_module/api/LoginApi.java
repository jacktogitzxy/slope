package slope.zxy.com.login_module.api;


import com.zig.slope.common.base.bean.BaseResponseBean;
import com.zig.slope.common.base.bean.LoginBean;
import com.zig.slope.common.base.bean.LoginMsg;
import com.zig.slope.common.base.bean.SlopeBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import slope.zxy.com.login_module.BannerBean;

/**
 * Author：CHENHAO
 * date：2018/5/4
 * desc：
 */

public interface LoginApi {
    /**
     *登录数据
     */
    @POST("loginApp")
    Observable<BaseResponseBean<LoginMsg>> getLoginSolpe(@Query("operatorID") String userName, @Query("operatorPassWord") String pwd);

}
