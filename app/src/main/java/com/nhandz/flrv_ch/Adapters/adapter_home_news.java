package com.nhandz.flrv_ch.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.media.MediaBrowserCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.app.DialogCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beardedhen.androidbootstrap.api.attributes.BootstrapBrand;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.google.gson.Gson;
import com.nhandz.flrv_ch.Alert.News_bottom_sheet;
import com.nhandz.flrv_ch.DT.*;
import com.nhandz.flrv_ch.Detail2Activity;
import com.nhandz.flrv_ch.DetailnewsActivity;
import com.nhandz.flrv_ch.HomeActivity;
import com.nhandz.flrv_ch.MainActivity;
import com.nhandz.flrv_ch.R;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.beardedhen.androidbootstrap.*;

public class adapter_home_news extends RecyclerView.Adapter<adapter_home_news.ViewHolder> {

    ArrayList<news> data_news;
    Context context;
    public  static DrawerLayout drawerLayout;
    SendIDBV sendIDBV;
    FragmentManager fragmentManager;
    private Activity mActivity;
    public adapter_home_news(ArrayList<news> data_news, Context context) {
        this.data_news = data_news;
        this.context = context;
    }

    public adapter_home_news(ArrayList<news> data_news, Context context, FragmentManager fragmentManager) {
        this.data_news = data_news;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    public adapter_home_news(ArrayList<news> data_news, Context context, FragmentManager fragmentManager, Activity mActivity) {
        this.data_news = data_news;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.mActivity = mActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View itemView=layoutInflater.inflate(R.layout.home_item_news,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        int maxs= holder.ctntt.getWidth();
        holder.txtname.setText(String.valueOf(data_news.get(position).getName()));
        String urlAvt="";

        if (data_news.get(position).getAvt()!=null && data_news.get(position).getImg().startsWith("http")){
            urlAvt=data_news.get(position).getImg();
        }
        else {
            urlAvt=MainActivity.serverImg +""+data_news.get(position).getImg();
            urlAvt=urlAvt.trim();
        }
        GlideUrl url1=new GlideUrl(urlAvt,
                new LazyHeaders
                        .Builder()
                        .build()
                );
        Glide
                .with(context.getApplicationContext())
                .load(url1)
                .timeout(10000)
                .skipMemoryCache(false)
                .fitCenter()
                //.centerCrop()
                .placeholder(R.drawable.loading)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //.override((int)MainActivity.Screen_width,500)
                .error(R.drawable.logo2)
                .into(holder.Imgcontent);
        holder.txtContent.setText(data_news.get(position).getContent());

        GlideUrl url2=new GlideUrl(MainActivity.serverImg +""+data_news.get(position).getAvt(),
                new LazyHeaders.Builder()
                        .addHeader("User-Agent",MainActivity.User_Agent)
                        .addHeader("Cookie",MainActivity.cookies)
                        .build()
        );
        Glide
                .with(context.getApplicationContext())
                .load(url2)
                .timeout(3000)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .error(R.drawable.logo2)
                .into(holder.Imgavt);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, data_news.get(position).getID(), Toast.LENGTH_SHORT).show();
                Log.e("homeadt", "onClick: "+data_news.get(position).getID());
            }
        });
        holder.txtTime.setText("1 gio trước");
        holder.txtclike.setText(String.valueOf(data_news.get(position).getCLike())+" người đã thích");
        //Log.e("time", "onBindViewHolder: "+data_news.get(position).getCreated_at() );
        //new apicmt(holder.listcmt,holder.txtccmt).execute(String.valueOf(data_news.get(position).getIDBV()));
        if (data_news.get(position).getCmt()==null){
            holder.txtccmt.setText("0 bình luận");
        }
        else {
            holder.txtccmt.setText(data_news.get(position).getCmt().length+" bình luận");
        }


        holder.ctnlike.setWeightSum(maxs/3);
        holder.iconlike.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.e("letclick", "onClick: like" );
                holder.iconlike.getBootstrapBrand();
                holder.iconlike.setTextColor(context.getResources().getColor(R.color.bootstrap_brand_danger));
                holder.iconlike.setClickable(false);
                new postlike(String.valueOf(data_news.get(position).getIDBV()),String.valueOf(MainActivity.OnAccount.getID()),holder.iconlike,holder.txtclike,data_news.get(position)).execute();


            }
        });
        holder.iconcmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.RIGHT);
                sendIDBV=new HomeActivity();
                sendIDBV.GetCmt(data_news.get(position).getCmt(),data_news.get(position).getIDBV());
            }
        });
        holder.btnshowmn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentManager!=null){
                    News_bottom_sheet news_bottom_sheet=new News_bottom_sheet();
                    news_bottom_sheet.show(fragmentManager,"");
                }

            }
        });
        holder.Imgcontent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, Detail2Activity.class);
                intent.putExtra("news",data_news.get(position));
                androidx.core.util.Pair<View,String> viewStringPair= Pair.create((View) holder.Imgcontent, ViewCompat.getTransitionName(holder.Imgcontent));
                ActivityOptionsCompat optionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,
                        holder.Imgcontent,
                        ViewCompat.getTransitionName(holder.Imgcontent)
                        );
                context.startActivity(intent,optionsCompat.toBundle());
            }
        });

    }


    public class postlike extends AsyncTask<Void,Void,String>{
        private String IDBV;
        private String IDNL;
        private BootstrapButton txticon;
        private TextView txtclike;
        private news count;

        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .build();

        public postlike(String IDBV, String IDNL, BootstrapButton txticon,TextView cl,news c) {
            this.IDBV = IDBV;
            this.IDNL = IDNL;
            this.txticon=txticon;
            this.txtclike=cl;
            //this.txtTl = txtTl;
            count=c;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestBody requestBody=new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("IDBV",String.valueOf(IDBV))
                    .addFormDataPart("IDND",String.valueOf(IDNL))
                    .build();
            Request request=new Request.Builder()
                    .addHeader("User-Agent",MainActivity.User_Agent)
                    .addHeader("Cookie", MainActivity.cookies)
                    .url(MainActivity.server+"/api/postlike")
                    .post(requestBody)
                    .build();

            try {

                Response response=okHttpClient.newCall(request).execute();

                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            boolean check=Boolean.parseBoolean(s);
            s=s.trim();
            Log.e("postlike", "onPostExecute: "+s );
            if (s.equals("true")){
                txticon.setTextColor(context.getResources().getColor(R.color.bootstrap_brand_danger));
                count.setCLike(count.getCLike()+1);
                txtclike.setText((count.getCLike())+ " người đã thích");
            }
            else {
                txticon.setTextColor(context.getResources().getColor(R.color.bootstrap_brand_secondary_text));
                count.setCLike(count.getCLike()-1);
                txtclike.setText((count.getCLike())+ " người đã thích");
                //txticon.setShowOutline(true);
//                txticon.setTextColor(R.color.bootstrap_gray);
//                //txtTl.setTextColor(R.color.bootstrap_gray);
//                txticon.setBackgroundColor(R.color.bootstrap_brand_success);
            }
            txticon.setClickable(true);
            //new loadclike(IDBV,txtclike).execute();

        }
    }





    @Override
    public int getItemCount() {
        return data_news.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtname;
        de.hdodenhof.circleimageview.CircleImageView Imgavt;
        ImageView Imgcontent;
        TextView txtContent;
        ConstraintLayout linearLayout;
        RecyclerView listcmt;
        TextView txtTime;
        TextView txtclike;
        TextView txtccmt;
        BootstrapEditText txtinputcmt;
        BootstrapButton btnsend;
        LinearLayout ctnlike;
        BootstrapButton iconlike;
        LinearLayout ctntt;
        BootstrapButton iconcmt;
        BootstrapButton iconshare;
        LinearLayout ctncmt;
        LinearLayout ctnshare;
        DrawerLayout drawerLayout;
        ImageButton btnshowmn;
        View itv;
        //TextView txtlike;
        //de.hdodenhof.circleimageview.CircleImageView cimgT;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtname=itemView.findViewById(R.id.home_adt_header);
            Imgavt=itemView.findViewById(R.id.cmt_avt);
            Imgcontent=itemView.findViewById(R.id.home_adt_imgcontent);
            txtContent=itemView.findViewById(R.id.home_adt_txtcontent);
            linearLayout=itemView.findViewById(R.id.home_adt_head);
            //cimgT=itemView.findViewById(R.id.home_adt_imgt);

            txtTime=itemView.findViewById(R.id.home_adt_time);
            txtclike=itemView.findViewById(R.id.cmt_clike);
            txtccmt=itemView.findViewById(R.id.cmt_ccmt);

            ctnlike=itemView.findViewById(R.id.home_adt_ctnlike);
            iconlike=itemView.findViewById(R.id.home_adt_iconlike);
            ctntt=itemView.findViewById(R.id.adt_news_ctntt);
            iconcmt=itemView.findViewById(R.id.home_adt_iconcmt);
            iconshare=itemView.findViewById(R.id.home_adt_iconshare);
            ctncmt=itemView.findViewById(R.id.home_adt_ctncmt);
            ctnshare=itemView.findViewById(R.id.home_adt_ctnshare);

            btnshowmn=itemView.findViewById(R.id.item_news_showmn);
            itv=itemView;
        }
    }
}
