package com.yl.appleweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Gson数据basic
 * Created by Luke on 2017/9/6.
 */

public class Basic {

    @SerializedName("city")
    public String cityName;
    public Update update;

    public class Update {
        @SerializedName("loc")
        public String updateTime;
    }
}
