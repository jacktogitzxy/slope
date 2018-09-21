package com.zig.slope.charts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
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
import com.github.mikephil.charting.data.BaseDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.zig.slope.R;
import com.zig.slope.adapter.ChartDataAdapter;
import com.zig.slope.charts.listviewitems.BarChartItem;
import com.zig.slope.charts.listviewitems.ChartItem;
import com.zig.slope.charts.listviewitems.LineChartItem;
import com.zig.slope.charts.listviewitems.WyLineChartItem;
import com.zig.slope.common.base.BaseMvpActivity;
import com.zig.slope.common.base.bean.BaseResponseBean;
import com.zig.slope.common.base.bean.DataBean;
import com.zig.slope.common.base.bean.MySensor;
import com.zig.slope.contract.SensorContract;
import com.zig.slope.presenter.SensorPresenterImpl;

import java.util.ArrayList;
import java.util.List;

public class ListViewMultiChartActivity extends BaseMvpActivity<SensorContract.SensorView,SensorPresenterImpl>
        implements SensorContract.SensorView  {
    private ScrollView myDataScro;
    private SurfaceView mSurface;
    private SurfaceHolder mHolder;
    private ListView    lv;
    private List<DataBean> dataBeans;
    private  String newName;
    private boolean isCurrentType = true;





    @SuppressLint("ResourceAsColor")
    private void draw(String temp,String moveSize,String a,String b) {
        float tem1 = Float.parseFloat(temp)/10;
        int y = 260 - (int) ((tem1 - 5) * 20);
        if(y<0){
            y=0;
        }
        Canvas canvas = mHolder.lockCanvas();
        Paint mPaint = new Paint();
        mPaint.setTextSize(20);
        mPaint.setColor(R.color.black_22);
        canvas.drawRect(40, 50, 60, 280, mPaint);
        Paint paintCircle = new Paint();
        paintCircle.setColor(Color.GREEN);
//        Shader mShader = new LinearGradient(0, 0, 100, 100,
//                new int[] { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
//                        Color.LTGRAY }, null, Shader.TileMode.REPEAT);
//
//        paintCircle.setShader(mShader);
        Paint paintLine = new Paint();
        paintLine.setColor(Color.BLUE);
        canvas.drawRect(40, y, 60, 280, paintCircle);
        canvas.drawLine(60, y, 165, y, paintLine);
        canvas.drawText( "当前水压 ", 165, y, paintLine);
        canvas.drawCircle(50, 300, 25, paintCircle);
        int ydegree = 260;
        int tem = 5;
        while (ydegree > 55) {
            canvas.drawLine(60, ydegree, 67, ydegree, mPaint);
            if (ydegree % 20 == 0) {
                canvas.drawLine(60, ydegree, 72, ydegree, paintLine);
                canvas.drawText(tem*10 + "", 70, ydegree + 4, paintLine);
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
        canvas.drawText( "水压:"+tem1+"KPa", 10, 400, mPaint);
        canvas.drawText( "位移:"+moveSize+"mm", 300, 400, mPaint);
        canvas.drawText("α="+a+"°;"+"β="+b+"°",gridX , 400, mPaint);
        canvas.drawText("孔隙水压",20 , 40, mPaint);
        canvas.drawText("表面变形",300 , 40, mPaint);
        canvas.drawText("深部位移",600 , 40, mPaint);
        mHolder.unlockCanvasAndPost(canvas);// 更新屏幕显示内容

    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_list_view_multi_chart;
    }

    @Override
    protected SensorPresenterImpl createPresenter() {
        return new SensorPresenterImpl();
    }

    @Override
    protected void findViews() {
        Intent intent = getIntent();
        newName = intent.getStringExtra("newName");
        lv = findViewById(R.id.listView1);
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
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarData);
        toolbar.setNavigationIcon(R.mipmap.return_icon);
        toolbar.setTitleMarginStart(180);
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setTitle("编号"+newName+getResources().getString(R.string.type_data));
        toolbar.setSubtitle("         地质监测点1");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListViewMultiChartActivity.this.finish();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String text = item.getTitle().toString();
                toolbar.setSubtitle("         "+text);
                isCurrentType = toolbar.getSubtitle().toString().trim().equals(item.getTitle().toString().trim());
                if(!isCurrentType){
                    int index = item.getItemId();
                    drawData(index);
                }
                return true;
            }
        });
    }

    @Override
    protected void setViews() {

    }

    @Override
    protected void getData() {
        getPresenter().requestSensorData(ListViewMultiChartActivity.this,"409");
    }

    @Override
    public void onSensorSucess(BaseResponseBean<List<DataBean>> data) {
        dataBeans = data.getData();
        drawData(0);
        Log.i("zxy", "onSensorSucess: dataBeans=="+dataBeans.size());
    }

    @Override
    public void onSensorFail(String msg) {
        Log.i("zxy", "onSensorFail: msg="+msg);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
       menu.clear();
        menu.add(0, 0, 0, "地质监测点1").setIcon(R.drawable.ic_router_black_24dp);
        if(dataBeans!=null&&dataBeans.size()>0) {
            for (int i = 2; i <= dataBeans.size(); i++) {
                menu.add(0, i-1, 0, "地质监测点" + i).setIcon(R.drawable.ic_router_black_24dp);
            }
        }
        return true;
    }

    public void drawData(int i){
        DataBean dataBean =  dataBeans.get(i);
        List<MySensor>mySensors =dataBean.getData();
        ArrayList<ChartItem> list = new ArrayList<ChartItem>();
        String p="0",a="0",b="0",m="0";
        for (int x = 0;x<mySensors.size();x++){
            MySensor mySensor = mySensors.get(x);
            int type = mySensor.getType_s();
            String sensorId = mySensor.getSensorId();
            List<MySensor.MySensorData> datas = mySensor.getData();
            if(type==1) {//表面位移
                m=datas.get(0).getXdata();
                String[]labs = new String[]{"x","y表面变形(mm)         周期（"+datas.get(0).getFrequency()+"）"};
                list.add(new LineChartItem(getLineData(datas,labs),ListViewMultiChartActivity.this,2));
            }
            if(type==2){//内部位移
                a=datas.get(0).getXdata();
                b=datas.get(0).getXdata();
                String[]labs = new String[]{"x","y深部位移(°)         周期（"+datas.get(0).getFrequency()+"）"};
                list.add(new LineChartItem(getLineData(datas,labs),ListViewMultiChartActivity.this,1));
            }
            if (type==3){//水压
                p=datas.get(0).getXdata();
                String[]labs = new String[]{"孔隙水压(kpa)         周期（"+datas.get(0).getFrequency()+"）"};
                list.add(new BarChartItem(getDataBar(datas,labs),ListViewMultiChartActivity.this));
            }
        }
        Log.i("zxy", "drawData: list=="+list.size());
        ChartDataAdapter cda = new ChartDataAdapter(getApplicationContext(), list);
        lv.setAdapter(cda);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                myDataScro.scrollTo(0,0);
            }
        },1000);
        draw(p,m,a,b);


    }
    public LineData getLineData( List<MySensor.MySensorData> datas,String[]labs){
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
        d2.setDrawValues(true);
        d2.setDrawFilled(true);
        d1.setHighLightColor(Color.rgb(55, 247, 201));
        d1.setDrawValues(true);
        d1.setDrawFilled(true);
        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
        sets.add(d1);
        sets.add(d2);
        LineData cd = new LineData(sets);
        return cd;
    }

    private BarData getDataBar(List<MySensor.MySensorData> datas,String[]labs) {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        for (int i = 0; i < datas.size(); i++) {
            entries.add(new BarEntry(i, Float.valueOf(datas.get(i).getXdata())));
        }
        BarDataSet d = new BarDataSet(entries, labs[0]);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d.setHighLightAlpha(255);
        BarData cd = new BarData(d);
        cd.setBarWidth(0.9f);
        return cd;
    }
    public void changeFrequency(View v){

    }
}
