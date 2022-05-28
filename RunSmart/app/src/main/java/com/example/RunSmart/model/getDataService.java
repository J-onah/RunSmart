package com.example.RunSmart.model;

import retrofit2.Call;
import retrofit2.http.*;


public interface getDataService {

    @GET("v1/environment/2-hour-weather-forecast")
    Call<weatherDataModel> getData(@Query("date") String date);
}
