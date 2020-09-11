package com.nhandz.flrv_ch.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.nhandz.flrv_ch.DT.news;
import com.nhandz.flrv_ch.MainActivity;
import com.nhandz.flrv_ch.R;

import java.util.ArrayList;

public class adapter_slide extends RecyclerView.Adapter<adapter_slide.ViewHolder> {

    ArrayList<news> newsArrayList;
    Context context;

    public adapter_slide(ArrayList<news> newsArrayList, Context context) {
        this.newsArrayList = newsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.item_slide,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(MainActivity.serverImg+newsArrayList.get(position).getImg())
                .error(R.drawable.backgroundl)
                .into(holder.itSlide);
    }

    @Override
    public int getItemCount() {
        return newsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public KenBurnsView itSlide;
        public View itv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itSlide=itemView.findViewById(R.id.it_slide_img);
            itv=itemView;
        }
    }
}
