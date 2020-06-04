package com.realmax.smarttrafficclient;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author ayuan
 */
public interface MainView {
    /**
     * 获取上下文换季
     *
     * @return 返回上下文环境
     */
    AppCompatActivity getActivity();

    /**
     * 设置车牌号
     *
     * @param numberPlate 车牌号
     */
    void setNumberPlate(String numberPlate);
}
