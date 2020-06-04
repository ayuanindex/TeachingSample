package com.realmax.smarttrafficclient;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity implements MainView {

    private TextView tvTime;
    private ImageView ivWeatherIcon;
    private TextView tvTemperature;
    private TextView tvDate;
    private TextView tvWeather;
    private TextView tvNumberPlate;
    private TextView tvDescription;
    private CardView cardMyCar;
    private CardView cardPayment;
    private MainLogic mainLogic;
    private MainPresent mainPresent;

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
        tvNumberPlate = findViewById(R.id.tvNumberPlate);
        tvDescription = findViewById(R.id.tvDescription);
        cardMyCar = findViewById(R.id.cardMyCar);
        cardPayment = findViewById(R.id.cardPayment);
    }

    private void initListener() {
        cardMyCar.setOnClickListener((View v) -> {
            mainPresent.setNumberPlate();
        });

        cardPayment.setOnClickListener((View v) -> {
            // TODO: 2020/6/4
        });
    }

    private void initData() {
        mainLogic = new MainLogic();
        mainPresent = new MainPresent(this, mainLogic);

        mainPresent.initData();
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setNumberPlate(String numberPlate) {
        tvNumberPlate.setText("车牌号:" + numberPlate);
    }
}
