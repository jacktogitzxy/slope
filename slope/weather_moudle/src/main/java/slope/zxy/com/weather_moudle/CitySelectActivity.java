package slope.zxy.com.weather_moudle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import slope.zxy.com.weather_moudle.adapter.CityListAdapter;
import slope.zxy.com.weather_moudle.adapter.ResultListAdapter;
import slope.zxy.com.weather_moudle.bean.City;
import slope.zxy.com.weather_moudle.db.DBManager;
import slope.zxy.com.weather_moudle.utils.LocateState;

public class CitySelectActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String KEY_PICKED_CITY = "picked_city";

    private ListView mListView;
    private ListView mResultListView;
    private SideLetterBar mLetterBar;
    private EditText mSearchEditText;
    private ImageView mClearImageView;
    private ImageView mBackImageView;
    private ViewGroup mEmptyView;

    private CityListAdapter mCityAdapter;
    private ResultListAdapter mResultAdapter;
    private List<City> mAllCities;
    private DBManager dbManager;
    private String localCity = "深圳";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_select);
        Intent intent= getIntent();
        localCity = intent.getStringExtra("city");
        initData();
        initView();
    }


    private void initData() {
        dbManager = new DBManager(this);//初始化数据库
        dbManager.copyDBFile();//复制数据库
        mAllCities = dbManager.getAllCities();//获取到所有城市
        mCityAdapter = new CityListAdapter(this, mAllCities);//设置适配器
        mCityAdapter.updateLocateState(LocateState.SUCCESS, localCity);
        mCityAdapter.setOnCityClickListener(new CityListAdapter.OnCityClickListener() {
            @Override
            public void onCityClick(String name) {
                back(name);
            }
            @Override
            public void onLocateClick() {
                back(localCity);
            }
        });
        mResultAdapter = new ResultListAdapter(this, null);
    }
    /**
     * 初始化视图
     */
    private void initView() {
        mListView = (ListView) findViewById(R.id.listview_all_city);
        mListView.setAdapter(mCityAdapter);
        TextView overlay = (TextView) findViewById(R.id.tv_letter_overlay);
        mLetterBar = (SideLetterBar) findViewById(R.id.side_letter_bar);
        mLetterBar.setOverlay(overlay);
        mLetterBar.setmOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
                int position = mCityAdapter.getLetterPosition(letter);//获取到letter所在的位置
                mListView.setSelection(position);
            }
        });

        mSearchEditText = (EditText) findViewById(R.id.et_search);
        //文字框输入监听
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString();
                if (TextUtils.isEmpty(keyword)) {
                    mClearImageView.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.GONE);
                    mResultListView.setVisibility(View.GONE);
                } else {
                    mClearImageView.setVisibility(View.VISIBLE);
                    mResultListView.setVisibility(View.VISIBLE);
                    List<City> result = dbManager.searchCity(keyword);
                    if (result == null || result.size() == 0) {
                        mEmptyView.setVisibility(View.VISIBLE);
                    } else {
                        mEmptyView.setVisibility(View.GONE);
                        mResultAdapter.changeData(result);
                    }
                }
            }
        });

        mEmptyView = (ViewGroup) findViewById(R.id.empty_view);
        mResultListView = (ListView) findViewById(R.id.listview_search_result);
        mResultListView.setAdapter(mResultAdapter);
        mResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                back(mResultAdapter.getItem(position).getName());
            }
        });

        mClearImageView = (ImageView) findViewById(R.id.iv_search_clear);
        mBackImageView = (ImageView) findViewById(R.id.back);

        mClearImageView.setOnClickListener(this);
        mBackImageView.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void back(String city) {
        Intent data = new Intent();
        data.putExtra(KEY_PICKED_CITY, city);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_search_clear) {
            mSearchEditText.setText("");
            mClearImageView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.GONE);
            mResultListView.setVisibility(View.GONE);
        } else if (i == R.id.back) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}