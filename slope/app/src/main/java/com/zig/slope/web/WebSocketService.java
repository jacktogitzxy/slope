package com.zig.slope.web;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.zig.slope.common.Constants.Constant;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import de.tavendo.autobahn.WebSocketOptions;

/**
 * Created by wxs on 16/8/17.
 */
public class WebSocketService extends Service {

    private static final String TAG = WebSocketService.class.getSimpleName();

    public static final String WEBSOCKET_ACTION = "WEBSOCKET_ACTION";

    private BroadcastReceiver connectionReceiver;
    private static boolean isClosed = true;
    private static WebSocketConnection webSocketConnection;
    private static WebSocketOptions options = new WebSocketOptions();
    private static boolean isExitApp = false;
    private static String websocketHost = Constant.SOCKET_URL+"websocket?operatorID=";//"ws://192.168.1.133:8080/WebSocketTest/ws/websocket"; //websocket服务端的url,,,ws是协议,和http一样,我写的时候是用的我们公司的服务器所以这里不能贴出来
    private static String mid;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (connectionReceiver == null) {
            connectionReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                    if (networkInfo == null || !networkInfo.isAvailable()) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webSocketConnect(null,mid);
                            }
                        },2000);
                        Toast.makeText(getApplicationContext(), "网络已断开，请重新连接", Toast.LENGTH_SHORT).show();
                    } else {
                        if (webSocketConnection != null) {
                            webSocketConnection.disconnect();
                        }
                        if (isClosed) {
                            webSocketConnect(null,mid);
                        }
                    }

                }
            };

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(connectionReceiver, intentFilter);
        }
        return super.onStartCommand(intent, flags, startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void closeWebsocket(boolean exitApp) {
        isExitApp = exitApp;
        if (webSocketConnection != null && webSocketConnection.isConnected()) {
            webSocketConnection.disconnect();
            webSocketConnection = null;
        }
    }

    public static void webSocketConnect(final Inofation inf,String id){
        if(inf!=null){
            if (id!=null){
                mid =id;
            }
        }
        webSocketConnection = new WebSocketConnection();
        Log.i(TAG, "webSocketConnect: host ="+websocketHost+id);
        try {
            webSocketConnection.connect(websocketHost+id,new WebSocketHandler(){

                //websocket启动时候的回调
                @Override
                public void onOpen() {
                    Log.d(TAG,"open");
                    isClosed = false;
                    if(inf!=null){
                        inf.onConnect();
                    }
                }


                //websocket接收到消息后的回调
                @Override
                public void onTextMessage(String payload) {
                    Log.d(TAG, "payload = " + payload+"====inf="+inf);
                    if(inf!=null){
                        inf.onMsg(payload);
                    }
                }

                //websocket关闭时候的回调
                @Override
                public void onClose(int code, String reason) {
                    isClosed = true;
                    if(inf!=null){
                        inf.onDisConnect(code,reason);
                    }
                    Log.d(TAG, "code = " + code + " reason = " + reason);
                    switch (code) {
                        case 1:
                            break;
                        case 2:
                            break;
                        case 3://手动断开连接
//                            if (!isExitApp) {
//                                webSocketConnect();
//                            }
                            break;
                        case 4:
                            break;
                        /**
                         * 由于我在这里已经对网络进行了判断,所以相关操作就不在这里做了
                         */
                        case 5://网络断开连接
//                            closeWebsocket(false);
//                            webSocketConnect();
                            break;
                    }
                }
            } , options);
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }

    public static void sendMsg(String s,String fromUser,String toUser) {
        Log.d(TAG, "sendMsg = " + s);
        if (!TextUtils.isEmpty(s))
            if (webSocketConnection != null) {
                webSocketConnection.sendTextMessage(toUser+"#"+fromUser+"#"+s);
            }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (connectionReceiver != null) {
            unregisterReceiver(connectionReceiver);
        }
    }


}
