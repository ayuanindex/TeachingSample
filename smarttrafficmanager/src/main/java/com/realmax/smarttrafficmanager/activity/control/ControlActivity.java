package com.realmax.smarttrafficmanager.activity.control;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
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
    private CheckBox cbEnterLine;
    private CheckBox cbOutLine;
    private Switch swControl;
    private ControlPresent controlPresent;
    private final int SOUTH_ENTRY = 25;
    private final int SOUTH_OUT = 26;
    private final int NORTH_ENTRY = 27;
    private final int NORTH_OUT = 28;
    private int barrierId = SOUTH_ENTRY;
    private int entryId = 17;
    private int outId = 18;

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
        // RadioButton rbSouthEnter = findViewById(R.id.rbSouthEnter);
        // RadioButton rbSouthOut = findViewById(R.id.rbSouthOut);
        // RadioButton rbNorthEnter = findViewById(R.id.rbNorthEnter);
        // RadioButton rbNorthOut = findViewById(R.id.rbNorthOut);
        cbEnterLine = findViewById(R.id.cbEnterLine);
        cbOutLine = findViewById(R.id.cbOutLine);
        swControl = findViewById(R.id.swControl);
    }

    private void initListener() {
        rgSelect.setOnCheckedChangeListener((RadioGroup group, int checkedId) -> {
            switch (checkedId) {
                case R.id.rbSouthEnter:
                    // 南进
                    controlPresent.switchCamera("ETC收费站", 2, 1);
                    barrierId = SOUTH_ENTRY;
                    entryId = 17;
                    outId = 18;
                    break;
                case R.id.rbSouthOut:
                    // 南出
                    controlPresent.switchCamera("ETC收费站", 2, 2);
                    barrierId = SOUTH_OUT;
                    entryId = 19;
                    outId = 20;
                    break;
                case R.id.rbNorthEnter:
                    // 北进
                    controlPresent.switchCamera("ETC收费站", 1, 2);
                    barrierId = NORTH_ENTRY;
                    entryId = 21;
                    outId = 22;
                    break;
                case R.id.rbNorthOut:
                    // 北出
                    controlPresent.switchCamera("ETC收费站", 1, 1);
                    barrierId = NORTH_OUT;
                    entryId = 23;
                    outId = 24;
                    break;
                default:
            }
            controlPresent.getBarrierStatus(barrierId);
            controlPresent.getInductionLine(entryId, outId);
        });

        swControl.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> controlPresent.updateBarrier(barrierId, isChecked));
    }

    private void initData() {
        ControlLogic controlLogic = new ControlLogic();
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

    @Override
    public void setBarrierStatus(int signalValue) {
        swControl.setChecked(signalValue == 1);
    }

    @Override
    public void setLineWidgetStatus(int entryStatus, int outStatus) {
        cbEnterLine.setChecked(entryStatus == 1);
        cbOutLine.setChecked(outStatus == 1);
    }

    @Override
    public void setNumberPlate(String numberPlate) {
        tvNumberPlate.setText(numberPlate);
    }

    @Override
    protected void onDestroy() {
        controlPresent.onDestroy();
        super.onDestroy();
    }
}
