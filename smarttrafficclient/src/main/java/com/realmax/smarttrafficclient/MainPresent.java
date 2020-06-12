package com.realmax.smarttrafficclient;

import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.realmax.base.App;
import com.realmax.smarttrafficclient.bean.WeatherBean;

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
        mainLogic.setMainUiRefresh(this);
        uiHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 切换到主线程刷新UI
     *
     * @param runnable 回调
     */
    @Override
    public void switchToMainThread(Runnable runnable) {
        uiHandler.post(runnable);
    }

    /**
     * 初始化
     */
    public void initData() {
        // 连接大虚拟场景获取天气信息
        connectVirtualScene();

        // 从Sp中读取车牌号并显示
        showNumberPlate();
    }

    /**
     * 连接虚拟场景
     */
    private void connectVirtualScene() {
        mainLogic.connectVirtualScene();
    }

    /**
     * 显示车牌号
     */
    private void showNumberPlate() {
        mainLogic.showNumberPlate();
    }

    /**
     * 设置车牌号
     */
    public void setNumberPlate() {
        mainLogic.showSetNumberPlateDialog();
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

    @Override
    public void setNumberPlate(String numberPlate) {
        mainView.setNumberPlate(numberPlate);
    }

    /**
     * 开始进行缴费
     */
    public void startPayment() {
        mainLogic.startPayment();
    }

    /**
     * @param message 需要显示的文字
     */
    @Override
    public void showToast(String message) {
        switchToMainThread(() -> App.showToast(message));
    }

    /**
     * @param weatherBean 天气信息
     */
    @Override
    public void setWeatherToWidget(WeatherBean weatherBean) {
        switchToMainThread(() -> mainView.setWeatherToWidget(weatherBean, mainLogic.getWeatherIcon(weatherBean)));
    }

    /**
     * @param message 分析结果
     */
    @Override
    public void setWidget(String message) {
        switchToMainThread(() -> mainView.setWidget(message));
    }
}