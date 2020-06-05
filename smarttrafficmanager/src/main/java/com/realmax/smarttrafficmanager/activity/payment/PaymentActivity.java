package com.realmax.smarttrafficmanager.activity.payment;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.realmax.smarttrafficmanager.R;

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

    }

    private void initData() {

    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }
}
