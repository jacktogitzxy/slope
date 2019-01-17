package com.zig.slope.clusterutil;

import android.os.Bundle;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;
import com.zig.slope.R;
import com.zig.slope.clusterutil.clustering.ClusterItem;

/**
 * Created by 17120 on 2019/1/9.
 */

public class MyItem implements ClusterItem {
    private final LatLng mPosition;//点
    private Bundle buns;//额外信息
    private BitmapDescriptor bitmapDescriptor;
    public MyItem(LatLng latLng) {
        mPosition = latLng;
    }
    public MyItem(LatLng latLng,Bundle bun,BitmapDescriptor bitmapDescriptor) {
        mPosition = latLng;
        buns=bun;
        this.bitmapDescriptor = bitmapDescriptor;
    }
    @Override
    public LatLng getPosition() {
        return mPosition;
    }
    @Override
    public Bundle getExtraInfo() {
        return buns;
    }
    @Override
    public BitmapDescriptor getBitmapDescriptor() {//点图标
        if(bitmapDescriptor==null){
            return BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_gcoding);
        }
        return bitmapDescriptor;
    }
}
