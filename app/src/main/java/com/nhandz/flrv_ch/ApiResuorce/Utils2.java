package com.nhandz.flrv_ch.ApiResuorce;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nhandz.flrv_ch.MainActivity;
import com.nhandz.flrv_ch.VideoCall.TurnServer;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Utils2 {
    static Utils2 instance;
    public static final String API_ENDPOINT = MainActivity.server;

    public static Utils2 getInstance() {
        if (instance == null) {
            instance = new Utils2();
        }
        return instance;
    }

    private Retrofit retrofitInstance;
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build();
    public TurnServer getRetrofitInstance() {
        if (retrofitInstance == null) {
            Gson gson=new GsonBuilder()
                    .setLenient()
                    .create();
            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(API_ENDPOINT)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofitInstance.create(TurnServer.class);
    }
}
