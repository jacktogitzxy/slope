package com.zig.slope.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zig.slope.R;
import com.zig.slope.common.base.bean.ProcessBean;
import com.zig.slope.view.CircleImageView;

import java.util.List;

/**
 * Created by 17120 on 2018/8/15.
 */
public class MySpAdapter extends BaseAdapter {
    private List<ProcessBean> mList;
    private Context mContext;

    public MySpAdapter(Context pContext, List<ProcessBean> pList) {
        this.mContext = pContext;
        this.mList = pList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    /**
     * 下面是重要代码
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater _LayoutInflater=LayoutInflater.from(mContext);
        convertView=_LayoutInflater.inflate(R.layout.item_spprocess, null);
        if(convertView!=null)
        {

            TextView tv = convertView.findViewById(R.id.processName);
            tv.setText(mList.get(position).getId()+"."+mList.get(position).getProgress());
        }
        return convertView;
    }
}