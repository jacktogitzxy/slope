package com.zig.slope.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zig.slope.R;
import com.zig.slope.adapter.OnRecyclerViewItemOnClickListener;
import com.zig.slope.adapter.WFdapter;
import com.zig.slope.bean.WeiFang;

import java.util.List;


/**
 * Created by 17120 on 2018/7/2.
 */
public class TakePhotoPopTop extends PopupWindow {
    private View view;
    private NestedScrollView nestedScrollView;
    private RecyclerView recyclerView;
    private LinearLayout emptyView;
    private SwipeRefreshLayout refreshLayout;
    private List<WeiFang> data;
    private WFdapter adapter;
    private TextView titlewf;
    private Context context;
    public TakePhotoPopTop(final Context mContext, View.OnClickListener clickListener,
                           SwipeRefreshLayout.OnRefreshListener onRefreshListener,
                           NestedScrollView.OnScrollChangeListener onScrollChangeListener   ) {
        this.view = LayoutInflater.from(mContext).inflate(R.layout.menu_top, null);
        this.context = mContext;
        nestedScrollView = view.findViewById(R.id.nested_scroll_view);
        recyclerView = view.findViewById(R.id.recycler_view);
        emptyView = view.findViewById(R.id.empty_view);
        titlewf = view.findViewById(R.id.titlewf);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        recyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.INVISIBLE);
        refreshLayout = view.findViewById(R.id.refresh_layout);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(onRefreshListener);
        nestedScrollView.setOnScrollChangeListener(onScrollChangeListener);
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
        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(1000);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        // 设置弹出窗体可点击
        this.setFocusable(true);
//        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
//        // 设置弹出窗体的背景
//        this.setBackgroundDrawable(dw);
        // 设置弹出窗体显示时的动画
        this.setAnimationStyle(R.style.take_photo_anim);
    }
    public View getView() {
        return view;
    }
    public List<WeiFang> getData() {
        return data;
    }

    public void setData(List<WeiFang> datas,OnRecyclerViewItemOnClickListener listener,String s) {
        titlewf.setText(s);
        if(datas==null||datas.size()==0){
            showEmptyView(true);
        }else {
            if(adapter==null) {
                this.data = datas;
                adapter = new WFdapter(context, datas);
                adapter.setItemClickListener(listener);
                recyclerView.setAdapter(adapter);
            }else{
                this.data.clear();
                updateData(datas);
            }
        }
    }

    public void updateData(List<WeiFang> list){
        if(list!=null) {
            this.data.addAll(list);
            adapter.notifyItemRemoved(list.size());
        }
        adapter.notifyDataSetChanged();

    }

    public void showEmptyView(boolean toShow) {
        emptyView.setVisibility(toShow?View.VISIBLE:View.INVISIBLE);
        nestedScrollView.setVisibility(!toShow?View.VISIBLE:View.INVISIBLE);
    }

    public void hiddenRefresh(){
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        });
    }
}