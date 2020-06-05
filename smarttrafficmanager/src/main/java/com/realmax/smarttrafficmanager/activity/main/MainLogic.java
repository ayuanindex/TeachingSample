package com.realmax.smarttrafficmanager.activity.main;

import com.google.gson.Gson;
import com.realmax.base.BaseLogic;
import com.realmax.base.BaseUiRefresh;
import com.realmax.base.utils.L;
import com.realmax.smarttrafficmanager.R;
import com.realmax.smarttrafficmanager.activity.tcp.CustomerCallback;
import com.realmax.smarttrafficmanager.activity.tcp.CustomerHandlerBase;
import com.realmax.smarttrafficmanager.activity.tcp.NettyControl;
import com.realmax.smarttrafficmanager.bean.WeatherBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author ayuan
 */
public class MainLogic extends BaseLogic {

    private WeatherBean weatherBean;

    /**
     * 获取天气
     *
     * @param mainUiRefresh 回调
     */
    public void getWeather(MainUiRefresh mainUiRefresh) {
        CustomerHandlerBase customerHandlerBase = NettyControl.getHandlerHashMap().get("Camera");
        if (customerHandlerBase != null) {
            customerHandlerBase.setCustomerCallback(new CustomerCallback() {
                @Override
                public void disConnected() {
                    L.e("连接断开");
                }

                @Override
                public void getResultData(String msg) {
                    L.e(msg);
                    retrieveData(msg, mainUiRefresh);
                }
            });
        }

        NettyControl.sendWeatherCmd();
    }

    /**
     * 获取数据并并显示
     *
     * @param msg           服务端发送的消息
     * @param mainUiRefresh 回调
     */
    private void retrieveData(String msg, MainUiRefresh mainUiRefresh) {
        try {
            JSONObject jsonObject = new JSONObject(msg);
            if ("ans".equals(jsonObject.optString("cmd"))) {
                weatherBean = new Gson().fromJson(msg, WeatherBean.class);
                mainUiRefresh.setWeather(weatherBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            String substring = msg.substring(1);
            retrieveData(substring, mainUiRefresh);
        }
    }

    /**
     * 设置天气图标
     *
     * @param weatherBean   天气数据
     * @param mainUiRefresh 互调
     * @return 返回Drawable
     */
    public int getWeatherIcon(WeatherBean weatherBean, MainUiRefresh mainUiRefresh) {
        switch (weatherBean.getWeather()) {
            case "雷雨":
                return R.drawable.pic_weather_leizhenyu;
            case "大雾":
                return R.drawable.pic_weather_dawu;
            case "晴天":
                return R.drawable.pic_weather_tianqing;
            default:
                return R.drawable.pic_weather_na;
        }
    }

    interface MainUiRefresh extends BaseUiRefresh {

        /**
         * 设置当前天气状态
         *
         * @param weatherBean 天气
         */
        void setWeather(WeatherBean weatherBean);
    }
}
