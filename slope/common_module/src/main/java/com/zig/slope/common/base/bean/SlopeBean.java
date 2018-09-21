package com.zig.slope.common.base.bean;

import com.zig.slope.common.utils.TimeUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 17120 on 2018/7/12.
 */

public class SlopeBean implements Serializable {
    private String id;//id
    private String newName;//编号
    private String cityNum;//全市统一编号
    private String street ;//街道
    private String community ;//社区
    private String company ;//牵头单位
    private String dangerName;//隐患点名称
    private String address;//隐患点最新交通位置
    private String type;//隐患类型（市、省厅文件）
    private String x;//x
    private String y ;//y
    private Double e;
    private Double n;
    private String feature;//土质
    private String lon;//坡长
    private String height ;//坡高
    private String slope;//坡度
    private String stability;//预测稳定性
    private String obj;//威胁对象
    private String numbers ;//涉险数量
    private String area ;//涉险面积（m2）
    private String peoples;//威胁人数
    private String loss ;//潜在经济损失（万）
    private String grade ;//隐患等级
    private String danger ;//预测危险性
    private String works;//防灾责任单位（社区工作站）
    private String contacts  ;//联系人
    private String tel ;//电话
    private String precautions  ;//采取预防措施
    private String process;//治理进度
    private String isDoing;//落实管理维护
    private String year;//纳入年度防治方案治理计划
    private String management   ;//管理维护单位
    private String remark;//自行治理
    private String createTime;//备注
    private String imageAddress1;//图片
    private String streamAddress;//监控地址

    public String getStreamAddress() {
        return streamAddress;
    }

    public void setStreamAddress(String streamAddress) {
        this.streamAddress = streamAddress;
    }

    public String getImageAddress1() {
        return imageAddress1;
    }

    public void setImageAddress1(String imageAddress1) {
        this.imageAddress1 = imageAddress1;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public String getCityNum() {
        return cityNum;
    }

    public void setCityNum(String cityNum) {
        this.cityNum = cityNum;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDangerName() {
        return dangerName;
    }

    public void setDangerName(String dangerName) {
        this.dangerName = dangerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getSlope() {
        return slope;
    }

    public void setSlope(String slope) {
        this.slope = slope;
    }

    public String getStability() {
        return stability;
    }

    public void setStability(String stability) {
        this.stability = stability;
    }

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPeoples() {
        return peoples;
    }

    public void setPeoples(String peoples) {
        this.peoples = peoples;
    }

    public String getLoss() {
        return loss;
    }

    public void setLoss(String loss) {
        this.loss = loss;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDanger() {
        return danger;
    }

    public void setDanger(String danger) {
        this.danger = danger;
    }

    public String getWorks() {
        return works;
    }

    public void setWorks(String works) {
        this.works = works;
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

    public String getPrecautions() {
        return precautions;
    }

    public void setPrecautions(String precautions) {
        this.precautions = precautions;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getIsDoing() {
        return isDoing;
    }

    public void setIsDoing(String isDoing) {
        this.isDoing = isDoing;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getManagement() {
        return management;
    }

    public void setManagement(String management) {
        this.management = management;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public SlopeBean(String id, String newName, String cityNum, String street, String community, String company, String dangerName, String address, String type, String x, String y, Double e, Double n, String feature, String lon, String height, String slope, String stability, String obj, String numbers, String area, String peoples, String loss, String grade, String danger, String works, String contacts, String tel, String precautions, String process, String isDoing, String year, String management, String remark, String createTime) {
        this.id = id;
        this.newName = newName;
        this.cityNum = cityNum;
        this.street = street;
        this.community = community;
        this.company = company;
        this.dangerName = dangerName;
        this.address = address;
        this.type = type;
        this.x = x;
        this.y = y;
        this.e = e;
        this.n = n;
        this.feature = feature;
        this.lon = lon;
        this.height = height;
        this.slope = slope;
        this.stability = stability;
        this.obj = obj;
        this.numbers = numbers;
        this.area = area;
        this.peoples = peoples;
        this.loss = loss;
        this.grade = grade;
        this.danger = danger;
        this.works = works;
        this.contacts = contacts;
        this.tel = tel;
        this.precautions = precautions;
        this.process = process;
        this.isDoing = isDoing;
        this.year = year;
        this.management = management;
        this.remark = remark;
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SlopeBean{" +
                "id='" + id + '\'' +
                ", newName='" + newName + '\'' +
                ", cityNum='" + cityNum + '\'' +
                ", street='" + street + '\'' +
                ", community='" + community + '\'' +
                ", company='" + company + '\'' +
                ", dangerName='" + dangerName + '\'' +
                ", address='" + address + '\'' +
                ", type='" + type + '\'' +
                ", x='" + x + '\'' +
                ", y='" + y + '\'' +
                ", e=" + e +
                ", n=" + n +
                ", feature='" + feature + '\'' +
                ", lon='" + lon + '\'' +
                ", height='" + height + '\'' +
                ", slope='" + slope + '\'' +
                ", stability='" + stability + '\'' +
                ", obj='" + obj + '\'' +
                ", numbers='" + numbers + '\'' +
                ", area='" + area + '\'' +
                ", peoples='" + peoples + '\'' +
                ", loss='" + loss + '\'' +
                ", grade='" + grade + '\'' +
                ", danger='" + danger + '\'' +
                ", works='" + works + '\'' +
                ", contacts='" + contacts + '\'' +
                ", tel='" + tel + '\'' +
                ", precautions='" + precautions + '\'' +
                ", process='" + process + '\'' +
                ", isDoing='" + isDoing + '\'' +
                ", year='" + year + '\'' +
                ", management='" + management + '\'' +
                ", remark='" + remark + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }


    public List<String> PltoList(){
        List<String> lists = new ArrayList<>();
        lists.add("编号:"+this.newName);
        lists.add("全市统一编号:"+this.cityNum);
        lists.add("街道:"+this.street);
        lists.add("社区:"+this.community);
        lists.add("牵头单位:"+this.company);
        lists.add("隐患点名称:"+this.dangerName);
        lists.add("隐患点最新交通位置:"+this.address);
        lists.add("隐患类型（市、省厅文件）:"+this.type);
        lists.add("中心x:"+this.x);
        lists.add("中心y:"+this.y);
        lists.add("土质:"+this.feature);
        lists.add("坡长:"+this.lon);
        lists.add("坡高:"+this.height);
        lists.add("坡度:"+this.slope);
        lists.add("预测稳定性:"+this.stability);
        lists.add("威胁对象:"+this.obj);
        lists.add("涉险数量:"+this.numbers);
        lists.add("涉险面积（m2）:"+this.area);
        lists.add("威胁人数:"+this.peoples);
        lists.add("潜在经济损失（万）:"+this.loss);
        lists.add("隐患等级:"+this.grade);
        lists.add("预测危险性:"+this.danger);
        lists.add("防灾责任单位（社区工作站）:"+this.works);
        lists.add("联系人:"+this.contacts);
        lists.add("电话:"+this.tel);
        lists.add("采取预防措施:"+this.precautions);
        lists.add("治理进度:"+this.process);
        lists.add("落实管理维护:"+this.isDoing);
        lists.add("纳入年度防治方案治理计划:"+this.year);
        lists.add("管理维护单位:"+this.management);
        lists.add("自行治理:"+this.remark);
        lists.add("备注:"+ TimeUtils.transleteTime(this.createTime));
        return lists;
    }
}
