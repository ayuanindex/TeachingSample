package com.realmax.smarttrafficmanager.activity.control;

import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author ayuan
 */
public class ControlPresent implements ControlLogic.ControlUiRefresh {

    private final ControlView controlView;
    private final ControlLogic controlLogic;
    private final Handler uiHandler;

    public ControlPresent(ControlView controlView, ControlLogic controlLogic) {
        this.controlView = controlView;
        this.controlLogic = controlLogic;
        uiHandler = new Handler(Looper.getMainLooper());
    }


    @Override
    public void switchToMainThread(Runnable runnable) {
        uiHandler.post(runnable);
    }

    public void initData() {

    }

    @Override
    public AppCompatActivity getActivity() {
        return controlView.getActivity();
    }
}
