package com.nhandz.flrv_ch.VideoCall;

import com.nhandz.flrv_ch.DT.Notification;
import com.nhandz.flrv_ch.DT.news;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface TurnServer {
    @GET("turnsv")///_turn/streamx
    Call<TurnServerPojo> getIceCandidates() ;//@Header("Authorization") String authkey
    @POST("api/loadnoti")
    @FormUrlEncoded
    Call<Notification[]> getNotification(@Field("ID") String ID);
    @POST("api/getnews2")
    @FormUrlEncoded
    Call<news[]> getNews(@Field("offset") String offset);
    @GET("api/getGalary")
    Call<news[]> getGalary();
}
