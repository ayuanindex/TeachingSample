package com.realmax.smarttrafficmanager.activity.count;

import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.realmax.smarttrafficmanager.bean.ParkingBean;

import java.util.ArrayList;

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
        countLogic.setCountUiRefresh(this);
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

    public void initData() {
        getParking();
    }

    /**
     * 获取停车位
     */
    private void getParking() {
        countLogic.getParking();
    }

    @Override
    public void setListData(ArrayList<ParkingBean> parkingBeans) {
        switchToMainThread(() -> countView.setListData(parkingBeans));
    }

    public void onDestroy() {
        countLogic.onDestroy();

    }
}
