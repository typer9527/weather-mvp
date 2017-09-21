package com.yl.appleweather.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.yl.appleweather.R;
import com.yl.appleweather.gson.DailyForecast;
import com.yl.appleweather.gson.HourlyForecast;
import com.yl.appleweather.gson.Weather;
import com.yl.appleweather.presenter.MainPresenter;
import com.yl.appleweather.util.ToastUtil;
import com.yl.appleweather.view.MainView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements MainView,
        View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout refreshLayout;
    private ImageView bgImage;
    private TextView cityName;
    private TextView weatherText;
    private TextView temperature;
    private TextView dayName;
    private TextView maxTemp;
    private TextView minTemp;
    private LinearLayout hourlyForecast;
    private LinearLayout dailyForecast;
    private TextView airText, aqiText, pm25Text;
    private TextView rainText, pressText, bodyTempText,
            windText, visibilityText, wetText;
    private Button showMarkedCounty;
    private ProgressDialog progressDialog;
    private MainPresenter mPresenter = new MainPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bgImage = (ImageView) findViewById(R.id.bg_image);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_weather);
        cityName = (TextView) findViewById(R.id.city_name);
        weatherText = (TextView) findViewById(R.id.weather_text);
        temperature = (TextView) findViewById(R.id.temperature);
        dayName = (TextView) findViewById(R.id.day_name);
        maxTemp = (TextView) findViewById(R.id.max_temperature);
        minTemp = (TextView) findViewById(R.id.min_temperature);
        hourlyForecast = (LinearLayout) findViewById(R.id.hourly_forecast_view);
        dailyForecast = (LinearLayout) findViewById(R.id.daily_forecast_view);
        airText = (TextView) findViewById(R.id.air_text);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        rainText = (TextView) findViewById(R.id.rain_text);
        pressText = (TextView) findViewById(R.id.press_text);
        bodyTempText = (TextView) findViewById(R.id.body_temp_text);
        windText = (TextView) findViewById(R.id.wind_text);
        visibilityText = (TextView) findViewById(R.id.visibility_text);
        wetText = (TextView) findViewById(R.id.wet_text);
        showMarkedCounty = (Button) findViewById(R.id.show_marked_county);

        refreshLayout.setOnRefreshListener(this);
        showMarkedCounty.setOnClickListener(this);

        init();
    }

    public void init() {
        if (Build.VERSION.SDK_INT > 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        loadBackgroundImg();
        String weatherJson = mPresenter.getWeatherPrefs();
        if (weatherJson == null) {
            chooseArea();
        } else {
            showWeatherInfo();
            refreshLayout.setRefreshing(true);
            mPresenter.refreshWeatherInfo(mPresenter.getCurrentWeatherId());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String weatherId = data.getStringExtra("weather_id");
                    String currentWeatherId = mPresenter.getCurrentWeatherId();
                    if (!weatherId.equals(currentWeatherId)) {
                        requestWeather(weatherId);
                    }
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    String weatherId = data.getStringExtra("weather_id");
                    requestWeather(weatherId);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void chooseArea() {
        startActivityForResult(new Intent(MainActivity.this,
                ChooseAreaActivity.class), 2);
    }

    @Override
    public void showCountyList() {
        startActivityForResult(new Intent(MainActivity.this,
                MarkedCountyActivity.class), 1);
    }

    @Override
    public void showWeatherInfo() {
        String weatherJson = mPresenter.getWeatherPrefs();
        Weather weather = new Gson().fromJson(weatherJson, Weather.class);

        cityName.setText(weather.basic.cityName);
        weatherText.setText(weather.now.more.info);
        temperature.setText(weather.now.temperature + "℃");

        Date date = new Date(System.currentTimeMillis());
        dayName.setText(new SimpleDateFormat("EEEE").format(date) + " 今天");
        maxTemp.setText(weather.dailyForecastList.get(0).temperature.max);
        minTemp.setText(weather.dailyForecastList.get(0).temperature.min);

        // 时段预报
        hourlyForecast.removeAllViews();
        for (HourlyForecast forecast : weather.hourlyForecastList) {
            View view = LayoutInflater.from(this)
                    .inflate(R.layout.item_hourly_forecast, hourlyForecast, false);
            TextView dateText = (TextView) view.findViewById(R.id.time_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView tempText = (TextView) view.findViewById(R.id.temp_text);
            String time = forecast.date.substring(forecast.date.indexOf(" ") + 1);
            dateText.setText(time);
            infoText.setText(forecast.more.info);
            tempText.setText(forecast.temperature + "℃");
            hourlyForecast.addView(view);
        }

        if (weather.aqi == null) {
            airText.setText("无");
            aqiText.setText("无");
            pm25Text.setText("无");
        } else {
            airText.setText(weather.aqi.city.airQuality);
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }

        // 三天预报
        dailyForecast.removeAllViews();
        for (DailyForecast forecast : weather.dailyForecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_daily_forecast,
                    dailyForecast, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            dailyForecast.addView(view);
        }

        rainText.setText(weather.now.pcpn + " mm");
        pressText.setText(weather.now.pres);
        bodyTempText.setText(weather.now.fl + "℃");
        windText.setText(weather.now.wind.dir + ", "
                + weather.now.wind.spd + " km/h");
        visibilityText.setText(weather.now.vis + " km");
        wetText.setText(weather.now.hum + " %");
    }

    @Override
    public void refreshWeatherInfo() {
        mPresenter.refreshWeatherInfo(mPresenter.getCurrentWeatherId());
    }

    @Override
    public void onRefreshFailed() {
        refreshLayout.setRefreshing(false);
        ToastUtil.showToast(this, "刷新失败", 0);
    }

    @Override
    public void onRefreshSucceed() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void loadBackgroundImg() {
        String imageUrl = mPresenter.getImagePrefs();
        if (imageUrl == null) {
            mPresenter.loadImage();
        } else {
            Glide.with(this).load(imageUrl).centerCrop().into(bgImage);
        }
    }

    @Override
    public void requestWeather(String weatherId) {
        mPresenter.getWeatherInfo(weatherId);
    }

    @Override
    public void onFailed() {
        ToastUtil.showToast(this, "加载失败", 0);
    }

    @Override
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDestroyView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_marked_county:
                showCountyList();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        refreshWeatherInfo();
    }
}
