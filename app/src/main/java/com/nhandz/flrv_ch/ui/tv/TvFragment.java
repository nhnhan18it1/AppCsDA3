package com.nhandz.flrv_ch.ui.tv;

import androidx.lifecycle.ViewModelProviders;

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

import com.nhandz.flrv_ch.Adapters.adapter_home_news;
import com.nhandz.flrv_ch.Adapters.adapter_watch;
import com.nhandz.flrv_ch.ApiResuorce.Utils2;
import com.nhandz.flrv_ch.DT.news;
import com.nhandz.flrv_ch.MainActivity;
import com.nhandz.flrv_ch.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvFragment extends Fragment {

    private String TAG = this.getClass().getSimpleName();
    private TvViewModel mViewModel;
    private RecyclerView RCV_video;
    private View itemView;
    private ArrayList<news> listnews;
    public static adapter_watch adt;


    public static TvFragment newInstance() {
        return new TvFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        itemView=inflater.inflate(R.layout.tv_fragment, container, false);
        return itemView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TvViewModel.class);
        // TODO: Use the ViewModel
        InitRe();
        Utils2.getInstance().getRetrofitInstance().getVideo(String.valueOf(MainActivity.OnAccount.getID()),"0").enqueue(new Callback<news[]>() {
            @Override
            public void onResponse(Call<news[]> call, Response<news[]> response) {
                for (news s:response.body()
                     ) {
                    listnews.add(s);
                }
                adt.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<news[]> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage() );
            }
        });
    }

    public void InitRe(){
        RCV_video=itemView.findViewById(R.id.tvf_list_video);
        RCV_video.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,
                false);
        linearLayoutManager.setAutoMeasureEnabled(true);
        RCV_video.setLayoutManager(linearLayoutManager);
        listnews=new ArrayList<>();
        listnews.add(new news("nhandz",
                MainActivity.OnAccount.getAvt(),
                1,
                1,
                "hello",
                "https://nhavbnm.000webhostapp.com/Sea%20-%2033194.mp4",
                "video",
                1,
                true,
                "",
                "",
                null
                ));
        adt=new adapter_watch(listnews,getContext());
        RCV_video.setAdapter(adt);
        RCV_video.setNestedScrollingEnabled(false);
    }

}