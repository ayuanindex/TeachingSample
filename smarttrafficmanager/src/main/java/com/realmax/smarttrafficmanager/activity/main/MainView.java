package com.realmax.smarttrafficmanager.activity.main;

import androidx.appcompat.app.AppCompatActivity;

import com.realmax.smarttrafficmanager.bean.WeatherBean;

/**
 * @author ayuan
 */
public interface MainView {
    /**
     * 获取上下文环境
     *
     * @return 返回上下文环境
     */
    AppCompatActivity getActivity();

    /**
     * 设置天气
     *
     * @param weatherBean 天气
     * @param weatherIcon 天气图标
     */
    void setWeather(WeatherBean weatherBean, int weatherIcon);
}
