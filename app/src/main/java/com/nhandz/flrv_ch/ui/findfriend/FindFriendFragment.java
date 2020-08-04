package com.nhandz.flrv_ch.ui.findfriend;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.nhandz.flrv_ch.Adapters.adapter_galary;
import com.nhandz.flrv_ch.ApiResuorce.Utils2;
import com.nhandz.flrv_ch.DT.news;
import com.nhandz.flrv_ch.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FindFriendFragment extends Fragment {

    private String TAG="fff";
    private FindFriendViewModel mViewModel;
    private View itemview;
    private GridView gridView;
    private adapter_galary adt;
    private ArrayList<news> newsArrayList;
    public static FindFriendFragment newInstance() {
        return new FindFriendFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        itemview=inflater.inflate(R.layout.find_friend_fragment, container, false);
        return itemview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FindFriendViewModel.class);
        gridView = itemview.findViewById(R.id.fff_gridview);

        newsArrayList=new ArrayList<>();
        adt=new adapter_galary(newsArrayList,getContext());
        //adt.notifyDataSetChanged();
        gridView.setAdapter(adt);
        Utils2.getInstance().getRetrofitInstance().getGalary().enqueue(new Callback<news[]>() {
            @Override
            public void onResponse(Call<news[]> call, Response<news[]> response) {
                for (news s:response.body()
                     ) {
                    newsArrayList.add(s);
                }
                adt.notifyDataSetChanged();
                Log.e(TAG, "onResponse: "+response.body().toString() );
            }

            @Override
            public void onFailure(Call<news[]> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage() );
            }
        });
        // TODO: Use the ViewModel
    }

}