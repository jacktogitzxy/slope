package com.zig.slope.common.base.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 17120 on 2018/9/12.
 */

public class MySensor implements Serializable {
    private int type_s;
    private String sensorId;
    private List<MySensorData> data;

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

    public List<MySensorData> getData() {
        return data;
    }

    public void setData(List<MySensorData> data) {
        this.data = data;
    }

    public  class MySensorData implements Serializable {
        private int id;
        private String sensorID;
        private String createTime;
        private String xdata;
        private String ydata;
        private String slopeNewName;
        private int groups;
        private int type_s;
        private String xCriticality;
        private String yCriticality;
        private String monitoringName;
        private String frequency;

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

        public String getMonitoringName() {
            return monitoringName;
        }

        public void setMonitoringName(String monitoringName) {
            this.monitoringName = monitoringName;
        }

        public String getFrequency() {
            return frequency;
        }

        public void setFrequency(String frequency) {
            this.frequency = frequency;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
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

        public String getSlopeNewName() {
            return slopeNewName;
        }

        public void setSlopeNewName(String slopeNewName) {
            this.slopeNewName = slopeNewName;
        }

        public int getGroups() {
            return groups;
        }

        public void setGroups(int groups) {
            this.groups = groups;
        }

        public int getType_s() {
            return type_s;
        }

        public void setType_s(int type_s) {
            this.type_s = type_s;
        }
    }
}

