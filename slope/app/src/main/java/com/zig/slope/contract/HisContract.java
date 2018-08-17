package com.zig.slope.contract;

import android.content.Context;

import com.zig.slope.common.base.bean.BaseResponseBean;
import com.zig.slope.common.base.bean.HisReport;

import java.util.List;

/**
 * Author：CHENHAO
 * date：2018/5/3
 * desc：契约类
 */

public class HisContract {
    public interface HisReportView{
        void onHisSucess(List<HisReport> data);
        void onHisFail(String msg);
    }

    public interface HisModel{
        //请求数据，回调
        void getHisDatas(Context context, String admin ,IHisModelCallback callback);
        //取消请求
        void cancleHttpRequest();
    }

    public interface IHisModelCallback{
        void onSuccess(BaseResponseBean<List<HisReport>> response);
        void onFail(String msg);
    }

}
