package com.zig.slope;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.LinearLayout;

import com.youth.banner.Banner;
import com.zig.slope.adapter.HisAdapter;
import com.zig.slope.adapter.OnRecyclerViewItemOnClickListener;
import com.zig.slope.common.base.BaseMvpActivity;
import com.zig.slope.common.base.bean.HisReport;
import com.zig.slope.common.utils.PreferenceManager;
import com.zig.slope.contract.HisContract;
import com.zig.slope.presenter.HisPresenterImpl;

import java.util.List;

import slope.zxy.com.login_module.contract.LoginContract;
import slope.zxy.com.login_module.presenter.LoginPresenterImpl;

public class HisReportActivity extends BaseMvpActivity<HisContract.HisReportView,HisPresenterImpl>
        implements HisContract.HisReportView {
    public String TAG = HisReportActivity.class.getName();
    private PreferenceManager pm;
    private NestedScrollView nestedScrollView;
    private RecyclerView recyclerView;
    private LinearLayout emptyView;
    private SwipeRefreshLayout refreshLayout;
//    private Banner banner;
    private  final int INDEX = 0;
    private LinearLayoutManager layoutManager;
    private int currentPage;
    private   String admin;
    private HisAdapter adapter;
    private Toolbar toolbar;
    private boolean isFirstLoad=true;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_his_report;
    }

    @Override
    protected HisPresenterImpl createPresenter() {
        return new HisPresenterImpl();
    }

    @Override
    protected void findViews() {
        emptyView = findViewById(R.id.empty_view);
        layoutManager = new LinearLayoutManager(HisReportActivity.this);
        nestedScrollView = findViewById(R.id.nested_scroll_view);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.INVISIBLE);
        refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(HisReportActivity.this, R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = INDEX;
                getPresenter().requestLoginData(HisReportActivity.this,admin);
            }
        });
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
//                    loadMore();
                }
            }
        });
        toolbar = (Toolbar) findViewById(R.id.toolbarhis);
        toolbar.setNavigationIcon(R.mipmap.return_icon);
        toolbar.setTitleMarginStart(200);
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setTitle(R.string.type_his);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              HisReportActivity.this.finish();
            }
        });

    }

    @Override
    protected void setViews() {

    }

    @Override
    protected void getData() {
        pm = PreferenceManager.getInstance(HisReportActivity.this);
        admin = pm.getPackage("operatorId");
        getPresenter().requestLoginData(HisReportActivity.this,admin);
    }

    @Override
    public void onHisSucess(final List<HisReport> data) {
        Log.i(TAG, "onHisSucess: data=="+data.size());
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        });
        if (adapter!=null){
            adapter.updateData(data);
        }else {
            adapter = new HisAdapter(HisReportActivity.this, data);

            adapter.setItemClickListener(new OnRecyclerViewItemOnClickListener() {
                @Override
                public void onClick(View view, int position) {

                        Intent intent = new Intent(HisReportActivity.this,HisReportDetilActivity.class);
                        intent.putExtra("data",data.get(position));
                        startActivity(intent);
                }
            });
//            adapter.setHeaderView(banner);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onHisFail(String msg) {
        Log.i(TAG, "onHisFail: msg=="+msg);
        emptyView.setVisibility(View.VISIBLE);
        nestedScrollView.setVisibility(View.INVISIBLE);
    }
}
