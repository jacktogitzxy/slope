package com.sh.camera;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sh.RTMP_Pusher.R;
import com.sh.camera.ServerManager.PreManager;
import com.sh.camera.codec.MediaCodecManager2;
import com.sh.camera.service.MainService;
import com.sh.camera.util.AppLog;
import com.sh.camera.util.CameraFileUtil;
import com.sh.camera.util.Constants;
import com.sh.camera.util.ExceptionUtil;

import org.push.push.Pusher;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@Route(path = "/rtmp/live")
public class LiveActivity extends AppCompatActivity {
    private static final String TAG = LiveActivity.class.getName();
    public Context application;
    private PreManager pm;
    private MediaCodecManager2 mm;
    public static Pusher mPusher;
    public int[] StreamIndex;
    public Camera[] camera;
    private MediaRecorder[] mrs;
    private String[] MrTempName;
    private ContentValues[] mCurrentVideoValues;
    private int framerate = Constants.FRAMERATE;
    public boolean isrun = false;
    public String ACTION = "com.dss.car.dvr";
    public String FULLSCREEN = "fullscreen";
    public String PASSWINFULL = "passwinfullscreen";
    public String WINDOW = "window";
    public String MINIMIZE = "minimize";
    public String RESTART = "restart";
    public String STARTRECORDER = "startrecorder";
    public String STARTPUSH = "startpush";
    public String STOPRECORDER = "stoprecorder";
    public String STOPPUSH = "stoppush";
    public boolean isRecording = false;
    private Button btn_app_minimize, btn_app_exit;
    private FrameLayout inc_alertaui;
    private FrameLayout inc_url;
    private TextView text_url;
    private ImageView btiv1, btiv2;
    private LinearLayout[] lys;
    private int[] lyids = {R.id.ly_1_0, R.id.ly_1_1};
    //主要显示控件
    public TextureView[] ttvs;
    private SurfaceTexture[] stHolder;
    //按钮容器
    private LinearLayout ly_bts;
    public Camera.PreviewCallback[] preview;
    public TextureView.SurfaceTextureListener[] stListener;
    //记录当前录制视屏的起点，未录制时-1；
    long recoTime = -1;
    boolean isSC = false;
    boolean sd_inject = false;
    public boolean clickLock = false;

