package com.realmax.base;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author ayuan
 */
public interface BaseUiRefresh {
    String TAG = "BaseUiRefresh";
    String TIP = "暂未实现";

    /**
     * 获取上下文换季个
     *
     * @return 返回上下文环境
     */
    default AppCompatActivity getActivity() {
        Log.e(TAG, "getActivity: " + TIP);
        return null;
    }

    /**
     * 显示Toast
     *
     * @param message 需要显示的文字
     */
    default void showToast(String message) {
        App.showToast(message);
    }
}
