package com.realmax.smarttrafficmanager.activity.setting;

import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author ayuan
 */
public class SettingPresent implements SettingLogic.SettingUiRefresh {

    private final SettingView settingView;
    private final SettingLogic settingLogic;
    private final Handler uiHandler;

    public SettingPresent(SettingView settingView, SettingLogic settingLogic) {
        this.settingView = settingView;
        this.settingLogic = settingLogic;
        uiHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void switchToMainThread(Runnable runnable) {
        uiHandler.post(runnable);
    }

    @Override
    public AppCompatActivity getActivity() {
        return settingView.getActivity();
    }
}
