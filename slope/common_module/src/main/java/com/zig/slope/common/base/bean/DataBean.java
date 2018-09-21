package com.zig.slope.common.base.bean;


import java.io.Serializable;
import java.util.List;

/**
 * Created by 17120 on 2018/9/12.
 */

public class DataBean implements Serializable {
    private List<MySensor>data;
    private int groups;

    public List<MySensor> getData() {
        return data;
    }

    public void setData(List<MySensor> data) {
        this.data = data;
    }

    public int getGroups() {
        return groups;
    }

    public void setGroups(int groups) {
        this.groups = groups;
    }
}
