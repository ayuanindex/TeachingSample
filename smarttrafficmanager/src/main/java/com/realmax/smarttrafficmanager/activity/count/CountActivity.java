package com.realmax.smarttrafficmanager.activity.count;

import android.os.Bundle;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.realmax.smarttrafficmanager.R;

/**
 * @author ayuan
 */
public class CountActivity extends AppCompatActivity implements CountView {
    private GridView gvParkingSpace;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);
        initView();
        initData();
    }

    private void initView() {
        gvParkingSpace = (GridView) findViewById(R.id.gvParkingSpace);
    }

    private void initData() {

    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }
}
