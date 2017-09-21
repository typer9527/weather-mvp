package com.yl.appleweather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yl.appleweather.R;
import com.yl.appleweather.db.City;
import com.yl.appleweather.db.County;
import com.yl.appleweather.db.Province;
import com.yl.appleweather.presenter.ChooseAreaPresenter;
import com.yl.appleweather.util.ToastUtil;
import com.yl.appleweather.view.ChooseAreaView;

import java.util.ArrayList;
import java.util.List;

public class ChooseAreaActivity extends AppCompatActivity implements ChooseAreaView,
        AdapterView.OnItemClickListener, View.OnClickListener {

    public static final int PROVINCE_LEVEL = 1;
    public static final int CITY_LEVEL = 2;
    public static final int COUNTY_LEVEL = 3;
    private int mCurrentLevel;
    private ListView listView;
    private ArrayAdapter<String> mAdapter;
    private List<String> dataList = new ArrayList<>();
    private ProgressBar progressBar;
    private ChooseAreaPresenter mPresenter = new ChooseAreaPresenter(this);
    private TextView areaName;
    private Button backButton;
    private List<Province> mProvinceList;
    private Province mSelectedProvince;
    private List<City> mCityList;
    private City mSelectedCity;
    private List<County> mCountyList;
    private County mSelectedCounty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_area);
        backButton = (Button) findViewById(R.id.back_button);
        areaName = (TextView) findViewById(R.id.area_name);
        listView = (ListView) findViewById(R.id.list_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        mAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
        backButton.setOnClickListener(this);
        showProvinceList();
    }

    @Override
    public void showProgressbar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressbar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProvinceList() {
        mCurrentLevel = 1;
        dataList.clear();
        backButton.setVisibility(View.GONE);
        areaName.setText("中国");
        mProvinceList = mPresenter.getProvinceList();
        for (Province province : mProvinceList) {
            dataList.add(province.getProvinceName());
        }
        mAdapter.notifyDataSetChanged();
        listView.setSelection(0);
    }

    @Override
    public void showCityList() {
        mCurrentLevel = 2;
        dataList.clear();
        backButton.setVisibility(View.VISIBLE);
        areaName.setText(mSelectedProvince.getProvinceName());
        mCityList = mPresenter.getCityList(mSelectedProvince);
        for (City city : mCityList) {
            dataList.add(city.getCityName());
        }
        mAdapter.notifyDataSetChanged();
        listView.setSelection(0);
    }

    @Override
    public void showCountyList() {
        mCurrentLevel = 3;
        dataList.clear();
        backButton.setVisibility(View.VISIBLE);
        areaName.setText(mSelectedCity.getCityName());
        mCountyList = mPresenter.getCountyList(mSelectedProvince, mSelectedCity);
        for (County county : mCountyList) {
            dataList.add(county.getCountyName());
        }
        mAdapter.notifyDataSetChanged();
        mAdapter.notifyDataSetInvalidated();
        listView.setSelection(0);
    }

    @Override
    public void loadAreaInfoFailed() {
        ToastUtil.showToast(this, "获取区域信息失败", 0);
    }

    @Override
    public void jumpToMarkedCounty() {
        Intent intent = new Intent(this, MarkedCountyActivity.class);
        intent.putExtra("WEATHER_ID", mSelectedCounty.getWeatherId());
        setResult(RESULT_OK, intent);
        finish();
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mCurrentLevel == PROVINCE_LEVEL) {
            mSelectedProvince = mProvinceList.get(position);
            showCityList();
        } else if (mCurrentLevel == CITY_LEVEL) {
            mSelectedCity = mCityList.get(position);
            showCountyList();
        } else if (mCurrentLevel == COUNTY_LEVEL) {
            mSelectedCounty = mCountyList.get(position);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String weatherJson = prefs.getString("weather", null);
            if (weatherJson == null) {
                // 第一次打开
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("weather_id", mSelectedCounty.getWeatherId());
                setResult(RESULT_OK, intent);
                finish();
            } else {
                jumpToMarkedCounty();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                if (mCurrentLevel == CITY_LEVEL) {
                    mCurrentLevel = PROVINCE_LEVEL;
                    showProvinceList();
                } else if (mCurrentLevel == COUNTY_LEVEL) {
                    mCurrentLevel = CITY_LEVEL;
                    showCityList();
                }
                break;
            default:
                break;
        }
    }
}
