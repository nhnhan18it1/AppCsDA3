package com.nhandz.flrv_ch.ui.pagehome;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nhandz.flrv_ch.DT.news;

import java.util.ArrayList;

public class PageHomeViewModel extends ViewModel {
    private MutableLiveData<ArrayList<news>> mNews;
    private MutableLiveData<String> mAvt;

    public PageHomeViewModel() {
        mNews=new MutableLiveData<>();
        mAvt=new MutableLiveData<>();
        mAvt.setValue("https://cdn.fado.vn/images/I/419yx7m3bTL._SR600,600_.jpg");

    }

    public LiveData<String> getAvt(){
        return mAvt;
    }

    public LiveData<ArrayList<news>> getNews(){return mNews;}

    // TODO: Implement the ViewModel
}
