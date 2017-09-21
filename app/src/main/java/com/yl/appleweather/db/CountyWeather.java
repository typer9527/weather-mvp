package com.yl.appleweather.db;

import org.litepal.crud.DataSupport;

/**
 * 本地保存的CountyWeather
 * Created by Luke on 2017/9/19.
 */

public class CountyWeather extends DataSupport {
    private String weatherId;
    private String countyName;
    private String currentTemp;
    private String updateTime;

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(String currentTemp) {
        this.currentTemp = currentTemp;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
