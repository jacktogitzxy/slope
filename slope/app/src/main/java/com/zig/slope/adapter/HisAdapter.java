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
    private int type;

    public HisAdapter(Context context, List<HisReport> list,int type){
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        mList = list;
        this.type = type;
    }

    public void updateData(List<HisReport> list,boolean isRefresh){
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
        View view = inflater.inflate(R.layout.item_hisreport, parent, false);
        return new NormalViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
        HisReport data = mList.get(position);
        normalViewHolder.textAuthor.setText(data.getRemark());
        normalViewHolder.textTitle.setText(data.getContents());
        if(type==3){
            normalViewHolder.btnCategory.setText("  "+data.getT_id()+"  ");
            normalViewHolder.typeHisicon.setImageResource(R.mipmap.sficonleft);
        }else if(type==1) {
            normalViewHolder.btnCategory.setText("  " + data.getNewName() + "  ");
            normalViewHolder.typeHisicon.setImageResource(R.mipmap.solpeiconleft);
        }
        else if(type==2) {
            normalViewHolder.btnCategory.setText("  " + data.getDid() + "  ");
            normalViewHolder.typeHisicon.setImageResource(R.mipmap.wficonleft);
        }
        else if(type==4) {
            normalViewHolder.btnCategory.setText("  " + data.getSid() + "  ");
            normalViewHolder.typeHisicon.setImageResource(R.mipmap.dxleft);
        }
        else if(type==5) {
            normalViewHolder.btnCategory.setText("  " + data.getCid() + "  ");
            normalViewHolder.typeHisicon.setImageResource(R.mipmap.gdleft);
        }
        else if(type==6) {
            normalViewHolder.btnCategory.setText("  " + data.getSid() + "  ");
            normalViewHolder.typeHisicon.setImageResource(R.mipmap.hdleft);
        }
        Log.i("zxy", "onBindViewHolder: data.getCreateTime()=="+data.getCreateTime());
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
        ImageView typeHisicon;



        public NormalViewHolder(View itemView, final OnRecyclerViewItemOnClickListener listener) {
            super(itemView);
            this.listener = listener;
            btnCategory = itemView.findViewById(R.id.btn_category);
            btnCategory.setOnClickListener(this);
            textTitle = itemView.findViewById(R.id.text_view_title);
            textAuthor = itemView.findViewById(R.id.text_view_author);
            textTime = itemView.findViewById(R.id.text_view_time);
            cardView = itemView.findViewById(R.id.card_view_layout);
            typeHisicon = itemView.findViewById(R.id.typeHisicon);
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
