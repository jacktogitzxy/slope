package com.zig.slope.web;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zig.slope.R;
import com.zig.slope.common.utils.PreferenceManager;

public class MainActivity extends BaseActivity implements View.OnClickListener{


    private Button connectBtn;
    private Button disconnectBtn;
    private TextView messageTv;
    private EditText sendMsgEdit;
    private Button sendMsgBtn;
    private Intent websocketServiceIntent;
    public String TAG = MainActivity.class.getName();
    private String operatorId;
    private PreferenceManager pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainweb);
        pm = PreferenceManager.getInstance(this);
        operatorId = pm.getPackage("operatorId");
        websocketServiceIntent = new Intent(this, WebSocketService.class);
        startService(websocketServiceIntent);
        findViews();
        initViews();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.connect_btn:
                WebSocketService.webSocketConnect(new Inofation() {
                    @Override
                    public void onMsg(String msg) {
                        Log.i(TAG, "onMsg: "+msg);
                        getMessage(msg);
                    }

                    @Override
                    public void onConnect() {
                        Log.i(TAG, "onConnect: ");
                    }

                    @Override
                    public void onDisConnect(int code, String reason) {
                        Log.i(TAG, "onDisConnect: ");
                    }
                },operatorId);
                break;

            case R.id.disconnect_btn:
                WebSocketService.closeWebsocket(false);
                break;

            case R.id.send_msg_btn:
                WebSocketService.sendMsg(sendMsgEdit.getText().toString(),operatorId,"0");
                break;
        }
    }


    private void findViews(){
        connectBtn = (Button)findViewById(R.id.connect_btn);
        disconnectBtn = (Button)findViewById(R.id.disconnect_btn);
        messageTv = (TextView)findViewById(R.id.message_tv);
        sendMsgEdit = (EditText)findViewById(R.id.send_msg_edit);
        sendMsgBtn = (Button)findViewById(R.id.send_msg_btn);
    }

    private void initViews(){
        connectBtn.setOnClickListener(this);
        disconnectBtn.setOnClickListener(this);
        sendMsgBtn.setOnClickListener(this);
    }


    @Override
    protected void getMessage(String msg) {
        messageTv.setText("");
        messageTv.setText("收到:"+msg);
    }

    @Override
    public void onBackPressed() {
        WebSocketService.closeWebsocket(true);
        stopService(websocketServiceIntent);
        super.onBackPressed();
    }
}
