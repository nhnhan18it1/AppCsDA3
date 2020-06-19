package com.nhandz.flrv_ch.ui.pagehome;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.nhandz.flrv_ch.ApiResuorce.NewsApi;
import com.nhandz.flrv_ch.DT.news;
import com.nhandz.flrv_ch.HomeActivity;
import com.nhandz.flrv_ch.MainActivity;
import com.nhandz.flrv_ch.R;
import com.nhandz.flrv_ch.DT.*;
import java.util.ArrayList;
import com.nhandz.flrv_ch.Adapters.*;
import com.beardedhen.androidbootstrap.*;

public class PageHomeFragment extends Fragment {

    public static DrawerLayout drawerLayout;
    private PageHomeViewModel mViewModel;
    private RecyclerView recyclerViewNews;
    ArrayList<news> listnews;
    adapter_home_news adt;
    private BootstrapCircleThumbnail Avt;
    private BootstrapEditText txtNewNews;
    public static PageHomeFragment newInstance() {
        return new PageHomeFragment();
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.page_home_fragment, container, false);
        Anhxa(root);
        return root;

    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewModel=new PageHomeViewModel(adt,listnews);
        mViewModel = ViewModelProviders.of(this).get(PageHomeViewModel.class);
        mViewModel.getAvt().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.e("fragment home", "onChanged: "+MainActivity.serverImg+""+s );
                Glide.with(getContext())
                        .load(MainActivity.serverImg+""+s)
                        .error(R.drawable.logo)
                        .into(Avt);
            }
        });

        initRecycleView();
//        mViewModel.UpdateDataNews();
        new NewsApi.getNews(listnews,adt).execute(MainActivity.server+"/api/getnews");
        // TODO: Use the ViewModel
    }

    private void Anhxa(View container) {
        recyclerViewNews=container.findViewById(R.id.home_rcyv_news);
        Avt=container.findViewById(R.id.home_avtfix);
    }

    private void initRecycleView(){
        recyclerViewNews.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,
                false);
        recyclerViewNews.setLayoutManager(linearLayoutManager);
        listnews=new ArrayList<>();
        adt=new adapter_home_news(listnews,getContext());
        recyclerViewNews.setAdapter(adt);
    }

}
