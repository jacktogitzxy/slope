package com.zig.slope.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * Created by 17120 on 2018/7/23.
 */

@SuppressLint("AppCompatCustomView")
public class MyImageBt extends ImageButton {
    private int type ;//0 无；1图片；2视频
    private String path;
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public MyImageBt(Context context) {
        super(context);
    }

    public MyImageBt(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageBt(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
