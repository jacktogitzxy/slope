//package org.easydarwin.easypusher;
//
//import android.app.Application;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.content.SharedPreferences;
//import android.content.res.AssetManager;
//import android.os.Build;
//import android.preference.PreferenceManager;
//
//import com.squareup.otto.Bus;
//import com.squareup.otto.Subscribe;
//import com.squareup.otto.ThreadEnforcer;
//import com.tencent.bugly.crashreport.CrashReport;
//
//import org.easydarwin.bus.StartRecord;
//import org.easydarwin.bus.StopRecord;
//import org.easydarwin.config.Config;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
//public class EasyApplication extends Application {
//
//    public static final String KEY_ENABLE_VIDEO = "key-enable-video";
//    public static final String KEY_ENABLE_AUDIO = "key-enable-audio";
//    public static final String CHANNEL_CAMERA = "camera";
//
//    private static EasyApplication mApplication;
//
//
//    public static final Bus BUS = new Bus(ThreadEnforcer.ANY);
//    public long mRecordingBegin;
//    public boolean mRecording;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        mApplication = this;
//        if (!BuildConfig.DEBUG){
//            CrashReport.initCrashReport(getApplicationContext(), "678a09e90e", false);
//        }
//        // for compatibility
//        resetDefaultServer();
//        File youyuan = getFileStreamPath("SIMYOU.ttf");
//        if (!youyuan.exists()){
//            AssetManager am = getAssets();
//            try {
//                InputStream is = am.open("zk/SIMYOU.ttf");
//                FileOutputStream os = openFileOutput("SIMYOU.ttf", MODE_PRIVATE);
//                byte[] buffer = new byte[1024];
//                int len = 0;
//                while ((len = is.read(buffer)) != -1) {
//                    os.write(buffer, 0, len);
//                }
//                os.close();
//                is.close();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        BUS.register(this);
//
//
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString(R.string.camera);
//
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_CAMERA, name, importance);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
//
//    private void resetDefaultServer() {
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        String defaultIP = sharedPreferences.getString(Config.SERVER_IP, Config.DEFAULT_SERVER_IP);
//        if ("114.55.107.180".equals(defaultIP)
//                || "121.40.50.44".equals(defaultIP)
//                || "www.easydarwin.org".equals(defaultIP)){
//            sharedPreferences.edit().putString(Config.SERVER_IP, Config.DEFAULT_SERVER_IP).apply();
//        }
//
//        String defaultRtmpURL = sharedPreferences.getString(Config.SERVER_URL, Config.DEFAULT_SERVER_URL);
//        int result1 = defaultRtmpURL.indexOf("rtmp://www.easydss.com/live");
//        int result2 = defaultRtmpURL.indexOf("rtmp://121.40.50.44/live");
//        if(result1 != -1 || result2 != -1){
//            sharedPreferences.edit().putString(Config.SERVER_URL, Config.DEFAULT_SERVER_URL).apply();
//        }
//    }
//
//    public static EasyApplication getEasyApplication() {
//        return mApplication;
//    }
//
//    public void saveStringIntoPref(String key, String value) {
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(key, value);
//        editor.commit();
//    }
//
//    public String getIp() {
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        String ip = sharedPreferences.getString(Config.SERVER_IP, Config.DEFAULT_SERVER_IP);
//        return ip;
//    }
//
//    public String getPort() {
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        String port = sharedPreferences.getString(Config.SERVER_PORT, Config.DEFAULT_SERVER_PORT);
//        return port;
//    }
//
//    public String getId() {
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        String id = sharedPreferences.getString(Config.STREAM_ID, Config.DEFAULT_STREAM_ID);
//        if (!id.contains(Config.STREAM_ID_PREFIX)) {
//            id = Config.STREAM_ID_PREFIX + id;
//        }
//        saveStringIntoPref(Config.STREAM_ID, id);
//        return id;
//    }
//
//
//    public String getUrl() {
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        String defValue = Config.DEFAULT_SERVER_URL;
//        String ip = sharedPreferences.getString(Config.SERVER_URL, defValue);
//        if (ip.equals(defValue)){
//            sharedPreferences.edit().putString(Config.SERVER_URL, defValue).apply();
//        }
//        return ip;
//    }
//
//    public static boolean isRTMP() {
//        return true;
//    }
//
//    @Subscribe
//    public void onStartRecord(StartRecord sr){
//        mRecording = true;
//        mRecordingBegin = System.currentTimeMillis();
//    }
//
//    @Subscribe
//    public void onStopRecord(StopRecord sr){
//        mRecording = false;
//        mRecordingBegin = 0;
//    }
//}
