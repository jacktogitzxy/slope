package com.zig.slope.common.base.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 17120 on 2018/8/1.
 */

public class LoginMsg implements Serializable {
    private LoginBean operators;
    private List<SlopeBean> slopeInfo;
    private String bannerList;

    public LoginBean getOperators() {
        return operators;
    }

    public void setOperators(LoginBean operators) {
        this.operators = operators;
    }

    public List<SlopeBean> getSlopeInfo() {
        return slopeInfo;
    }

    public void setSlopeInfo(List<SlopeBean> slopeInfo) {
        this.slopeInfo = slopeInfo;
    }

    public String getBannerList() {
        return bannerList;
    }

    public void setBannerList(String bannerList) {
        this.bannerList = bannerList;
    }
}
