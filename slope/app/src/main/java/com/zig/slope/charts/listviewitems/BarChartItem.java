package com.zig.slope.charts.listviewitems;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.zig.slope.R;
import com.zig.slope.charts.MyMarkerView;
import com.zig.slope.common.Constants.Constant;
import com.zig.slope.util.OkhttpWorkUtil;
import com.zig.slope.view.TakePhotoPopLeft;

public class BarChartItem extends ChartItem {
    private TakePhotoPopLeft poup ;
    private Typeface mTf;
    private String sid;
    private OkhttpWorkUtil okhttpWorkUtil;
    private Activity activity;
    public BarChartItem(ChartData<?> cd, Activity c,String sid ) {
        super(cd);
        this.sid = sid;
        mTf = Typeface.createFromAsset(c.getAssets(), "OpenSans-Regular.ttf");
        this.activity=c;
        if(okhttpWorkUtil==null){
            okhttpWorkUtil = new OkhttpWorkUtil(c,null);
        }
        poup = new TakePhotoPopLeft(c,onClickListener);
    }

    @Override
    public int getItemType() {
        return TYPE_BARCHART;
    }

    @Override
    public View getView(int position, View convertView, Context c) {

        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();

            convertView = LayoutInflater.from(c).inflate(
                    R.layout.list_item_barchart, null);
            holder.chart = convertView.findViewById(R.id.chart);
            convertView.findViewById(R.id.bf).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("zxy", "onClick: sid=="+sid);
                    poup.showAtLocation(view, Gravity.BOTTOM,0,0);
                }
            });
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // apply styling
        holder.chart.getDescription().setEnabled(false);
        holder.chart.setDrawGridBackground(false);
        holder.chart.setDrawBarShadow(false);

        XAxis xAxis = holder.chart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(10);
        xAxis.setDrawAxisLine(true);
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(20);

        IAxisValueFormatter custom = new MyAxisValueFormatter(3);
        YAxis leftAxis = holder.chart.getAxisLeft();
        leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(5, false);
        leftAxis.setSpaceTop(20f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setValueFormatter(custom);

        YAxis rightAxis = holder.chart.getAxisRight();
        rightAxis.setTypeface(mTf);
        rightAxis.setLabelCount(5, false);
        rightAxis.setSpaceTop(20f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        rightAxis.setValueFormatter(custom);
        mChartData.setValueTypeface(mTf);
        
        // set data
        holder.chart.setData((BarData) mChartData);
        holder.chart.setFitBars(true);
        MyMarkerView mv = new MyMarkerView(c, R.layout.custom_marker_view);
        mv.setChartView(holder.chart); // For bounds control
        holder.chart.setMarker(mv);
        
        // do not forget to refresh the chart
//        holder.chart.invalidate();
        holder.chart.animateY(700);
        return convertView;
    }

    private static class ViewHolder {
        BarChart chart;
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i("zxy", "onClick: view=="+view.getTag().toString());
            int i = Integer.parseInt(view.getTag().toString())-1;
            Resources res = activity.getResources();
            String[] times = res.getStringArray(R.array.times);
            if(okhttpWorkUtil!=null){
                okhttpWorkUtil.postAsynF(Constant.BASE_URL+"modifySlopeSendorFrequence",sid,times[i]);
            }
            poup.dismiss();
        }
    };
}
