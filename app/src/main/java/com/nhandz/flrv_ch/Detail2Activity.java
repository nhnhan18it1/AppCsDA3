package com.nhandz.flrv_ch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.nhandz.flrv_ch.Alert.comments_bottom_sheet;
import com.nhandz.flrv_ch.ApiResuorce.Utils2;
import com.nhandz.flrv_ch.DT.comments;
import com.nhandz.flrv_ch.DT.news;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Detail2Activity extends AppCompatActivity {

    private String TAG=getClass().getSimpleName();
    private news OnNews;
    private ImageView imgContent;
    private TextView txtName,txtContent,txtClike;
    private BootstrapButton btnlike,btnComment;

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
        if (OnNews.isIslike()){
            btnlike.setTextColor(getApplicationContext().getResources().getColor(R.color.bootstrap_brand_danger));
        }
        else {
            btnlike.setTextColor(getApplicationContext().getResources().getColor(R.color.bootstrap_brand_secondary_text));
        }
        btnlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (OnNews.isIslike()){
                    btnlike.setTextColor(getApplicationContext().getResources().getColor(R.color.bootstrap_brand_secondary_text));
                    btnlike.setClickable(false);
                    Utils2.getInstance().getRetrofitInstance().postLike(String.valueOf(MainActivity.OnAccount.getID()),
                            String.valueOf(OnNews.getID()))
                            .enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if (response.body().equals("true")){
                                        btnlike.setTextColor(getApplicationContext().getResources().getColor(R.color.bootstrap_brand_danger));
                                        OnNews.setCLike(OnNews.getCLike()+1);
                                        btnlike.setText("Bạn và"+OnNews.getCLike()+"người đã thích");
                                        OnNews.setIslike(true);

                                    }
                                    else {
                                        btnlike.setTextColor(getApplicationContext().getResources().getColor(R.color.bootstrap_brand_secondary_text));
                                        OnNews.setCLike(OnNews.getCLike()-1);
                                        btnlike.setText(OnNews.getCLike()+"người đã thích");
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
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comments_bottom_sheet comments_bottom_sheet=new comments_bottom_sheet();
                Bundle bundle=new Bundle();
                Gson gson=new Gson();
                JsonElement jsonElement=gson.toJsonTree(OnNews.getCmt(),new TypeToken<comments[]>(){}.getType());


                JsonArray jsonArray=jsonElement.getAsJsonArray();
                Log.e(TAG, "onClick: "+jsonArray.toString() );
                bundle.putString("comments",jsonArray.toString());
                bundle.putString("IDBV",String.valueOf(OnNews.getIDBV()));
                comments_bottom_sheet.setArguments(bundle);
                comments_bottom_sheet.show(getSupportFragmentManager(),"");
            }
        });
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
        btnComment=findViewById(R.id.dtn2A_btnComment);
        btnlike=findViewById(R.id.dtn2A_btnLike);
    }

    @Override
    public void finish() {
        super.finish();
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.DONUT) {
            Detail2Activity.this.overridePendingTransition(R.anim.fragment_fade_enter,R.anim.nav_default_pop_exit_anim);
        }
    }
}