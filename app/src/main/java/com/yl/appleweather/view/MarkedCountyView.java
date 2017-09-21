package com.yl.appleweather.view;

import android.content.Intent;

/**
 * 本地County列表页面的操作
 * Created by Luke on 2017/9/19.
 */

public interface MarkedCountyView {
    void refreshCountyList(Intent intent);

    void showMarkedCounty();

    void addMarkedCounty();

    void addCountyFailed();

    void jumpToCountyWeather(String weatherId);

    void showProgressDialog();

    void hideProgressDialog();

    void onDestroyView();
}
