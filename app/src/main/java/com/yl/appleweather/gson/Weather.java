package com.yl.appleweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Gson数据Weather
 * Created by Luke on 2017/9/6.
 */

public class Weather {
    public AQI aqi;
    public Basic basic;
    @SerializedName("daily_forecast")
    public List<DailyForecast> dailyForecastList;
    @SerializedName("hourly_forecast")
    public List<HourlyForecast> hourlyForecastList;
    public Now now;
    public String status;
}
