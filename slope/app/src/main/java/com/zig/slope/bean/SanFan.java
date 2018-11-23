package com.zig.slope.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 17120 on 2018/11/16.
 */

public class SanFan implements Serializable {
    private Double e;
    private Double n;
    private String id;//序号
    private String community;//社区
    private String types;//类别
    private String address;//地址
    private String imageAddress1;//现场图片
    private String danger;//隐患情况
    private String reason;//隐患原因
    private String remark;//应急处置
    private String projectName;//项目名称
    private String projectContent;//建设内容
    private String projectFund;//所需资金（万元）
    private String process;//进展情况
    private String note;//备注
    private String contacts;//联系人
    private String tel;//电话
    private String leaderContacts;//社区联系认
    private String leaderTel;//电话

    public Double getE() {
        return e;
    }

    public void setE(Double e) {
        this.e = e;
    }

    public Double getN() {
        return n;
    }

    public void setN(Double n) {
        this.n = n;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageAddress1() {
        return imageAddress1;
    }

    public void setImageAddress1(String imageAddress1) {
        this.imageAddress1 = imageAddress1;
    }

    public String getDanger() {
        return danger;
    }

    public void setDanger(String danger) {
        this.danger = danger;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectContent() {
        return projectContent;
    }

    public void setProjectContent(String projectContent) {
        this.projectContent = projectContent;
    }

    public String getProjectFund() {
        return projectFund;
    }

    public void setProjectFund(String projectFund) {
        this.projectFund = projectFund;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getLeaderContacts() {
        return leaderContacts;
    }

    public void setLeaderContacts(String leaderContacts) {
        this.leaderContacts = leaderContacts;
    }

    public String getLeaderTel() {
        return leaderTel;
    }

    public void setLeaderTel(String leaderTel) {
        this.leaderTel = leaderTel;
    }

    public SanFan(Double e, Double n, String id, String community, String types, String address, String imageAddress1, String danger, String reason, String remark, String projectName, String projectContent, String projectFund, String process, String note, String contacts, String tel, String leaderContacts, String leaderTel) {
        this.e = e;
        this.n = n;
        this.id = id;
        this.community = community;
        this.types = types;
        this.address = address;
        this.imageAddress1 = imageAddress1;
        this.danger = danger;
        this.reason = reason;
        this.remark = remark;
        this.projectName = projectName;
        this.projectContent = projectContent;
        this.projectFund = projectFund;
        this.process = process;
        this.note = note;
        this.contacts = contacts;
        this.tel = tel;
        this.leaderContacts = leaderContacts;
        this.leaderTel = leaderTel;
    }
    public List<String> PltoList(){
        List<String> lists = new ArrayList<>();
        lists.add("编号:"+this.id);
        lists.add("社区:"+this.community);
        lists.add("类别:"+this.types);
        lists.add("地址:"+this.address);
        lists.add("隐患情况:"+this.danger);
        lists.add("隐患原因:"+this.reason);
        lists.add("应急处置:"+this.remark);
        lists.add("项目名称:"+this.projectName);
        lists.add("建设内容:"+this.projectContent);
        lists.add("所需资金（万元）:"+this.projectFund);
        lists.add("进展情况:"+this.process);
        lists.add("备注:"+this.note);
        lists.add("现场值守人员（国盾）:"+this.contacts);
        lists.add("联系电话:"+this.tel);
        lists.add("社区联系人:"+this.leaderContacts);
        lists.add("社区联系人电话:"+this.leaderTel);
        return lists;
    }
}
