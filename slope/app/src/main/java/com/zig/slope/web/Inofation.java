package com.zig.slope.web;

/**
 * Created by 17120 on 2018/8/9.
 */

public interface Inofation {
    void onMsg(String msg);
    void onConnect();
    void onDisConnect(int code, String reason);
}
