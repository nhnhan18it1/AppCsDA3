package com.nhandz.flrv_ch.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nhandz.flrv_ch.DT.*;
import com.nhandz.flrv_ch.MainActivity;
import com.nhandz.flrv_ch.R;
import com.beardedhen.androidbootstrap.*;
import java.util.ArrayList;

public class adapter_adviseFriend extends RecyclerView.Adapter<adapter_adviseFriend.ViewHolder> {

    ArrayList<com.nhandz.flrv_ch.DT.AdviseFriends> adviseFriends;
    Context context;

    public adapter_adviseFriend(ArrayList<AdviseFriends> adviseFriends, Context context) {
        this.adviseFriends = adviseFriends;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View itemView=layoutInflater.inflate(R.layout.item_advise_friend,parent,false);
        return new adapter_adviseFriend.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(MainActivity.serverImg+""+adviseFriends.get(position).getAccountx().getAvt())
                .error(R.drawable.logo)
                .into(holder.bootstrapCircleThumbnail);
        holder.textViewName.setText(adviseFriends.get(position).getAccountx().getName());
        holder.btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return adviseFriends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public BootstrapCircleThumbnail bootstrapCircleThumbnail;
        public TextView textViewName;
        public BootstrapButton btnXacNhan;
        public BootstrapButton btnXoa;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bootstrapCircleThumbnail=itemView.findViewById(R.id.advise_avt);
            textViewName=itemView.findViewById(R.id.advise_Name);
            btnXacNhan=itemView.findViewById(R.id.advise_btnXacNhan);
            btnXoa=itemView.findViewById(R.id.advise_btnXoa);
        }
    }
}
