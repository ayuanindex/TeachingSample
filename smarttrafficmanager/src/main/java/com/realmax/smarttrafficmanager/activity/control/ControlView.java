package com.realmax.smarttrafficmanager.activity.control;

import android.graphics.Bitmap;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author ayuan
 */
public interface ControlView {
    /**
     * 获取上下文环境
     *
     * @return 返回上下文环境
     */
    AppCompatActivity getActivity();

    /**
     * 设置图片
     *
     * @param bitmap 图片
     */
    void setImageData(Bitmap bitmap);

    /**
     * 设置道闸状态
     *
     * @param signalValue 状态
     */
    void setBarrierStatus(int signalValue);

    /**
     * 设置感应线的状态
     *
     * @param entryStatus 入口入车状态
     * @param outStatus   出口出车状态
     */
    void setLineWidgetStatus(int entryStatus, int outStatus);

    void setNumberPlate(String numberPlate);
}
