package com.nhandz.flrv_ch.ApiResuorce;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nhandz.flrv_ch.DT.*;
import com.nhandz.flrv_ch.Adapters.*;
import com.nhandz.flrv_ch.MainActivity;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MessSengerApi {
    public static class SendMess extends AsyncTask<Void,Void,String>{

        private adapter_ContentChat adt;
        private ArrayList<Chats> chatsArrayList;
        private String IDsend;
        private String IDeceive;
        private String Content;
        private Context context;

        public SendMess(adapter_ContentChat adt, ArrayList<Chats> chatsArrayList, String IDsend, String IDeceive, String content, Context context) {
            this.adt = adt;
            this.chatsArrayList = chatsArrayList;
            this.IDsend = IDsend;
            this.IDeceive = IDeceive;
            Content = content;
            this.context = context;
        }

        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .build();

        @Override
        protected String doInBackground(Void... voids) {
            RequestBody requestBody=new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("IDsend",IDsend)
                    .addFormDataPart("IDreceive",IDeceive)
                    .addFormDataPart("Content",Content)
                    .build();
            Request request=new Request.Builder()
                    .addHeader("User-Agent", MainActivity.User_Agent)
                    .addHeader("Cookie", MainActivity.cookies)
                    .url(MainActivity.server+"/api/SendMess")
                    .post(requestBody)
                    .build();
            try {
                Response response=okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("sendMess", "onPostExecute: "+s );
            if (!s.equals(null)){
               if (s.equals("success!")){
                   chatsArrayList.add(new Chats("",IDsend,IDeceive,Content,"","",""));
                   adt.notifyDataSetChanged();
               }
               else {
                   Toast.makeText(context, "Tin nhắn Chưa Được gửi!!!", Toast.LENGTH_SHORT).show();
               }

            }
            else {
                Toast.makeText(context, "Lỗi Hệ thống!!!", Toast.LENGTH_SHORT).show();
                Log.e("Err senmess", "onPostExecute: "+s );
            }
        }
    }
}
