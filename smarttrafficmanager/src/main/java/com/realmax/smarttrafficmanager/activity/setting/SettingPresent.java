package com.realmax.smarttrafficmanager.activity.setting;

import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.realmax.base.App;
import com.realmax.base.utils.SpUtil;

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

    /**
     * 初始化
     */
    public void initData() {
        // 回显保存的IP地址和端口号
        setEditText();

        // 获取当前连接状态
        settingLogic.getCurrentConnectStatus(this);
    }

    /**
     * 回显已经设置的IP和端口号
     */
    private void setEditText() {
        settingView.setEditIp(SpUtil.getString("ip", "192.168.50.247"));
        settingView.setEditPort(SpUtil.getString("port", "8527"));
    }

    @Override
    public void setEditAndButton(boolean isConnectHashMap) {
        switchToMainThread(() -> {
            if (settingView != null) {
                settingView.setEditAndButton(isConnectHashMap);
            }
        });
    }

    /**
     * 连接虚拟场景
     *
     * @param ip   需要连接的IP地址
     * @param port 需要设置的端口号
     */
    public void connectVirtualScene(String ip, String port) {
        settingLogic.connectVirtualScene(ip, port, this);
    }

    @Override
    public void showToast(String message) {
        switchToMainThread(() -> App.showToast(message));
    }
}
