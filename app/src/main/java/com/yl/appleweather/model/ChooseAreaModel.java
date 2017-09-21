package com.yl.appleweather.model;

import android.text.TextUtils;

import com.yl.appleweather.db.City;
import com.yl.appleweather.db.County;
import com.yl.appleweather.db.Province;
import com.yl.appleweather.util.OkHttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 区域选择Model
 * Created by Luke on 2017/9/18.
 */

public class ChooseAreaModel {

    private String url = "http://guolin.tech/api/china";

    public void getProvinceInfo(final OnGetServerInfoListener listener) {
        OkHttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                listener.onFailed();
            }

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException {
                String responseText = response.body().string();
                if (!TextUtils.isEmpty(responseText)) {
                    try {
                        JSONArray allProvinces = new JSONArray(responseText);
                        for (int i = 0; i < allProvinces.length(); i++) {
                            JSONObject provinceObject = allProvinces.getJSONObject(i);
                            Province province = new Province();
                            province.setProvinceName(provinceObject.getString("name"));
                            province.setProvinceCode(provinceObject.getInt("id"));
                            province.save();
                        }
                        listener.onSucceed();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void getCityInfo(final Province province,
                            final OnGetServerInfoListener listener) {
        String address = url + "/" + province.getProvinceCode();
        OkHttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                listener.onFailed();
            }

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException {
                String responseText = response.body().string();
                if (!TextUtils.isEmpty(responseText)) {
                    try {
                        JSONArray allCities = new JSONArray(responseText);
                        for (int i = 0; i < allCities.length(); i++) {
                            JSONObject cityObject = allCities.getJSONObject(i);
                            City city = new City();
                            city.setCityName(cityObject.getString("name"));
                            city.setCityCode(cityObject.getInt("id"));
                            city.setProvinceId(province.getId());
                            city.save();
                        }
                        listener.onSucceed();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void getCountyInfo(Province province, final City city,
                              final OnGetServerInfoListener listener) {
        String address = url + "/" + province.getProvinceCode() + "/" + city.getCityCode();
        OkHttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                listener.onFailed();
            }

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException {
                String responseText = response.body().string();
                if (!TextUtils.isEmpty(responseText)) {
                    try {
                        JSONArray allCounties = new JSONArray(responseText);
                        for (int i = 0; i < allCounties.length(); i++) {
                            JSONObject countyObject = allCounties.getJSONObject(i);
                            County county = new County();
                            county.setCountyName(countyObject.getString("name"));
                            county.setWeatherId(countyObject.getString("weather_id"));
                            county.setCityId(city.getId());
                            county.save();
                        }
                        listener.onSucceed();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
