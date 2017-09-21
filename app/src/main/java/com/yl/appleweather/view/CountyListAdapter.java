package com.yl.appleweather.view;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yl.appleweather.R;
import com.yl.appleweather.db.CountyWeather;

import java.util.List;

/**
 * 本地County列表的适配器
 * Created by Luke on 2017/9/19.
 */

public class CountyListAdapter extends ArrayAdapter<CountyWeather> {

    private int resourceId;

    public CountyListAdapter(@NonNull Context context,
                             @LayoutRes int resource, List<CountyWeather> weathers) {
        super(context, resource, weathers);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView,
                        @NonNull ViewGroup parent) {
        CountyWeather weather = getItem(position);
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext())
                    .inflate(resourceId, parent, false);
            holder = new ViewHolder();
            holder.updateTime = (TextView) view.findViewById(R.id.update_time);
            holder.countyName = (TextView) view.findViewById(R.id.county_name);
            holder.currentTemp = (TextView) view.findViewById(R.id.current_temperature);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.countyName.setText(weather.getCountyName());
        holder.updateTime.setText(weather.getUpdateTime());
        holder.currentTemp.setText(weather.getCurrentTemp() + "℃");
        return view;
    }

    class ViewHolder {
        TextView updateTime;
        TextView countyName;
        TextView currentTemp;
    }
}
