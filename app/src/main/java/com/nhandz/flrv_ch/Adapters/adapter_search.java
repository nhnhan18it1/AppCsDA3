package com.nhandz.flrv_ch.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.bumptech.glide.Glide;
import com.nhandz.flrv_ch.DT.account;
import com.nhandz.flrv_ch.DT.news;
import com.nhandz.flrv_ch.MainActivity;
import com.nhandz.flrv_ch.OtprofileActivity;
import com.nhandz.flrv_ch.R;

import java.util.ArrayList;

public class adapter_search extends RecyclerView.Adapter<adapter_search.ViewHolder>  {
    Context context;
    ArrayList<account> accountsArrayList;

    public adapter_search(Context context, ArrayList<account> newsArrayList) {
        this.context = context;
        this.accountsArrayList = newsArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_search,parent,false);
        return new  adapter_search.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        account ac = accountsArrayList.get(position);
        if (ac!=null){
            String lA="";
            if (ac.getAvt().indexOf("http")!=-1){
                lA=ac.getAvt();
            }
            else {
                lA = MainActivity.serverImg+ac.getAvt();
            }
            Glide.with(context)
                    .load(lA)
                    .into(holder.avt);
            holder.txtName.setText(ac.getName());
            holder.txtBanChung.setText("2 bạn chung");
            holder.itv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, OtprofileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }

    }



    @Override
    public int getItemCount() {
        return accountsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public BootstrapCircleThumbnail avt;
        public TextView txtName;
        public TextView txtBanChung;
        public View itv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avt=itemView.findViewById(R.id.itSearch_avt);
            txtName=itemView.findViewById(R.id.itSearch_txtName);
            txtBanChung=itemView.findViewById(R.id.itSearch_txtBC);
            itv=itemView;
        }
    }
}
