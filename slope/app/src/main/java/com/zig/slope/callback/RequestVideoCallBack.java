package com.zig.slope.callback;



import java.util.ArrayList;
import java.util.List;

import slope.zxy.com.rtmp.VideoBean;

/**
 * Created by 17120 on 2018/8/31.
 */

public interface RequestVideoCallBack {
    void onSuccess(ArrayList<VideoBean> response);
    void onFail(String msg);
}
