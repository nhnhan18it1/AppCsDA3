package com.nhandz.flrv_ch.VideoCall;

import com.nhandz.flrv_ch.DT.Notification;
import com.nhandz.flrv_ch.DT.account;
import com.nhandz.flrv_ch.DT.news;
import com.nhandz.flrv_ch.MainActivity;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TurnServer {
    @GET("turnsv")///_turn/streamx
    Call<TurnServerPojo> getIceCandidates() ;//@Header("Authorization") String authkey
    @POST("api/loadnoti")
    @FormUrlEncoded
    Call<Notification[]> getNotification(@Field("ID") String ID);
    @POST("api/getnews2")
    @FormUrlEncoded
    Call<news[]> getNews(@Field("offset") String offset,@Field("IDND")String IDND);
    @GET("api/getGalary")
    Call<news[]> getGalary();
    @GET("api/getvideo")
    Call<news[]> getVideo(@Query("ID") String ID, @Query("offset") String offset);
    @GET("api/getnews4")
    Call<news[]> getNewsProfile(@Query("IDND") String IDND, @Query("offset") String offset);
    @GET("api/search")
    Call<account[]> Search(@Query("name") String name);
    @POST("api/scmt")
    @FormUrlEncoded
    Call<String> sendComments(@Field("IDBV") String IDBV,
                              @Field("ID") String ID,
                              @Field("Avt") String Avt,
                              @Field("Name") String Name,
                              @Field("CmtContent") String cmtct,
                              @Field("Content") String smContent
    );
    @GET("api/GetANews")
    Call<news[]> getAnews(@Query("IDBV")String IDBV ,@Query("IDND")String IDND);
    @POST("api/postlike")
    @FormUrlEncoded
    Call<String> postLike(@Field("IDND")String IDND,@Field("IDBV")String IDBV);
    @GET("api/getInforS")
    Call<account[]> getInforAcc(@Query("IDND") String IDND,@Query("IDS") String IDS);
    @GET("api/addFriend")
    Call<String> getAddfriends(@Query("IDS")String IDS,@Query("IDR")String IDR);
    @DELETE("api/deleteAdvise")
    Call<String> deleteAdvise(@Query("IDS")String IDS,@Query("IDR")String IDR);
    @POST("api/acceptAdvise")
    @FormUrlEncoded
    Call<String> postAceptAdvise(@Field("IDS")String IDS,@Field("IDR")String IDR);

}
