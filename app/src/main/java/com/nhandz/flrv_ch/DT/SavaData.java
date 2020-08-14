package com.nhandz.flrv_ch.DT;

import java.util.ArrayList;

public class SavaData {

    public static SavaData instance;
    public static ArrayList<news> listNews;
    public static ArrayList<Story> listStory;

    public static SavaData getInstance(){
        if (instance==null){
            instance=new SavaData();
            listNews=new ArrayList<>();
            listStory=new ArrayList<>();
        }
        return instance;
    }

    public static void SetListNews(news n){
        if (listNews.size()>=30){
            listNews.remove(0);
            listNews.add(n);
        }
        else {
            listNews.add(n);
        }
    }

}
