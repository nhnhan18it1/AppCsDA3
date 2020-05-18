package com.nhandz.flrv_ch.ui.pagehome;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.nhandz.flrv_ch.DT.news;
import com.nhandz.flrv_ch.R;
import com.nhandz.flrv_ch.DT.*;
import java.util.ArrayList;
import com.nhandz.flrv_ch.Adapters.*;
import com.beardedhen.androidbootstrap.*;

public class PageHomeFragment extends Fragment {

    private PageHomeViewModel mViewModel;
    private RecyclerView recyclerViewNews;
    ArrayList<news> Lnews;
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
        Anhxa(container);
        return inflater.inflate(R.layout.page_home_fragment, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(PageHomeViewModel.class);
        mViewModel.getAvt().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Glide.with(getContext())
                        .load(s)
                        .error(R.drawable.logo)
                        .into(Avt);
            }
        });
        // TODO: Use the ViewModel
    }

    private void Anhxa(ViewGroup container) {
        recyclerViewNews=container.findViewById(R.id.home_rcyv_news);
        Avt=container.findViewById(R.id.home_avtfix);
    }

}
