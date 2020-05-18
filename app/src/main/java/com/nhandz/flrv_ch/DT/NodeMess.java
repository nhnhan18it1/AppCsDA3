package com.nhandz.flrv_ch.DT;

import java.io.Serializable;

public class NodeMess implements Serializable {
    private String ID;
    private String IDR;
    private String Name;
    private String Content;

    public NodeMess(String ID, String IDR, String name, String content) {
        this.ID = ID;
        this.IDR = IDR;
        Name = name;
        Content = content;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getIDR() {
        return IDR;
    }

    public void setIDR(String IDR) {
        this.IDR = IDR;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
