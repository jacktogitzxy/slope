
package com.zig.slope.charts.listviewitems;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.design.internal.BottomNavigationItemView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.zig.slope.R;
import com.zig.slope.charts.MyMarkerView;
import com.zig.slope.common.Constants.Constant;
import com.zig.slope.util.OkhttpWorkUtil;
import com.zig.slope.view.TakePhotoPopLeft;

import java.util.List;

public class LineChartItem extends ChartItem {
    private Typeface mTf;
    private int mode=1;
    private String sid ;
    private OkhttpWorkUtil okhttpWorkUtil;
    private TakePhotoPopLeft poup ;
    private Activity activity;
    private List<String> xdata;
    private boolean isShow = true;

    public List<String> getXdata() {
        return xdata;
    }

    public void setXdata(List<String> xdata) {
        this.xdata = xdata;
    }

    public LineChartItem(ChartData<?> cd, Activity c, int mode, String sid,boolean isShow ) {
        super(cd);
        mTf = Typeface.createFromAsset(c.getAssets(), "OpenSans-Regular.ttf");
        this.mode = mode;
        this.sid = sid;
        this.activity=c;
        this.isShow = isShow;
        if(okhttpWorkUtil==null){
            okhttpWorkUtil = new OkhttpWorkUtil(c,null);
        }
        poup = new TakePhotoPopLeft(c,onClickListener);
    }

    @Override
    public int getItemType() {
        return TYPE_LINECHART;
    }

    @Override
    public View getView(int position, View convertView, Context c) {

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(c).inflate(
                    R.layout.list_item_linechart, null);
            holder.chart = convertView.findViewById(R.id.chart);
            Button button =  convertView.findViewById(R.id.bt_f);
            if(isShow){
                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("zxy", "onClick: sid=="+sid);
                        poup.showAtLocation(view, Gravity.BOTTOM,0,0);
                    }
                });
            }else{
                button.setVisibility(View.GONE);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // apply styling
        // holder.chart.setValueTypeface(mTf);
        holder.chart.getDescription().setEnabled(false);
        holder.chart.setDrawGridBackground(false);
        IAxisValueFormatter custom = new MyAxisValueFormatter(mode);
        XAxis xAxis = holder.chart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setLabelCount(10);
        xAxis.setGranularity(1f);
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(19);
        xAxis.setLabelRotationAngle(15);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if(xdata.size()>(int)value) {
                    return xdata.get((int) value);
                }else{
                    return xdata.get(xdata.size()-1);
                }
            }
        });

        YAxis leftAxis = holder.chart.getAxisLeft();
        leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(10, false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setValueFormatter(custom);
        if(mode==2) {
            leftAxis.setAxisMinimum(0);
            leftAxis.setAxisMaximum(100);
        }
        if(mode==1) {
            leftAxis.setAxisMinimum(-30);
            leftAxis.setAxisMaximum(30);
        }


        YAxis rightAxis = holder.chart.getAxisRight();
        rightAxis.setEnabled(false);
        rightAxis.setTypeface(mTf);
        rightAxis.setLabelCount(5, false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        rightAxis.setValueFormatter(custom);
        // set data
        holder.chart.setData((LineData) mChartData);
        MyMarkerView mv = new MyMarkerView(c, R.layout.custom_marker_view);
        mv.setChartView(holder.chart); // For bounds control
        holder.chart.setMarker(mv);
        // do not forget to refresh the chart
        // holder.chart.invalidate();
        holder.chart.animateX(750);
        return convertView;
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
