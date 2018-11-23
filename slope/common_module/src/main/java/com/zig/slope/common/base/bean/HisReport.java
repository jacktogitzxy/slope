package com.zig.slope.common.base.bean;

import java.io.Serializable;

/**
 * Created by 17120 on 2018/8/3.
 */



public class HisReport implements Serializable {
        private int id;
        private String patrollerID;
        private String newName;
        private String t_id;
        private String createTime;
        private String contents;
        private int controlFlag1;
        private String url1;
        private int controlFlag2;
        private String url2;
        private int controlFlag3;
        private int controlFlag4;
        private int flag;
        private String admins;
        private String adminsContent;
        private String leaders;
        private String leadersContent;
        private String remark;
        private String remark1;
    public int getId() {
        return id;
    }

    public String getT_id() {
        return t_id;
    }

    public void setT_id(String t_id) {
        this.t_id = t_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPatrollerID() {
        return patrollerID;
    }

    public void setPatrollerID(String patrollerID) {
        this.patrollerID = patrollerID;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public int getControlFlag1() {
        return controlFlag1;
    }

    public void setControlFlag1(int controlFlag1) {
        this.controlFlag1 = controlFlag1;
    }

    public String getUrl1() {
        return url1;
    }

    public void setUrl1(String url1) {
        this.url1 = url1;
    }

    public int getControlFlag2() {
        return controlFlag2;
    }

    public void setControlFlag2(int controlFlag2) {
        this.controlFlag2 = controlFlag2;
    }

    public String getUrl2() {
        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    public int getControlFlag3() {
        return controlFlag3;
    }

    public void setControlFlag3(int controlFlag3) {
        this.controlFlag3 = controlFlag3;
    }

    public int getControlFlag4() {
        return controlFlag4;
    }

    public void setControlFlag4(int controlFlag4) {
        this.controlFlag4 = controlFlag4;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getAdmins() {
        return admins;
    }

    public void setAdmins(String admins) {
        this.admins = admins;
    }

    public String getAdminsContent() {
        return adminsContent;
    }

    public void setAdminsContent(String adminsContent) {
        this.adminsContent = adminsContent;
    }

    public String getLeaders() {
        return leaders;
    }

    public void setLeaders(String leaders) {
        this.leaders = leaders;
    }

    public String getLeadersContent() {
        return leadersContent;
    }

    public void setLeadersContent(String leadersContent) {
        this.leadersContent = leadersContent;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark1() {
        return remark1;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }

    public HisReport(int id, String patrollerID,String t_id, String newName, String createTime, String contents, int controlFlag1, String url1, int controlFlag2, String url2, int controlFlag3, int controlFlag4, int flag, String admins, String adminsContent, String leaders, String leadersContent, String remark,String remark1) {
        this.id = id;
        this.patrollerID = patrollerID;
        this.newName = newName;
        this.createTime = createTime;
        this.contents = contents;
        this.controlFlag1 = controlFlag1;
        this.url1 = url1;
        this.controlFlag2 = controlFlag2;
        this.url2 = url2;
        this.controlFlag3 = controlFlag3;
        this.controlFlag4 = controlFlag4;
        this.flag = flag;
        this.admins = admins;
        this.adminsContent = adminsContent;
        this.leaders = leaders;
        this.leadersContent = leadersContent;
        this.remark = remark;
        this.t_id=t_id;
        this.remark1 = remark1;

    }
}
