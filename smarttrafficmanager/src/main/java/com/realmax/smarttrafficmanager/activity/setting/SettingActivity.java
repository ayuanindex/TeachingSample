package com.realmax.smarttrafficmanager.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.realmax.smarttrafficmanager.R;

/**
 * @author ayuan
 */
public class SettingActivity extends AppCompatActivity implements SettingView {
    private EditText etIp;
    private EditText etPort;
    private MaterialCardView cardConnect;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initListener();
        initData();
    }

    private void initView() {
        etIp = findViewById(R.id.etIp);
        etPort = findViewById(R.id.etPort);
        cardConnect = findViewById(R.id.cardConnect);
    }

    private void initListener() {
        cardConnect.setOnClickListener((View v) -> {

        });
    }

    private void initData() {

    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }
}
