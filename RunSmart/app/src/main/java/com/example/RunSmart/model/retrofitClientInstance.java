package com.example.RunSmart.model;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class retrofitClientInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.data.gov.sg/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}