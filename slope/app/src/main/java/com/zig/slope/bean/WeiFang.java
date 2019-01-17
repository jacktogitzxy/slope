package com.zig.slope.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 17120 on 2018/11/16.
 */

public class WeiFang implements Serializable {
    private String id;//序号
    private String community;//社区
    private String seq;//序号
    private String code;//测绘编号
    private String owner;//房东
    private String phone;//电话
    private String houseCode;//房屋编码
    private String houseNumber;//房屋地址
    private String rent;//出租
    private String floor;//楼层
    private String constructionTime;//建成年份
    private String structure;//结构形式
    private String used;//房屋现状
    private String caution;//警示措施
    private String improvement;//整治措施
    private String types;//类别
    private String x;//
    private String y;//
    private String area;//面积
    private String note;//备注
    private String imageAddress1;//图片地址


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

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHouseCode() {
        return houseCode;
    }

    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getConstructionTime() {
        return constructionTime;
    }

    public void setConstructionTime(String constructionTime) {
        this.constructionTime = constructionTime;
    }

    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public String getCaution() {
        return caution;
    }

    public void setCaution(String caution) {
        this.caution = caution;
    }

    public String getImprovement() {
        return improvement;
    }

    public void setImprovement(String improvement) {
        this.improvement = improvement;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImageAddress1() {
        return imageAddress1;
    }

    public void setImageAddress1(String imageAddress1) {
        this.imageAddress1 = imageAddress1;
    }

    public WeiFang(String id, String community, String seq, String code, String owner, String phone, String houseCode, String houseNumber, String rent, String floor, String constructionTime, String structure, String used, String caution, String improvement, String types, String x, String y, String area, String note, String imageAddress1) {

        this.id = id;
        this.community = community;
        this.seq = seq;
        this.code = code;
        this.owner = owner;
        this.phone = phone;
        this.houseCode = houseCode;
        this.houseNumber = houseNumber;
        this.rent = rent;
        this.floor = floor;
        this.constructionTime = constructionTime;
        this.structure = structure;
        this.used = used;
        this.caution = caution;
        this.improvement = improvement;
        this.types = types;
        this.x = x;
        this.y = y;
        this.area = area;
        this.note = note;
        this.imageAddress1 = imageAddress1;
    }

    public List<String> PltoList(){
        List<String> lists = new ArrayList<>();
        lists.add("编号:"+this.id);
        lists.add("地址:"+this.houseNumber);
        lists.add("社区:"+this.community);
        lists.add("测绘编号:"+this.code);
        lists.add("房东:"+this.owner);
        lists.add("联系电话:"+this.phone);
        lists.add("房屋编码:"+this.houseCode);
        lists.add("是否出租:"+this.rent);
        lists.add("楼层:"+this.floor);
        lists.add("建成年份:"+this.constructionTime);
        lists.add("结构形式:"+this.structure);
        lists.add("房屋现状:"+this.used);
        lists.add("警示措施:"+this.caution);
        lists.add("整治措施:"+this.improvement);
        lists.add("类别:"+this.types);
        lists.add("x:"+this.x);
        lists.add("y:"+this.y);
        lists.add("面积:"+this.area);
        lists.add("备注:"+this.note);
        return lists;
    }
}
