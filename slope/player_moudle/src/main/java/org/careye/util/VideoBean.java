package org.careye.util;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by 17120 on 2018/10/19.
 */

public class VideoBean implements Parcelable {
    private String id;
    private String url;
    private String monitoringId;
    private String remark;

    protected VideoBean(Parcel in) {
        id = in.readString();
        url = in.readString();
        monitoringId = in.readString();
        remark = in.readString();
    }

    public static final Creator<VideoBean> CREATOR = new Creator<VideoBean>() {
        @Override
        public VideoBean createFromParcel(Parcel in) {
            return new VideoBean(in);
        }

        @Override
        public VideoBean[] newArray(int size) {
            return new VideoBean[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMonitoringId() {
        return monitoringId;
    }

    public void setMonitoringId(String monitoringId) {
        this.monitoringId = monitoringId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(url);
        parcel.writeString(monitoringId);
        parcel.writeString(remark);
    }
}
