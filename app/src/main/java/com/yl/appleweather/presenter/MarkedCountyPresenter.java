package com.yl.appleweather.presenter;

import com.yl.appleweather.activity.MarkedCountyActivity;
import com.yl.appleweather.db.CountyWeather;
import com.yl.appleweather.model.MarkedCountyModel;
import com.yl.appleweather.model.OnGetServerInfoListener;
import com.yl.appleweather.view.MarkedCountyView;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * 显示和添加本地County列表的Presenter
 * Created by Luke on 2017/9/19.
 */

public class MarkedCountyPresenter implements OnGetServerInfoListener {

    private MarkedCountyView view;
    private MarkedCountyModel model;
    private MarkedCountyActivity activity;

    public MarkedCountyPresenter(MarkedCountyView view) {
        this.view = view;
        model = new MarkedCountyModel();
        activity = (MarkedCountyActivity) view;
    }

    public List<CountyWeather> getCountyList() {
        return DataSupport.findAll(CountyWeather.class);
    }

    public void addMarkedCounty(String weatherId) {
        view.showProgressDialog();
        model.getSimpleWeatherInfo(weatherId, this);
    }

    @Override
    public void onFailed() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.hideProgressDialog();
                view.showMarkedCounty();
                view.addCountyFailed();
            }
        });
    }

    @Override
    public void onSucceed() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.hideProgressDialog();
                view.showMarkedCounty();
            }
        });
    }
}
