package com.zig.slope.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zig.slope.R;
import org.careye.util.VideoBean;

import org.careye.player.media.EyeVideoView;

import java.util.List;

/**
 * Created by 17120 on 2018/10/17.
 */

public class GrideAdapter extends BaseAdapter {
    private List<VideoBean> urls;
    private Context mContext;

    public GrideAdapter(Context context, List<VideoBean> list) {
        this.mContext = context;
        this.urls = list;
    }
    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public Object getItem(int i) {
        return urls.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewholder;
        if (null == convertView) {
            convertView = View.inflate(mContext, R.layout.videogride_item, null);
            viewholder = new ViewHolder(convertView);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }
        String content = urls.get(position).getUrl();
        viewholder.videoView.setVideoPath(content);
        viewholder.videoView.start();
        viewholder.textView.setText( urls.get(position).getRemark());
        viewholder.textView.setTag(position);
        return convertView;
    }

    class ViewHolder {
        EyeVideoView videoView;
        TextView textView;
        public ViewHolder(View view) {
            textView = (TextView) view.findViewById(R.id.videonum);
            videoView = view.findViewById(R.id.video_player_item);
        }
    }
}
