package com.nhandz.flrv_ch;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nhandz.flrv_ch.ApiResuorce.NewsApi;
import com.nhandz.flrv_ch.DT.Story;
import com.stone.pile.libs.PileLayout;

import java.util.ArrayList;
import com.beardedhen.androidbootstrap.*;
public class DtStoryActivity extends AppCompatActivity {

    private PileLayout pileLayout;
    private ArrayList<Story> stories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dt_story);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("TIÊU ĐỀ ACTIVITY"); //Thiết lập tiêu đề nếu muốn
        String title = actionBar.getTitle().toString(); //Lấy tiêu đề nếu muốn
        actionBar.hide(); //Ẩn ActionBar nếu muốn
        pileLayout=findViewById(R.id.pileLayout);
        stories=new ArrayList<>();
        PileLayout.Adapter adt = new PileLayout.Adapter() {
            @Override
            public int getLayoutId() {
                return R.layout.item_pile;
            }

            @Override
            public void bindView(View view, int position) {
                super.bindView(view, position);
                ViewHolder viewHolder = (ViewHolder) view.getTag();
                if (viewHolder==null){
                    viewHolder=new ViewHolder();
                    viewHolder.imageView=view.findViewById(R.id.imageView);
                    viewHolder.avt=view.findViewById(R.id.story_avt);
                    viewHolder.txtname=view.findViewById(R.id.story_name);
                    view.setTag(viewHolder);
                }
                Glide.with(getApplicationContext())
                        .load(MainActivity.serverImg+stories.get(position).getSrc())
                        .override((int)MainActivity.Screen_width-20,(int)MainActivity.Screen_height)
                        .into(viewHolder.imageView);
                Glide.with(getApplicationContext())
                        .load(MainActivity.serverImg+stories.get(position).getAvt())
                        .into(viewHolder.avt);
                viewHolder.txtname.setText(stories.get(position).getName());
            }

            @Override
            public int getItemCount() {
                return stories.size();
            }

            @Override
            public void displaying(int position) {
                super.displaying(position);
//                descriptionView.setText("dfafsa");
//                if (lastDisplay<0){
//                    initScene(position);
//                    lastDisplay=0;
//                }
//                else if (lastDisplay!=position){
//                    transitionScene(position);
//                    lastDisplay=position;
//                }

            }

            @Override
            public void onItemClick(View view, int position) {
                super.onItemClick(view, position);
            }
        };
        new NewsApi.getStory(stories,pileLayout,adt).execute();
        pileLayout.setAdapter(adt);



    }
    public class ViewHolder{
        ImageView imageView;
        BootstrapCircleThumbnail avt;
        TextView txtname;
    }
}