package com.realmax.smarttrafficmanager.activity.count;

import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author ayuan
 */
public class CountPresent implements CountLogic.CountUiRefresh {

    private final CountView countView;
    private final CountLogic countLogic;
    private final Handler uiHandler;

    public CountPresent(CountView countView, CountLogic countLogic) {
        this.countView = countView;
        this.countLogic = countLogic;
        uiHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void switchToMainThread(Runnable runnable) {
        uiHandler.post(runnable);
    }

    @Override
    public AppCompatActivity getActivity() {
        return countView.getActivity();
    }
}
