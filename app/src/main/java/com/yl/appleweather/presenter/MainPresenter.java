package com.yl.appleweather.presenter;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.yl.appleweather.activity.MainActivity;
import com.yl.appleweather.model.MainModel;
import com.yl.appleweather.model.OnGetWeatherListener;
import com.yl.appleweather.util.OkHttpUtil;
import com.yl.appleweather.view.MainView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 天气显示和刷新的Presenter
 * Created by Luke on 2017/9/20.
 */

public class MainPresenter {
    private MainView view;
    private MainModel model;
    private MainActivity activity;
    private final String url = "http://guolin.tech/api/bing_pic";

    public MainPresenter(MainView view) {
        this.view = view;
        model = new MainModel();
        activity = (MainActivity) view;
    }

    public void loadImage() {
        OkHttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException {
                String imageUrl = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager
                        .getDefaultSharedPreferences(activity).edit();
                editor.putString("image_url", imageUrl);
                editor.apply();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.loadBackgroundImg();
                    }
                });
            }
        });
    }

    public String getImagePrefs() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(activity);
        return prefs.getString("image_url", null);
    }

    public String getWeatherPrefs() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(activity);
        return prefs.getString("weather", null);
    }

    public String getCurrentWeatherId() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(activity);
        return prefs.getString("weather_id", null);
    }

    public void refreshWeatherInfo(String weatherId) {
        loadImage();
        model.getWeatherInfo(weatherId, new OnGetWeatherListener() {
            @Override
            public void onFailed() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.onRefreshFailed();
                    }
                });
            }

            @Override
            public void onSucceed(String weatherJson) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.onRefreshSucceed();
                        view.showWeatherInfo();
                    }
                });
            }
        });
    }

    public void getWeatherInfo(final String weatherId) {
        view.showProgressDialog();
        model.getWeatherInfo(weatherId, new OnGetWeatherListener() {
            @Override
            public void onFailed() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.hideProgressDialog();
                        view.onFailed();
                    }
                });
            }

            @Override
            public void onSucceed(String weatherJson) {
                view.hideProgressDialog();
                // 存
                SharedPreferences.Editor editor = PreferenceManager
                        .getDefaultSharedPreferences(activity).edit();
                editor.putString("weather_id", weatherId);
                editor.putString("weather", weatherJson);
                editor.apply();
                // 显示
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.showWeatherInfo();
                    }
                });
            }
        });
    }
}
