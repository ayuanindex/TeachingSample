package com.realmax.smarttrafficmanager.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.realmax.smarttrafficmanager.R;
import com.realmax.smarttrafficmanager.activity.control.ControlActivity;
import com.realmax.smarttrafficmanager.activity.count.CountActivity;
import com.realmax.smarttrafficmanager.activity.payment.PaymentActivity;
import com.realmax.smarttrafficmanager.activity.setting.SettingActivity;

/**
 * @author ayuan
 */
public class MainActivity extends AppCompatActivity implements MainView {

    private TextView tvTime;
    private ImageView ivWeatherIcon;
    private TextView tvTemperature;
    private TextView tvDate;
    private TextView tvWeather;
    private ImageView ivControl;
    private ImageView ivCount;
    private ImageView ivTraffic;
    private ImageView ivPayment;
    private MaterialCardView cardCommunicationSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        initData();
    }

    private void initView() {
        tvTime = findViewById(R.id.tvTime);
        ivWeatherIcon = findViewById(R.id.ivWeatherIcon);
        tvTemperature = findViewById(R.id.tvTemperature);
        tvDate = findViewById(R.id.tvDate);
        tvWeather = findViewById(R.id.tvWeather);
        ivControl = findViewById(R.id.ivControl);
        ivCount = findViewById(R.id.ivCount);
        ivTraffic = findViewById(R.id.ivTraffic);
        ivPayment = findViewById(R.id.ivPayment);
        cardCommunicationSettings = findViewById(R.id.cardCommunicationSettings);
    }

    private void initListener() {
        ivControl.setOnClickListener((View v) -> {
            Intent intent = new Intent(this, ControlActivity.class);
            startActivity(intent);
        });

        ivCount.setOnClickListener((View v) -> {
            Intent intent = new Intent(this, CountActivity.class);
            startActivity(intent);
        });

        ivTraffic.setOnClickListener((View v) -> {

        });

        ivPayment.setOnClickListener((View v) -> {
            Intent intent = new Intent(this, PaymentActivity.class);
            startActivity(intent);
        });

        cardCommunicationSettings.setOnClickListener((View v) -> {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        });
    }

    private void initData() {

    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }
}
