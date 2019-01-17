package com.zig.slope.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 17120 on 2018/11/16.
 */

public class DiXian implements Serializable {
    private Double e;
    private Double n;
    private String id;//序号
    private String community;//社区
    private String street;//街道办
    private String road;//道路
    private String address;//地址
    private String pipelineType;//管道类型
    private String material;//管材
    private String diameters;//管径(m)
    private String depth;//埋深
    private String repair;//修复等级
    private String curing;//养护等级
    private String loss;//潜在损失
    private String buliding;//潜在威胁建筑
    private String person;//潜在威胁人数
    private String grade;//地面坍塌
    private String advise;//建议处理
    private String imageAddress1;//图片
    private String subsidenceType;//类型

    public String getSubsidenceType() {
        return subsidenceType;
    }

    public void setSubsidenceType(String subsidenceType) {
        this.subsidenceType = subsidenceType;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPipelineType() {
        return pipelineType;
    }

    public void setPipelineType(String pipelineType) {
        this.pipelineType = pipelineType;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getDiameters() {
        return diameters;
    }

    public void setDiameters(String diameters) {
        this.diameters = diameters;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public String getRepair() {
        return repair;
    }

    public void setRepair(String repair) {
        this.repair = repair;
    }

    public String getCuring() {
        return curing;
    }

    public void setCuring(String curing) {
        this.curing = curing;
    }

    public String getLoss() {
        return loss;
    }

    public void setLoss(String loss) {
        this.loss = loss;
    }

    public String getBuliding() {
        return buliding;
    }

    public void setBuliding(String buliding) {
        this.buliding = buliding;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getAdvise() {
        return advise;
    }

    public void setAdvise(String advise) {
        this.advise = advise;
    }

    public String getImageAddress1() {
        return imageAddress1;
    }

    public void setImageAddress1(String imageAddress1) {
        this.imageAddress1 = imageAddress1;
    }

    public DiXian(Double e, Double n, String id, String community, String street, String road, String address, String pipelineType, String material, String diameters, String depth, String repair, String curing, String loss, String buliding, String person, String grade, String advise, String imageAddress1) {
        this.e = e;
        this.n = n;
        this.id = id;
        this.community = community;
        this.street = street;
        this.road = road;
        this.address = address;
        this.pipelineType = pipelineType;
        this.material = material;
        this.diameters = diameters;
        this.depth = depth;
        this.repair = repair;
        this.curing = curing;
        this.loss = loss;
        this.buliding = buliding;
        this.person = person;
        this.grade = grade;
        this.advise = advise;
        this.imageAddress1 = imageAddress1;
    }

    public List<String> PltoList(){
        List<String> lists = new ArrayList<>();
        lists.add("编号:"+this.id);
        lists.add("隐患类型:"+this.subsidenceType);
        lists.add("社区:"+this.community);
        lists.add("街道办:"+this.street);
        lists.add("位置:"+this.road);
//        lists.add("地址:"+this.address);
        lists.add("管道类型:"+this.pipelineType);
        lists.add("管材:"+this.material);
        lists.add("管径(m):"+this.diameters);
        lists.add("埋深:"+this.depth);
        lists.add("修复等级:"+this.repair);
        lists.add("养护等级:"+this.curing);
//        lists.add("潜在损失:"+this.loss);
//        lists.add("潜在威胁建筑:"+this.buliding);
//        lists.add("潜在威胁人数:"+this.person);
        lists.add("地面坍塌:"+this.grade);
        lists.add("建议处理:"+this.advise);
        return lists;
    }
}
