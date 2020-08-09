package com.nhandz.flrv_ch.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.tcking.giraffeplayer2.*;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.nhandz.flrv_ch.DT.news;
import com.nhandz.flrv_ch.MainActivity;
import com.nhandz.flrv_ch.R;

import java.util.ArrayList;

import tcking.github.com.giraffeplayer2.VideoInfo;
import tcking.github.com.giraffeplayer2.VideoView;

public class adapter_watch extends RecyclerView.Adapter<adapter_watch.ViewHolder>{

    private ArrayList<news> newsArrayList;
    private Context context;

    public adapter_watch(ArrayList<news> newsArrayList, Context context) {
        this.newsArrayList = newsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View itemview=layoutInflater.inflate(R.layout.item_watch,parent,false);
        return new adapter_watch.ViewHolder(itemview);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.videoView.getVideoInfo().setBgColor(Color.GRAY).setAspectRatio(VideoInfo.AR_MATCH_PARENT);
        holder.videoView.getVideoInfo().setLooping(true);
        holder.videoView.setVideoPath("https://nhavbnm.000webhostapp.com/Sea%20-%2033194.mp4").setFingerprint(position);
        Glide.with(context)
                .load(MainActivity.serverImg+newsArrayList.get(position).getAvt())
                .into(holder.avt);
        holder.txtName.setText(newsArrayList.get(position).getName());
        holder.txtContent.setText(newsArrayList.get(position).getContent());
        holder.txtTime.setText("10p trước");

    }

    @Override
    public int getItemCount() {
        return newsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public BootstrapCircleThumbnail avt;
        public TextView txtName;
        public TextView txtTime;
        public TextView txtContent;
        public VideoView videoView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName=itemView.findViewById(R.id.itw_txtname);
            txtTime=itemView.findViewById(R.id.itw_txtTime);
            avt=itemView.findViewById(R.id.itw_avt);
            videoView=itemView.findViewById(R.id.itw_videoview);
            txtContent=itemView.findViewById(R.id.itw_txtContent);
        }
    }
}
