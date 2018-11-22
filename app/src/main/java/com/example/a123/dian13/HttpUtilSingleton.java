package com.example.a123.dian13;


import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtilSingleton {
    private static HttpUtilSingleton mHttpUtilSingleton;
    private final Handler handler;
    private final OkHttpClient okHttpClient;

    private HttpUtilSingleton() {
        handler = new Handler(Looper.getMainLooper());
        okHttpClient = new OkHttpClient().newBuilder()
                .writeTimeout(0, TimeUnit.MILLISECONDS)
                .connectTimeout(0, TimeUnit.MILLISECONDS)
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build();
    }

    public static HttpUtilSingleton getInstance(){
        if(mHttpUtilSingleton==null){
            synchronized (HttpUtilSingleton.class){
                if(mHttpUtilSingleton==null){
                    mHttpUtilSingleton=new HttpUtilSingleton();
                }
            }
        }
        return mHttpUtilSingleton;
    }

    public void doPost(String url, Map<String,String> map , final UtilListener utilListener){
        FormBody.Builder builder = new FormBody.Builder();
        for(String key:map.keySet()){
            builder.add(key,map.get(key));
        }
        FormBody formBody = builder.build();
        final Request request = new Request.Builder().post(formBody).url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if(utilListener!=null){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            utilListener.fail(e);
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response!=null&&response.isSuccessful()){
                    final String json = response.body().string();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            utilListener.succed(json);
                        }
                    });
                }
            }
        });
    }

    public interface UtilListener{
        void succed(String json);
        void fail(Exception e);
    }
    UtilListener mUtilListener;

    public void setUtilListener(UtilListener utilListener) {
        mUtilListener = utilListener;
    }
}
