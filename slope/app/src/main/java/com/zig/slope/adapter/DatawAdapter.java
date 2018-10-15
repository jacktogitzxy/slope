package com.zig.slope.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.zig.slope.R;
import com.zig.slope.charts.listviewitems.BarChartItem;
import com.zig.slope.charts.listviewitems.ChartItem;
import com.zig.slope.charts.listviewitems.LineChartItem;
import com.zig.slope.common.base.bean.MySensor;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by CoderLengary
 */


public class DatawAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity context;
    private LayoutInflater inflater;
    private List<MySensor> mList;
    private OnRecyclerViewItemOnClickListener listener;
    private Handler handler;


    public DatawAdapter(Activity context, List<MySensor> list,Handler handler){
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        mList = list;
        this.handler=handler;
    }

    public void updateData(List<MySensor> list,boolean isRefresh){
        if(isRefresh){
            mList.clear();
        }
        mList.addAll(list);
        notifyDataSetChanged();
        notifyItemRemoved(list.size());
    }

    public void setItemClickListener(OnRecyclerViewItemOnClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.data_warning_item, parent, false);
        return new NormalViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
        MySensor data = mList.get(position);
        MySensor.MySensorData mySensorData= data.getData().get(0);
        int currentType=mySensorData.getType_s();
        String tyeps = "表面位移传感器";
        if(currentType==1){
            tyeps = "表面位移传感器";
        }
        if(currentType==2){
            tyeps = "深部位移传感器";
        }
        if(currentType==3){
            tyeps = "孔隙水压传感器";
        }
        String s = String.format(context.getResources().getString(R.string.titlew), mySensorData.getSlopeNewName(), mySensorData.getGroups(),
                mySensorData.getSensorID(),tyeps);
        normalViewHolder.titlew.setText(s);
        Log.i("zxy", "onBindViewHolder: position=="+position);
        drawData(data,normalViewHolder.listVieww,normalViewHolder.mHolder);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    private int getRealPosition(int position) {

        return position;
    }

    class NormalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        OnRecyclerViewItemOnClickListener listener;
        TextView titlew;
        SurfaceView surfacew;
        ListView listVieww;
        SurfaceHolder mHolder;
        public NormalViewHolder(View itemView, final OnRecyclerViewItemOnClickListener listener) {
            super(itemView);
            this.listener = listener;
            titlew = itemView.findViewById(R.id.titlew);
            surfacew = itemView.findViewById(R.id.surfacew);
            listVieww = itemView.findViewById(R.id.listVieww);
            surfacew.setZOrderOnTop(true);
            mHolder = surfacew.getHolder();
            mHolder.setFormat(PixelFormat.TRANSLUCENT);
            mHolder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                }
            });
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                default:break;

            }
        }
    }


    public void drawData(final MySensor mySensor, ListView listView, final SurfaceHolder mHolder){
        List<MySensor.MySensorData>datas =mySensor.getData();
        ArrayList<ChartItem> list = new ArrayList<ChartItem>();
        String sensorId = mySensor.getSensorId();
        String p="0",a="0",b="0",m="0";
        if(mySensor.getType_s()==1){
            m=datas.get(datas.size()-1).getXdata();
            String[]labs = new String[]{"x","y表面变形(mm)         周期（"+datas.get(0).getFrequency()+"）"};
            LineChartItem lineChartItem = new LineChartItem(getLineData(datas,labs),context,2,sensorId);
            list.add(lineChartItem);
        }else if(mySensor.getType_s()==2){
            a=datas.get(datas.size()-1).getXdata();
            b=datas.get(datas.size()-1).getYdata();
            String[]labs = new String[]{"x","y深部位移(°)         周期（"+datas.get(0).getFrequency()+"）"};
            list.add(new LineChartItem(getLineData(datas,labs),context,1,sensorId));
        }else{
            p=datas.get(0).getXdata();
            String[]labs = new String[]{"孔隙水压(kpa)         周期（"+datas.get(0).getFrequency()+"）"};
            list.add(new BarChartItem(getDataBar(datas,labs),context,sensorId));
        }
        ChartDataAdapter cda = new ChartDataAdapter(context, list);
        final String finalP = p;
        final String finalM = m;
        final String finalA = a;
        final String finalB = b;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                drawx(finalP, finalM, finalA, finalB,mySensor.getData().get(0).getxCriticality(),mySensor.getType_s(),mHolder);
            }
        },1000);

        listView.setAdapter(cda);
    }

    private void drawx(String temp,String moveSize,String a,String b,String n,int currentType,SurfaceHolder mHolder) {
        Canvas canvas = mHolder.lockCanvas();
        Log.i("zxy", "drawx: canvas=="+canvas+"====mHolder="+mHolder);
        Paint mPaint = new Paint();
        mPaint.setTextSize(40);
        mPaint.setColor(context.getResources().getColor(R.color.black_dd));
        Paint paintCircle = new Paint();
        paintCircle.setColor(Color.RED);
        Paint paintLine = new Paint();
        paintLine.setColor(Color.BLUE);

        Paint linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth((float) 1.5);
        linePaint.setColor(Color.BLACK);
        linePaint.setAntiAlias(true);// 锯齿不显示
        if(currentType==3) {
            float tem1 = Float.parseFloat(temp) / 10;
            int y = 260 - (int) ((tem1 - 5) * 20);
            if (y < 0) {
                y = 0;
            }
            if(y>270){
                y=270;
            }
            canvas.drawRect(400, 50, 420, 280, mPaint);
            canvas.drawRect(400, y, 420, 280, paintCircle);
            canvas.drawLine(420, y, 525, y, paintLine);
            canvas.drawText("当前水压 ", 525, y, paintLine);
            canvas.drawCircle(410, 300, 25, paintCircle);
            int ydegree = 260;
            int tem = 5;
            while (ydegree > 55) {
                canvas.drawLine(420, ydegree, 427, ydegree, mPaint);
                if (ydegree % 20 == 0) {
                    canvas.drawLine(420, ydegree, 432, ydegree, paintLine);
                    canvas.drawText(tem * 10 + "", 432, ydegree + 4, paintLine);
                    tem++;
                }
                ydegree = ydegree - 2;
            }
            canvas.drawText("当前水压:" + temp + "KPa", 350, 360, mPaint);
            canvas.drawText("临界水压:" + n + "KPa", 350, 410, mPaint);
            canvas.drawText("孔隙水压", 420, 40, mPaint);
        }
        if(currentType==1) {

            canvas.rotate(10);
            int ydegree2 = 260;
            while (ydegree2 > 55) {
                if (ydegree2 % 20 == 0) {
                    canvas.drawLine(320, ydegree2 + 10, 350, ydegree2, paintLine);
                    canvas.drawLine(490, ydegree2, 520, ydegree2 + 10, paintLine);
                }
                ydegree2 = ydegree2 - 20;
            }
            //画位移
            canvas.drawRect(350, 0, 390, 260, mPaint);
            canvas.drawRect(450, 0, 490, 270, mPaint);
            canvas.drawLine(390, 150, 450, 150, mPaint);
            canvas.rotate(-10);
            canvas.drawText("位移:" + moveSize + "mm", 300, 400, mPaint);
            canvas.drawText("表面变形", 300, 40, mPaint);
        }
        //基准点。
        float gridX = 350;
        float gridY = 280;
        if(currentType==2) {
            //画Y轴(带箭头)。
            canvas.drawLine(gridX, gridY - 20 - 10, gridX, 30 + 10 + 20, linePaint);
            canvas.drawLine(gridX, 30 + 10 + 20, gridX - 6, 30 + 14 + 10 + 20, linePaint);//Y轴箭头。
            canvas.drawLine(gridX, 30 + 10 + 20, gridX + 6, 30 + 14 + 10 + 20, linePaint);
            //画Y轴名字。
            //由于是竖直显示的，先以原点顺时针旋转90度后为新的坐标系
            canvas.rotate(-90);
            //当xyChartPaint的setTextAlign（）设置为center时第二、三个参数代表这四个字中点所在的xy坐标
            canvas.drawText("位移角度", -((float) (540) - 15 - 5 - 1 / ((float) 1.6 * 1) * (540) / 2), gridX - 15, mPaint);
            canvas.rotate(90);
            //画X轴。190x 480y
            float y1 = gridY - 20;
            canvas.drawLine(gridX, y1 - 10, 600, y1 - 10, linePaint);//X轴.
            canvas.drawLine(600, y1 - 10, 600 - 14, y1 - 6 - 10, linePaint);//X轴箭头。
            canvas.drawLine(600, y1 - 10, 600 - 14, y1 + 6 - 10, linePaint);
            //画z轴
            canvas.drawLine(gridX, y1 - 10, gridX - 70, y1 + 100, linePaint);//z
            canvas.drawLine(gridX - 72, y1 + 85, gridX - 70, y1 + 100, linePaint);
            canvas.drawLine(gridX - 55, y1 + 92, gridX - 70, y1 + 100, linePaint);
            //画角度
            canvas.drawLine(gridX, y1 - 10, gridX+190, 120, paintCircle);//β
            //RectF oval1=new RectF(150,20,180,40);
            RectF oval1 = new RectF(gridX + 30, y1 - 40, gridX + 50, y1);
            canvas.drawArc(oval1, 300, 80, false, linePaint);
            canvas.drawText("α", gridX + 100, y1 - 40, mPaint);
            oval1.set(gridX, y1 - 40, gridX + 30, y1 - 20);
            canvas.drawArc(oval1, 180, 180, false, linePaint);//小弧形
            canvas.drawText("β", gridX + 20, y1 - 100, mPaint);
            mPaint.setTextSize(40);
            canvas.drawText("α="+a+"°",gridX , 400, mPaint);
            canvas.drawText("β="+b+"°",gridX , 350, mPaint);
            canvas.drawText("深部位移", gridX+50, 40, mPaint);
        }
        mHolder.unlockCanvasAndPost(canvas);// 更新屏幕显示内容
    }


    public LineData getLineData(List<MySensor.MySensorData> datas, String[]labs){
        ArrayList<Entry> e1 = new ArrayList<Entry>();
        ArrayList<Entry> e2 = new ArrayList<Entry>();
        for (int i = 0; i < datas.size(); i++) {
            e1.add(new Entry(i,Float.valueOf( datas.get(i).getXdata())));
            e2.add(new Entry(i,Float.valueOf( datas.get(i).getYdata())));
        }
        LineDataSet d1 = new LineDataSet(e1, labs[0]);
        LineDataSet d2 = new LineDataSet(e2, labs[1]);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setDrawValues(false);
        d2.setDrawFilled(true);
        d1.setHighLightColor(Color.rgb(55, 247, 201));
        d1.setDrawValues(false);
        d1.setDrawFilled(true);
        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
        sets.add(d1);
        sets.add(d2);
        LineData cd = new LineData(sets);
        return cd;
    }

    private BarData getDataBar(List<MySensor.MySensorData> datas, String[]labs) {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        for (int i = 0; i < datas.size(); i++) {
            entries.add(new BarEntry(i, Float.valueOf(datas.get(i).getXdata())));
        }
        BarDataSet d = new BarDataSet(entries, labs[0]);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d.setHighLightAlpha(255);
        d.setDrawValues(false);
        BarData cd = new BarData(d);
        cd.setBarWidth(0.9f);
        return cd;
    }



}
