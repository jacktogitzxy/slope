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
import android.widget.TextView;

import com.zig.slope.R;
import com.zig.slope.common.base.bean.HisReport;
import com.zig.slope.common.utils.TimeUtils;

import java.util.List;


/**
 * Created by CoderLengary
 */


public class HisAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<HisReport> mList;
    private OnRecyclerViewItemOnClickListener listener;

    public HisAdapter(Context context, List<HisReport> list){
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        mList = list;
    }

    public void updateData(List<HisReport> list,boolean isRefresh){
        if(isRefresh){
            mList.clear();
        }
        mList.addAll(list);
        notifyDataSetChanged();
        notifyItemRemoved(list.size());
    }

    public void setItemClickListener(OnRecyclerViewItemOnClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_hisreport, parent, false);
        return new NormalViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
        HisReport data = mList.get(position);
        normalViewHolder.textAuthor.setText(data.getRemark());
        normalViewHolder.textTitle.setText(data.getContents());
        normalViewHolder.btnCategory.setText("  "+data.getNewName()+"  ");
        normalViewHolder.textTime.setText(TimeUtils.transleteTime(data.getCreateTime()));
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
        CardView cardView;
        AppCompatButton btnCategory;
        TextView textTitle;
        AppCompatTextView textAuthor;
        AppCompatTextView textTime;



        public NormalViewHolder(View itemView, final OnRecyclerViewItemOnClickListener listener) {
            super(itemView);
            this.listener = listener;
            btnCategory = itemView.findViewById(R.id.btn_category);
            btnCategory.setOnClickListener(this);
            textTitle = itemView.findViewById(R.id.text_view_title);
            textAuthor = itemView.findViewById(R.id.text_view_author);
            textTime = itemView.findViewById(R.id.text_view_time);
            cardView = itemView.findViewById(R.id.card_view_layout);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.card_view_layout:
                    listener.onClick(view,getRealPosition(getAdapterPosition()));
                    break;
                default:break;

            }
        }
    }
}
