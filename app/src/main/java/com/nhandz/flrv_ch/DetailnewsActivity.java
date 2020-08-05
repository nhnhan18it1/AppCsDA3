package com.nhandz.flrv_ch;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.bumptech.glide.Glide;
import com.nhandz.flrv_ch.DT.news;

public class DetailnewsActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailnews);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle("TIÊU ĐỀ ACTIVITY"); //Thiết lập tiêu đề nếu muốn
//        String title = actionBar.getTitle().toString(); //Lấy tiêu đề nếu muốn
//        actionBar.hide(); //Ẩn ActionBar nếu muốn
        Anhxa();
        Intent intent=getIntent();
        OnNews=(news)intent.getSerializableExtra("news");
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
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
    }
}