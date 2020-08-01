package com.nhandz.flrv_ch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.beardedhen.androidbootstrap.api.attributes.BootstrapBrand;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.beardedhen.androidbootstrap.api.view.BootstrapBrandView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.nhandz.flrv_ch.Adapters.adapter_comment;
import com.nhandz.flrv_ch.Adapters.adapter_home_news;
import com.nhandz.flrv_ch.ApiResuorce.NewsApi;
import com.nhandz.flrv_ch.DT.SendIDBV;
import com.nhandz.flrv_ch.DT.account;
import com.nhandz.flrv_ch.DT.comments;
import com.nhandz.flrv_ch.DT.news;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.beardedhen.androidbootstrap.*;
import com.nhandz.flrv_ch.Service.ReceiveMess;
import com.nhandz.flrv_ch.VideoCall.RCallActivity;
import com.nhandz.flrv_ch.VideoCall.SendCallActivity;
import com.nhandz.flrv_ch.ui.pagehome.PageHomeFragment;


public class HomeActivity extends AppCompatActivity implements SendIDBV {


    private TextView txtT;

    private int REQUEST_CODE_IMGCHOICE=123;
    private String newsjson;
    ArrayList<news> listnews=new ArrayList<>();
    private RecyclerView recyclerView;
    adapter_home_news adt;
    private BootstrapCircleThumbnail avtfix;
    private BootstrapEditText txtinputtus;
    private BootstrapButton btnmess;
    public DrawerLayout drawerLayout;
    private RecyclerView recyclerViewCMT;
    ArrayList<comments> listCMT=new ArrayList<>();
    public static adapter_comment adapterCMT=null;
    private LinearLayout linearLayout_Header;
    private CardView relativeLayout_ctnComments;
    private BottomNavigationView bottomNavigationView;
    private ImageButton btnCloseDr;
    private BootstrapButton btnFacebook;
    private BootstrapButton btnSendCmt;
    private BootstrapEditText txtCmt;
    private BootstrapButton btnSearch;
    public static int OnIDBV=0;
    SendIDBV sendIDBV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypefaceProvider.registerDefaultIconSets();
        setContentView(R.layout.activity_home);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("TIÊU ĐỀ ACTIVITY"); //Thiết lập tiêu đề nếu muốn
        String title = actionBar.getTitle().toString(); //Lấy tiêu đề nếu muốn
        actionBar.hide(); //Ẩn ActionBar nếu muốn
        final Intent intent=new Intent(HomeActivity.this, ReceiveMess.class);
        startService(intent);
        Anhxa();
        //sendIDBV.SendDrawerlayout(drawerLayout);
        initView2();
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.groupFragment,R.id.navigation_watch ,R.id.navigation_notifications,R.id.navigation_menu)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.avtHome_frm);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        BadgeDrawable badgeDrawable=bottomNavigationView.getOrCreateBadge(R.id.navigation_notifications);
        badgeDrawable.setBackgroundColor(Color.RED);
        badgeDrawable.setBadgeTextColor(Color.WHITE);
        badgeDrawable.setMaxCharacterCount(3);
        badgeDrawable.setNumber(20);
        badgeDrawable.setVisible(true);
        GlideUrl url2=new GlideUrl(MainActivity.serverImg+""+LoginActivity.Avt,
                new LazyHeaders.Builder()
                        .addHeader("Cookie",MainActivity.cookies)
                        .build()
        );

        btnmess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,MessengerListActivity.class);
                startActivity(intent);
            }
        });
        adapter_home_news.drawerLayout=drawerLayout;
        btnCloseDr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(intent);
            }
        });
        btnSendCmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter_comment.data_comments!=null){
                    adapter_comment.data_comments.add(new comments(OnIDBV,
                            MainActivity.OnAccount.getID(),
                            MainActivity.OnAccount.getName(),
                            MainActivity.OnAccount.getAvt(),
                            txtCmt.getText().toString(),
                            "",
                            ""));
                }
                adapterCMT.notifyDataSetChanged();
                new SendCmt().execute();

            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(HomeActivity.this, RCallActivity.class);
                startActivity(intent1);
            }
        });
        btnSearch.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent1=new Intent(HomeActivity.this, SendCallActivity.class);
                startActivity(intent1);
                return false;
            }
        });
    }
    public void Anhxa(){
        avtfix=findViewById(R.id.home_avtfix);
        txtinputtus=findViewById(R.id.home_inputtus);
        btnmess=findViewById(R.id.home_btnmess);
        btnCloseDr=findViewById(R.id.atvt_home_closeDrawer);
        btnFacebook=findViewById(R.id.avtHome_facebook);
        drawerLayout=findViewById(R.id.home_drawermain);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        btnSendCmt=findViewById(R.id.home_adt_sendcmt);
        linearLayout_Header=findViewById(R.id.avtHome_header);
        txtCmt=findViewById(R.id.home_adt_ipcmt);
        //drawerLayout.openDrawer(Gravity.CENTER);
        relativeLayout_ctnComments=findViewById(R.id.avtHome_ctnComments);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        relativeLayout_ctnComments.getLayoutParams().width=width;
        relativeLayout_ctnComments.requestLayout();
        Log.e("headWidth", "Anhxa: "+width );
        bottomNavigationView=findViewById(R.id.naviBar);
        //bottomNavigationView.set
        btnSearch=findViewById(R.id.avtHome_search);


    }

    public void initView(){
        recyclerView=findViewById(R.id.home_rcyv_news);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(HomeActivity.this,
                LinearLayoutManager.VERTICAL,
                false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adt=new adapter_home_news(listnews,getApplicationContext());
        recyclerView.setAdapter(adt);
        //recyclerV2
        //adt.setLoadMore()
        //listnews.add();

        //listCMT=new ArrayList<>();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    // Scrolling up
                    linearLayout_Header.getLayoutParams().height=55;



                } else {
                    // Scrolling down
                    linearLayout_Header.getLayoutParams().height=0;

                }
                linearLayout_Header.animate().translationY(1);
                linearLayout_Header.requestLayout();
            }
        });


    }

    public void initView2(){

        recyclerViewCMT=findViewById(R.id.home_list_cmt);
        recyclerViewCMT.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerCMT=new LinearLayoutManager(HomeActivity.this,LinearLayoutManager.VERTICAL,false);
        recyclerViewCMT.setLayoutManager(linearLayoutManagerCMT);
        adapterCMT=new adapter_comment(listCMT,getApplicationContext());
        recyclerViewCMT.setAdapter(adapterCMT);
        ViewGroup.LayoutParams params=recyclerViewCMT.getLayoutParams();
        params.height=(int)(MainActivity.Screen_height-330);
        recyclerViewCMT.setLayoutParams(params);
        //listCMT.add(new comments(1,2,"asd","asd","asd","sad","asd"));
        //adapterCMT.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
//        recyclerView.getLayoutManager().scrollToPosition(0);
//        new NewsApi.getNews(listnews,adt).execute(MainActivity.server+"/api/getnews");
//        listnews.removeAll(listnews);
        if (bottomNavigationView.getSelectedItemId()==R.id.navigation_home){
            PageHomeFragment.reLoadNews();
        }
        Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
        if (PageHomeFragment.nestedScrollView!=null){
            PageHomeFragment.nestedScrollView.smoothScrollTo(0,0);
        }
    }

    public String getRealPathFromURI(Uri contentUri) {


            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = managedQuery(contentUri, proj,
                    null, null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
    }


    public void callapi(String IDBV){

        new apicmt(adapterCMT).execute(IDBV);
    }

    @Override
    public void GetCmt(comments[] cmt,int IDBV) {
        Log.e("idbvclick", "GetCmt: "+cmt.length+"-"+listCMT.size() );
//            if (adapterCMT!=null) Log.e("adt", "Getadt: !=null" );
//            else Log.e("adt", "Getadt: =null" );
        //callapi(IDBV);
        OnIDBV=IDBV;
        ArrayList<comments> cmm=new ArrayList<>();
        if (adapterCMT!=null){
            Log.e("adt", "Getadt: !=null" );
            //listCMT.removeAll(listCMT);
            for (int i=0;i<cmt.length;i++){
                cmm.add(cmt[i]);
            }

        }
        else Log.e("adt", "Getadt: =null" );
        adapterCMT.UpdateData(cmm);
        adapterCMT.notifyDataSetChanged();

        //listCMT.removeAll(listCMT);
    }


    public class apicmt extends AsyncTask<String,Void,String>{

        private adapter_comment adapter;
        private ArrayList<comments> list=new ArrayList<>();

        public apicmt(adapter_comment adapter) {
            this.adapter = adapter;
        }

        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .build();
        @Override
        protected String doInBackground(String... strings) {
            RequestBody requestBody=new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("IDBV",strings[0])
                    .build();
            Request request=new Request.Builder()
                    .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.10240 ")
                    .addHeader("Cookie", MainActivity.cookies)
                    .url(MainActivity.server+"/api/getcmt")
                    .post(requestBody)
                    .build();
            try {
                Response response=okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("rescmt", "onPostExecute: "+s );
            if (s!=""){
                Gson gson=new Gson();
                comments[] comments=gson.fromJson(s, comments[].class);
                for (int i=0;i<comments.length;i++){
                    list.add(comments[i]);
                }
                Log.e("adt_cmtx", "getcmt: "+comments.length+"-"+listCMT.size() );
                adapterCMT.UpdateData(list);
                adapterCMT.notifyDataSetChanged();

            }

        }
    }

    public class SendCmt extends AsyncTask<Void,Void,String>
    {
        String cmtct="";
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .build();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cmtct=txtCmt.getText().toString();
            txtCmt.setText("");
        }

        @Override
        protected String doInBackground(Void... voids) {
            if (OnIDBV==0){
                return null;
            }
            if (cmtct.equals("")||cmtct.equals(null)){
                return null;
            }
            RequestBody requestBody=new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("IDBV",String.valueOf(OnIDBV))
                    .addFormDataPart("ID",String.valueOf(MainActivity.OnAccount.getID()))
                    .addFormDataPart("Avt",MainActivity.OnAccount.getAvt())
                    .addFormDataPart("Name",MainActivity.OnAccount.getName())
                    .addFormDataPart("CmtContent",cmtct)
                    .addFormDataPart("Content","Cũng đã bình luận về bài viết")
                    .build();
            Request request=new Request.Builder()
                    .url(MainActivity.server+"/api/scmt")
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
            super.onPostExecute(s);
            Log.e("heme_sencmt", "onPostExecute: "+s );
        }
    }
}
