package com.zig.slope.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 17120 on 2019/1/12.
 */

public class DataWarnBean implements Serializable {
    private int type_s;
    private String sensorId;
    private List<SensorWarn> list;

    public int getType_s() {
        return type_s;
    }

    public void setType_s(int type_s) {
        this.type_s = type_s;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public List<SensorWarn> getList() {
        return list;
    }

    public void setList(List<SensorWarn> list) {
        this.list = list;
    }
}
