package com.nhandz.flrv_ch.ApiResuorce;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.nhandz.flrv_ch.DT.news;
import com.nhandz.flrv_ch.MainActivity;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.nhandz.flrv_ch.Adapters.*;

public class NewsApi {
    public static class getNews extends AsyncTask<String,Void,String> {

        private ArrayList<news> listnews;
        private adapter_home_news adt;

        OkHttpClient okHttpClient =new OkHttpClient.Builder()
                .build();

        public getNews(ArrayList<news> listnews, adapter_home_news adt) {
            this.listnews = listnews;
            this.adt = adt;
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestBody requestBody=new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("abc","1")
                    .build();
            Request request=new Request.Builder()
                    .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.10240 ")
                    .addHeader("Cookie", MainActivity.cookies)
                    .url(strings[0])
                    .post(requestBody)
                    .build();
            try {
                Response response=okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                //e.printStackTrace();
                Log.e("getnews", "doInBackground: "+e );
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Gson gson=new Gson();
            news[] newsx=gson.fromJson(s, com.nhandz.flrv_ch.DT.news[].class);
            //Toast.makeText(HomeActivity.this, s, Toast.LENGTH_SHORT).show();
            Log.e("ness", "onPostExecute: "+newsx.length );
            for (int i=0;i<newsx.length;i++){
                listnews.add(newsx[i]);
            }

            adt.notifyDataSetChanged();
        }
    }
}
