package com.zig.slope.common.base.bean;

import java.io.Serializable;

/**
 * Author：CHENHAO
 * date：2018/5/3
 * desc：
 */

public class BaseResponseBean<T> implements Serializable {
    private int code;
    private String info;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
