package com.realmax.smarttrafficmanager.activity.main;

import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.realmax.smarttrafficmanager.bean.WeatherBean;

/**
 * @author ayuan
 */
public class MainPresent implements MainLogic.MainUiRefresh {

    private final MainView mainView;
    private final MainLogic mainLogic;
    private final Handler uiHandler;

    public MainPresent(MainView mainView, MainLogic mainLogic) {
        this.mainView = mainView;
        this.mainLogic = mainLogic;
        uiHandler = new Handler(Looper.getMainLooper());
    }

    public void initData() {
        getWeather();
    }

    private void getWeather() {
        mainLogic.getWeather(this);
    }

    /**
     * 获取上下文环境
     *
     * @return 返回上下文环境
     */
    @Override
    public AppCompatActivity getActivity() {
        return mainView.getActivity();
    }

    /**
     * 切换到主线程运行
     *
     * @param runnable 运行的回调
     */
    @Override
    public void switchToMainThread(Runnable runnable) {
        uiHandler.post(runnable);
    }

    /**
     * 获取天气
     */
    public void onResume() {
        getWeather();
    }

    /**
     * @param weatherBean 天气
     */
    @Override
    public void setWeather(WeatherBean weatherBean) {
        switchToMainThread(new Runnable() {
            @Override
            public void run() {
                if (mainView != null) {
                    mainView.setWeather(weatherBean, mainLogic.getWeatherIcon(weatherBean, MainPresent.this));
                }
            }
        });
    }
}
