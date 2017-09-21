package com.yl.appleweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Gson数据AQI
 * Created by Luke on 2017/9/6.
 */

public class AQI {

    public AQICity city;

    public class AQICity {
        public String aqi;
        public String pm25;
        @SerializedName("qlty")
        public String airQuality;
    }
}
