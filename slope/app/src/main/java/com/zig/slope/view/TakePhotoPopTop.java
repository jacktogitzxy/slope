package com.zig.slope.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zig.slope.R;


/**
 * Created by 17120 on 2018/7/2.
 */
public class TakePhotoPopTop extends PopupWindow {
    private View view;
    private Button[] buttons;
    public TakePhotoPopTop(Context mContext,boolean isOpen) {

        this.view = LayoutInflater.from(mContext).inflate(R.layout.menu_top, null);
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
        buttons = new Button[4];
        buttons[0] = view.findViewById(R.id.btn1);
        buttons[1] = view.findViewById(R.id.btn2);
        buttons[2] = view.findViewById(R.id.btn3);
        buttons[3] = view.findViewById(R.id.btn4);
        if(isOpen){
            buttons[0].setText(R.string.type_report);
            buttons[1].setText(R.string.type_his);
            buttons[2].setText(R.string.type_data);
            buttons[3].setText(R.string.type_video);
            buttons[0].setTag(5);
            buttons[1].setTag(6);
            buttons[2].setTag(7);
            buttons[3].setTag(8);
            buttons[0].setBackground(mContext.getResources().getDrawable(R.drawable.buttonred));
            buttons[1].setBackground(mContext.getResources().getDrawable(R.drawable.buttonred));
            buttons[2].setBackground(mContext.getResources().getDrawable(R.drawable.buttonred));
            buttons[3].setBackground(mContext.getResources().getDrawable(R.drawable.buttonred));
        }
    /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.take_right_anim);

    }



}