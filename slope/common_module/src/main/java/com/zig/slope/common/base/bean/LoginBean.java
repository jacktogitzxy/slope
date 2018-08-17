package com.zig.slope.common.base.bean;

import java.io.Serializable;

/**
 * Created by 17120 on 2018/7/18.
 */

public class LoginBean implements Serializable {

        private int id;
        private String operatorID;
        private String operatorName;
        private String operatorLevel;
        private String operatorTel;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

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

    public String getOperatorLevel() {
        return operatorLevel;
    }

    public void setOperatorLevel(String operatorLevel) {
        this.operatorLevel = operatorLevel;
    }

    public String getOperatorTel() {
        return operatorTel;
    }

    public void setOperatorTel(String operatorTel) {
        this.operatorTel = operatorTel;
    }

    @Override
    public String toString() {
        return "LoginBean{" +
                "id=" + id +
                ", operatorID='" + operatorID + '\'' +
                ", operatorName='" + operatorName + '\'' +
                ", operatorLevel='" + operatorLevel + '\'' +
                ", operatorTel='" + operatorTel + '\'' +
                '}';
    }
}
