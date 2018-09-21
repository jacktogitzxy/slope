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

import com.alibaba.android.arouter.launcher.ARouter;
import com.zig.slope.R;
import com.zig.slope.web.model.ChatModel;
import com.zig.slope.web.model.ItemModel;

import java.util.ArrayList;


/**
 * Created by CoderLengary
 */


public class ChatsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private OnRecyclerViewItemOnClickListener listener;
    private ArrayList<ItemModel> mList = new ArrayList<>();

    public ChatsAdapter(Context context, ArrayList<ItemModel> list){
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        mList = list;
    }

    public void updateData(ArrayList<ItemModel> list,boolean isRefresh){
        if(isRefresh){
            mList.clear();
        }
        mList.addAll(list);
        notifyItemRangeInserted(mList.size(),list.size());
    }

    public void setItemClickListener(OnRecyclerViewItemOnClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int lay =  R.layout.chat_a;
        Log.i("zxy", "onCreateViewHolder: viewType==="+viewType);
        switch (viewType) {
            case ItemModel.CHAT_A:
                lay = R.layout.chat_a;
                break;
            case ItemModel.CHAT_B:
                lay = R.layout.chat_b;
                break;
        }
        View view = inflater.inflate(lay, parent, false);
        return new NormalViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
        ChatModel model = (ChatModel) mList.get(position).object;
        normalViewHolder.ic_user.setImageBitmap(model.getIcons());
        normalViewHolder.tv.setText(model.getContent());
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).type;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class NormalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView ic_user;
        private TextView tv;
        OnRecyclerViewItemOnClickListener listener;



        public NormalViewHolder(View itemView, final OnRecyclerViewItemOnClickListener listener) {
            super(itemView);
            this.listener = listener;
            ic_user = (ImageView) itemView.findViewById(R.id.ic_user);
            tv = (TextView) itemView.findViewById(R.id.tv);
            this.listener = listener;
            tv.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            TextView tv = (TextView) view;
            String contet = tv.getText().toString();
            if(contet.startsWith("rtmp")){
                //play
                startPlay(contet);
            }
        }
    }

    private void startPlay(String url){
        ARouter.getInstance().build("/player/play").withString("url",url).navigation();
    }
}
