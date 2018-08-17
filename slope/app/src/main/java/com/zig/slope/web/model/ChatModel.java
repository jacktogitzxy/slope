package com.zig.slope.web.model;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by WangChang on 2016/4/28.
 */
public class ChatModel implements Serializable {
    private Bitmap icons;
    private String content="";
    private String type="";

    public Bitmap getIcons() {
        return icons;
    }

    public void setIcons(Bitmap icons) {
        this.icons = icons;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
