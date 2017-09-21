package com.yl.appleweather.view;

/**
 * 天气主页面的操作
 * Created by Luke on 2017/9/20.
 */

public interface MainView {
    void chooseArea();

    void showCountyList();

    void showWeatherInfo();

    void refreshWeatherInfo();

    void onRefreshFailed();

    void onRefreshSucceed();

    void loadBackgroundImg();

    void requestWeather(String weatherId);

    void onFailed();

    void showProgressDialog();

    void hideProgressDialog();

    void onDestroyView();
}
