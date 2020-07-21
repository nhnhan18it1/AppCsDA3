package com.nhandz.flrv_ch.DT;

import java.io.Serializable;

public class AdviseFriends implements Serializable {
    private  String IDNN;
    private  String IDNG;
    private  String created_at;
    private  String updated_at;
    private  account accountx;

    public AdviseFriends(String IDNN, String IDNG, String created_at, String updated_at,account acc) {
        this.IDNN = IDNN;
        this.IDNG = IDNG;
        this.created_at = created_at;
        this.updated_at = updated_at;
        accountx=acc;
    }

    public String getIDNN() {
        return IDNN;
    }

    public String getIDNG() {
        return IDNG;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public account getAccountx() {
        return accountx;
    }
}
