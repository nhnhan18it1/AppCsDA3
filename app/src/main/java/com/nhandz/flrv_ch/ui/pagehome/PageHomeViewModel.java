package com.nhandz.flrv_ch.ui.pagehome;

import android.widget.Adapter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nhandz.flrv_ch.DT.news;
import com.nhandz.flrv_ch.Adapters.*;
import com.nhandz.flrv_ch.MainActivity;
import com.nhandz.flrv_ch.ApiResuorce.*;
import java.util.ArrayList;

public class PageHomeViewModel extends ViewModel {
    private MutableLiveData<ArrayList<news>> mNews;
    private MutableLiveData<String> mAvt;
    private adapter_home_news adt;
    private ArrayList<news> listnews;

    public PageHomeViewModel() {
        mNews=new MutableLiveData<>();
        mAvt=new MutableLiveData<>();
        mAvt.setValue(MainActivity.OnAccount.getAvt());

    }

    public void setDataRe(adapter_home_news adt,ArrayList<news> news){
        this.adt=adt;
        this.listnews=news;
    }

    public void UpdateDataNews(){
        new NewsApi.getNews(listnews,adt).execute(MainActivity.server+"/api/getnews");
    }

    public LiveData<String> getAvt(){
        return mAvt;
    }

    public LiveData<ArrayList<news>> getNews(){return mNews;}

    // TODO: Implement the ViewModel
}
