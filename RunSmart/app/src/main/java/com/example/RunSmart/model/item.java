package com.example.RunSmart.model;

import java.util.List;

public class item {
    String update_timestamp;
    String timestamp;
    valid_period valid_period;
    List<Forecast> forecasts;
    public item(String update_timestamp,String timestamp, valid_period valid_period,List<Forecast> forecasts){
        this.update_timestamp=update_timestamp;
        this.timestamp=timestamp;
        this.valid_period=valid_period;
        this.forecasts=forecasts;
    }

    public Forecast getChangiForecast() {
        return forecasts.get(9);//9 bcos changi is alw at index 9
    }
}
