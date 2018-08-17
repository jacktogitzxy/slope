package com.zig.slope.view;


import android.app.Application;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.zig.slope.LocationdrawActivity;
import com.zig.slope.R;
import com.zig.slope.common.base.bean.LoginBean;


/**
 * Created by zxy on 17/1/5.
 */

public class LeftMenuFragment extends Fragment {
   private TextView un,uid;
    private RadioGroup group;
    private LoginBean bean;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.main_menu,null);
        un = view.findViewById(R.id.user_name);
        uid = view.findViewById(R.id.user_id);
        group = view.findViewById(R.id.radioGroup);
        group.setOnCheckedChangeListener(radioButtonListener);
        group.setOnCheckedChangeListener(radioButtonListener);
        return view;
    }

    public LoginBean getBean() {
        return bean;
    }

    public void setdata(LoginBean bean){
        this.bean = bean;
        un.setText(bean.getOperatorName());
        uid.setText(bean.getOperatorTel());
    }
    RadioGroup.OnCheckedChangeListener radioButtonListener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.defaulticon) {
                    ((LocationdrawActivity)getActivity()).mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

                }
                if (checkedId == R.id.customicon) {
                    ((LocationdrawActivity)getActivity()).mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                }

            }
        };

}
