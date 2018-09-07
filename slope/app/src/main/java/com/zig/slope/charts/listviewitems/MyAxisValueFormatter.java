package com.zig.slope.charts.listviewitems;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

public class MyAxisValueFormatter implements IAxisValueFormatter
{

    private DecimalFormat mFormat;
    private int model;

    public MyAxisValueFormatter(int model) {
        mFormat = new DecimalFormat("###,###,###,##0.0");
        this.model=model;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if(model==1) {
            return mFormat.format(value) + " °";
        }else if(model==2){
            return mFormat.format(value) + " mm";
        }else{
            return mFormat.format(value) + " Pa";
        }
    }
}
