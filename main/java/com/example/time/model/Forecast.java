package com.example.time.model;

public class Forecast {
    String area;
    String forecast;
    public Forecast(String area, String forecast){
        this.area=area;
        this.forecast=forecast;
    }
    public String getWeather(){
        return forecast;
    }
}
