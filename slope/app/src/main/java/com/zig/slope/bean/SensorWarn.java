package com.zig.slope.bean;

import java.io.Serializable;

/**
 * Created by 17120 on 2019/1/12.
 */

public class SensorWarn implements Serializable {
    private String id;
    private String sensorID;
    private String createTime;
    private String xdata;
    private String ydata;
    private String type_s;
    private String xCriticality;
    private String yCriticality;
    private String initx;
    private String inity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSensorID() {
        return sensorID;
    }

    public void setSensorID(String sensorID) {
        this.sensorID = sensorID;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getXdata() {
        return xdata;
    }

    public void setXdata(String xdata) {
        this.xdata = xdata;
    }

    public String getYdata() {
        return ydata;
    }

    public void setYdata(String ydata) {
        this.ydata = ydata;
    }

    public String getType_s() {
        return type_s;
    }

    public void setType_s(String type_s) {
        this.type_s = type_s;
    }

    public String getxCriticality() {
        return xCriticality;
    }

    public void setxCriticality(String xCriticality) {
        this.xCriticality = xCriticality;
    }

    public String getyCriticality() {
        return yCriticality;
    }

    public void setyCriticality(String yCriticality) {
        this.yCriticality = yCriticality;
    }

    public String getInitx() {
        return initx;
    }

    public void setInitx(String initx) {
        this.initx = initx;
    }

    public String getInity() {
        return inity;
    }

    public void setInity(String inity) {
        this.inity = inity;
    }

    public SensorWarn(String id, String sensorID, String createTime, String xdata, String ydata, String type_s, String xCriticality, String yCriticality, String initx, String inity) {
        this.id = id;
        this.sensorID = sensorID;
        this.createTime = createTime;
        this.xdata = xdata;
        this.ydata = ydata;
        this.type_s = type_s;
        this.xCriticality = xCriticality;
        this.yCriticality = yCriticality;
        this.initx = initx;
        this.inity = inity;
    }
}
