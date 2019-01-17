package com.zig.slope.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zig.slope.R;
import com.zig.slope.bean.WeiFang;
import com.zig.slope.common.base.bean.HisReport;
import com.zig.slope.common.utils.TimeUtils;

import java.util.List;


/**
 * Created by CoderLengary
 */


public class WFdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<WeiFang> mList;
    private OnRecyclerViewItemOnClickListener listener;

    public WFdapter(Context context, List<WeiFang> list){
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        mList = list;
    }

    public void updateData(List<WeiFang> list,boolean isRefresh){
        if(isRefresh){
            mList.clear();
        }
        if(list!=null) {
            mList.addAll(list);
            notifyItemRemoved(list.size());
        }
        notifyDataSetChanged();

    }

    public void setItemClickListener(OnRecyclerViewItemOnClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_wf, parent, false);
        return new NormalViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
        WeiFang data = mList.get(position);
        if(data.getTypes()!=null&&data.getTypes().contains("C")){
            normalViewHolder.typeHisicon.setImageResource(R.mipmap.wfred);
        }
        normalViewHolder.btnCategory.setText("  " + data.getId() + "  ");
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
        AppCompatButton btnCategory;
        ImageView typeHisicon;
        public NormalViewHolder(View itemView, final OnRecyclerViewItemOnClickListener listener) {
            super(itemView);
            this.listener = listener;
            btnCategory = itemView.findViewById(R.id.btn_category);
            typeHisicon = itemView.findViewById(R.id.typeHisicon);
            btnCategory.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.i("zxy", "onClick: wfadapter");
              listener.onClick(view,getRealPosition(getAdapterPosition()));
            }
        }
}
