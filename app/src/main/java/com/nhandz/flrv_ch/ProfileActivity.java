package com.nhandz.flrv_ch;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nhandz.flrv_ch.Adapters.adapter_home_news;

import java.util.ArrayList;

import com.nhandz.flrv_ch.ApiResuorce.NewsApi;
import com.nhandz.flrv_ch.DT.*;

public class ProfileActivity extends AppCompatActivity implements SendIDBV {

    private ImageView imgPage;
    private ImageView imgAvt;
    private TextView txtname;
    private TextView txtSubName;
    public static TextView txtPhoto;
    public static TextView txtFriend;
    public static TextView txtFolows;
    public static adapter_home_news adt;
    public static ArrayList<news> listnews;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("TIÊU ĐỀ ACTIVITY"); //Thiết lập tiêu đề nếu muốn
        String title = actionBar.getTitle().toString(); //Lấy tiêu đề nếu muốn
        actionBar.hide(); //Ẩn ActionBar nếu muốn
        Anhxa();
        if (MainActivity.OnAccount.getAvtpage()!=null){
            Glide.with(getApplicationContext())
                    .load(MainActivity.serverImg+MainActivity.OnAccount.getAvtpage())
                    .centerCrop()
                    .into(imgPage);
        }
        else {
            Glide.with(getApplicationContext())
                    .load(R.drawable.ac11)
                    .centerCrop()
                    .into(imgPage);
        }
        Glide.with(getApplicationContext())
                .load(MainActivity.serverImg+MainActivity.OnAccount.getAvt())
                .into(imgAvt);
        txtname.setText(MainActivity.OnAccount.getName());
        txtSubName.setText("Vua lì đòn");
        initR();
    }

    private void Anhxa() {
        imgPage=findViewById(R.id.profile_imgpage);
        imgAvt=findViewById(R.id.profile_avt);
        txtname=findViewById(R.id.profile_name);
        txtSubName=findViewById(R.id.profile_subname);
        txtPhoto=findViewById(R.id.profile_photos);
        txtFriend=findViewById(R.id.profile_friends);
        txtFolows=findViewById(R.id.profile_friends);
        recyclerView=findViewById(R.id.profile_news);
    }
    public void initR(){
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL,
                false);
        listnews=new ArrayList<>();
        recyclerView.setLayoutManager(linearLayoutManager);
        adt=new adapter_home_news(listnews,getApplicationContext());
        recyclerView.setAdapter(adt);
        recyclerView.setNestedScrollingEnabled(true);
        new NewsApi.getNewsProfile(listnews,adt).execute();
    }

    @Override
    public void GetCmt(comments[] cmt, int IDBV) {

    }
}