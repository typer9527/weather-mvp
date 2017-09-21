package com.yl.appleweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Gson数据Forecast
 * Created by Luke on 2017/9/6.
 */

public class DailyForecast {

    @SerializedName("cond")
    public More more;
    public String date;
    @SerializedName("tmp")
    public Temperature temperature;

    public class More {
        @SerializedName("txt_d")
        public String info;
    }

    public class Temperature {
        public String max;
        public String min;
    }
}
