package com.nhandz.flrv_ch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nhandz.flrv_ch.DT.news;

public class Detail2Activity extends AppCompatActivity {
    private news OnNews;
    private ImageView imgContent;
    private TextView txtName,txtContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail2);
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.DONUT) {
            Detail2Activity.this.overridePendingTransition(R.anim.fragment_fade_enter,R.anim.nav_default_pop_exit_anim);
        }
        Anhxa();
        Intent intent=getIntent();
        OnNews=(news) intent.getSerializableExtra("news");
        Glide.with(this)
                .load(MainActivity.serverImg+OnNews.getImg())
                .into(imgContent);
        txtContent.setText(OnNews.getContent());
        txtName.setText(OnNews.getName());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.DONUT) {
            Detail2Activity.this.overridePendingTransition(R.anim.fragment_fade_enter,R.anim.nav_default_pop_exit_anim);
        }
    }

    private void Anhxa() {
        imgContent=findViewById(R.id.dtn2A_imgContent);
        txtContent=findViewById(R.id.dtn2A_txtContent);
        txtName=findViewById(R.id.dtn2A_txtName);
    }

    @Override
    public void finish() {
        super.finish();
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.DONUT) {
            Detail2Activity.this.overridePendingTransition(R.anim.fragment_fade_enter,R.anim.nav_default_pop_exit_anim);
        }
    }
}