package com.nhandz.flrv_ch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import com.nhandz.flrv_ch.Adapters.adapter_search;
import com.nhandz.flrv_ch.ApiResuorce.Utils2;
import com.nhandz.flrv_ch.DT.account;
import com.nhandz.flrv_ch.DT.news;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();
    private SearchView searchView;
    private RecyclerView recyclerView;
    private ArrayList<account> accounts;
    public adapter_search adt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Anhxa();
        initRE();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals("")){
                    accounts.removeAll(accounts);
                }
                else {
                    Search(s);
                }

                return false;
            }
        });
    }

    private void initRE() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        accounts=new ArrayList<>();
        adt=new adapter_search(getApplicationContext(),accounts);
        recyclerView.setAdapter(adt);
    }

    public void Search(String n){
        Utils2.getInstance().getRetrofitInstance().Search(n).enqueue(new Callback<account[]>() {
            @Override
            public void onResponse(Call<account[]> call, Response<account[]> response) {
                accounts.removeAll(accounts);
                if (response.body()!=null && response.body().length!=0){
                    for (account sx : response.body()
                         ) {;
                        accounts.add(sx);
                    }
                    Log.e(TAG, "onResponse: "+response.body()[0].getAvt() );
                    adt.notifyDataSetChanged();
                }
                else Log.e(TAG, "onResponse: null" );

            }

            @Override
            public void onFailure(Call<account[]> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage() );
            }
        });
    }

    private void Anhxa() {
        searchView=findViewById(R.id.s_inputS);
        recyclerView=findViewById(R.id.s_listRS);
    }
}