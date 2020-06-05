package com.realmax.smarttrafficmanager.activity.setting;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author ayuan
 */
public interface SettingView {

    /**
     * 获取上上下文环境
     *
     * @return 上下文
     */
    AppCompatActivity getActivity();

    /**
     * 根据连接状态设置界面中控件的状态
     *
     * @param isConnectHashMap ture表示已连接，false表示未连接
     */
    void setEditAndButton(boolean isConnectHashMap);

    /**
     * 设置控件的字符串
     *
     * @param ip ip地址
     */
    void setEditIp(String ip);

    /**
     * 设置控件的字符串
     *
     * @param port 端口号
     */
    void setEditPort(String port);
}
