package com.nhandz.flrv_ch.ui.pagehome;

import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.nhandz.flrv_ch.ApiResuorce.NewsApi;
import com.nhandz.flrv_ch.ApiResuorce.Utils2;
import com.nhandz.flrv_ch.DT.news;
import com.nhandz.flrv_ch.HomeActivity;
import com.nhandz.flrv_ch.MainActivity;
import com.nhandz.flrv_ch.R;
import com.nhandz.flrv_ch.DT.*;
import java.util.ArrayList;
import com.nhandz.flrv_ch.Adapters.*;
import com.beardedhen.androidbootstrap.*;
import com.nhandz.flrv_ch.baivietmoiActivity;
import com.stone.pile.libs.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nhandz.flrv_ch.MainActivity.server;


public class PageHomeFragment extends Fragment {

    public static DrawerLayout drawerLayout;
    private PageHomeViewModel mViewModel;
    private static RecyclerView recyclerViewNews;
    private static ArrayList<news> listnews;
    private  static adapter_home_news adt;
    private BootstrapCircleThumbnail Avt;
    private BootstrapEditText txtNewNews;
    public static PageHomeFragment newInstance() {
        return new PageHomeFragment();
    }
    private PileLayout pileLayout;
    public static ArrayList<ItemEntity> list_itemEntities;
    public boolean loading =true;
    public static NestedScrollView nestedScrollView;
    private EditText inputNews;
    private RecyclerView rVwStory;
    public static SwipeRefreshLayout swipeRefreshLayout;
    public static boolean isLoading=false;
    public static adapter_story adtStory;
    public static ArrayList<Story> stories;
    public static boolean first=true;
    private static int count=0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.page_home_fragment, container, false);
        Anhxa(root);
        Glide.get(getContext()).setMemoryCategory(MemoryCategory.HIGH);
        return root;

    }

    public static void reLoadNews(){
        if (recyclerViewNews!=null && adt!=null){
                //listnews.removeAll(listnews);
             //new NewsApi.getNews(listnews,adt,"reload").execute(server+"/api/getnews");
             recyclerViewNews.scrollToPosition(0);
             swipeRefreshLayout.setRefreshing(true);
             count=0;
            Utils2.getInstance().getRetrofitInstance().getNews("0").enqueue(new Callback<news[]>() {
                @Override
                public void onResponse(Call<news[]> call, Response<news[]> response) {
                    Log.e("F_home", "onResponse: "+response.body().length );
                    listnews.removeAll(listnews);
                    for (news n:response.body()
                         ) {
                        listnews.add(n);
                    }
                    adt.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<news[]> call, Throwable t) {
                    Log.e("F_home", "onResponse: "+t.getMessage() );
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        NestedScrollView nestedScrollView;
        getActivity();
        //mViewModel=new PageHomeViewModel(adt,listnews);
        if (mViewModel==null){
            mViewModel = ViewModelProviders.of(this).get(PageHomeViewModel.class);
        }

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
        if (MainActivity.OnAccount!=null){
            Glide.with(getContext())
                    .load(MainActivity.serverImg+""+MainActivity.OnAccount.getAvt())
                    .error(R.drawable.logo)
                    .into(Avt);
        }

        initReStory();
        initRecycleView();
        new NewsApi.getStory(stories,adtStory).execute();

        mViewModel.getNews().observe(getViewLifecycleOwner(), new Observer<ArrayList<news>>() {
            @Override
            public void onChanged(ArrayList<news> news) {
                if (news!=null){
                    if (news.size()==0){
                        getLnews("0");
                    }
                    else {
                        if (listnews.size()>=30){
                            for (int i=0;i<10;i++)
                                listnews.remove(0);
                        }
                        listnews.addAll(news);
                        //recyclerViewNews.scrollToPosition(listnews.size()-10-1);
                        adt.notifyDataSetChanged();
                    }
                }
                else {
                    getLnews("0");
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listnews.removeAll(listnews);
                count=0;
                //new NewsApi.getNews(listnews,adt,mViewModel.getNews()).execute(server+"/api/test1");
                reLoadNews();
            }
        });

        inputNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("home frag", "onClick: true" );
                Intent intent=new Intent(getActivity() , baivietmoiActivity.class);
                startActivity(intent);
            }
        });
        // TODO: Use the ViewModel
    }

    private void Anhxa(View container) {
        recyclerViewNews=container.findViewById(R.id.home_rcyv_news);
        Avt=container.findViewById(R.id.home_avtfix);
        //pileLayout=container.findViewById(R.id.pileLayout);
        rVwStory=container.findViewById(R.id.home_rcyv_story);
        list_itemEntities=new ArrayList<>();
        nestedScrollView=container.findViewById(R.id.scrollView_CTfragHome);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                //Log.e("home_frag","y="+(scrollY-(v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()))+" x="+scrollX);
                if (!isLoading){
                    if (scrollY-(v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())==0){
                        //isLoading=true;
                        getLnews(String.valueOf(listnews.size()));
                        //new NewsApi.getNews(listnews,adt).execute(server+"/api/test1");
                        //adt.notifyDataSetChanged();
                    }
                }

            }
        });
        inputNews=container.findViewById(R.id.home_inputtus);
        swipeRefreshLayout=container.findViewById(R.id.swipeRefreshLayout);
    }

    public void getLnews(String s){
        s=String.valueOf(10*count);
        Log.e("F_home", "getLnews:s "+s );
        if (isLoading){
            return;
        }
        isLoading=true;
        count++;
        Utils2.getInstance().getRetrofitInstance().getNews(s).enqueue(new Callback<news[]>() {
            @Override
            public void onResponse(Call<news[]> call, Response<news[]> response) {
                Log.e("F_home", "onResponse: "+response.body().length );
                ArrayList<news>x=new ArrayList<>();
                //listnews.removeAll(listnews);
                for (news n:response.body()
                ) {
                    x.add(n);
                }
                //adt.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                isLoading=false;
                mViewModel.setmNews(x);
            }

            @Override
            public void onFailure(Call<news[]> call, Throwable t) {
                Log.e("F_home", "onResponse: "+t.getMessage() );
                swipeRefreshLayout.setRefreshing(false);
                isLoading=false;
            }
        });
    }

    private void initRecycleView(){
        recyclerViewNews.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,
                false);
        linearLayoutManager.setAutoMeasureEnabled(true);
        recyclerViewNews.setLayoutManager(linearLayoutManager);
        listnews=new ArrayList<>();
        adt=new adapter_home_news(listnews,getContext(),getFragmentManager());
        recyclerViewNews.setAdapter(adt);
        recyclerViewNews.setNestedScrollingEnabled(false);

    }

    public void initReStory(){
        rVwStory.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL,
                false);
        stories=new ArrayList<>();
        rVwStory.setLayoutManager(linearLayoutManager);
        adtStory=new adapter_story(getContext(),stories);
        rVwStory.setAdapter(adtStory);
        rVwStory.setNestedScrollingEnabled(false);
        stories.add(new Story("1","1","nhan","/public/assets/img/newi/news_74.png","img","/public/assets/img/newi/news_74.png","",""));
        stories.add(new Story("1","1","nhan","/public/assets/img/newi/news_74.png","img","/public/assets/img/newi/news_74.png","",""));
        stories.add(new Story("1","1","nhan","/public/assets/img/newi/news_74.png","img","/public/assets/img/newi/news_74.png","",""));

    }

    public class ViewHolder {
        ImageView imageView;
    }

}
