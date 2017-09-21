package com.yl.appleweather.model;

/**
 * 获取简要天气结果的回调
 * Created by Luke on 2017/9/18.
 */

public interface OnGetServerInfoListener {
    void onFailed();

    void onSucceed();
}
