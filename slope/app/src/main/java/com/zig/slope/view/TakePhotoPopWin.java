//package com.zig.slope.view;
//
//import android.content.Context;
//import android.graphics.drawable.ColorDrawable;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.PopupWindow;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.zig.slope.R;
//
//
//
///**
// * Created by 17120 on 2018/7/2.
// */
//public class TakePhotoPopWin extends PopupWindow {
//
//    private View view;
//    public TakePhotoPopWin(Context mContext) {
//
//        this.view = LayoutInflater.from(mContext).inflate(R.layout.pops, null);
//
//        // 设置外部可点击
//        this.setOutsideTouchable(true);
//        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
//        this.view.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                int height = view.findViewById(R.id.pop_layout).getTop();
//
//                int y = (int) event.getY();
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (y < height) {
//                        dismiss();
//                    }
//                }
//                return true;
//            }
//        });
//
//
//    /* 设置弹出窗口特征 */
//        // 设置视图
//        this.setContentView(this.view);
//        // 设置弹出窗体的宽和高
//        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
//        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
//
//        // 设置弹出窗体可点击
//        this.setFocusable(true);
//
//        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
//        // 设置弹出窗体的背景
//        this.setBackgroundDrawable(dw);
//        // 设置弹出窗体显示时的动画，从底部向上弹出
//        this.setAnimationStyle(R.style.take_photo_anim);
//
//    }
//
//
//    public void setData(Placemark pk){
//       ImageView iv = (ImageView) view.findViewById(R.id.iv_mall);
//       TextView point_name = (TextView) view.findViewById(R.id.point_name);
//       TextView point_dangername = (TextView) view.findViewById(R.id.point_dangername);
//       TextView point_street = (TextView) view.findViewById(R.id.point_street);
//        TextView point_community = (TextView) view.findViewById(R.id.point_community);
//        point_name.setText("边坡编号："+pk.getName());
//        point_dangername.setText("隐患名称："+pk.getDangername());
//        point_street.setText("街道："+pk.getStreet());
//        point_community.setText("社区："+pk.getCommunity());
//    }
//}