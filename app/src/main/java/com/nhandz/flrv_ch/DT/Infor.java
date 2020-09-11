package com.nhandz.flrv_ch.DT;

import java.io.Serializable;

public class Infor implements Serializable {
    private String[] education ;
    private String[] works;
    private String[] relation;
    private String[] live;
    private String friend;

    public Infor(String[] education, String[] works, String[] relation, String[] live, String STfriend) {
        this.education = education;
        this.works = works;
        this.relation = relation;
        this.live = live;
        this.friend = STfriend;
    }

    public String getSTfriend() {
        return friend;
    }

    public void setSTfriend(String STfriend) {
        this.friend = STfriend;
    }

    public String[] getEducation() {
        return education;
    }

    public void setEducation(String[] education) {
        this.education = education;
    }

    public String[] getWorks() {
        return works;
    }

    public void setWorks(String[] works) {
        this.works = works;
    }

    public String[] getRelation() {
        return relation;
    }

    public void setRelation(String[] relation) {
        this.relation = relation;
    }

    public String[] getLive() {
        return live;
    }

    public void setLive(String[] live) {
        this.live = live;
    }
}