    //推流
    private int m_index_channel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_live);
        Constants.setParam(this);
        pm = PreManager.getInstance(this);
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        String ANDROID_ID = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
        String te1 = tm.getDeviceId();
        Log.i(TAG, "onCreate: ANDROID_ID=="+te1);
        if(ANDROID_ID!=null) {
            pm.putStreamname(ANDROID_ID);
            pm.putStreamURL(String.format(Constants.Default_URL,ANDROID_ID));
        }else{
            pm.putStreamURL(String.format(Constants.Default_URL,System.currentTimeMillis()));
        }
        mm = MediaCodecManager2.getInstance(this);
        initView();
        initData();
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("com.zig.photo.success");
        localIntentFilter.addAction("com.zig.photo.fail");
        localIntentFilter.addAction("com.zig.restart");
        localIntentFilter.addAction("com.zig.stopupload");
        registerReceiver(resver,localIntentFilter);
    }

    private void initData() {
        mPusher = new Pusher(application);
        StreamIndex = new int[Constants.MAX_NUM_OF_CAMERAS];
        camera = new Camera[Constants.MAX_NUM_OF_CAMERAS];
        mrs = new MediaRecorder[Constants.MAX_NUM_OF_CAMERAS];
        MrTempName = new String[Constants.MAX_NUM_OF_CAMERAS];
        mCurrentVideoValues = new ContentValues[Constants.MAX_NUM_OF_CAMERAS];
        framerate =pm.getFramerate();
        isrun = true;
        Constants.setParam(application);

        startCam();

    }
    //预览回调
    public void startCam(){
        preview[0] = new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera1) {
                // TODO Auto-generated method stub
                mm.onPreviewFrameUpload(data,0,camera[0]);
            }
        };
        //初始化摄像头、开始预览
        for (int i = 0; i < Constants.MAX_NUM_OF_CAMERAS; i++) {
            initPreview(i);
        }
    }

    @Override
    protected void onDestroy() {
        try{
            unregisterReceiver(resver);
        }catch (Exception e){

        }
        super.onDestroy();
    }

    private void initView() {
        application = LiveActivity.this;
        lys = new LinearLayout[2];
        for (int i = 0; i < lys.length; i++) {
            lys[i] = findViewById(lyids[i]);
        }
        ttvs = new TextureView[Constants.MAX_NUM_OF_CAMERAS];
        stHolder = new SurfaceTexture[Constants.MAX_NUM_OF_CAMERAS];
        preview = new Camera.PreviewCallback[Constants.MAX_NUM_OF_CAMERAS];
        stListener = new TextureView.SurfaceTextureListener[Constants.MAX_NUM_OF_CAMERAS];
        ly_bts = findViewById(R.id.main_bottom_btly);
        ly_bts.setVisibility(View.VISIBLE);
        btiv1 = findViewById(R.id.imageView1_bottom);
        btiv2 = findViewById(R.id.imageView2_bottom);
        btn_app_minimize = findViewById(R.id.btn_app_minimize);
        btn_app_exit = findViewById(R.id.btn_app_exit);
        inc_alertaui = findViewById(R.id.inc_alertaui);
        inc_url = findViewById(R.id.inc_url);
        text_url = findViewById(R.id.text_url);
    }


    public void StopCameraprocess()
    {
        if(isRecording){
            btiv1.setImageResource(R.drawable.a02);
            stoprecorder(0,0);
            isRecording = false;
        }
        if(isSC){
            stopSC();
        }
        try {
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            stopMrs(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            closeCamera(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 初始化预览
     * @param i
     */
    public void initPreview(int i){

        final int index = i;
        ttvs[i] = findViewById(R.id.tv_one);
        stListener[i] = new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture arg0) {
            }
            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture arg0, int arg1, int arg2) {
            }
            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture arg0) {
                colseCamera(index);
                return true;
            }
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture arg0, int arg1,int arg2) {
                stHolder[index] = arg0;
                openCamera(index, 1);
            }
        };
        ttvs[i].setSurfaceTextureListener(stListener[i]);
    }

    //根据摄像头id停止录像
  private   void stoprecorder(int index,int i){
        try {
            if(camera[0]!=null){
                recoTime = -1;
                if (mrs[index] != null) {
                    try {
                        mrs[index].setOnErrorListener(null);
                        mrs[index].setOnInfoListener(null);
                        mrs[index].setPreviewDisplay(null);
                        mrs[index].stop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d("CMD", String.format(" stop record:"));
                    mrs[index].release();
                    mrs[index] = null;
                    camera[index].lock();
                    addVideo(MrTempName[index], mCurrentVideoValues[index]);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public  void addVideo(final String path,final ContentValues values)
    {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try
                {
                    String finalName  = values.getAsString(MediaStore.Video.Media.DATA);
                    new File(path).renameTo(new File(finalName));
                }
                catch(Exception e)
                {
                }
            }
        });
    }

    //结束上传
    private void stopSC() {
        btiv2.setImageResource(R.drawable.a03);

        stopVideoUpload(0);
        try {
            Thread.sleep(500);
        } catch (Exception e) {
        }
        isSC = false;

    }

    /**
     * 结束视频上传
     * @param i
     */
    public void stopVideoUpload(int i){
        try {
            Log.d("SERVICE", " stop upload"+i);
            if(camera[i]!=null){
                mm.StopUpload(i);
                camera[i].setPreviewCallback(null);
                mPusher.stopPush(StreamIndex[i]);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    //释放录像资源
    public void stopMrs(int index){
        if (mrs[index]!=null) {
            mrs[index].stop();
            mrs[index].release();
            mrs[index] = null;
        }
    }
    //释放摄像头资源
    public void closeCamera(int index){
        if(camera[index]!=null){
            camera[index].setPreviewCallback(null);
            camera[index].stopPreview();
            camera[index].release();
            camera[index] = null;
        }
    }
    /**
     * 关闭释放摄像头
     * @param @i
     */
    public void colseCamera(int index){
        try {
            if(camera[index]!=null){
                camera[index].stopPreview();
                camera[index].release();
                camera[index] = null;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * 打开摄像头并预览
     * @param @i
     * @param type 1 正常启动  2 重启
     */
    public void openCamera(int index,int type){
        try {
            boolean falg = true;
            if(falg){
                try {
                    camera[index] = Camera.open(index);
                    camera[index].setDisplayOrientation(90);
                } catch (Exception e) {
                    e.printStackTrace();
                    camera[index] = null;
                }
                if (camera[index] != null) {
                    try {
                        camera[index].setPreviewTexture(stHolder[index]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Camera.Parameters parameters = camera[index].getParameters();
                    parameters.setPreviewSize(Constants.RECORD_VIDEO_WIDTH, Constants.RECORD_VIDEO_HEIGHT);
                    camera[index].setErrorCallback(new CameraErrorCallback(index));
                    camera[index].setParameters(parameters);
                    camera[index].startPreview();
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            AppLog.d(TAG, ExceptionUtil.getInfo(e));
        }
    }

    public class CameraErrorCallback implements Camera.ErrorCallback {
        private int mCameraId = -1;
        private Object switchLock = new Object();
        public CameraErrorCallback(int cameraId) {
            mCameraId = cameraId;
        }
        @Override
        public void onError(int error, Camera camera) {
            if (error == Camera.CAMERA_ERROR_SERVER_DIED) {        //底层camera实例挂掉了
                // We are not sure about the current state of the app (in preview or snapshot or recording). Closing the app is better than creating a new Camera object.
                //如果是mipi挂掉了，usb断电，然后杀掉自己所在的进程，监听心跳广播启动自己
                //usb camera挂掉了，先断电然后再上电
                //Toast.makeText(c, "摄像头：error="+error+",mCameraId="+mCameraId, Toast.LENGTH_LONG).show();
            }
            Log.d("	error!!!", "code!!!!:"+error);
        }
    }

    public void startRecoders_SD_ERR()
    {
        if(!isRecording && sd_inject){
            btiv1.setImageResource(R.drawable.a02);
            prepareRecorder(0,1);
            isRecording = true;
            sd_inject = false;
        }
    }
    /**
     * 准备录像
     * @param index
     */
    public void prepareRecorder(int index,int type){
        try {
            btiv1.setImageResource(R.drawable.b02);
            if(type == 1){
                recoTime = System.currentTimeMillis();
            }
            isRecording = true;
            startRecorder(0);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void startRecorder(int index){
        try {
            camera[index].unlock();
            mrs[index] = new MediaRecorder();
            mrs[index].reset();
            mrs[index].setCamera(camera[index]);
            mrs[index].setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mrs[index].setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mrs[index].setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mrs[index].setVideoSize(Constants.RECORD_VIDEO_WIDTH, Constants.RECORD_VIDEO_HEIGHT);
            mrs[index].setVideoEncodingBitRate(3*Constants.RECORD_VIDEO_WIDTH*Constants.RECORD_VIDEO_HEIGHT/2);
            mrs[index].setVideoFrameRate(framerate);
            mrs[index].setOnErrorListener(new MediaRecorderErrorListener(index));
            generateVideoFilename(index, MediaRecorder.OutputFormat.MPEG_4 );
            mrs[index].setOutputFile( MrTempName[index]);
            mrs[index].prepare();
            mrs[index].start();
            Constants.CAMERA_RECORD[index] = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MediaRecorderErrorListener implements MediaRecorder.OnErrorListener {                 //底层mediaRecorder上报错误信息
        private int mCameraId = -1;
        public MediaRecorderErrorListener(int cameraId) {
            mCameraId = cameraId;
        }
        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
            //先停止掉录制
            if(what == MediaRecorder.MEDIA_ERROR_SERVER_DIED){      //MediaRecorder.MEDIA_ERROR_SERVER_DIED--100，说明mediaService死了，需要释放MediaRecorder
                btiv1.setImageResource(R.drawable.a02);
                //遍历受控数组，停止录像
                stoprecorder(0,0);
                openCamera(0,1);
                isRecording = false;
            }

        }
    }

    private void generateVideoFilename(int index,  int outputFileFormat) {
        File mFile;
        try {
            mFile = CameraFileUtil.CreateText(CameraFileUtil.getRootFilePath() + Constants.CAMERA_PATH);
            String title = String.format("%d-%d", index+1, System.currentTimeMillis()) ;
            String filename = title + convertOutputFormatToFileExt(outputFileFormat);
            File file = new File(mFile,filename);
            String path = file.getPath();
            String tmpPath = path + ".tmp";
            String mime = convertOutputFormatToMimeType(outputFileFormat);
            mCurrentVideoValues[index] = new ContentValues(4);
            mCurrentVideoValues[index].put(MediaStore.Video.Media.TITLE, title);
            mCurrentVideoValues[index].put(MediaStore.Video.Media.DISPLAY_NAME, filename);
            mCurrentVideoValues[index].put(MediaStore.Video.Media.MIME_TYPE, mime);
            mCurrentVideoValues[index].put(MediaStore.Video.Media.DATA, path);
            MrTempName[index] = tmpPath;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 开始录像
     * @param @index
     */
    private  String convertOutputFormatToFileExt(int outputFileFormat) {
        if (outputFileFormat == MediaRecorder.OutputFormat.MPEG_4) {
            return ".mp4";
        }
        return ".3gp";
    }

    public static String convertOutputFormatToMimeType(int outputFileFormat) {
        if (outputFileFormat == MediaRecorder.OutputFormat.MPEG_4) {
            return "video/mp4";
        }
        return "video/3gpp";
    }

    public void click(View v){
        click(v.getId());
    }
    public void click(int id){
        if(clickLock) {
            return;
        }
        if (id == R.id.bt_ly_1 || id == R.id.bt_ly_1_bottom) {//检查SD卡是否存在
            clickLock = true;
            TakePictureAll(1);
            clickLock = false;

        } else if (id == R.id.bt_ly_2 || id == R.id.bt_ly_2_bottom) {
            clickLock = true;
            //先判断是否录制中
            if (isRecording) {
                btiv1.setImageResource(R.drawable.a02);
                //遍历受控数组,停止录像
                stoprecorder(0, 0);
                isRecording = false;
            } else {
                btiv1.setImageResource(R.drawable.b02);
                //遍历受控数组,开始录像
                if (camera[0] != null) {
                    startRecorder(0);
                }
                recoTime =System.currentTimeMillis();
                isRecording = true;
            }
            clickLock = false;


        } else if (id == R.id.bt_ly_3 || id == R.id.bt_ly_3_bottom) {
            clickLock = true;
            if (isSC) {
                inc_url.setVisibility(View.GONE);
                stopSC();
            } else {
                //处理上传
                btiv2.setImageResource(R.drawable.b03);
                startVideoUpload2(pm.getIp(), pm.getPort(), pm.getapp(),pm.getStreamname(), 0);
                isSC = true;
            }
            clickLock = false;

        } else if (id == R.id.bt_ly_4 || id == R.id.bt_ly_4_bottom) {
            Intent intent_file = new Intent(application, FileActivity.class);
            intent_file.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent_file);

        } else if (id == R.id.bt_ly_5 || id == R.id.bt_ly_5_bottom) {
            Intent intent_set = new Intent(application, SetActivity.class);
            startActivity(intent_set);

        } else if (id == R.id.bt_ly_6 || id == R.id.bt_ly_6_bottom) {
            inc_alertaui.setVisibility(View.VISIBLE);

        } else if (id == R.id.btn_app_minimize) {//娣诲姞涓€夋嫨绐?
            try {
                inc_alertaui.setVisibility(View.GONE);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else if (id == R.id.btn_app_exit) {
            try {
                inc_alertaui.setVisibility(View.GONE);
                StopCameraprocess();
                LiveActivity.this.finish();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }
        //拍照
    public  void TakePictureAll(int type)
    {
        if((camera[0]!= null) ) {
            mm.PrepareTakePicture();
            camera[0].setPreviewCallback(preview[0]);
        }else
        {
            if(handler != null){
                handler.sendMessage(handler.obtainMessage(1003));
            }
        }
    }
    //推流
    public void startVideoUpload2(final String ipstr,final String portstr, final String app, final String serialno, final int index){
        final int CameraId;
        CameraId = index+1;
        if(camera[index]==null) {
            return;
        }
        if(serialno.equals(Constants.STREAM_NAME))
        {
            Toast.makeText(application, "请修改设备名", Toast.LENGTH_LONG * 1000).show();
        }
        try {

            if(camera[index]!=null){

                //初始化推流工具
                m_index_channel = mPusher.CarEyeInitNetWorkRTMP( getApplicationContext(),Constants.Key,ipstr, portstr, String.format("%s/%s&channel=%d",app,serialno,CameraId), Constants.CAREYE_VCODE_H264,20,Constants.CAREYE_ACODE_AAC,1,8000);
                //控制预览回调
                if(m_index_channel < 0)
                {
                    Log.d("CMD", " init error, error number"+m_index_channel);
                    return;
                }
                StreamIndex[index] = m_index_channel;
                mm.StartUpload(index,camera[index]);
                camera[index].setPreviewCallback(preview[index]);
            }
            inc_url.setVisibility(View.VISIBLE);
            text_url.setText(pm.getURL());
            Intent intent= new Intent("com.zig.live");
            intent.putExtra("liveurl",pm.getURL());
            sendBroadcast(intent);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            if(msg.what==1001){
                boolean lock = false;
                Toast.makeText(application, "执行拍照成功", Toast.LENGTH_LONG * 1000).show();
                if(!lock){
                    clickLock = false;
                }
            }
            if(msg.what==1003){
                boolean lock = false;
                Toast.makeText(application, "执行拍照失败", Toast.LENGTH_LONG * 1000).show();
                if(!lock){
                    clickLock = false;
                }
            }
            //录制达到规定时长，重录
            if(msg.what==1002){
                clickLock = true;
                try {
                    stoprecorder(0,0);
                    if(camera[0]!=null) startRecorder(0);

                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                recoTime = System.currentTimeMillis();
                clickLock = false;
            }
            else if(msg.what==1022){
            }
        };
    };
    private void restart() {
        isrun = true;
        Constants.setParam(LiveActivity.this);
        StopCameraprocess();
        startCam();
    }
    BroadcastReceiver resver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("com.zig.photo.success")){
                Toast.makeText(application, "执行拍照成功", Toast.LENGTH_LONG * 1000).show();
            }
            if(action.equals("com.zig.photo.fail")){
                Toast.makeText(application, "执行拍照失败", Toast.LENGTH_LONG * 1000).show();
            }
            if(action.equals("com.zig.restart")){
                restart();
            }
            if(action.equals("com.zig.stopupload")){
                int i = intent.getIntExtra("id",0);
                stopVideoUpload(i);
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
