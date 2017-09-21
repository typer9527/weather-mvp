package com.yl.appleweather.presenter;

import com.yl.appleweather.activity.ChooseAreaActivity;
import com.yl.appleweather.db.City;
import com.yl.appleweather.db.County;
import com.yl.appleweather.db.Province;
import com.yl.appleweather.model.ChooseAreaModel;
import com.yl.appleweather.model.OnGetServerInfoListener;
import com.yl.appleweather.view.ChooseAreaView;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * 区域选择Presenter
 * Created by Luke on 2017/9/18.
 */

public class ChooseAreaPresenter {
    private ChooseAreaView view;
    private ChooseAreaModel model;
    private ChooseAreaActivity activity;

    public ChooseAreaPresenter(ChooseAreaView view) {
        this.view = view;
        model = new ChooseAreaModel();
        activity = (ChooseAreaActivity) view;
    }

    public List<Province> getProvinceList() {
        List<Province> provinceList = DataSupport.findAll(Province.class);
        if (provinceList.size() > 0) {
            return provinceList;
        } else {
            view.showProgressbar();
            model.getProvinceInfo(new OnGetServerInfoListener() {
                @Override
                public void onFailed() {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.hideProgressbar();
                            view.loadAreaInfoFailed();
                        }
                    });
                }

                @Override
                public void onSucceed() {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.hideProgressbar();
                            view.showProvinceList();
                        }
                    });
                }
            });
            return provinceList;
        }
    }

    public List<City> getCityList(final Province province) {
        List<City> cityList = DataSupport.where("provinceid = ?",
                String.valueOf(province.getId())).find(City.class);
        if (cityList.size() > 0) {
            return cityList;
        } else {
            view.showProgressbar();
            model.getCityInfo(province, new OnGetServerInfoListener() {
                @Override
                public void onFailed() {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.hideProgressbar();
                            view.loadAreaInfoFailed();
                        }
                    });
                }

                @Override
                public void onSucceed() {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.hideProgressbar();
                            view.showCityList();
                        }
                    });
                }
            });
            return cityList;
        }
    }

    public List<County> getCountyList(final Province province, final City city) {
        List<County> countyList = DataSupport.where("cityid = ?",
                String.valueOf(city.getId())).find(County.class);
        if (countyList.size() > 0) {
            return countyList;
        } else {
            view.showProgressbar();
            model.getCountyInfo(province, city, new OnGetServerInfoListener() {
                @Override
                public void onFailed() {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.hideProgressbar();
                            view.loadAreaInfoFailed();
                        }
                    });
                }

                @Override
                public void onSucceed() {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.hideProgressbar();
                            view.showCountyList();
                        }
                    });
                }
            });
            return countyList;
        }
    }
}
