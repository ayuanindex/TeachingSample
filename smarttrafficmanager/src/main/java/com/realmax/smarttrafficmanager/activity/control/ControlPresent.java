package com.realmax.smarttrafficmanager.activity.control;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.realmax.base.App;

/**
 * @author ayuan
 */
public class ControlPresent implements ControlLogic.ControlUiRefresh {

    public static final String ETC = "ETC收费站";
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
        startCamera();
    }

    /**
     * 发送查看虚拟摄像头命令
     */
    private void startCamera() {
        controlLogic.startCamera(ETC, 1, 2, this);
    }

    @Override
    public AppCompatActivity getActivity() {
        return controlView.getActivity();
    }

    @Override
    public void showToast(String message) {
        switchToMainThread(new Runnable() {
            @Override
            public void run() {
                App.showToast(message);
            }
        });
    }

    @Override
    public void setImageData(Bitmap bitmap) {
        switchToMainThread(() -> controlView.setImageData(bitmap));
    }

    /**
     * 打开对应的摄像头
     *
     * @param deviceType 设备名称
     * @param deviceId   设备编号
     * @param cameraNum  摄像头编号
     */
    public void switchCamera(String deviceType, int deviceId, int cameraNum) {
        controlLogic.startCamera(deviceType, deviceId, cameraNum, this);
    }
}
