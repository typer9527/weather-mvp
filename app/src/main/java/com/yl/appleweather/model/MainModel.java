package com.yl.appleweather.model;

import com.google.gson.Gson;
import com.yl.appleweather.db.CountyWeather;
import com.yl.appleweather.gson.Weather;
import com.yl.appleweather.util.OkHttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * MainActivityModel, 获取天气详细信息
 * Created by Luke on 2017/9/20.
 */

public class MainModel {
    private String url = "https://free-api.heweather.com/v5/weather?city=";
    private String key = "&key=935656148760408aa7b50b5a201891d0";

    public void getWeatherInfo(final String weatherId,
                               final OnGetWeatherListener listener) {
        OkHttpUtil.sendOkHttpRequest(url + weatherId + key, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                listener.onFailed();
            }

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException {
                String responseText = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseText);
                    JSONArray jsonArray = jsonObject.getJSONArray("HeWeather5");
                    String weatherContent = jsonArray.get(0).toString();
                    Weather weather = new Gson().fromJson(weatherContent,
                            Weather.class);
                    // 更新时间
                    CountyWeather county = new CountyWeather();
                    county.setUpdateTime(weather.basic.update.updateTime);
                    county.updateAll("weatherId = ?", weatherId);

                    if (weather != null && "ok".equals(weather.status)) {
                        if (isFirstUse()) {
                            CountyWeather countyWeather = new CountyWeather();
                            countyWeather.setWeatherId(weatherId);
                            countyWeather.setCountyName(weather.basic.cityName);
                            countyWeather.setCurrentTemp(weather.now.temperature);
                            countyWeather.setUpdateTime(weather.basic.update.updateTime);
                            countyWeather.save();
                        }
                        listener.onSucceed(weatherContent);
                    } else {
                        listener.onFailed();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailed();
                }
            }
        });
    }

    private boolean isFirstUse() {
        boolean b = DataSupport.findAll(CountyWeather.class).size() == 0;
        return b;
    }
}
