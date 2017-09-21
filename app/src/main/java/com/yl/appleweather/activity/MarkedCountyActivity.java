package com.yl.appleweather.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.yl.appleweather.R;
import com.yl.appleweather.db.CountyWeather;
import com.yl.appleweather.presenter.MarkedCountyPresenter;
import com.yl.appleweather.util.ToastUtil;
import com.yl.appleweather.view.CountyListAdapter;
import com.yl.appleweather.view.MarkedCountyView;

import java.util.ArrayList;
import java.util.List;

public class MarkedCountyActivity extends AppCompatActivity implements
        View.OnClickListener, MarkedCountyView, AdapterView.OnItemClickListener {

    private ListView listView;
    private Button addCounty;
    private ProgressDialog progressDialog;
    private List<CountyWeather> mCountyList = new ArrayList<>();
    private CountyListAdapter mAdapter;
    private MarkedCountyPresenter mPresenter = new MarkedCountyPresenter(this);
    private View foot_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marked_county);
        listView = (ListView) findViewById(R.id.marked_county_list);
        foot_view = LayoutInflater.from(this).inflate(R.layout.foot_view, null);
        addCounty = (Button) foot_view.findViewById(R.id.add_marked_county);

        mAdapter = new CountyListAdapter(this, R.layout.item_marked_county, mCountyList);
        listView.addFooterView(foot_view);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
        addCounty.setOnClickListener(this);
        showMarkedCounty();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_marked_county:
                addMarkedCounty();
                break;
            default:
                break;
        }
    }

    @Override
    public void refreshCountyList(Intent intent) {
        String weatherId = intent.getStringExtra("WEATHER_ID");
        if (weatherId != null) {
            mPresenter.addMarkedCounty(weatherId);
        } else {
            showMarkedCounty();
        }
    }

    @Override
    public void showMarkedCounty() {
        mCountyList.clear();
        List<CountyWeather> countyList = mPresenter.getCountyList();
        for (CountyWeather county : countyList) {
            mCountyList.add(county);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void addMarkedCounty() {
        startActivityForResult(new Intent(this, ChooseAreaActivity.class), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    refreshCountyList(data);
                }
                break;
        }
    }

    @Override
    public void addCountyFailed() {
        ToastUtil.showToast(this, "加载失败", 0);
    }

    @Override
    public void jumpToCountyWeather(String weatherId) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("weather_id", weatherId);
        setResult(RESULT_OK, intent);
        finish();
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String weatherId = mPresenter.getCountyList().get(position).getWeatherId();
        jumpToCountyWeather(weatherId);
    }
}
