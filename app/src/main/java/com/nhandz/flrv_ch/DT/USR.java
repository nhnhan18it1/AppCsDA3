package com.nhandz.flrv_ch.DT;

import java.io.Serializable;

public class USR implements Serializable {
    private  String ID;
    private  String Status;

    public USR(String ID, String status) {
        this.ID = ID;
        Status = status;
    }

    public String getID() {
        return ID;
    }

    public String getStatus() {
        return Status;
    }
}
