package com.yl.appleweather.model;

/**
 * 获取详细天气结果的回调
 * Created by Luke on 2017/9/18.
 */

public interface OnGetWeatherListener {
    void onFailed();

    void onSucceed(String weatherJson);
}
