package com.nhandz.flrv_ch.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nhandz.flrv_ch.DT.Story;
import com.nhandz.flrv_ch.DtStoryActivity;
import com.nhandz.flrv_ch.MainActivity;
import com.nhandz.flrv_ch.R;
import com.beardedhen.androidbootstrap.*;
import java.util.ArrayList;

public class adapter_story extends RecyclerView.Adapter<adapter_story.ViewHolder> {

    private Context context;
    private ArrayList<Story> stories;

    public adapter_story(Context context, ArrayList<Story> stories) {
        this.context = context;
        this.stories = stories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View itemview=layoutInflater.inflate(R.layout.item_story,parent,false);
        return new adapter_story.ViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(MainActivity.serverImg+stories.get(position).getAvt())
                .into(holder.avt);
        Glide.with(context)
                .load(MainActivity.serverImg+stories.get(position).getSrc())
                //.fitCenter()
                .centerCrop()
                //.centerInside()
                .into(holder.imageView);
        holder.itv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DtStoryActivity.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        BootstrapCircleThumbnail avt;
        ImageView imageView;
        View itv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avt=itemView.findViewById(R.id.item_story_avt);
            imageView=itemView.findViewById(R.id.item_story_content);
            itv=itemView;
        }
    }
}
