package com.realmax.smarttrafficmanager.activity.control;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.realmax.smarttrafficmanager.R;

/**
 * @author ayuan
 */
public class ControlActivity extends AppCompatActivity implements ControlView {
    private ImageView ivImage;
    private TextView tvNumberPlate;
    private RadioGroup rgSelect;
    private RadioButton rbSouthEnter;
    private RadioButton rbSouthOut;
    private RadioButton rbNorthEnter;
    private RadioButton rbNorthOut;
    private CheckBox cbEnterLine;
    private CheckBox cbOutLine;
    private Switch swControl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        initView();
        initListener();
        initData();
    }

    private void initView() {
        ivImage = findViewById(R.id.ivImage);
        tvNumberPlate = findViewById(R.id.tvNumberPlate);
        rgSelect = findViewById(R.id.rgSelect);
        rbSouthEnter = findViewById(R.id.rbSouthEnter);
        rbSouthOut = findViewById(R.id.rbSouthOut);
        rbNorthEnter = findViewById(R.id.rbNorthEnter);
        rbNorthOut = findViewById(R.id.rbNorthOut);
        cbEnterLine = findViewById(R.id.cbEnterLine);
        cbOutLine = findViewById(R.id.cbOutLine);
        swControl = findViewById(R.id.swControl);
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
