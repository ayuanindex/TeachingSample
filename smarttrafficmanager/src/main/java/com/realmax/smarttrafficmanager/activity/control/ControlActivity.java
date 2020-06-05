package com.realmax.smarttrafficmanager.activity.control;

import android.graphics.Bitmap;
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
    private ControlPresent controlPresent;
    private ControlLogic controlLogic;

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
        rgSelect.setOnCheckedChangeListener((RadioGroup group, int checkedId) -> {
            switch (checkedId) {
                case R.id.rbSouthEnter:
                    // 南进
                    controlPresent.switchCamera("ETC收费站", 1, 2);
                    break;
                case R.id.rbSouthOut:
                    controlPresent.switchCamera("ETC收费站", 1, 1);
                    // 南出
                    break;
                case R.id.rbNorthEnter:
                    controlPresent.switchCamera("ETC收费站", 2, 1);
                    // 北进
                    break;
                case R.id.rbNorthOut:
                    controlPresent.switchCamera("ETC收费站", 2, 2);
                    // 北出
                    break;
                default:
            }
        });
    }

    private void initData() {
        controlLogic = new ControlLogic();
        controlPresent = new ControlPresent(this, controlLogic);

        controlPresent.initData();
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public void setImageData(Bitmap bitmap) {
        ivImage.setImageBitmap(bitmap);
    }
}
