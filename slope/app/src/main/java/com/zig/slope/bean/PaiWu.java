package com.zig.slope.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 17120 on 2018/11/16.
 */

public class PaiWu implements Serializable {
    private Double e;
    private Double n;
    private String id;//序号
    private String community;//社区
    private String sewageName;//水体名称
    private String address;//地址
    private String sewageType;//排放口类型
    private String sewageId;//编号
    private String des;//排放口描述
    private String river;//河道左/右岸
    private String outType;//出流方式
    private String inriver;//入河方式
    private String isout;//是否有水排出
    private String sources;//排水来源
    private String blend;//内部混流
    private String disorder;//错接乱排
    private String other;//其他
    private String imageAddress1;//现场图片

    public String getSewageName() {
        return sewageName;
    }

    public void setSewageName(String sewageName) {
        this.sewageName = sewageName;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSewageType() {
        return sewageType;
    }

    public void setSewageType(String sewageType) {
        this.sewageType = sewageType;
    }

    public String getSewageId() {
        return sewageId;
    }

    public void setSewageId(String sewageId) {
        this.sewageId = sewageId;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getRiver() {
        return river;
    }

    public void setRiver(String river) {
        this.river = river;
    }

    public String getOutType() {
        return outType;
    }

    public void setOutType(String outType) {
        this.outType = outType;
    }

    public String getInriver() {
        return inriver;
    }

    public void setInriver(String inriver) {
        this.inriver = inriver;
    }

    public String getIsout() {
        return isout;
    }

    public void setIsout(String isout) {
        this.isout = isout;
    }

    public String getSources() {
        return sources;
    }

    public void setSources(String sources) {
        this.sources = sources;
    }

    public String getBlend() {
        return blend;
    }

    public void setBlend(String blend) {
        this.blend = blend;
    }

    public String getDisorder() {
        return disorder;
    }

    public void setDisorder(String disorder) {
        this.disorder = disorder;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getImageAddress1() {
        return imageAddress1;
    }

    public void setImageAddress1(String imageAddress1) {
        this.imageAddress1 = imageAddress1;
    }

    public PaiWu(Double e, Double n, String id, String community, String address, String sewageType, String sewageId, String des, String river, String outType, String inriver, String isout, String sources, String blend, String disorder, String other, String imageAddress1) {
        this.e = e;
        this.n = n;
        this.id = id;
        this.community = community;
        this.address = address;
        this.sewageType = sewageType;
        this.sewageId = sewageId;
        this.des = des;
        this.river = river;
        this.outType = outType;
        this.inriver = inriver;
        this.isout = isout;
        this.sources = sources;
        this.blend = blend;
        this.disorder = disorder;
        this.other = other;
        this.imageAddress1 = imageAddress1;
    }
    public List<String> PltoList(){
        List<String> lists = new ArrayList<>();
        lists.add("编号:"+this.sewageId);
        lists.add("地址:"+this.address);
        lists.add("社区:"+this.community);
        lists.add("水体名称:"+this.sewageName);
        lists.add("排放口类型:"+this.sewageType);
        lists.add("排放口描述:"+this.des);
        lists.add("河道左/右岸:"+this.river);
        lists.add("出流方式:"+this.outType);
        lists.add("入河方式:"+this.inriver);
        lists.add("是否有水排出:"+this.isout);
        lists.add("排水来源:"+this.sources);
        lists.add("内部混流:"+this.blend);
        lists.add("错接乱排:"+this.disorder);
        lists.add("其他:"+this.other);
        return lists;
    }
}
