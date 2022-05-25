package com.example.time.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;


public interface getDataService {

    @GET("v1/environment/2-hour-weather-forecast")
    Call<weatherDataModel> getData(@Query("date") String date);
}
