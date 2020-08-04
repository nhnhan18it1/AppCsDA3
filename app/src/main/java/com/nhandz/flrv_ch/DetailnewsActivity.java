package com.nhandz.flrv_ch;

import android.app.Application;
import android.os.Bundle;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailnewsActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailnews);

    }
}