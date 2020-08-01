package com.nhandz.flrv_ch.ui.notifications;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.google.gson.Gson;
import com.nhandz.flrv_ch.ApiResuorce.Utils2;
import com.nhandz.flrv_ch.MainActivity;
import com.nhandz.flrv_ch.R;

import java.io.IOException;
import java.security.Timestamp;
import java.util.ArrayList;
import com.nhandz.flrv_ch.DT.*;
import com.nhandz.flrv_ch.Adapters.*;
import com.nhandz.flrv_ch.VideoCall.TurnServerPojo;
import com.nhandz.flrv_ch.VideoCall.Utils;

import org.json.JSONArray;

import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private View itemView;
    private at.markushi.ui.CircleButton btnSearch;
    private RecyclerView reAdviseFriend;
    public  adapter_adviseFriend adtAdvise;
    private ArrayList<AdviseFriends> ListAdviseFriends;
    private TextView txtXemThem;
    private RecyclerView reNotifications;
    private ArrayList<Notification> Listnotifications;
    private adapter_notification adt_notifi;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        Glide.get(getContext()).setMemoryCategory(MemoryCategory.HIGH);
        itemView=root;
        Anhxa();
        return root;
    }


    private void Anhxa() {
        btnSearch=itemView.findViewById(R.id.notifi_btnSearch);
        reAdviseFriend=itemView.findViewById(R.id.recyclerView_adviseF);
        txtXemThem=itemView.findViewById(R.id.notifi_seeMore);
        reNotifications=itemView.findViewById(R.id.notifi_reNotification);
    }

    public void initRecyc1(){
        reAdviseFriend.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        reAdviseFriend.setLayoutManager(linearLayoutManager);
        ListAdviseFriends=new ArrayList<>();
        adtAdvise=new adapter_adviseFriend(ListAdviseFriends,getContext());
        reAdviseFriend.setAdapter(adtAdvise);
        if (notificationsViewModel.getmNotification().getValue()==null){
            new LoadAdvise().execute();
        }

        notificationsViewModel.getmAdviseFriend().observe(getViewLifecycleOwner(), new Observer<ArrayList<AdviseFriends>>() {
            @Override
            public void onChanged(ArrayList<AdviseFriends> adviseFriends) {

                if (adviseFriends==null){
                    new LoadAdvise().execute();
                }
                else {
                    ListAdviseFriends.removeAll(ListAdviseFriends);
                    ListAdviseFriends.addAll(adviseFriends);
                    adtAdvise.notifyDataSetChanged();
                }

            }
        });
    }


    public void  initRecyc2(){
        reNotifications.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        reNotifications.setLayoutManager(linearLayoutManager);
        Listnotifications=new ArrayList<>();
        adt_notifi=new adapter_notification(Listnotifications,getContext());
        reNotifications.setAdapter(adt_notifi);
        if (notificationsViewModel.getmNotification().getValue()==null){
            //new LoadNoti().execute();
            Utils2.getInstance().getRetrofitInstance().getNotification(String.valueOf(MainActivity.OnAccount.getID())).enqueue(new Callback<Notification[]>() {
                @Override
                public void onResponse(Call<Notification[]> call, retrofit2.Response<Notification[]> response) {


                    ArrayList ss=new ArrayList();
                    for (Notification nt:response.body()
                         ) {
                        Listnotifications.add(nt);
                        if (nt!=null){
                            Log.e("F_notification","onResponse"+nt.getIDBV());
                        }

                    }
                    adt_notifi.notifyDataSetChanged();
                    //notificationsViewModel.setmNotification(Listnotifications);
                    //adt_notifi.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<Notification[]> call, Throwable t) {
                    Log.e("F_notification","onFailure"+t.getMessage());
                }
            });
        }

        notificationsViewModel.getmNotification().observe(getViewLifecycleOwner(), new Observer<ArrayList<Notification>>() {
            @Override
            public void onChanged(ArrayList<Notification> notifications) {
                if (notifications==null){
                    new LoadAdvise().execute();
                }
                else {
                    Listnotifications.removeAll(Listnotifications);
                    Listnotifications.addAll(notifications);
                    adt_notifi.notifyDataSetChanged();
                }
            }

        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        notificationsViewModel = ViewModelProviders.of(this).get(NotificationsViewModel.class);
        initRecyc1();
        initRecyc2();
    }

    public class LoadAdvise extends AsyncTask<Void,Void,String>{
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .build();

        @Override
        protected String doInBackground(Void... voids) {
            RequestBody requestBody=new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("ID", String.valueOf(MainActivity.OnAccount.getID()))
                    .build();
            Request request=new Request.Builder()
//                    .addHeader("cookies",MainActivity.cookies)
//                    .addHeader("User_Agent",MainActivity.User_Agent)
                    .post(requestBody)
                    .url(MainActivity.server+"/api/loadAdvise")
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
            super.onPostExecute(s);
            Log.e("NotiFragment ", "onPostExecute: "+s );
            Gson gson=new Gson();
            AdviseFriends[] friends=gson.fromJson(s,AdviseFriends[].class);
            Log.e("NotiFragment", "onPostExecute: "+friends.length );
            ArrayList<AdviseFriends> ss=new ArrayList<>();
            for (int i=0;i<friends.length;i++){
                ss.add(friends[i]);
            }
            notificationsViewModel.SetDataAdvise(ss);
        }
    }

    public class LoadNoti extends AsyncTask<Void,Void,String>{

        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .build()
                ;



        @Override
        protected String doInBackground(Void... voids) {
            okHttpClient.dispatcher().setMaxRequestsPerHost(50);
            okHttpClient.dispatcher().executorService().shutdown();
            RequestBody requestBody=new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("ID", String.valueOf(MainActivity.OnAccount.getID()))
                    .build();
            Request request=new Request.Builder()
//                    .addHeader("cookies",MainActivity.cookies)
//                    .addHeader("User_Agent",MainActivity.User_Agent)
                    .post(requestBody)
                    .url(MainActivity.server+"/api/loadnoti")
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
            super.onPostExecute(s);
            Log.e("NotiFragment ", "onPostExecute: "+s );
            Gson gson=new Gson();
            Notification[] notifications=gson.fromJson(s,Notification[].class);
            Log.e("NotiFragment", "onPostExecute: "+notifications.length );
            ArrayList<Notification> ss=new ArrayList<>();
            for (int i=0;i<notifications.length;i++){
                ss.add(notifications[i]);
            }
            notificationsViewModel.setmNotification(ss);
        }
    }
}
