package com.realmax.smarttrafficmanager.activity.control;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.realmax.base.App;
import com.realmax.smarttrafficmanager.bean.InductionLineBean;

import java.util.ArrayList;

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
        getEntranceStatus();
    }

    /**
     * 开启出入口的循环检测
     */
    private void getEntranceStatus() {
        controlLogic.getEntranceStatus(this);
    }

    /**
     * 发送查看虚拟摄像头命令
     */
    private void startCamera() {
        controlLogic.startCamera(ETC, 2, 1, this);
    }

    @Override
    public AppCompatActivity getActivity() {
        return controlView.getActivity();
    }

    @Override
    public void showToast(String message) {
        switchToMainThread(() -> App.showToast(message));
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

    /**
     * 查看道闸状态
     *
     * @param barrierId 道闸在数据库中的ID
     */
    public void getBarrierStatus(int barrierId) {
        controlLogic.getBarrierStatus(barrierId);
    }

    /**
     * 设置道闸状态
     *
     * @param signalValue 0表示关，1表示开
     */
    @Override
    public void setBarrierStatus(int signalValue) {
        switchToMainThread(() -> controlView.setBarrierStatus(signalValue));
    }

    /**
     * 更新道闸状态
     *
     * @param barrierId 道闸ID
     * @param isChecked true表示打开，false表示关闭
     */
    public void updateBarrier(int barrierId, boolean isChecked) {
        controlLogic.updateBarrier(barrierId, isChecked);
    }

    /**
     * 获取感应线的状态
     *
     * @param entryId 入口入车
     * @param outId   入口出车
     */
    public void getInductionLine(int entryId, int outId) {
        controlLogic.getInductionLine(entryId, outId);
    }

    /**
     * 设置感应线状态
     *
     * @param inductionLineBeans 感应线集合
     */
    @Override
    public void setLineWidgetStatus(ArrayList<InductionLineBean> inductionLineBeans) {
        int entryStatus = Integer.parseInt(inductionLineBeans.get(0).getSignalValue());
        int outStatus = Integer.parseInt(inductionLineBeans.get(1).getSignalValue());
        switchToMainThread(() -> controlView.setLineWidgetStatus(entryStatus, outStatus));
    }

    /**
     * 设置车牌号
     *
     * @param numberPlate 识别出的车牌号
     */
    @Override
    public void setNumberPlate(String numberPlate) {
        switchToMainThread(new Runnable() {
            @Override
            public void run() {
                controlView.setNumberPlate(numberPlate);
            }
        });
    }
}
