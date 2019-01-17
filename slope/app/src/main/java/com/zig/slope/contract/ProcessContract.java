package com.zig.slope.contract;

import android.content.Context;

import com.zig.slope.bean.DiXian;
import com.zig.slope.bean.GongDi;
import com.zig.slope.bean.PaiWu;
import com.zig.slope.bean.SanFan;
import com.zig.slope.bean.WeiFangBean;
import com.zig.slope.common.base.bean.BaseResponseBean;
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
        void onThreeDefenseSucess(List<SanFan> data);
        void onThreeDefenseFail(String msg);
        void onConstructionSucess(List<GongDi> data);
        void onConstructionFail(String msg);
        void onSubsidenceSucess(List<DiXian> data);
        void onSubsidenceFail(String msg);
        void onSewageSucess(List<PaiWu> data);
        void onSewageFail(String msg);
        void onDangerousSucess(WeiFangBean data);
        void onDangerousFail(String msg);
    }

    public interface ProcessModel{
        //请求数据，回调
        void getProcessDatas(Context context, IProcessModelCallback callback);
        void getThreeDefenseDatas(Context context, IProcessModelCallbacksf callback);
        void getConstructionDatas(Context context, IProcessModelCallbackgd callback);
        void getSubsidenceDatas(Context context, IProcessModelCallbackdx callback);
        void getSewageDatas(Context context, IProcessModelCallbackpw callback);
        void getDangerousDatas(Context context, int page,int community,IProcessModelCallbackwf callback);
        //取消请求
        void cancleHttpRequest();


    }

    public interface IProcessModelCallback{
        void onSuccess(BaseResponseBean<List<ProcessBean>> response);
        void onFail(String msg);
    }
    public interface IProcessModelCallbacksf{
        void onSuccess(BaseResponseBean<List<SanFan>> response);
        void onFail(String msg);
    }

    public interface IProcessModelCallbackgd{
        void onSuccess(BaseResponseBean<List<GongDi>> response);
        void onFail(String msg);
    }
    public interface IProcessModelCallbackdx{
        void onSuccess(BaseResponseBean<List<DiXian>> response);
        void onFail(String msg);
    }
    public interface IProcessModelCallbackpw{
        void onSuccess(BaseResponseBean<List<PaiWu>> response);
        void onFail(String msg);
    }
    public interface IProcessModelCallbackwf{
        void onSuccess(BaseResponseBean<WeiFangBean> response);
        void onFail(String msg);
    }
}
