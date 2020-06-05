package com.realmax.smarttrafficmanager.activity.payment;

import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

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
}
