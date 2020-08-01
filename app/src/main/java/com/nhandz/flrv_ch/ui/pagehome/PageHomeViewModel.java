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
    public static ArrayList<news> listnews;

    public PageHomeViewModel() {
        mNews=new MutableLiveData<>();
        mAvt=new MutableLiveData<>();
//        mAvt.setValue(MainActivity.OnAccount.getAvt());
        if (listnews!=null){
            mNews.setValue(listnews);
        }
    }

    public LiveData<String> getAvt(){
        return mAvt;
    }

    public LiveData<ArrayList<news>> getNews(){return mNews;}

    public MutableLiveData<ArrayList<news>> getmNews() {
        return mNews;
    }
    // TODO: Implement the ViewModel
}
