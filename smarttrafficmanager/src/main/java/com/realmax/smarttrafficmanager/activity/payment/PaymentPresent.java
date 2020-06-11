package com.realmax.smarttrafficmanager.activity.payment;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.realmax.base.App;

/**
 * @author ayuan
 */
public class PaymentPresent implements PaymentLogic.PaymentUiRefresh {

    private final PaymentView paymentView;
    private final PaymentLogic paymentLogic;
    private final Handler uiHandler;

    public PaymentPresent(PaymentView paymentView, PaymentLogic paymentLogic) {
        this.paymentView = paymentView;
        this.paymentLogic = paymentLogic;
        uiHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void switchToMainThread(Runnable runnable) {
        uiHandler.post(runnable);
    }

    @Override
    public AppCompatActivity getActivity() {
        return paymentView.getActivity();
    }

    public void init() {
        // 获取摄像头数据
        startCamera();
        // 获取感应线的状态
        paymentLogic.getEntranceStatus(this);
    }

    /**
     * 开启摄像头
     */
    private void startCamera() {
        paymentLogic.startCamera(this);
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

    /**
     * 设置图片
     *
     * @param bitmap 图片
     */
    @Override
    public void setImage(Bitmap bitmap) {
        switchToMainThread(() -> paymentView.setImage(bitmap));
    }

    /**
     * 设置车牌号
     *
     * @param numberPlate 车牌号
     */
    @Override
    public void setNumberPlate(String numberPlate) {
        switchToMainThread(() -> {
            paymentView.setNumberPlate(numberPlate);
        });
    }
}
