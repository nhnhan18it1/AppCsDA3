package com.nhandz.flrv_ch.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.beardedhen.androidbootstrap.*;
import com.bumptech.glide.Glide;
import com.nhandz.flrv_ch.DT.Notification;
import com.nhandz.flrv_ch.DetailnewsActivity;
import com.nhandz.flrv_ch.MainActivity;
import com.nhandz.flrv_ch.R;

import java.util.ArrayList;


public class adapter_notification extends RecyclerView.Adapter<adapter_notification.Viewholder> {

    ArrayList<Notification> notifications;
    Context context;

    public adapter_notification(ArrayList<Notification> notifications, Context context) {
        this.notifications = notifications;
        this.context = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_notifications,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        if (notifications.get(position) != null) {
            Glide.with(context)
                    .load(MainActivity.serverImg+notifications.get(position).getAccountx().getAvt())
                    .into(holder.avtNoti);
            holder.txtName.setText(notifications.get(position).getAccountx().getName());
            holder.txtContent.setText(notifications.get(position).getType());
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, DetailnewsActivity.class);
                    intent.putExtra("IDBV",notifications.get(position).getIDBV());
                    context.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        public BootstrapCircleThumbnail avtNoti;
        public TextView txtName;
        public TextView txtContent;
        public ImageButton btnMenu;
        public View view;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            avtNoti=itemView.findViewById(R.id.item_noti_avt);
            txtName=itemView.findViewById(R.id.item_notifi_name);
            txtContent=itemView.findViewById(R.id.item_notifi_content);
            btnMenu=itemView.findViewById(R.id.item_notifi_btnMenu);
            this.view=itemView;
        }
    }
}
