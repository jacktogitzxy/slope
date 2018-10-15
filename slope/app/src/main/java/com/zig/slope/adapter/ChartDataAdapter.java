package com.zig.slope.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.zig.slope.charts.listviewitems.ChartItem;

import java.util.List;

/**
 * Created by 17120 on 2018/9/12.
 */

public class ChartDataAdapter extends ArrayAdapter<ChartItem> {
    List<ChartItem> objects;
    public ChartDataAdapter(Context context, List<ChartItem> objects) {
        super(context, 0, objects);
        this.objects=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItem(position).getView(position, convertView, getContext());
    }

    @Override
    public int getItemViewType(int position) {
        // return the views type
        return getItem(position).getItemType();
    }

    public void updateData(List<ChartItem> list,boolean isRefresh){
        if(isRefresh){
            objects.clear();
        }
        objects.addAll(list);
        notifyDataSetChanged();

    }
    @Override
    public int getViewTypeCount() {
        return 3; // we have 3 different item-types
    }
}
