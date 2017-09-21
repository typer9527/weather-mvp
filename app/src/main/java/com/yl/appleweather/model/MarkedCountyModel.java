package com.yl.appleweather.model;

import com.google.gson.Gson;
import com.yl.appleweather.db.CountyWeather;
import com.yl.appleweather.gson.SimpleWeather;
import com.yl.appleweather.util.OkHttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 保存的County Model，获取简要的天气信息
 * Created by Luke on 2017/9/19.
 */

public class MarkedCountyModel {

    private String url = "https://free-api.heweather.com/v5/weather?city=";
    private String key = "&key=935656148760408aa7b50b5a201891d0";

    public void getSimpleWeatherInfo(final String weatherId,
                                     final OnGetServerInfoListener listener) {
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
                    SimpleWeather simpleWeather = new Gson().fromJson(weatherContent,
                            SimpleWeather.class);
                    if (simpleWeather != null && "ok".equals(simpleWeather.status)) {
                        CountyWeather countyWeather = new CountyWeather();
                        countyWeather.setWeatherId(weatherId);
                        countyWeather.setCountyName(simpleWeather.basic.cityName);
                        countyWeather.setCurrentTemp(simpleWeather.now.temperature);
                        countyWeather.setUpdateTime(simpleWeather.basic.update.updateTime);
                        countyWeather.save();
                        listener.onSucceed();
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
}
