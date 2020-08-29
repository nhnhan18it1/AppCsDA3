package com.nhandz.flrv_ch.ui.pagehome;

import android.util.Log;
import android.widget.Adapter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModel;

import com.nhandz.flrv_ch.DT.SavaData;
import com.nhandz.flrv_ch.DT.news;
import com.nhandz.flrv_ch.Adapters.*;
import com.nhandz.flrv_ch.MainActivity;
import com.nhandz.flrv_ch.ApiResuorce.*;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PageHomeViewModel extends ViewModel {
    private MutableLiveData<ArrayList<news>> mNews;
    private MutableLiveData<String> mAvt;
    public static ArrayList<news> listnews;
    private String TAG = this.getClass().getSimpleName();
    public PageHomeViewModel() {

        if (mNews==null){
            mNews=new MutableLiveData<>();
            if (SavaData.getInstance().listNews.size()==0){

                createNews();
            }
            else {
                mNews.setValue(SavaData.getInstance().listNews);
            }

        }
        mAvt=new MutableLiveData<>();
//        mAvt.setValue(MainActivity.OnAccount.getAvt());
        if (listnews!=null){
            mNews.setValue(listnews);
        }
    }


    public LiveData<String> getAvt(){
        return mAvt;
    }
    public void setmNews(ArrayList<news> n){
        mNews.postValue(n);
    }
    public LiveData<ArrayList<news>> getNews(){return mNews;}

    public MutableLiveData<ArrayList<news>> getmNews() {
        return mNews;
    }



    public void createNews(){
        Utils2.getInstance().getRetrofitInstance().getNews("0",String.valueOf(MainActivity.OnAccount.getID())).enqueue(new Callback<news[]>() {
            @Override
            public void onResponse(Call<news[]> call, Response<news[]> response) {
                ArrayList<news> s=new ArrayList<>();
                for (news sx:response.body()
                     ) {
                    s.add(sx);
                    SavaData.getInstance().SetListNews(sx);
                }
                mNews.postValue(s);
                Log.e(TAG, "onResponse: "+response.body().length );
            }

            @Override
            public void onFailure(Call<news[]> call, Throwable t) {

            }
        });
    }



    @Override
    protected void onCleared() {
        //super.onCleared();
        Log.e(TAG, "onCleared: " );
    }
    // TODO: Implement the ViewModel
}
