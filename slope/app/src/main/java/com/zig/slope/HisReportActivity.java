package com.zig.slope;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zig.slope.adapter.HisAdapter;
import com.zig.slope.adapter.OnRecyclerViewItemOnClickListener;
import com.zig.slope.common.base.BaseMvpActivity;
import com.zig.slope.common.base.bean.HisBean;
import com.zig.slope.common.base.bean.HisReport;
import com.zig.slope.common.utils.PreferenceManager;
import com.zig.slope.contract.HisContract;
import com.zig.slope.presenter.HisPresenterImpl;
import com.zig.slope.util.NetworkUtil;

import java.util.ArrayList;
import java.util.List;
public class HisReportActivity extends BaseMvpActivity<HisContract.HisReportView,HisPresenterImpl>
        implements HisContract.HisReportView {
    public String TAG = HisReportActivity.class.getName();
    private PreferenceManager pm;
    private NestedScrollView nestedScrollView;
    private RecyclerView recyclerView;
    private LinearLayout emptyView;
    private SwipeRefreshLayout refreshLayout;
    private List<HisReport> data;
    private List<HisReport> datap = new ArrayList<>();
    private  final int INDEX = 1;//首页
    private LinearLayoutManager layoutManager;
    private int currentPage;//当前页
    private int totalPage = 1;//总页
    private   String admin;
    private HisAdapter adapter;
    private Toolbar toolbar;
    private String NOTDO = "queryInspectionResultsApp";//未审核
    private String ADMINDO = "queryInspectionResultsListAdminApp";//管理审核
    private  String LEADERDO = "queryInspectionResultsListLeaderApp";//领导审核
    private boolean isCurrentType = true;
    private  String CURRENTTYPE=NOTDO;
    private String operatorLevel,operatorName;
    private int type=1;//1 边坡 2危房  3三防 4地陷 5工地  6河道 7全部
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
        Intent intent = getIntent();
        type = intent.getIntExtra("type",1);//查询类型
        pm = PreferenceManager.getInstance(HisReportActivity.this);
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
                getTypeData();
            }
        });
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    if(currentPage<totalPage) {
                        loadMore();
                    }else{
                        Toast.makeText(HisReportActivity.this,getResources().getString(R.string.full_text),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        toolbar = (Toolbar) findViewById(R.id.toolbarhis);
        toolbar.setNavigationIcon(R.mipmap.return_icon);
        toolbar.setTitleMarginStart(250);
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setTitle(R.string.type_his);
        toolbar.setTitleTextColor(getResources().getColor(R.color.main_color));
        toolbar.setSubtitle(R.string.notdo);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              HisReportActivity.this.finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.i(TAG, "onMenuItemClick: item="+item.getTitle());
                isCurrentType = toolbar.getSubtitle().equals(item.getTitle());
                Log.i(TAG, "onMenuItemClick: isCurrentType=="+isCurrentType);
                if(!isCurrentType) {//分类查询
                    if (item.getItemId() == R.id.notdo) {
                        CURRENTTYPE = NOTDO;
                    }
                    if (item.getItemId() == R.id.admindo) {
                        CURRENTTYPE = ADMINDO;
                    }
                    if (item.getItemId() == R.id.leaderdo) {
                        CURRENTTYPE = LEADERDO;
                    }
                    currentPage=INDEX;
                    getTypeData();
                    toolbar.setSubtitle(item.getTitle());
                }
                return true;
            }
        });


    }

    @Override
    protected void setViews() {

    }

    public void getTypeData(){
            getPresenter().requestHisData(HisReportActivity.this, admin, currentPage, CURRENTTYPE,type);
    }
    @Override
    protected void getData() {
        operatorLevel = pm.getPackage("operatorLevel");
        admin = pm.getPackage("operatorId");
        currentPage = INDEX;
        operatorName = pm.getPackage("operatorName");
        getPresenter().requestHisData(HisReportActivity.this,admin,INDEX,NOTDO,type);
    }

    @Override
    public void onHisSucess(final HisBean databean) {
        totalPage = databean.getPageTotal();
        data = databean.getInspectList();
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        });
        if(isCurrentType){
        if (adapter!=null){
            if(currentPage==INDEX) {
                datap = data;
            }else{
               datap.addAll(data);
            }
             adapter.updateData(data, currentPage == INDEX);
        }else {
            if (data == null || data.size() == 0) {
                showEmptyView(true);
                return;
            }
            showEmptyView(false);
            adapter = new HisAdapter(HisReportActivity.this, data,type);
            setListeners();
            recyclerView.setAdapter(adapter);
            datap = data;
        }
        }else{
            if(datap!=null) {
                datap.clear();
            }
            datap = data;
            if (data == null || data.size() == 0) {
                showEmptyView(true);
                return;
            }
            showEmptyView(false);
            if(adapter==null){
                adapter = new HisAdapter(HisReportActivity.this, data,type);
                setListeners();
                recyclerView.setAdapter(adapter);
            }else {
                adapter.updateData(data, true);
            }
            isCurrentType = true;
        }

    }

    private void setListeners(){
        adapter.setItemClickListener(new OnRecyclerViewItemOnClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(HisReportActivity.this, HisReportDetilActivity.class);
                intent.putExtra("data", datap.get(position));
                intent.putExtra("operatorLevel",operatorLevel);
                intent.putExtra("flag",datap.get(position).getFlag());
                intent.putExtra("operatorName",operatorName);
                intent.putExtra("type",type);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onHisFail(String msg) {
        Log.i(TAG, "onHisFail: msg=="+msg);
       showEmptyView(true);
    }

    @Override
    public void showEmptyView(boolean toShow) {
        emptyView.setVisibility(toShow?View.VISIBLE:View.INVISIBLE);
        nestedScrollView.setVisibility(!toShow?View.VISIBLE:View.INVISIBLE);
    }

    private void loadMore(){
        boolean isNetworkAvailable = NetworkUtil.isNetworkAvailable(HisReportActivity.this);
        if (isNetworkAvailable){
            currentPage+=1;
            getTypeData();
        }else {
            Toast.makeText(HisReportActivity.this,R.string.network_error,Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }


}
