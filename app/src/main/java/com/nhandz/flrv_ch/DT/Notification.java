package com.nhandz.flrv_ch.DT;

public class Notification {
    private  String ID;
    private  String IDBV;
    private  String IDNG;
    private  String Type;
    private  boolean isRead;
    private  String created_at;
    private  String updated_at;
    private  account accountx;

    public Notification(String ID, String IDBV, String IDNG, String type, boolean isRead, String created_at, String updated_at, account accountx) {
        this.ID = ID;
        this.IDBV = IDBV;
        this.IDNG = IDNG;
        Type = type;
        this.isRead = isRead;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.accountx = accountx;
    }

    public String getType() {
        return Type;
    }

    public String getID() {
        return ID;
    }

    public String getIDBV() {
        return IDBV;
    }

    public String getIDNG() {
        return IDNG;
    }

    public boolean isRead() {
        return isRead;
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
