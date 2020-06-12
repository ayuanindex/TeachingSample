package com.realmax.smarttrafficclient;

import androidx.appcompat.app.AppCompatActivity;

import com.realmax.smarttrafficclient.bean.WeatherBean;

/**
 * @author ayuan
 */
public interface MainView {
    /**
     * 获取上下文换季
     *
     * @return 返回上下文环境
     */
    AppCompatActivity getActivity();

    /**
     * 设置车牌号
     *
     * @param numberPlate 车牌号
     */
    void setNumberPlate(String numberPlate);

    /**
     * 将天气信息设置到控件中
     *
     * @param weatherBean 数据
     * @param weatherIcon 图标
     */
    void setWeatherToWidget(WeatherBean weatherBean, int weatherIcon);

    /**
     * 将文字设置到空间中
     *
     * @param message 文字
     */
    void setWidget(String message);
}
