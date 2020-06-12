package com.realmax.smarttrafficmanager.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.realmax.smarttrafficmanager.R;

/**
 * @author ayuan
 */
public class SettingActivity extends AppCompatActivity implements SettingView {
    private EditText etIp;
    private EditText etPort;
    private MaterialCardView cardConnect;
    private SettingPresent settingPresent;
    private MaterialTextView tvText;

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
        tvText = findViewById(R.id.tvText);
    }

    private void initListener() {
        cardConnect.setOnClickListener((View v) -> settingPresent.connectVirtualScene(etIp.getText().toString().trim(), etPort.getText().toString().trim()));
    }

    private void initData() {
        SettingLogic settingLogic = new SettingLogic();
        settingPresent = new SettingPresent(this, settingLogic);

        settingPresent.initData();
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public void setEditAndButton(boolean isConnectHashMap) {
        etIp.setEnabled(!isConnectHashMap);
        etPort.setEnabled(!isConnectHashMap);
        tvText.setText(isConnectHashMap ? "disConnect" : "connect");
        tvText.setBackgroundColor(isConnectHashMap ? getColor(R.color.red) : getColor(R.color.colorAccent));
    }

    @Override
    public void setEditIp(String ip) {
        etIp.setText(ip);
    }

    @Override
    public void setEditPort(String port) {
        etPort.setText(port);
    }
}
