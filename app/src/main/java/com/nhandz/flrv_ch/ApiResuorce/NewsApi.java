package com.nhandz.flrv_ch.ApiResuorce;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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
import com.nhandz.flrv_ch.ui.pagehome.PageHomeFragment;
import com.nhandz.flrv_ch.DT.*;
import com.nhandz.flrv_ch.ui.pagehome.PageHomeViewModel;

public class NewsApi {
    public static class getNews extends AsyncTask<String,Void,String> {

        private ArrayList<news> listnews;
        private adapter_home_news adt;
        private LiveData<ArrayList<news>> mlistnews;
        private MutableLiveData<ArrayList<news>> mlistnews2;
        private String Ck;
        OkHttpClient okHttpClient =new OkHttpClient.Builder()
                .build();

        public getNews(ArrayList<news> listnews, adapter_home_news adt) {
            this.listnews = listnews;
            this.adt = adt;
        }

        public getNews(ArrayList<news> listnews, adapter_home_news adt, LiveData<ArrayList<news>> mlistnews) {
            this.listnews = listnews;
            this.adt = adt;
            this.mlistnews = mlistnews;
        }

        public getNews(ArrayList<news> listnews, adapter_home_news adt, MutableLiveData<ArrayList<news>> mlistnews2) {
            this.listnews = listnews;
            this.adt = adt;
            this.mlistnews2 = mlistnews2;
        }

        public getNews(ArrayList<news> listnews, adapter_home_news adt, String ck) {
            this.listnews = listnews;
            this.adt = adt;
            Ck = ck;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {
            String off="";
            if (Ck!=null){
                off="0";
            }
            else {
                off=String.valueOf(listnews.size());
            }


            RequestBody requestBody=new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("offset",off)
                    .build();
            Request request=new Request.Builder()
                    .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.10240 ")
                    .addHeader("Cookie", MainActivity.cookies)
                    .url(MainActivity.server+"/api/getnews2")
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
            Log.e("newApi", "onPostExecute: "+s );
            if (s!=null){
                news[] newsx=gson.fromJson(s, com.nhandz.flrv_ch.DT.news[].class);
                //Toast.makeText(HomeActivity.this, s, Toast.LENGTH_SHORT).show();


//            if (listnews.size()!=0){
//                listnews.removeAll(listnews);
//            }
                if (Ck!=null){
                    listnews.removeAll(listnews);
                }
                for (int i=0;i<newsx.length;i++){
                    if (newsx[i]!=null){
                        if (newsx[i].getCmt()!=null){
                            //Log.e("ness", "onPostExecute: "+newsx[i].getCmt() );
                        }
                        listnews.add(newsx[i]);
                        PageHomeFragment.list_itemEntities.add(new ItemEntity(newsx[i].getImg()));
                    }


                }
                if (mlistnews2!=null){
                    PageHomeViewModel.listnews=listnews;
                    if (mlistnews2.getValue()!=null){
                        mlistnews2.setValue(listnews);
                    }
                }
                adt.notifyDataSetChanged();
                PageHomeFragment.isLoading=false;
            }


        }
    }
    public static class getStory extends AsyncTask<Void,Void,String>{

        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .build();

        private ArrayList<Story> stories;
        private adapter_story adapter_story;

        public getStory( ArrayList<Story> stories, com.nhandz.flrv_ch.Adapters.adapter_story adapter_story) {

            this.stories = stories;
            this.adapter_story = adapter_story;
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestBody requestBody=new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("ID",String.valueOf(MainActivity.OnAccount.getID()))
                    .build();
            Request request=new Request.Builder()
                    .url(MainActivity.server+"/api/getStory")
                    .post(requestBody)
                    .build();
            try {
                Response response=okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("newApi-getStory", "onPostExecute: "+s );
            super.onPostExecute(s);
            if (s!=null && !s.equals("[]") && s.indexOf("html")==(-1)){
                Gson gson=new Gson();
                Story[] sts=gson.fromJson(s,Story[].class);
                if (PageHomeFragment.first){
                    stories.removeAll(stories);
                    PageHomeFragment.first=false;
                }
                for ( Story st:sts ) {
                    stories.add(st);
                }
                adapter_story.notifyDataSetChanged();
            }


        }
    }
}
