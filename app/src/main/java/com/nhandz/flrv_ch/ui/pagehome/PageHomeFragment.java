package com.nhandz.flrv_ch.ui.pagehome;

import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.nhandz.flrv_ch.ApiResuorce.NewsApi;
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
    public static boolean isLoading=false;
    public static adapter_story adtStory;
    public static ArrayList<Story> stories;
    public static boolean first=true;


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
             new NewsApi.getNews(listnews,adt,"reload").execute(server+"/api/getnews");
             recyclerViewNews.scrollToPosition(0);
        }
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        NestedScrollView nestedScrollView;

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
        if (mViewModel.getmNews().getValue()==null){
            if (PageHomeViewModel.listnews!=null){
                listnews=PageHomeViewModel.listnews;
                initRecycleView();
                new NewsApi.getNews(listnews,adt,mViewModel.getNews()).execute(server+"/api/test1");
            }
            else {
                listnews=new ArrayList<>();
                initRecycleView();
                new NewsApi.getNews(listnews,adt,mViewModel.getNews()).execute(server+"/api/test1");
            }
        }
        else {
            listnews=mViewModel.getmNews().getValue();
            initRecycleView();
            adt.notifyDataSetChanged();
        }

        initReStory();
        new NewsApi.getStory(stories,adtStory).execute();

//        mViewModel.UpdateDataNews();
        mViewModel.getNews().observe(getViewLifecycleOwner(), new Observer<ArrayList<news>>() {
            @Override
            public void onChanged(ArrayList<news> news) {
                if (news!=null){
                    if (news.size()==0){
//                        listnews=new ArrayList<>();
//                        initRecycleView();
//                        new NewsApi.getNews(listnews,adt,mViewModel.getNews()).execute(server+"/api/getnews");
                    }
                    else {
                        listnews.addAll(news);
                        adt.notifyDataSetChanged();
                    }
                }
                else {
//                    listnews=new ArrayList<>();
//                    initRecycleView();
//                    new NewsApi.getNews(listnews,adt).execute(server+"/api/getnews");
                }
            }
        });



//        pileLayout.setAdapter(new PileLayout.Adapter() {
//
//            @Override
//            public int getLayoutId() {
//                return R.layout.item_pile;
//            }
//
//            @Override
//            public void bindView(View view, int position) {
//                super.bindView(view, position);
//                ViewHolder viewHolder = (ViewHolder) view.getTag();
//                if (viewHolder==null){
//                    viewHolder=new ViewHolder();
//                    viewHolder.imageView=view.findViewById(R.id.imageView);
//                    view.setTag(viewHolder);
//                }
//                Glide.with(getContext())
//                        .load(MainActivity.serverImg+listnews.get(position).getImg())
//                        .override(600,721)
//                        .into(viewHolder.imageView);
//
//            }
//
//            @Override
//            public int getItemCount() {
//                return list_itemEntities.size();
//            }
//
//
//            @Override
//            public void displaying(int position) {
//                // super.displaying(position);
////                descriptionView.setText("dfafsa");
////                if (lastDisplay<0){
////                    initScene(position);
////                    lastDisplay=0;
////                }
////                else if (lastDisplay!=position){
////                    transitionScene(position);
////                    lastDisplay=position;
////                }
//
//            }
//
//            @Override
//            public void onItemClick(View view, int position) {
//                super.onItemClick(view, position);
//            }
//        });
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
                        isLoading=true;
                        new NewsApi.getNews(listnews,adt).execute(server+"/api/test1");
                        //adt.notifyDataSetChanged();
                    }
                }

            }
        });
        inputNews=container.findViewById(R.id.home_inputtus);
    }

    private void initRecycleView(){
        recyclerViewNews.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,
                false);

        recyclerViewNews.setLayoutManager(linearLayoutManager);
        adt=new adapter_home_news(listnews,getContext());
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
