package com.realmax.smarttrafficclient;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.realmax.smarttrafficclient.bean.WeatherBean;

import java.text.SimpleDateFormat;
import java.util.Date;

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
        cardMyCar.setOnClickListener((View v) -> mainPresent.setNumberPlate());

        cardPayment.setOnClickListener((View v) -> mainPresent.startPayment());

        tvDescription.setOnClickListener((View v) -> {
            mainPresent.queryRecode();
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

    /**
     * @param numberPlate 车牌号
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void setNumberPlate(String numberPlate) {
        tvNumberPlate.setText("车牌号:" + numberPlate);
    }

    /**
     * @param weatherBean 数据
     * @param weatherIcon 图标
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void setWeatherToWidget(WeatherBean weatherBean, int weatherIcon) {
        tvTime.setText(weatherBean.getTime().substring(0, 5));
        ivWeatherIcon.setImageResource(weatherIcon);
        tvTemperature.setText(weatherBean.getTemp() + "ºC");
        tvWeather.setText(weatherBean.getWeather());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy,MM,dd");
        String dateStr = simpleDateFormat.format(new Date());
        tvDate.setText(dateStr);
    }

    /**
     * @param message 文字
     */
    @Override
    public void setWidget(String message) {
        tvDescription.setText(message);
    }
}
