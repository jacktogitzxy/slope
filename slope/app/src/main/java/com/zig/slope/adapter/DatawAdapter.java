package com.zig.slope.adapter;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
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
import com.zig.slope.bean.DataWarnBean;
import com.zig.slope.bean.SensorWarn;
import com.zig.slope.charts.listviewitems.BarChartItem;
import com.zig.slope.charts.listviewitems.ChartItem;
import com.zig.slope.charts.listviewitems.LineChartItem;
import com.zig.slope.common.base.bean.MySensor;
import com.zig.slope.common.utils.TimeUtils;
import com.zig.slope.util.ToolUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by CoderLengary
 */


public class DatawAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity context;
    private LayoutInflater inflater;
    private List<DataWarnBean> mList;
    private OnRecyclerViewItemOnClickListener listener;
    private Handler handler;
    private String newName;


    public DatawAdapter(Activity context, List<DataWarnBean> list,Handler handler,String newName){
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        mList = list;
        this.handler=handler;
        this.newName=newName;
    }

    public void updateData(List<DataWarnBean> list,boolean isRefresh){
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
        DataWarnBean data = mList.get(position);
        int currentType=data.getType_s();
        String tyeps = "表面位移传感器";
        if(currentType==1){
            tyeps = "表面位移传感器";
            normalViewHolder.ivIndexblue.setVisibility(View.GONE);
            normalViewHolder.iv_dashboard.setImageResource(R.mipmap.dial);
            normalViewHolder.textViewS1.setText(context.getResources().getString(R.string.biaomian));
        }
        if(currentType==2){
            tyeps = "深部位移传感器";
            normalViewHolder.ivIndexblue.setVisibility(View.VISIBLE);
            normalViewHolder.iv_dashboard.setImageResource(R.mipmap.dials);
            normalViewHolder.textViewS1.setText(context.getResources().getString(R.string.shenbu));
        }
        if(currentType==3){
            tyeps = "孔隙水压传感器";
            normalViewHolder.ivIndexblue.setVisibility(View.GONE);
            normalViewHolder.iv_dashboard.setImageResource(R.mipmap.dialp);
            normalViewHolder.textViewS1.setText(context.getResources().getString(R.string.shuiya));
        }
        String s = String.format(context.getResources().getString(R.string.titlew), newName,
                data.getSensorId(),tyeps);
        normalViewHolder.titlew.setText(s);
        drawData(data,normalViewHolder);
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
        //        SurfaceView surfacew;
        ListView listVieww;
        //        SurfaceHolder mHolder;
        TextView textViewS1;
        private ImageView ivIndex,ivIndexblue,iv_dashboard;
        private TextView tvRatio;
        public NormalViewHolder(View itemView, final OnRecyclerViewItemOnClickListener listener) {
            super(itemView);
            this.listener = listener;
            ivIndex = (ImageView) itemView.findViewById(R.id.iv_index);
            tvRatio = (TextView) itemView.findViewById(R.id.tv_ratio);
            titlew = itemView.findViewById(R.id.titlew);
            iv_dashboard = itemView.findViewById(R.id.iv_dashboard);
            ivIndexblue = itemView.findViewById(R.id.iv_index2);
//            surfacew = itemView.findViewById(R.id.surfacew);
            listVieww = itemView.findViewById(R.id.listVieww);
            textViewS1 = itemView.findViewById(R.id.s1);
//            surfacew.setZOrderOnTop(true);
//            mHolder = surfacew.getHolder();
//            mHolder.setFormat(PixelFormat.TRANSLUCENT);
//            mHolder.addCallback(new SurfaceHolder.Callback() {
//                @Override
//                public void surfaceCreated(SurfaceHolder surfaceHolder) {
//                }
//
//                @Override
//                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
//                }
//
//                @Override
//                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
//                }
//            });
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                default:break;

            }
        }
    }
    private void ivRotate(double percent, double percent2, NormalViewHolder normalViewHolder, int type) {
        normalViewHolder.tvRatio.setText(String.valueOf(percent));
        if(type==1) {//表面位移
            double percentOffset = percent > 100 ? 100 : percent;
            RotateAnimation rotateAnimation = new RotateAnimation(270f, 270f + 180 * ((int) percentOffset / 100f),
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
            rotateAnimation.setDuration(1500);
            rotateAnimation.setFillAfter(true);
            normalViewHolder.ivIndex.startAnimation(rotateAnimation);
        }else if(type==2){//深部位移
            normalViewHolder.tvRatio.setText("x:"+String.valueOf(percent)+"\n"+"y:"+String.valueOf(percent2));
            double percentOffset = percent > 30 ? 30: percent;
            float value;
            if(percentOffset<=0) {
                if(percentOffset<-30){
                    percentOffset=-30;
                }
                 value = 180 * (int) percentOffset / 60f * -1;
            }else{
                 value = 180 * (int) percentOffset / 60f+90f;
            }
            RotateAnimation rotateAnimation = new RotateAnimation(270f, 270f + value,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
            rotateAnimation.setDuration(1500);
            rotateAnimation.setFillAfter(true);
            normalViewHolder.ivIndex.startAnimation(rotateAnimation);
            double percentOffset2 = percent2 > 30 ? 30 : percent2;
            if(percentOffset2<=0) {
                value = 180 * (int) percentOffset2 / 60f * -1;
            }else{
                if(percentOffset2>30){
                    percentOffset2=30;
                }
                value = 180 * (int) percentOffset2 / 60f+90f;
            }
            RotateAnimation rotateAnimation2 = new RotateAnimation(270f, 270f + value,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
            rotateAnimation2.setDuration(1500);
            rotateAnimation2.setFillAfter(true);
            normalViewHolder.ivIndexblue.startAnimation(rotateAnimation2);
        }else if(type==3){
            double percentOffset = percent > 200 ? 200 : percent;
            if(percentOffset<90){
                percentOffset=90;
            }
            float value = 180 * ((int) percentOffset-90)/110f;
            RotateAnimation rotateAnimation = new RotateAnimation(270f, 270f + value,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
            rotateAnimation.setDuration(1500);
            rotateAnimation.setFillAfter(true);
            normalViewHolder.ivIndex.startAnimation(rotateAnimation);
        }
    }

    public void drawData(final DataWarnBean mySensor, final NormalViewHolder normalViewHolder){
        List<SensorWarn>datas =mySensor.getList();
        ArrayList<ChartItem> list = new ArrayList<ChartItem>();
        String sensorId = mySensor.getSensorId();
        String a="0",b="0";
        final int  type = mySensor.getType_s();
        if(mySensor.getType_s()==1){
            a=datas.get(datas.size()-1).getXdata();
            String[]labs = new String[]{"x","y表面变形(mm)     日期("+TimeUtils.transleteTime3(datas.get(0).getCreateTime())+")"};
            LineChartItem lineChartItem = new LineChartItem(getLineData(datas,labs),context,2,sensorId,false);
            lineChartItem.setXdata(getxDatas(datas));
            list.add(lineChartItem);
        }else if(mySensor.getType_s()==2){
            a=datas.get(datas.size()-1).getXdata();
            b=datas.get(datas.size()-1).getYdata();
            Log.i("zxy", "run: finalA=="+a);
            String[]labs = new String[]{"x","y深部位移(°)         周期（"+11+"）    日期("+TimeUtils.transleteTime3(datas.get(0).getCreateTime())+")"};
            LineChartItem lineChartItem =new LineChartItem(getLineData(datas,labs),context,1,sensorId,false);
            lineChartItem.setXdata(getxDatas(datas));
            list.add(lineChartItem);
        }else{
            a=datas.get(datas.size()-1).getXdata();
            String[]labs = new String[]{"孔隙水压(kpa)       日期("+TimeUtils.transleteTime3(datas.get(0).getCreateTime())+")"};
            BarChartItem barChartItem = new BarChartItem(getDataBar(datas,labs),context,sensorId,false);
            barChartItem.setXdata(getxDatas(datas));
            list.add(barChartItem);
        }
        ChartDataAdapter cda = new ChartDataAdapter(context, list);
        final String finalA = a;
        final String finalB = b;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("zxy", "run: finalA=="+finalA);
                ivRotate(Double.parseDouble(finalA),Double.parseDouble(finalB),normalViewHolder, type);
                //    drawx(finalP, finalM, finalA, finalB,mySensor.getData().get(0).getxCriticality(),mySensor.getType_s(),mHolder);
            }
        },1000);

        normalViewHolder.listVieww.setAdapter(cda);
    }
    public ArrayList<String> getxDatas(List<SensorWarn> datas){
        ArrayList<String> e3 = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            e3.add(TimeUtils.transleteTime2(datas.get(i).getCreateTime()));
        }
        return e3;
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
            canvas.drawText("临界位移:" + n + "mm", 300, 460, mPaint);
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


    public LineData getLineData(List<SensorWarn> datas, String[]labs){
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

    private BarData getDataBar(List<SensorWarn> datas, String[]labs) {

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
