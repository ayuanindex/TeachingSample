package com.realmax.smarttrafficmanager.activity.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.realmax.smarttrafficmanager.R;
import com.realmax.smarttrafficmanager.activity.control.ControlActivity;
import com.realmax.smarttrafficmanager.activity.count.CountActivity;
import com.realmax.smarttrafficmanager.activity.payment.PaymentActivity;
import com.realmax.smarttrafficmanager.activity.setting.SettingActivity;
import com.realmax.smarttrafficmanager.bean.WeatherBean;

import java.text.SimpleDateFormat;
import java.util.Date;

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
        MainLogic mainLogic = new MainLogic();
        mainPresent = new MainPresent(this, mainLogic);

        mainPresent.initData();
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setWeather(WeatherBean weatherBean, int weatherIcon) {
        tvTime.setText(weatherBean.getTime().substring(0, 5));
        ivWeatherIcon.setImageResource(weatherIcon);
        tvTemperature.setText(weatherBean.getTemp() + "ºC");
        tvWeather.setText(weatherBean.getWeather());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy,MM,dd");
        String dateStr = simpleDateFormat.format(new Date());
        tvDate.setText(dateStr);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mainPresent.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        mainPresent.onResume();
        super.onResume();
    }
}
