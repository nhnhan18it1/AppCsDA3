package com.nhandz.flrv_ch;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.bumptech.glide.Glide;
import com.nhandz.flrv_ch.Adapters.adapter_comment;
import com.nhandz.flrv_ch.ApiResuorce.Utils2;
import com.nhandz.flrv_ch.DT.comments;
import com.nhandz.flrv_ch.DT.news;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailnewsActivity extends AppCompatActivity {

    private String TAG=getClass().getSimpleName();
    private ImageView imgContent;
    private BootstrapCircleThumbnail imgAvt;
    private TextView txtName;
    private TextView txtTime;
    private TextView txtSLike;
    private TextView txtName2;
    private TextView txtContent;
    private BootstrapButton btnLike,btnCmt,btnShare;
    private Button btnBack;
    private news OnNews;

    private RecyclerView lcmt;
    private ArrayList<comments> commentsArrayList;
    private adapter_comment adt;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailnews);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle("TIÊU ĐỀ ACTIVITY"); //Thiết lập tiêu đề nếu muốn
//        String title = actionBar.getTitle().toString(); //Lấy tiêu đề nếu muốn
//        actionBar.hide(); //Ẩn ActionBar nếu muốn
        Anhxa();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent intent=getIntent();
        String IDBV = intent.getStringExtra("IDBV");
        initRE();
        Utils2.getInstance().getRetrofitInstance().getAnews(IDBV,String.valueOf(MainActivity.OnAccount.getID())).enqueue(new Callback<news[]>() {
            @Override
            public void onResponse(Call<news[]> call, Response<news[]> response) {
                if (response.body()!=null&&response.body().length!=0){
                    OnNews = response.body()[0];
                    Initdt();
                    for (comments cmt:OnNews.getCmt()
                         ) {
                        commentsArrayList.add(cmt);
                    }
                    adt.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<news[]> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage() );
            }
        });
        //OnNews=(news)intent.getSerializableExtra("news");

    }

    private void Anhxa() {
        imgContent=findViewById(R.id.dtnA_imgvContent);
        imgAvt=findViewById(R.id.dtn_imgAvt);
        txtName=findViewById(R.id.dtnA_txtName);
        txtTime=findViewById(R.id.dtnA_txtTime);
        txtSLike=findViewById(R.id.dtnA_txtSlike);
        btnLike=findViewById(R.id.dtnA_btnLike);
        btnCmt=findViewById(R.id.dtnA_btnComment);
        btnShare=findViewById(R.id.dtnA_btnShare);
        txtContent=findViewById(R.id.dtnA_txtcontent);
        txtName2=findViewById(R.id.dtnA_txtName2);
        btnBack=findViewById(R.id.dtnA_btnBack);
        lcmt=findViewById(R.id.dtn_lcmt);
    }

    public void initRE(){
        lcmt.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        lcmt.setLayoutManager(linearLayoutManager);
        commentsArrayList=new ArrayList<>();
        adt=new adapter_comment(commentsArrayList,getApplicationContext());
        lcmt.setAdapter(adt);
    }

    public void Initdt(){
        Glide.with(getApplicationContext())
                .load(MainActivity.serverImg+OnNews.getImg())
                .centerCrop()
                .into(imgContent)
        ;
        Glide.with(getApplicationContext())
                .load(MainActivity.serverImg+OnNews.getAvt())
                .into(imgAvt)
        ;
        txtName.setText(OnNews.getName());
        txtTime.setText("1h trước");
        txtContent.setText(OnNews.getContent());
        txtName2.setText(OnNews.getName());

        if (OnNews.isIslike()){
            btnLike.setTextColor(getApplicationContext().getResources().getColor(R.color.bootstrap_brand_danger));
            txtSLike.setText("Bạn và"+OnNews.getCLike()+" người đã thích");
        }
        else {
            btnLike.setTextColor(getApplicationContext().getResources().getColor(R.color.bootstrap_brand_secondary_text));
            txtSLike.setText(OnNews.getCLike()+" người đã thích");
        }
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (OnNews.isIslike()){
                    btnLike.setTextColor(getApplicationContext().getResources().getColor(R.color.bootstrap_brand_secondary_text));
                    btnLike.setClickable(false);
                    Utils2.getInstance().getRetrofitInstance().postLike(String.valueOf(MainActivity.OnAccount.getID()),
                            String.valueOf(OnNews.getID()))
                            .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.body().equals("true")){
                                btnLike.setTextColor(getApplicationContext().getResources().getColor(R.color.bootstrap_brand_danger));
                                OnNews.setCLike(OnNews.getCLike()+1);
                                txtSLike.setText("Bạn và"+OnNews.getCLike()+"người đã thích");
                                OnNews.setIslike(true);

                            }
                            else {
                                btnLike.setTextColor(getApplicationContext().getResources().getColor(R.color.bootstrap_brand_secondary_text));
                                OnNews.setCLike(OnNews.getCLike()-1);
                                txtSLike.setText(OnNews.getCLike()+"người đã thích");
                                OnNews.setIslike(false);
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e(TAG, "onFailure: "+t.getMessage() );
                        }
                    });
                }
            }
        });

    }
}