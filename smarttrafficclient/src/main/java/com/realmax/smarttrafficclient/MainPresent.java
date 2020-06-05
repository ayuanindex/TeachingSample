package com.realmax.smarttrafficclient;

import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

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
        // 从Sp中读取车牌号并显示
        showNumberPlate();
    }

    /**
     * 显示车牌号
     */
    private void showNumberPlate() {
        mainLogic.showNumberPlate(this);
    }

    /**
     * 设置车牌号
     */
    public void setNumberPlate() {
        mainLogic.showSetNumberPlateDialog(this);
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
        mainLogic.startPayment(this);
    }
}