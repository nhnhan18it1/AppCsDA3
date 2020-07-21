package com.nhandz.flrv_ch.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.beardedhen.androidbootstrap.*;
import com.bumptech.glide.Glide;
import com.nhandz.flrv_ch.MainActivity;
import com.nhandz.flrv_ch.R;
import com.nhandz.flrv_ch.DT.*;

import java.util.ArrayList;

public class adapter_ContentChat extends RecyclerView.Adapter<adapter_ContentChat.ViewHolder> {
    ArrayList<Chats> listChats;
    Context context;
    account AccOnChat;

    public adapter_ContentChat(ArrayList<Chats> listChats, Context context, account accOnChat) {
        this.listChats = listChats;
        this.context = context;
        AccOnChat = accOnChat;
    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);

        if(listChats.get(position).getIDsend().equals(String.valueOf(MainActivity.OnAccount.getID()))){
            return 1;
        }
        else {
            return 0;
        }

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==0){
            LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
            View itemView=layoutInflater.inflate(R.layout.contentchat_item,parent,false);
            return new ViewHolder(itemView);
        }
        else {
            LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
            View itemView=layoutInflater.inflate(R.layout.contentchat2_item,parent,false);
            return new ViewHolder(itemView);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (getItemViewType(position)==0){
            if (AccOnChat!=null){
                Glide.with(context)
                        .load(MainActivity.serverImg+""+AccOnChat.getAvt())
                        .into(holder.avtF);
            }

            holder.bootstrapLabel.setText(listChats.get(position).getContent());
        }
        else {
            holder.bootstrapLabel2.setText(listChats.get(position).getContent());
        }


    }

    public void UpdateAvt(){
        for (int i=0;i<listChats.size();i++){
            if (!listChats.get(i).getIDsend().equals(String.valueOf(MainActivity.OnAccount.getID()))){
                this.notifyItemChanged(i);
            }
        }
    }

    @Override
    public int getItemCount() {
        return listChats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView bootstrapLabel;
        LinearLayout Container;
        BootstrapCircleThumbnail avtF;
        TextView bootstrapLabel2;
        LinearLayout Container2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bootstrapLabel=itemView.findViewById(R.id.adt_chat_lblCt);
            Container=itemView.findViewById(R.id.adt_chat_container);
            avtF=itemView.findViewById(R.id.adt_chat_avt);
            bootstrapLabel2=itemView.findViewById(R.id.adt_chat_lblCt2);
            Container2=itemView.findViewById(R.id.adt_chat_container2);
        }
    }
}
