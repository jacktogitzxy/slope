package com.zig.slope.contract;

import android.content.Context;

import com.zig.slope.common.base.bean.BaseResponseBean;
import com.zig.slope.common.base.bean.HisBean;

import java.util.List;

/**
 * Author：CHENHAO
 * date：2018/5/3
 * desc：契约类
 */

public class HisContract {
    public interface HisReportView{
        void onHisSucess(HisBean data);
        void onHisFail(String msg);
        void showEmptyView(boolean toShow);
    }

    public interface HisModel{
        //请求数据，回调
        void getHisDatas(Context context, String admin ,int page,int types,IHisModelCallback callback,String meths);
        //取消请求
        void cancleHttpRequest();


    }

    public interface IHisModelCallback{
        void onSuccess(BaseResponseBean<HisBean> response);
        void onFail(String msg);
    }

}
