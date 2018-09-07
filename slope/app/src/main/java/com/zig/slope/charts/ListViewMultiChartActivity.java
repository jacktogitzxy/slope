package com.zig.slope.charts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.zig.slope.R;
import com.zig.slope.charts.listviewitems.BarChartItem;
import com.zig.slope.charts.listviewitems.ChartItem;
import com.zig.slope.charts.listviewitems.LineChartItem;
import com.zig.slope.charts.listviewitems.WyLineChartItem;

import java.util.ArrayList;
import java.util.List;

public class ListViewMultiChartActivity extends AppCompatActivity {
    private ScrollView myDataScro;
    private SurfaceView mSurface;
    private SurfaceHolder mHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_multi_chart);

        ListView lv = findViewById(R.id.listView1);
        myDataScro = findViewById(R.id.myDataScro);
        mSurface = (SurfaceView) findViewById(R.id.surface);
        mHolder = mSurface.getHolder();
        mSurface.setZOrderOnTop(true);
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarData);
        toolbar.setNavigationIcon(R.mipmap.return_icon);
        toolbar.setTitleMarginStart(250);
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setTitle(R.string.type_data);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListViewMultiChartActivity.this.finish();
            }
        });
        ArrayList<ChartItem> list = new ArrayList<ChartItem>();

        // 30 items
        for (int i = 0; i < 3; i++) {

            if(i % 3 == 0) {
                list.add(new LineChartItem(generateDataLine( 2), getApplicationContext()));
            } else if(i % 3 == 1) {
                list.add(new WyLineChartItem(generateDataLine(1), getApplicationContext()));
            } else if(i % 3 == 2) {
                list.add(new BarChartItem(generateDataBar(i + 1), getApplicationContext()));
            }
        }

        ChartDataAdapter cda = new ChartDataAdapter(getApplicationContext(), list);
        lv.setAdapter(cda);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                myDataScro.scrollTo(0,0);
            }
        },200);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                draw("39.8","12.0mm","30","60");
              //  draw("140.0","12.0mm","60","30");
            }
        },2000);

    }


    /** adapter that supports 3 different item types */
    private class ChartDataAdapter extends ArrayAdapter<ChartItem> {

        public ChartDataAdapter(Context context, List<ChartItem> objects) {
            super(context, 0, objects);
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

        @Override
        public int getViewTypeCount() {
            return 3; // we have 3 different item-types
        }
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private LineData generateDataLine(int cnt) {
        if(cnt==2) {
            ArrayList<Entry> e1 = new ArrayList<Entry>();

            for (int i = 0; i < 20; i++) {
                e1.add(new Entry(i, (float) (Math.random() * 10)));
            }

            LineDataSet d1 = new LineDataSet(e1, "α");
//            d1.setLineWidth(2.5f);
//            d1.setCircleRadius(4.5f);
            d1.setHighLightColor(Color.rgb(244, 117, 117));
            d1.setDrawValues(true);
            d1.setDrawFilled(true);
            ArrayList<Entry> e2 = new ArrayList<Entry>();

            for (int i = 0; i < 20; i++) {
                e2.add(new Entry(i, (float) (Math.random() * 10+20)));
            }

            LineDataSet d2 = new LineDataSet(e2, "β    内部位移");
//            d2.setLineWidth(2.5f);
//            d2.setCircleRadius(4.5f);
            d2.setHighLightColor(Color.rgb(244, 117, 117));
            d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
            d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
            d2.setDrawValues(true);
            d2.setDrawFilled(true);
            ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
            sets.add(d1);
            sets.add(d2);
            LineData cd = new LineData(sets);
            return cd;
        }else{
            ArrayList<Entry> e1 = new ArrayList<Entry>();

            for (int i = 0; i < 20; i++) {
                e1.add(new Entry(i, (float) (Math.random() * 10) + 10));
            }

            LineDataSet d1 = new LineDataSet(e1, "表面位移");
//            d1.setLineWidth(2.5f);
//            d1.setCircleRadius(4.5f);
            d1.setHighLightColor(Color.rgb(55, 247, 201));
            d1.setDrawValues(true);
            d1.setDrawFilled(true);
            ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
            sets.add(d1);
            LineData cd = new LineData(sets);
            return cd;
        }
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private BarData generateDataBar(int cnt) {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        for (int i = 0; i < 20; i++) {
            entries.add(new BarEntry(i, (int) (Math.random() * 70) + 30));
        }

        BarDataSet d = new BarDataSet(entries, "含水量 ");
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d.setHighLightAlpha(255);

        BarData cd = new BarData(d);
        cd.setBarWidth(0.9f);
        return cd;
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private PieData generateDataPie(int cnt) {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        for (int i = 0; i < 4; i++) {
            entries.add(new PieEntry((float) ((Math.random() * 70) + 30), "Quarter " + (i+1)));
        }

        PieDataSet d = new PieDataSet(entries, "");

        // space between slices
        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);

        PieData cd = new PieData(d);
        return cd;
    }



//    @SuppressLint("ResourceAsColor")
//    private void draw(String temp,String moveSzie,String  a,String b) {
//        float tem1 = Float.parseFloat(temp);
//        int y = 440 - (int) ((tem1 - 100)*4);
//        Canvas canvas = mHolder.lockCanvas();
//        Paint mPaint = new Paint();
//        mPaint.setTextSize(30);
//        mPaint.setColor(R.color.black_22);
//        canvas.drawRect(30, 50, 70, 480, mPaint);
//        Paint paintCircle = new Paint();
//        paintCircle.setColor(Color.GREEN);
//        Paint paintLine = new Paint();
//        paintLine.setColor(Color.BLUE);
//        canvas.drawRect(30, y, 70, 480, paintCircle);
//        canvas.drawCircle(50, 500, 50, paintCircle);
//        int ydegree = 440;
//        int tem = 100;
//        while (ydegree > 55) {
//            canvas.drawLine(80, ydegree, 95, ydegree, mPaint);
//            if (ydegree % 40 == 0) {
//                canvas.drawLine(80, ydegree, 92, ydegree, paintLine);
//                canvas.drawText(tem + "pa", 95, ydegree + 4, mPaint);
////                canvas.drawLine(300, ydegree+10, 330, ydegree, paintLine);
////                canvas.drawLine(570, ydegree, 600, ydegree+10, paintLine);
//            }
//            tem++;
//            ydegree = ydegree - 4;
//        }
//
//        canvas.rotate(10);
//        int ydegree2 = 440;
//        while (ydegree2 > 40) {
//            if (ydegree2 % 40 == 0) {
//                canvas.drawLine(320, ydegree2+10, 350, ydegree2, paintLine);
//                canvas.drawLine(590, ydegree2, 620, ydegree2+10, paintLine);
//            }
//            ydegree2 = ydegree2 - 40;
//        }
//        //画位移
//        canvas.drawRect(350, 0, 390, 500, mPaint);
//        canvas.drawRect(550, 0, 590, 500, mPaint);
//        canvas.drawLine(390, 300, 550, 300, mPaint);
//        canvas.drawText( "<", 390, 310, mPaint);
//        canvas.drawText( ">", 540, 310, mPaint);
//        canvas.drawText( "位移:"+moveSzie, 420, 350, mPaint);
//        canvas.rotate(-10);
//        //只是绘制的XY轴
//        Paint linePaint = new Paint();
//        linePaint.setStyle(Paint.Style.STROKE);
//        linePaint.setStrokeWidth((float)1.5);
//        linePaint.setColor(Color.BLACK);
//        linePaint.setAntiAlias(true);// 锯齿不显示
//
//
//        //基准点。
//        float gridX = 710;
//        float gridY = 550;
//
//        //画Y轴(带箭头)。
//        canvas.drawLine(gridX, gridY-20-10, gridX, 30+10+20, linePaint);
//        canvas.drawLine(gridX, 30+10+20, gridX-6, 30+14+10+20, linePaint);//Y轴箭头。
//        canvas.drawLine(gridX, 30+10+20, gridX+6, 30+14+10+20, linePaint);
//        //画Y轴名字。
//        //由于是竖直显示的，先以原点顺时针旋转90度后为新的坐标系
//        canvas.rotate(-90);
//        //当xyChartPaint的setTextAlign（）设置为center时第二、三个参数代表这四个字中点所在的xy坐标
//        canvas.drawText("位移角度", -((float)(540)-15-5 - 1/((float)1.6*1) * (540)/2), gridX-15, mPaint);
//        canvas.rotate(90); //改变了坐标系还要再改过来
//        //画X轴。190x 480y
//        float y1 = gridY-20;
//        canvas.drawLine(gridX, y1-10, 900, y1-10, linePaint);//X轴.
//        canvas.drawLine(900, y1-10, 900-14, y1-6-10, linePaint);//X轴箭头。
//        canvas.drawLine(900, y1-10, 900-14, y1+6-10, linePaint);
//        //画z轴
//        canvas.drawLine(gridX, y1-10, gridX+40,y1+60, linePaint);//z
//        canvas.drawLine(gridX+40, y1+43, gridX+40,y1+60, linePaint);
//        canvas.drawLine(gridX+25, y1+50, gridX+40,y1+60, linePaint);
//        //画角度
//        canvas.drawLine(gridX, y1-10, 900, 80, paintCircle);//β
//        RectF oval1=new RectF(150,20,180,40);
//        oval1.set(gridX, y1-50, gridX+40, y1-10);
//        canvas.drawArc(oval1, 270, 140, false, linePaint);
//        canvas.drawText("α", 800, 450, mPaint);
//
//        mHolder.unlockCanvasAndPost(canvas);
//
//    }



    @SuppressLint("ResourceAsColor")
    private void draw(String temp,String moveSize,String a,String b) {
        float tem1 = Float.parseFloat(temp);
        int y = 260 - (int) ((tem1 - 35) * 20);
        Canvas canvas = mHolder.lockCanvas();
        Paint mPaint = new Paint();
        mPaint.setTextSize(20);
        mPaint.setColor(R.color.black_22);
        canvas.drawRect(40, 50, 60, 280, mPaint);
        Paint paintCircle = new Paint();
        paintCircle.setColor(Color.GREEN);
        Shader mShader = new LinearGradient(0, 0, 100, 100,
                new int[] { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
                        Color.LTGRAY }, null, Shader.TileMode.REPEAT);

        paintCircle.setShader(mShader);
        Paint paintLine = new Paint();
        paintLine.setColor(Color.BLUE);
        canvas.drawRect(40, y, 60, 280, paintCircle);
        canvas.drawLine(60, y, 165, y, paintLine);
        canvas.drawText( "当前含水量 ", 165, y, paintLine);
        canvas.drawCircle(50, 300, 25, paintCircle);
        int ydegree = 260;
        int tem = 35;
        while (ydegree > 55) {
            canvas.drawLine(60, ydegree, 67, ydegree, mPaint);
            if (ydegree % 20 == 0) {
                canvas.drawLine(60, ydegree, 72, ydegree, paintLine);
                canvas.drawText(tem + "", 70, ydegree + 4, paintLine);
                tem++;
            }
            ydegree = ydegree - 2;
        }


        canvas.rotate(10);
        int ydegree2 = 260;
        while (ydegree2 > 55) {
            if (ydegree2 % 20 == 0) {
                canvas.drawLine(320, ydegree2+10, 350, ydegree2, paintLine);
                canvas.drawLine(490, ydegree2, 520, ydegree2+10, paintLine);
            }
            ydegree2 = ydegree2 - 20;
        }
        //画位移
        canvas.drawRect(350, 0, 390, 260, mPaint);
        canvas.drawRect(450, 0, 490, 270, mPaint);
        canvas.drawLine(390, 150, 450, 150, mPaint);
        canvas.rotate(-10);



        Paint linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth((float)1.5);
        linePaint.setColor(Color.BLACK);
        linePaint.setAntiAlias(true);// 锯齿不显示


        //基准点。
        float gridX = 610;
        float gridY = 280;

        //画Y轴(带箭头)。
        canvas.drawLine(gridX, gridY-20-10, gridX, 30+10+20, linePaint);
        canvas.drawLine(gridX, 30+10+20, gridX-6, 30+14+10+20, linePaint);//Y轴箭头。
        canvas.drawLine(gridX, 30+10+20, gridX+6, 30+14+10+20, linePaint);
        //画Y轴名字。
        //由于是竖直显示的，先以原点顺时针旋转90度后为新的坐标系
        canvas.rotate(-90);
        //当xyChartPaint的setTextAlign（）设置为center时第二、三个参数代表这四个字中点所在的xy坐标
        canvas.drawText("位移角度", -((float)(540)-15-5 - 1/((float)1.6*1) * (540)/2), gridX-15, mPaint);
        canvas.rotate(90);
        //画X轴。190x 480y
        float y1 = gridY-20;
        canvas.drawLine(gridX, y1-10, 800, y1-10, linePaint);//X轴.
        canvas.drawLine(800, y1-10, 800-14, y1-6-10, linePaint);//X轴箭头。
        canvas.drawLine(800, y1-10, 800-14, y1+6-10, linePaint);
        //画z轴
        canvas.drawLine(gridX, y1-10, gridX+80,y1+100, linePaint);//z
        canvas.drawLine(gridX+78, y1+80, gridX+80,y1+100, linePaint);
        canvas.drawLine(gridX+60, y1+90, gridX+80,y1+100, linePaint);
        //画角度
        canvas.drawLine(gridX, y1-10, 800, 120, paintCircle);//β
        //RectF oval1=new RectF(150,20,180,40);
        RectF oval1=new RectF(gridX+30, y1-40, gridX+50, y1);
        canvas.drawArc(oval1, 300, 80, false, linePaint);
        canvas.drawText("α",gridX+100 , y1-40, mPaint);

        oval1.set(gridX, y1-40, gridX+30, y1-20);
        canvas.drawArc(oval1, 180, 180, false, linePaint);//小弧形
        canvas.drawText("β",gridX+20 , y1-100, mPaint);
        mPaint.setTextSize(40);
        canvas.drawText( "含水量:"+tem1, 10, 400, mPaint);
        canvas.drawText( "位移:"+moveSize, 300, 400, mPaint);
        canvas.drawText("α="+a+";"+"β="+b,gridX+50 , 400, mPaint);
        canvas.drawText("温湿度传感器",20 , 40, mPaint);
        canvas.drawText("表面位移传感器",300 , 40, mPaint);
        canvas.drawText("内部位移传感器",600 , 40, mPaint);
        mHolder.unlockCanvasAndPost(canvas);// 更新屏幕显示内容

    }

}
