package com.zig.slope.bean;

/**
 * Created by 17120 on 2018/8/15.
 */

public class User {
    private String operatorID;
    private String operatorName;

    public String getOperatorID() {
        return operatorID;
    }

    public void setOperatorID(String operatorID) {
        this.operatorID = operatorID;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public User(String operatorID, String operatorName) {
        this.operatorID = operatorID;
        this.operatorName = operatorName;
    }

    public User() {
    }
}
