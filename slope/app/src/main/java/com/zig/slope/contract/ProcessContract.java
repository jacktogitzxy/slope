package com.zig.slope.contract;

import android.content.Context;

import com.zig.slope.common.base.bean.BaseResponseBean;
import com.zig.slope.common.base.bean.HisBean;
import com.zig.slope.common.base.bean.ProcessBean;

import java.util.List;

/**
 * Author：CHENHAO
 * date：2018/5/3
 * desc：契约类
 */

public class ProcessContract {
    public interface ProcessView{
        void onProcessSucess(List<ProcessBean> data);
        void onProcessFail(String msg);
    }

    public interface ProcessModel{
        //请求数据，回调
        void getProcessDatas(Context context, IProcessModelCallback callback);
        //取消请求
        void cancleHttpRequest();


    }

    public interface IProcessModelCallback{
        void onSuccess(BaseResponseBean<List<ProcessBean>> response);
        void onFail(String msg);
    }

}
