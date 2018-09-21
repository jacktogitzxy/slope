package slope.zxy.com.weather_moudle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import slope.zxy.com.weather_moudle.R;
import slope.zxy.com.weather_moudle.bean.HourDataBean;
import slope.zxy.com.weather_moudle.utils.SystemUtils;


/**
 * Created by Administrator on 2016/12/29.
 */
public class HourDataListAdapter extends RecyclerView.Adapter<HourDataListAdapter.HourDataListViewHolder> {

    private Context context;
    private List<HourDataBean> datas =new ArrayList<>();
    private int mDisplayHeight;
    private int mDisplayWideth;

    public HourDataListAdapter(Context mContext, List<HourDataBean> datas) {
        this.context = mContext;
        this.datas = datas;
        this.mDisplayHeight = SystemUtils.getDisplayHeight(mContext);
        this.mDisplayWideth = SystemUtils.getDisplayWidth(mContext);
    }

    @Override
    public HourDataListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View hourDataListItemView = View.inflate(context, R.layout.hourdatalist_item,null);
        hourDataListItemView.setLayoutParams(new LinearLayout.LayoutParams(mDisplayWideth/5, ViewGroup.LayoutParams.MATCH_PARENT));
        return new HourDataListViewHolder(hourDataListItemView);
    }

    @Override
    public void onBindViewHolder(HourDataListViewHolder holder, int position) {
            HourDataBean hourDataBean = datas.get(position);

            holder.mHourDataListTemptureTime.setText(hourDataBean.getmTemperature_Time());
            Glide.with(context).load(hourDataBean.getmWeather_Pic()).into(holder.mHourDataWeatherImg);
            holder.mHourDataTempture.setText(hourDataBean.getmTemperature()+"°");
            holder.mHourDataWind.setText(hourDataBean.getmWind_Direction()+hourDataBean.getmWind_Power());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class HourDataListViewHolder extends RecyclerView.ViewHolder{
        TextView mHourDataListTemptureTime;
        ImageView mHourDataWeatherImg;
        TextView mHourDataTempture;
        TextView mHourDataWind;
        public HourDataListViewHolder(View itemView) {
            super(itemView);
            mHourDataWind = itemView.findViewById(R.id.hourdata_wind);
            mHourDataTempture = itemView.findViewById(R.id.hourdata_tempture);
            mHourDataWeatherImg = itemView.findViewById(R.id.hourdata_weather_img);
            mHourDataListTemptureTime = itemView.findViewById(R.id.hourdata_tempture_time);

        }
    }
}
