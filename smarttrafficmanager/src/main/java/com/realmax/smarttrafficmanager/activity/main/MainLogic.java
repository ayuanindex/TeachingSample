package com.realmax.smarttrafficmanager.activity.main;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.realmax.base.BaseLogic;
import com.realmax.base.BaseUiRefresh;
import com.realmax.base.tcp.CustomerCallback;
import com.realmax.base.tcp.CustomerHandlerBase;
import com.realmax.base.tcp.NettyControl;
import com.realmax.base.utils.L;
import com.realmax.smarttrafficmanager.R;
import com.realmax.smarttrafficmanager.bean.WeatherBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author ayuan
 */
public class MainLogic extends BaseLogic {
    private MainUiRefresh mainUiRefresh;
    private boolean flag = false;

    /**
     * 获取天气
     */
    public void getWeather() {
        CustomerHandlerBase customerHandlerBase = NettyControl.getHandlerHashMap().get("Camera");
        if (customerHandlerBase != null) {
            customerHandlerBase.setCustomerCallback(new CustomerCallback() {
                @Override
                public void disConnected() {
                    L.e("连接断开");
                }

                @Override
                public void getResultData(String msg) {
                    if (flag) {
                        L.e(msg);
                        retrieveData(msg);
                    }
                }
            });
        }

        flag = true;
        NettyControl.sendWeatherCmd();
    }

    /**
     * 获取数据并并显示
     *
     * @param msg 服务端发送的消息
     */
    private void retrieveData(String msg) {
        try {
            JSONObject jsonObject = new JSONObject(msg);
            if ("ans".equals(jsonObject.optString("cmd"))) {
                WeatherBean weatherBean = new Gson().fromJson(msg, WeatherBean.class);
                mainUiRefresh.setWeather(weatherBean);
                flag = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            String substring = msg.substring(1);
            retrieveData(substring);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置天气图标
     *
     * @param weatherBean 天气数据
     * @return 返回Drawable
     */
    public int getWeatherIcon(WeatherBean weatherBean) {
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

    public void setMainUiRefresh(MainUiRefresh mainUiRefresh) {
        this.mainUiRefresh = mainUiRefresh;
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
