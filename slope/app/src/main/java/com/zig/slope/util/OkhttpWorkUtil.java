package com.zig.slope.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zig.slope.HisReportDetilActivity;
import com.zig.slope.bean.UserLoacl;
import com.zig.slope.callback.RequestCallBack;
import com.zig.slope.callback.RequestWeatherCallBack;
import com.zig.slope.common.Constants.Constant;
import com.zig.slope.common.utils.CustomProgressDialog;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import slope.zxy.com.weather_moudle.bean.WeatherBean;
import slope.zxy.com.weather_moudle.utils.Utility;

/**
 * Created by 17120 on 2018/8/31.
 */

public class OkhttpWorkUtil {
    private Activity activity;
    private RequestCallBack callBack;
    private CustomProgressDialog progressDialog;

    public OkhttpWorkUtil(Activity activity,RequestCallBack callBack) {
        this.activity = activity;
        this.callBack=callBack;
        createProgressDialog(activity,true);
    }

    public void postAsynHttp(String url) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder().build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopProgressDialog();
                        if(callBack!=null){
                            callBack.onFail(null);
                        }
                        Toast.makeText(activity, "网络繁忙，访问失败", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String str = response.body().string();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                            stopProgressDialog();
                        if(callBack!=null){
                            Gson gson = new Gson();
                            List<UserLoacl> ps = gson.fromJson(str, new TypeToken<List<UserLoacl>>(){}.getType());
                            callBack.onSuccess(ps);
                        }
                    }
                });
            }
        });
    }
    /**
     * 创建进度条实例
     */
    public void createProgressDialog(Context cxt, boolean canCancle) {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
            if (progressDialog == null) {
                progressDialog = CustomProgressDialog.createDialog(cxt, canCancle);
                progressDialog.setCanceledOnTouchOutside(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动加载进度条
     */
    public void showProgressDialog(){
        try {
            if (progressDialog != null) {
                progressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭加载进度条
     */
    public void stopProgressDialog() {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postAsynHttpHis(String url,String id,String operatorName,String contents) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("id", id)
                .add("operatorName", operatorName)
                .add("contents", contents)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopProgressDialog();
                        Toast.makeText(activity, "网络繁忙，访问失败", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                final String str = response.body().string();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(str.contains("5")) {
                            stopProgressDialog();
                            Toast.makeText(activity, "修改成功", Toast.LENGTH_SHORT).show();
                            if(callBack!=null){callBack.onSuccess(null);}
                        }
                    }
                });
            }
        });
    }

    public void postAsynHttpProcess(String url, String id, String newName, final RequestCallBack callBackc) {
        showProgressDialog();
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("id", id)
                .add("newName", newName)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(callBackc!=null){
                            callBackc.onFail(null);
                        }
                        stopProgressDialog();
                        Toast.makeText(activity, "网络繁忙，访问失败", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                final String str = response.body().string();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(str.contains("5")) {
                            if(callBackc!=null){
                                callBackc.onSuccess(null);
                            }
                            stopProgressDialog();
                            Toast.makeText(activity, "修改成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


    public void postAsynHttpWeather(String url, final RequestWeatherCallBack callBackc2) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder().build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(callBackc2!=null){
                            callBackc2.onFail(null);
                        }
                        Toast.makeText(activity, "网络繁忙，访问失败", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                final WeatherBean weather = Utility.handleWeatherResponse(response.body().string());
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(callBackc2!=null&&weather!=null){
                            callBackc2.onSuccess(weather);
                        }
                    }
                });
            }
        });
    }
    public void postAsynF(String url, String id, String time) {
        showProgressDialog();
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("sensorId", id)
                .add("frequence", time)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopProgressDialog();
                        Toast.makeText(activity, "网络繁忙，访问失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                final String str = response.body().string();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("zxy", "run: str=="+str);
                        if(str.contains("5")) {
                            stopProgressDialog();
                            Toast.makeText(activity, "修改成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

}
