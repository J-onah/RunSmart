package com.example.time.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class weatherDataModel {

    private List<area_metadata> area_metadata;

    private List<item> items ;

    private APIinfo api_info ;


    public weatherDataModel(List<area_metadata> area_metadata,List<item> items,APIinfo api_info) {
        this.area_metadata = area_metadata;
        this.items = items;
        this.api_info=api_info;

    }

    public List<area_metadata> getArea_metadata() {
        return area_metadata;
    }

    public void setArea_metadata(List<area_metadata> area_metadata) {
        this.area_metadata = area_metadata;
    }
    public List<item> getItems() {
        return items;
    }

    public void setItems(List<item> items) {
        this.items = items;
    }
    public APIinfo getAPIinfo() {
        return api_info;
    }

    public void setAPIinfo(APIinfo api_info) {
        this.api_info = api_info;
    }
}



