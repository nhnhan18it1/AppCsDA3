package com.nhandz.flrv_ch.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nhandz.flrv_ch.DT.news;
import com.nhandz.flrv_ch.MainActivity;
import com.nhandz.flrv_ch.R;

import java.util.ArrayList;

public class adapter_galary extends BaseAdapter {
    private ArrayList<news> newsArrayList;
    private Context context;

    public adapter_galary(ArrayList<news> newsArrayList, Context context) {
        this.newsArrayList = newsArrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return newsArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return newsArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 250));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(0, 0, 0, 0);
        Glide.with(context)
                .load(MainActivity.serverImg+newsArrayList.get(i).getImg())
                .error(R.drawable.backgroundl)
                .into(imageView);
        //imageView.setImageResource(thumbImages[position]);
        return imageView;
    }
}
