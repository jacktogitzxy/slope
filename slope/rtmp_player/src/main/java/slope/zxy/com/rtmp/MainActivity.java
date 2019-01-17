package slope.zxy.com.rtmp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import org.easydarwin.video.EasyPlayerClient;
import java.util.ArrayList;
import java.util.List;

@Route(path = "/player/plays")
public class MainActivity extends AppCompatActivity {
    private List<VideoBean> videos;
    private String newName;
    private int size=1;
    private Toolbar toolbar;
    private GridView videogride;
    private VideoAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainrtmp);
        Intent intent = getIntent();
        videos = intent.getParcelableArrayListExtra("videos");
        newName = intent.getStringExtra("newName");
        size=videos.size();
        videogride = findViewById(R.id.videogride);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.return_icon);
        toolbar.setTitleMarginStart(120);
        toolbar.setTitle("编号："+newName+"监控视频列表");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finish();
            }
        });
        if(size>2){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,750);
            videogride.setLayoutParams(params);
            videogride.setNumColumns(2);
        }
        adapter= new VideoAdapter(MainActivity.this,videos);
        videogride.setAdapter(adapter);

//        pvideos = new TextureView[4];
//        pvideos[0] = findViewById(R.id.texture_view);
//        pvideos[1] = findViewById(R.id.texture_view1);
//        pvideos[2] = findViewById(R.id.texture_view2);
//        pvideos[3] = findViewById(R.id.texture_view3);
        /**
         * 参数说明
         * 第一个参数为Context,第二个参数为KEY
         * 第三个参数为的textureView,用来显示视频画面
         * 第四个参数为一个ResultReceiver,用来接收SDK层发上来的事件通知;
         * 第五个参数为I420DataCallback,如果不为空,那底层会把YUV数据回调上来.
         */
//        final String url = "rtmp://live.hkstv.hk.lxdns.com/live/hks2";//"rtmp://47.107.32.201:1935/live/livestream3";
//        for (int i = 0 ; i <size;i++){
//            if(i<4) {
//                pvideos[i].setVisibility(View.VISIBLE);
//                startPlay(videos.get(i).getUrl(),pvideos[i]);
//            }
//        }
    }
    private List<EasyPlayerClient> clients = null;
//    private void startPlay(final String url, TextureView textureView){
//        EasyPlayerClient client = new EasyPlayerClient(this, Constante.KEY, textureView, null, null);
//        client.play(url);
//        clients.add(client);
//        textureView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this,PlayActivity.class);
//                intent.putExtra("play_url",url);
//                startActivity(intent);
//            }
//        });
//    }
public void onClicktoPlay(View view){
    String x = view.findViewById(R.id.videonum).getTag().toString();
    Log.i("zxy", "onClicktoPlay: ");
    Intent intent = new Intent(MainActivity.this,PlayActivity.class);
    intent.putExtra("play_url",videos.get(Integer.parseInt(x)).getUrl());
    startActivity(intent);
}
    @Override
    protected void onResume() {
        super.onResume();
        startPlay();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(clients==null||clients.size()==0){
            return;
        }
        for (int i = 0 ;i<clients.size();i++){
            clients.get(i).pause();
        }
    }

    private void startPlay(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(videos==null||videos.size()==0){
                    return;
                }
                if(clients==null) {
                    clients = new ArrayList<>();
                    for (int i = 0; i < videos.size(); i++) {
                        TextureView videoView = videogride.getChildAt(i).findViewById(R.id.video_player_item);
                        EasyPlayerClient client = new EasyPlayerClient(MainActivity.this, Constante.KEY, videoView, null, null);
                        //String u = "rtmp://live.hkstv.hk.lxdns.com/live/hks2";
                        client.play(videos.get(i).getUrl());
                        //client.play(u);
                        clients.add(client);
                    }
                }else{
                    for (int i = 0; i < clients.size(); i++) {
                        clients.get(i).play(videos.get(i).getUrl());
                    }
                }
            }
        },500);
    }

    @Override
    protected void onStop() {
        if(clients!=null) {
            for (int i = 0; i < clients.size(); i++) {
                clients.get(i).stop();
            }
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
