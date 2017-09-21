package com.yl.appleweather.view;

/**
 * 区域选择选择的操作
 * Created by Luke on 2017/9/18.
 */

public interface ChooseAreaView {
    void showProgressbar();

    void hideProgressbar();

    void showProvinceList();

    void showCityList();

    void showCountyList();

    void loadAreaInfoFailed();

    void jumpToMarkedCounty();

    void onDestroyView();
}
