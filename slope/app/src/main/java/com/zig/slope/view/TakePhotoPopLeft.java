package com.zig.slope.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.zig.slope.R;


/**
 * Created by 17120 on 2018/7/2.
 */
public class TakePhotoPopLeft extends PopupWindow {
    private View view;
    private Button[] buttons;
    public TakePhotoPopLeft(Context mContext, View.OnClickListener clickListener) {
        this.view = LayoutInflater.from(mContext).inflate(R.layout.menu_left, null);
        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = view.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
        buttons = new Button[9];
        buttons[0] = view.findViewById(R.id.bt_time_1);
        buttons[1] = view.findViewById(R.id.bt_time_2);
        buttons[2] = view.findViewById(R.id.bt_time_3);
        buttons[3] = view.findViewById(R.id.bt_time_4);
        buttons[4] = view.findViewById(R.id.bt_time_5);
        buttons[5] = view.findViewById(R.id.bt_time_6);
        buttons[6] = view.findViewById(R.id.bt_time_7);
        buttons[7] = view.findViewById(R.id.bt_time_8);
        buttons[8] = view.findViewById(R.id.bt_time_9);
        for (int i =0;i<buttons.length;i++){
            buttons[i].setOnClickListener(clickListener);
        }
        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        // 设置弹出窗体可点击
        this.setFocusable(true);
//        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
//        // 设置弹出窗体的背景
//        this.setBackgroundDrawable(dw);
        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.take_right_anim);
    }
}