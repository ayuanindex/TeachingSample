package com.realmax.smarttrafficmanager.activity.main;

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
}
