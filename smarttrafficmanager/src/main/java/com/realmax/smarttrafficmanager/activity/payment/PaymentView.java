package com.realmax.smarttrafficmanager.activity.payment;

import android.graphics.Bitmap;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author ayuan
 */
public interface PaymentView {
    /**
     * 获取上下文环境
     *
     * @return 上下文
     */
    AppCompatActivity getActivity();

    /**
     * 设置图片到空间中
     *
     * @param bitmap 图片
     */
    void setImage(Bitmap bitmap);

    /**
     * 设置车牌号
     *
     * @param numberPlate 车牌号
     */
    void setNumberPlate(String numberPlate);
}
