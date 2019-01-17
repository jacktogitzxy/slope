package slope.zxy.com.rtmp;

import android.content.Context;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 17120 on 2018/10/17.
 */

public class VideoAdapter extends BaseAdapter {
    private List<VideoBean> urls;
    private Context mContext;

    public VideoAdapter(Context context, List<VideoBean> list) {
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
            if(getCount()<=2){
                convertView = View.inflate(mContext, R.layout.videogride_item2, null);
            }else {
                convertView = View.inflate(mContext, R.layout.videogride_item, null);
            }
            viewholder = new ViewHolder(convertView);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }
        viewholder.textView.setText( urls.get(position).getRemark());
        viewholder.textView.setTag(position);
        return convertView;
    }

    public class ViewHolder {
        TextureView videoView;
        TextView textView;
        public ViewHolder(View view) {
            textView = (TextView) view.findViewById(R.id.videonum);
            videoView = view.findViewById(R.id.video_player_item);
        }
    }
}
