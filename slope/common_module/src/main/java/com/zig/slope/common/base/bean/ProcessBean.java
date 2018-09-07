package com.zig.slope.common.base.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 17120 on 2018/7/12.
 */

public class ProcessBean implements Serializable {
    private int id;//id
    private String progress;//编号

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }
}
