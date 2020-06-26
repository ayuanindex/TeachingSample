package com.realmax.smarttrafficmanager.activity.count;

import android.os.Bundle;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.realmax.smarttrafficmanager.R;
import com.realmax.smarttrafficmanager.bean.ParkingBean;

import java.util.ArrayList;

/**
 * @author ayuan
 */
public class CountActivity extends AppCompatActivity implements CountView {
    private GridView gvParkingSpace;
    private CountPresent countPresent;
    private ParkingAdapter parkingAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);
        initView();
        initData();
    }

    private void initView() {
        gvParkingSpace = findViewById(R.id.gvParkingSpace);
    }

    private void initData() {
        countPresent = new CountPresent(this, new CountLogic());
        countPresent.initData();
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public void setListData(ArrayList<ParkingBean> parkingBeans) {
        parkingAdapter = new ParkingAdapter(parkingBeans, countPresent);
        gvParkingSpace.setAdapter(parkingAdapter);
    }

    @Override
    protected void onDestroy() {
        countPresent.onDestroy();
        super.onDestroy();
    }
}
