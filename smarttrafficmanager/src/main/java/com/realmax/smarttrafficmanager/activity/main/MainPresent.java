package com.realmax.smarttrafficmanager.activity.main;

import android.Manifest;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.realmax.base.utils.L;
import com.realmax.smarttrafficmanager.bean.WeatherBean;

import org.apache.commons.net.nntp.NewGroupsOrNewsQuery;

import java.time.OffsetDateTime;
import java.util.ArrayList;

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
        requestPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE});
    }

    private void requestPermission(String[] permissions) {
        mainView.getActivity().requestPermissions(permissions, 1000);
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
        switchToMainThread(() -> {
            if (mainView != null) {
                mainView.setWeather(weatherBean, mainLogic.getWeatherIcon(weatherBean, MainPresent.this));
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            ArrayList<String> permissionList = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] < 0) {
                    permissionList.add(permissions[i]);
                }
            }
            if (permissionList.size() > 0) {
                String[] strings = permissionList.toArray(new String[]{});
                requestPermission(strings);
            }
        }
    }
}
