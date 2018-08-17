//package com.zig.slope.web.ui;
//
//import android.annotation.TargetApi;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Handler;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.zig.slope.R;
//import com.zig.slope.common.utils.PreferenceManager;
//import com.zig.slope.web.Inofation;
//import com.zig.slope.web.MainActivity;
//import com.zig.slope.web.WebSocketService;
//import com.zig.slope.web.adapter.ChatAdapter;
//import com.zig.slope.web.model.ChatModel;
//import com.zig.slope.web.model.ItemModel;
//
//import java.util.ArrayList;
//
//public class UiActivity extends AppCompatActivity {
//    private RecyclerView recyclerView;
//    private ChatAdapter adapter;
//    private EditText et;
//    private TextView tvSend;
//    private String content;
//    private Intent websocketServiceIntent;
//    public String TAG = UiActivity.class.getName();
//    private String operatorId;
//    private PreferenceManager pm;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_ui);
//        pm = PreferenceManager.getInstance(this);
//        operatorId = pm.getPackage("operatorId");
//
//        websocketServiceIntent = new Intent(this, WebSocketService.class);
//        startService(websocketServiceIntent);
//        initView();
//        initData();
//    }
//
//    private void initView() {
//        recyclerView = (RecyclerView) findViewById(R.id.recylerView);
//        et = (EditText) findViewById(R.id.et);
//        tvSend = (TextView) findViewById(R.id.tvSend);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        recyclerView.setAdapter(adapter = new ChatAdapter());
//    }
//
//    private void initData() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                WebSocketService.webSocketConnect(new Inofation() {
//                    @Override
//                    public void onMsg(String msg) {
//                        Log.i(TAG, "onMsg: "+msg);
//                        getMessage(msg);
//                    }
//
//                    @Override
//                    public void onConnect() {
//                        Log.i(TAG, "onConnect: ");
//                    }
//
//                    @Override
//                    public void onDisConnect(int code, String reason) {
//                        Log.i(TAG, "onDisConnect: ");
//                    }
//                },operatorId);
//            }
//        },3000);
//
//
//        et.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                content = s.toString().trim();
//            }
//        });
//        tvSend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ArrayList<ItemModel> data = new ArrayList<>();
//                ChatModel model = new ChatModel();
//                model.setIcon(R.drawable.bga);
//                model.setContent(content);
//                data.add(new ItemModel(ItemModel.CHAT_B, model));
//                adapter.addAll(data);
//                WebSocketService.sendMsg(content,operatorId,"0");
//                et.setText("");
//                hideKeyBorad(et);
//
//            }
//        });
//    }
//
//    private void getMessage(String msg) {
//        if(msg.endsWith("#发送成功")){//过滤消息
//            return;
//        }
//        ArrayList<ItemModel> data = new ArrayList<>();
//        ChatModel model = new ChatModel();
//        model.setIcon(R.drawable.bgb);
//        model.setContent(msg);
//        data.add(new ItemModel(ItemModel.CHAT_A, model));
//        adapter.addAll(data);
//    }
//
//    private void hideKeyBorad(View v) {
//        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm.isActive()) {
//            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        WebSocketService.closeWebsocket(true);
//        stopService(websocketServiceIntent);
//        super.onBackPressed();
//    }
//}
