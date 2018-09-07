package com.zig.slope.callback;

import com.zig.slope.bean.UserLoacl;

import java.util.List;

/**
 * Created by 17120 on 2018/8/31.
 */

public interface RequestCallBack {
    void onSuccess(List<UserLoacl> response);
    void onFail(String msg);
}
