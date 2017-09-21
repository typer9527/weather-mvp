package com.yl.appleweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Gson数据Now
 * Created by Luke on 2017/9/6.
 */

public class Now {

    @SerializedName("cond")
    public More more;
    public String fl; // 体感温度
    public String hum; // 相对湿度（%）
    public String pcpn; // 降水量（mm）
    public String pres; // 气压
    @SerializedName("tmp")
    public String temperature;
    public String vis; // 能见度（km）
    public Wind wind;

    public class More {
        @SerializedName("txt")
        public String info;
    }

    public class Wind {
        public String dir;
        public String spd;
    }
}
