package com.realmax.smarttrafficmanager.activity.count;

import androidx.appcompat.app.AppCompatActivity;

import com.realmax.smarttrafficmanager.bean.ParkingBean;

import java.util.ArrayList;

/**
 * @author ayuan
 */
public interface CountView {
    /**
     * 获取上下文环境
     *
     * @return 上下文环境
     */
    AppCompatActivity getActivity();

    /**
     * 设置列表数据
     *
     * @param parkingBeans 停车位集合
     */
    void setListData(ArrayList<ParkingBean> parkingBeans);
}
