package com.zig.slope.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 17120 on 2018/11/16.
 */

public class GongDi implements Serializable {
    private Double e;
    private Double n;
    private String id;//序号
    private String constructionType;//工程类别
    private String community;//社区
    private String constructionName;//工地名称
    private String constructionAddress;//地址
    private String constructionUnit;//建设单位
    private String constructionTel;//建设单位联系人及联系方式
    private String constructionUnits;//施工单位
    private String constructionTels;//施工单位联系人及联系方式
    private String state;//施工状态
    private String regulators;//监管部门
    private String imageAddress1;//图片

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

    public String getConstructionType() {
        return constructionType;
    }

    public void setConstructionType(String constructionType) {
        this.constructionType = constructionType;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getConstructionName() {
        return constructionName;
    }

    public void setConstructionName(String constructionName) {
        this.constructionName = constructionName;
    }

    public String getConstructionAddress() {
        return constructionAddress;
    }

    public void setConstructionAddress(String constructionAddress) {
        this.constructionAddress = constructionAddress;
    }

    public String getConstructionUnit() {
        return constructionUnit;
    }

    public void setConstructionUnit(String constructionUnit) {
        this.constructionUnit = constructionUnit;
    }

    public String getConstructionTel() {
        return constructionTel;
    }

    public void setConstructionTel(String constructionTel) {
        this.constructionTel = constructionTel;
    }

    public String getConstructionUnits() {
        return constructionUnits;
    }

    public void setConstructionUnits(String constructionUnits) {
        this.constructionUnits = constructionUnits;
    }

    public String getConstructionTels() {
        return constructionTels;
    }

    public void setConstructionTels(String constructionTels) {
        this.constructionTels = constructionTels;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRegulators() {
        return regulators;
    }

    public void setRegulators(String regulators) {
        this.regulators = regulators;
    }

    public String getImageAddress1() {
        return imageAddress1;
    }

    public void setImageAddress1(String imageAddress1) {
        this.imageAddress1 = imageAddress1;
    }


    public GongDi(Double e, Double n, String id, String constructionType, String community, String constructionName, String constructionAddress, String constructionUnit, String constructionTel, String constructionUnits, String constructionTels, String state, String regulators, String imageAddress1) {
        this.e = e;
        this.n = n;
        this.id = id;
        this.constructionType = constructionType;
        this.community = community;
        this.constructionName = constructionName;
        this.constructionAddress = constructionAddress;
        this.constructionUnit = constructionUnit;
        this.constructionTel = constructionTel;
        this.constructionUnits = constructionUnits;
        this.constructionTels = constructionTels;
        this.state = state;
        this.regulators = regulators;
        this.imageAddress1 = imageAddress1;
    }

    public List<String> PltoList(){
        List<String> lists = new ArrayList<>();
        lists.add("编号:"+this.id);
        lists.add("社区:"+this.community);
        lists.add("工程类别:"+this.constructionType);
        lists.add("工地地址:"+this.constructionAddress);
        lists.add("工地名称:"+this.constructionName);
        lists.add("建设单位:"+this.constructionUnit);
        lists.add("建设单位联系人及联系方式:"+this.constructionTel);
        lists.add("施工单位:"+this.constructionUnits);
        lists.add("施工单位联系人电话:"+this.constructionTels);
        lists.add("施工状态:"+this.state);
        lists.add("监管部门:"+this.regulators);
        return lists;
    }
}
