package com.zig.slope.web.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.zig.slope.R;
import com.zig.slope.adapter.OnRecyclerViewItemOnClickListener;
import com.zig.slope.web.model.ChatModel;
import com.zig.slope.web.model.ItemModel;

import java.util.ArrayList;

/**
 * Created by WangChang on 2016/4/28.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.BaseAdapter> {
    private OnRecyclerViewItemOnClickListener listener;
    private ArrayList<ItemModel> dataList = new ArrayList<>();

    public void replaceAll(ArrayList<ItemModel> list) {
        dataList.clear();
        if (list != null && list.size() > 0) {
            dataList.addAll(list);
        }
        notifyDataSetChanged();
    }
    public void setItemClickListener(OnRecyclerViewItemOnClickListener listener){
        this.listener = listener;
    }

    public void addAll(ArrayList<ItemModel> list) {
        if (dataList != null && list != null) {
            dataList.addAll(list);
            notifyItemRangeInserted(dataList.size(),list.size());
        }

    }

    @Override
    public ChatAdapter.BaseAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ItemModel.CHAT_A:
                return new ChatAViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_a, parent, false),listener);
            case ItemModel.CHAT_B:
                return new ChatBViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_b, parent, false),listener);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ChatAdapter.BaseAdapter holder, int position) {
        holder.setData(dataList.get(position).object);
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).type;
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    public class BaseAdapter extends RecyclerView.ViewHolder {

        public BaseAdapter(View itemView) {
            super(itemView);
        }

        void setData(Object object) {

        }
    }

    private class ChatAViewHolder extends BaseAdapter implements View.OnClickListener{
        private ImageView ic_user;
        private TextView tv;
        OnRecyclerViewItemOnClickListener listener;

        public ChatAViewHolder(View view,final OnRecyclerViewItemOnClickListener listener) {
            super(view);
            ic_user = (ImageView) itemView.findViewById(R.id.ic_user);
            tv = (TextView) itemView.findViewById(R.id.tv);
            this.listener = listener;
            tv.setOnClickListener(this);
        }

        @Override
        void setData(Object object) {
            super.setData(object);
            ChatModel model = (ChatModel) object;
            ic_user.setImageBitmap(model.getIcons());
            tv.setText(model.getContent());
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

    private class ChatBViewHolder extends BaseAdapter implements View.OnClickListener{
        private ImageView ic_user;
        private TextView tv;
        OnRecyclerViewItemOnClickListener listener;

        public ChatBViewHolder(View view, final OnRecyclerViewItemOnClickListener listener) {
            super(view);
            ic_user = (ImageView) itemView.findViewById(R.id.ic_user);
            tv = (TextView) itemView.findViewById(R.id.tv);
            this.listener = listener;
            tv.setOnClickListener(this);
        }

        @Override
        void setData(Object object) {
            super.setData(object);
            ChatModel model = (ChatModel) object;
            ic_user.setImageBitmap(model.getIcons());
            tv.setText(model.getContent());
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
