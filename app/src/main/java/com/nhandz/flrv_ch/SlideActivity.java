package com.nhandz.flrv_ch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.nhandz.flrv_ch.Adapters.adapter_slide;
import com.nhandz.flrv_ch.DT.news;

import java.util.ArrayList;

public class SlideActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private ArrayList<news> newsArrayList;
    private adapter_slide adt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);

        viewPager2=findViewById(R.id.slie_viewpage2);
        newsArrayList=new ArrayList<>();
        adt=new adapter_slide(newsArrayList,this);
        viewPager2.setAdapter(adt);

        Intent intent=getIntent();
        Gson gson=new Gson();
        news[] ns=gson.fromJson(intent.getStringExtra("news"),news[].class);
        for (news n:ns
             ) {
            newsArrayList.add(n);
        }
       adt.notifyDataSetChanged();

    }
}