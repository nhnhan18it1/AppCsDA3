package com.nhandz.flrv_ch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.beardedhen.androidbootstrap.BootstrapText;
import com.beardedhen.androidbootstrap.font.FontAwesome;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.nhandz.flrv_ch.Adapters.adapter_galary;
import com.nhandz.flrv_ch.Adapters.adapter_home_news;
import com.nhandz.flrv_ch.ApiResuorce.Utils2;
import com.nhandz.flrv_ch.DT.Infor;
import com.nhandz.flrv_ch.DT.account;
import com.nhandz.flrv_ch.DT.comments;
import com.nhandz.flrv_ch.DT.news;
import com.ornach.nobobutton.NoboButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtprofileActivity extends AppCompatActivity {

    private String TAG=getClass().getSimpleName();
    private LinearLayout group_infor;
    private BootstrapCircleThumbnail avt;
    private TextView txtName,txtSname;
    private RecyclerView listBV;
    private adapter_home_news adt;
    private ArrayList<news> newsArrayList;
    private GridView gridView;
    private ArrayList<news> galaryList;
    private adapter_galary adtG;
    private BootstrapButton btnChat,btnMenu;
    private com.ornach.nobobutton.NoboButton btnAddf;
    private String stf="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otprofile);

        Anhxa();
        InitRe();

        Intent intent=getIntent();
        String IDND=intent.getStringExtra("IDND");
        Utils2.getInstance().getRetrofitInstance().getInforAcc(IDND,String.valueOf(MainActivity.OnAccount.getID())).enqueue(new Callback<account[]>() {
            @Override
            public void onResponse(Call<account[]> call, Response<account[]> response) {
                if (response.body()!=null){
                    Log.e(TAG, "onResponse: "+response.body().length );
                    for (account ac:response.body()
                         ) {
                        Log.e(TAG, "onResponse: ac-id"+ac.getID() );
                        Glide.with(getApplicationContext())
                                .load(MainActivity.serverImg+ac.getAvt())
                                .into(avt);
                        txtName.setText(ac.getName());
                        txtSname.setText("vua li don");

                        Infor ifo = ac.getInfor();
                        stf=ifo.getSTfriend();
                        Log.e(TAG, "onResponse: stf "+stf );
                        switch (stf){
                            case "true":
                                btnAddf.setFontIcon("\uf234");
                                btnAddf.setText("hủy kết bạn");
                                btnAddf.setAllCaps(true);
                                btnAddf.setBackgroundColor(Color.RED);
                                break;
                            case "false":
                                btnAddf.setFontIcon("\uf234");
                                btnAddf.setText("Thêm bạn bè");
                                ;break;
                            case "sended":
                                btnAddf.setFontIcon("\uf138");
                                btnAddf.setText("Hủy lời mời");
                                btnAddf.setBackgroundColor(Color.RED);
                                break;
                            case "received":
                                btnAddf.setFontIcon("\uf500");
                                btnAddf.setText("Chấp nhận");
                                btnAddf.setBackgroundColor(Color.rgb(19, 67, 171));
                                break;
                        }

                        if (ifo.getEducation()!=null){
                            for (String s:ifo.getEducation()
                            ) {
                                addtextViewinfor(s,R.drawable.ic_baseline_school_24);
                            }
                        }
                        if (ifo.getRelation()!=null){
                        for (String s:ifo.getRelation()
                             ) {
                            addtextViewinfor(s,R.drawable.ic_baseline_favorite_24);
                        }}
                        if (ifo.getLive()!=null){
                        for (String s:ifo.getLive()
                        ) {
                            addtextViewinfor(s,R.drawable.ic_home_black_24dp);
                        }}

                    }
                }
            }

            @Override
            public void onFailure(Call<account[]> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage() );
                t.printStackTrace();
            }
        });
        Utils2.getInstance().getRetrofitInstance().getNewsProfile(IDND,"0").enqueue(new Callback<news[]>() {
            @Override
            public void onResponse(Call<news[]> call, Response<news[]> response) {
                if (response.body()!=null){
                    for (news s:response.body()
                         ) {
                        newsArrayList.add(s);
                        galaryList.add(s);
                    }
                    adtG.notifyDataSetChanged();
                    adt.notifyDataSetChanged();
                    Log.e(TAG, "onResponse: "+adtG.getCount() );

                }
            }

            @Override
            public void onFailure(Call<news[]> call, Throwable t) {
                Log.e(TAG, "onFailure: news "+t.getMessage() );
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent1=new Intent(OtprofileActivity.this,SlideActivity.class);
                Gson gson=new Gson();
                JsonElement jsonElement=gson.toJsonTree(newsArrayList,new TypeToken<ArrayList<news>>(){}.getType());
                JsonArray jsonArray=jsonElement.getAsJsonArray();
                intent1.putExtra("news",jsonArray.toString());
                startActivity(intent1);
            }
        });
        btnAddf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAddf.setClickable(false);
                Utils2.getInstance().getRetrofitInstance().getAddfriends(String.valueOf(MainActivity.OnAccount.getID()),IDND).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Toast.makeText(OtprofileActivity.this, response.body(), Toast.LENGTH_SHORT).show();
                        if (response.body().equals("success")){
                            //btnAddf.setBootstrapText("Hủy lời mời");
                            changeBTNposition(btnAddf,"\uf138","Hủy lời mời",Color.RED);
                        }
                        else if(response.body().equals("false")){
                            changeBTNposition(btnAddf,"\uf234","Thêm bạn bè",Color.rgb(20, 75, 158));
                        }
                        else if(response.body().trim().equals("accept")){
                            changeBTNposition(btnAddf,"\uf138","Hủy kết bạn",Color.rgb(20, 75, 158));
                        }
                        else if (response.body().trim().equals("unfriend")){
                            changeBTNposition(btnAddf,"\uf234","Thêm bạn bè",Color.rgb(20, 75, 158));
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e(TAG, "onFailure: addF "+t.getMessage() );
                        t.printStackTrace();
                    }
                });
                btnAddf.setClickable(true);
            }
        });
    }

    public void changeBTNposition(NoboButton btn,String icon,String txt,int c){
        btn.setFontIcon(icon);
        btn.setText(txt);
        btn.setAllCaps(true);
        btn.setBackgroundColor(c);
    }

    private void Anhxa() {
        group_infor=findViewById(R.id.otp_groupIF);
        avt=findViewById(R.id.otp_avt);
        txtName=findViewById(R.id.otp_name);
        txtSname=findViewById(R.id.otp_Sname);
        listBV=findViewById(R.id.otp_listbv);
        gridView=findViewById(R.id.otp_galary);
        btnAddf=findViewById(R.id.otp_btnAddFriend);
    }

    public void InitRe(){
        listBV.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        listBV.setLayoutManager(linearLayoutManager);
        newsArrayList=new ArrayList<>();
        galaryList=new ArrayList<>();
        adt=new adapter_home_news(newsArrayList,this,getSupportFragmentManager());
        listBV.setAdapter(adt);

        adtG=new adapter_galary(newsArrayList,getApplicationContext());
        gridView.setAdapter(adtG);
    }

    public void addtextViewinfor(String text,int idDr){
        TextView textView=new TextView(this);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        textView.setLayoutParams(params);
        textView.setText(text);

        Typeface typeface = ResourcesCompat.getFont(this, R.font.roboto);
        textView.setTypeface(typeface);
        textView.setTextSize(17);
        textView.setPadding(18,0 ,0 , 0);
        Drawable img = getApplicationContext().getResources().getDrawable(idDr);
        textView.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
        textView.setCompoundDrawablePadding(15);
        textView.setTypeface(textView.getTypeface(),Typeface.BOLD);
        group_infor.addView(textView);
    }
}