package com.zig.slope.contract;

import android.content.Context;

import com.zig.slope.common.base.bean.BaseResponseBean;
import com.zig.slope.common.base.bean.DataBean;

import java.util.List;

/**
 * Author：CHENHAO
 * date：2018/5/3
 * desc：契约类
 */

public class SensorContract {
    public interface SensorView{
        void onSensorSucess(BaseResponseBean<List<DataBean>> data);
        void onSensorFail(String msg);
    }

    public interface SensorModel{
        //请求数据，回调
        void getSensorDatas(Context context,String newName, ISensorModelCallback callback);
        //取消请求
        void cancleHttpRequest();


    }

    public interface ISensorModelCallback{
        void onSuccess(BaseResponseBean<List<DataBean>> response);
        void onFail(String msg);
    }

}
