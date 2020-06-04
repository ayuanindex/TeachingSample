package com.realmax.base.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.realmax.base.App;

/**
 * @author ayuan
 */
public class SpUtil {
    private static SharedPreferences sharedPreferences;

    /**
     * 判断sharedPreferences是否存在，如果不存在则创建返回
     *
     * @return 返回SharedPreferences对象
     */
    private static SharedPreferences isHave() {
        if (sharedPreferences == null) {
            sharedPreferences = App.getContext().getSharedPreferences("setting", Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    /**
     * 传入字符串
     *
     * @param key   字符串对应的key
     * @param value 需要设置的值
     */
    public static void putString(String key, String value) {
        isHave().edit()
                .putString(key, value)
                .apply();
    }

    /**
     * 根据key获取字符串
     *
     * @param key      字符串对应的key
     * @param defValue key对应的value没有值的话返回默认字符串
     * @return 返回获取到的自腐出啊
     */
    public static String getString(String key, String defValue) {
        return isHave().getString(key, defValue);
    }

    /**
     * 传入int值
     *
     * @param key   int值对应的key
     * @param value 需要设置的值
     */
    public static void putInt(String key, int value) {
        isHave().edit()
                .putInt(key, value)
                .apply();
    }

    /**
     * 根据key获取int值
     *
     * @param key      int值对应的key
     * @param defValue key对应的value没有值的话返回默认int值
     * @return 返回获取到的自腐出啊
     */
    public static int getInt(String key, int defValue) {
        return isHave().getInt(key, defValue);
    }

    /**
     * 传入boolean值
     *
     * @param key   boolean值对应的key
     * @param value 需要设置的值
     */
    public static void putBoolean(String key, boolean value) {
        isHave().edit()
                .putBoolean(key, value)
                .apply();
    }

    /**
     * 根据key获取boolean值
     *
     * @param key      boolean值对应的key
     * @param defValue key对应的value没有值的话返回默认boolean值
     * @return 返回获取到的自腐出啊
     */
    public static boolean getBoolean(String key, boolean defValue) {
        return isHave().getBoolean(key, defValue);
    }

    /**
     * 根据key一处节点
     *
     * @param key 需要一处的节点对应的key
     */
    public static void removeNode(String key) {
        isHave().edit().remove(key).apply();
    }
}
