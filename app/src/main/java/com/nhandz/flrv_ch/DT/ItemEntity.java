package com.nhandz.flrv_ch.DT;

import org.json.JSONObject;

/**
 * Created by xmuSistone on 2017/5/12.
 */

public class ItemEntity {

//    private String country;
//    private String temperature;
    private String coverImageUrl;
//    private String address;
//    private String description;
//    private String time;
//    private String mapImageUrl;

    public ItemEntity(JSONObject jsonObject) {
//        this.country = jsonObject.optString("country");
//        this.temperature = jsonObject.optString("temperature");
        this.coverImageUrl = jsonObject.optString("coverImageUrl");
//        this.address = jsonObject.optString("address");
//        this.description = jsonObject.optString("description");
//        this.time = jsonObject.optString("time");
//        this.mapImageUrl = jsonObject.optString("mapImageUrl");
    }

    public ItemEntity(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }


}
