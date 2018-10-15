package com.zig.slope.charts;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.zig.slope.HisReportActivity;
import com.zig.slope.R;
import com.zig.slope.adapter.ChartDataAdapter;
import com.zig.slope.adapter.DatawAdapter;
import com.zig.slope.charts.listviewitems.BarChartItem;
import com.zig.slope.charts.listviewitems.ChartItem;
import com.zig.slope.charts.listviewitems.LineChartItem;
import com.zig.slope.common.base.BaseMvpActivity;
import com.zig.slope.common.base.bean.BaseResponseBean;
import com.zig.slope.common.base.bean.DataBean;
import com.zig.slope.common.base.bean.MySensor;
import com.zig.slope.contract.SensorContract;
import com.zig.slope.presenter.SensorPresenterImpl;

import java.util.ArrayList;
import java.util.List;

public class DataWarningActivity extends BaseMvpActivity<SensorContract.SensorView,SensorPresenterImpl>
        implements SensorContract.SensorView  {
    private List<DataBean> dataBeans;
    private  String newName;
    private int currentType = 0;
    private int currentPonit = 0;
    private int n = 0;
    private int type = 0;
    private RecyclerView recycler_view;
    private LinearLayout emptyView;
    private LinearLayoutManager layoutManager;
    private NestedScrollView nestedScrollView;
    private SwipeRefreshLayout refreshLayout;
    private DatawAdapter datawAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_data_warning;
    }
    @Override
    protected SensorPresenterImpl createPresenter() {
         return new SensorPresenterImpl();
    }
    @Override
    protected void findViews() {
        recycler_view = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(DataWarningActivity.this);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setVisibility(View.VISIBLE);
        emptyView = findViewById(R.id.empty_view);
        emptyView.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();
        newName = intent.getStringExtra("newName");
        currentType = intent.getIntExtra("currentType",3);
        currentPonit = intent.getIntExtra("currentPonit",1);
        n = intent.getIntExtra("safe",0);
        type = intent.getIntExtra("type",2);
        Log.i("zxy", "findViews: currentPonit=="+currentPonit+"===newName="+newName);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDataw);
        toolbar.setNavigationIcon(R.mipmap.return_icon);
        toolbar.setTitleMarginStart(230);
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        if(type==2){
            toolbar.setTitle(getResources().getString(R.string.sensorWarning));
        }
        if(type==4){
            toolbar.setTitle(getResources().getString(R.string.sensorWarning2));
        }
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataWarningActivity.this.finish();
            }
        });

        nestedScrollView = findViewById(R.id.nested_scroll_view);
        refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(DataWarningActivity.this, R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
               //     Toast.makeText(DataWarningActivity.this,getResources().getString(R.string.full_text),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void setViews() {
    }

    @Override
    protected void getData() {
        getPresenter().requestSensorData(DataWarningActivity.this,newName);
    }
    @Override
    public void onSensorSucess(BaseResponseBean<List<DataBean>> data) {
        dataBeans = data.getData();
        if(data.getCode()==2){
            showEmptyView(true);
            Toast.makeText(DataWarningActivity.this,getResources()
                    .getString(R.string.empty_tip),Toast.LENGTH_SHORT).show();
            return;
        }
        showEmptyView(false);

        if(datawAdapter==null) {
            datawAdapter  = new DatawAdapter(DataWarningActivity.this,dataBeans.get(0).getData(),new Handler());
            recycler_view.setAdapter(datawAdapter);
        }else{
            datawAdapter.updateData(dataBeans.get(0).getData(),true);
        }
        recycler_view.setAdapter(datawAdapter);
    }

    @Override
    public void onSensorFail(String msg) {
        showEmptyView(false);
    }
    public void showEmptyView(boolean toShow) {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        });
        emptyView.setVisibility(toShow?View.VISIBLE:View.INVISIBLE);
        nestedScrollView.setVisibility(!toShow?View.VISIBLE:View.INVISIBLE);
    }
}
