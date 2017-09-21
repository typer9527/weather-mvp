package com.yl.appleweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Gson数据Forecast
 * Created by Luke on 2017/9/6.
 */

public class HourlyForecast {

    @SerializedName("cond")
    public More more;
    public String date;
    @SerializedName("tmp")
    public String temperature;

    public class More {
        @SerializedName("txt")
        public String info;
    }
}
