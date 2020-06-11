package com.realmax.smarttrafficmanager.activity.payment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.realmax.smarttrafficmanager.R;
import com.realmax.smarttrafficmanager.activity.control.ControlActivity;

/**
 * @author ayuan
 */
public class PaymentActivity extends AppCompatActivity implements PaymentView {
    private ImageView ivImage;
    private TextView tvNumberPlate;
    private TextView tvEntryTime;
    private TextView tvPlayingTime;
    private TextView tvCost;
    private TextView tvPaymentStatus;
    private MaterialCardView cardManualControl;
    private PaymentPresent paymentPresent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initView();
        initListener();
        initData();
    }

    private void initView() {
        ivImage = findViewById(R.id.ivImage);
        tvNumberPlate = findViewById(R.id.tvNumberPlate);
        tvEntryTime = findViewById(R.id.tvEntryTime);
        tvPlayingTime = findViewById(R.id.tvPlayingTime);
        tvCost = findViewById(R.id.tvCost);
        tvPaymentStatus = findViewById(R.id.tvPaymentStatus);
        cardManualControl = findViewById(R.id.cardManualControl);
    }

    private void initListener() {
        cardManualControl.setOnClickListener((View v) -> {
            startActivity(new Intent(this, ControlActivity.class));
            finish();
        });
    }

    private void initData() {
        paymentPresent = new PaymentPresent(this, new PaymentLogic());

        paymentPresent.init();
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    /**
     * 设置图片到控件中
     *
     * @param bitmap 图片
     */
    @Override
    public void setImage(Bitmap bitmap) {
        ivImage.setImageBitmap(bitmap);
    }

    /**
     * 设置车牌号
     *
     * @param numberPlate 车牌号
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void setNumberPlate(String numberPlate) {
        tvNumberPlate.setText("车牌号：" + numberPlate);
    }
}
