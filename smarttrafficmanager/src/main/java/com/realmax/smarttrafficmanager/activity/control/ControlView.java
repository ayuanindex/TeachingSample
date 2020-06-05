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
}
