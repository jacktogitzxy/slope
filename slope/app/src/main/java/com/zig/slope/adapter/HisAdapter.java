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

import com.zig.slope.R;
import com.zig.slope.common.base.bean.HisReport;

import java.util.List;


/**
 * Created by CoderLengary
 */


public class HisAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<HisReport> mList;
    private OnRecyclerViewItemOnClickListener listener;


    public static final int HEADER_VIEW = 0;
    public static final int NORMAL_VIEW = 1;
    private View mHeaderView;

    public HisAdapter(Context context, List<HisReport> list){
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        mList = list;
    }

    public void updateData(List<HisReport> list){
        mList.clear();
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
        if (viewType == HEADER_VIEW) {
            return new NormalViewHolder(mHeaderView, null);
        }
        View view = inflater.inflate(R.layout.item_hisreport, parent, false);
        return new NormalViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == HEADER_VIEW) {
            return;
        }
        NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
        Log.i("zxy", "onBindViewHolder: position===="+position);
        HisReport data = mList.get(position);

        normalViewHolder.textAuthor.setText(data.getPatrollerID());
        normalViewHolder.textTitle.setText(data.getContents());
        //if the text is too long, the button can not show it correctly.The solution is adding " ".
        normalViewHolder.btnCategory.setText("  "+data.getNewName()+"  ");
        normalViewHolder.textTime.setText(data.getCreateTime());
    }

    private int getRealPosition(int position) {
        if (null != mHeaderView) {
            return position - 1;
        }
        return position;
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    @Override
    public int getItemViewType(int position) {
//        if (mHeaderView == null) return NORMAL_VIEW;
//        if (position == 0) return HEADER_VIEW;
        return NORMAL_VIEW;
    }

    class NormalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        OnRecyclerViewItemOnClickListener listener;
        CardView cardView;
        AppCompatButton btnCategory;
        AppCompatTextView textTitle;
        AppCompatTextView textAuthor;
        AppCompatTextView textTime;



        public NormalViewHolder(View itemView, final OnRecyclerViewItemOnClickListener listener) {
            super(itemView);
            if (itemView == mHeaderView) {
                return;
            }
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
