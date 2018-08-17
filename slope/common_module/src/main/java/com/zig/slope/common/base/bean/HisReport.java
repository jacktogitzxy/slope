package com.zig.slope.common.base.bean;

import java.io.Serializable;

/**
 * Created by 17120 on 2018/8/3.
 */



public class HisReport implements Serializable {
        private int id;
        private String newName;
        private String patrollerID;
        private int levels;
        private String contents;
        private int isContainPic;
        private String picAddress;
        private int isContainVideo;
        private String videoAddress;
        private String processInfo;
        private String createTime;

    public String getVideoAddress() {
        return videoAddress;
    }

    public void setVideoAddress(String videoAddress) {
        this.videoAddress = videoAddress;
    }

    public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNewName() {
            return newName;
        }

        public void setNewName(String newName) {
            this.newName = newName;
        }

        public String getPatrollerID() {
            return patrollerID;
        }

        public void setPatrollerID(String patrollerID) {
            this.patrollerID = patrollerID;
        }

        public int getLevels() {
            return levels;
        }

        public void setLevels(int levels) {
            this.levels = levels;
        }

        public String getContents() {
            return contents;
        }

        public void setContents(String contents) {
            this.contents = contents;
        }

        public int getIsContainPic() {
            return isContainPic;
        }

        public void setIsContainPic(int isContainPic) {
            this.isContainPic = isContainPic;
        }

        public String getPicAddress() {
            return picAddress;
        }

        public void setPicAddress(String picAddress) {
            this.picAddress = picAddress;
        }

        public int getIsContainVideo() {
            return isContainVideo;
        }

        public void setIsContainVideo(int isContainVideo) {
            this.isContainVideo = isContainVideo;
        }

        public String getProcessInfo() {
            return processInfo;
        }

        public void setProcessInfo(String processInfo) {
            this.processInfo = processInfo;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

    public HisReport(int id, String newName, String patrollerID, int levels, String contents, int isContainPic, String picAddress, int isContainVideo, String videoAddress, String processInfo, String createTime) {
        this.id = id;
        this.newName = newName;
        this.patrollerID = patrollerID;
        this.levels = levels;
        this.contents = contents;
        this.isContainPic = isContainPic;
        this.picAddress = picAddress;
        this.isContainVideo = isContainVideo;
        this.videoAddress = videoAddress;
        this.processInfo = processInfo;
        this.createTime = createTime;
    }
}
