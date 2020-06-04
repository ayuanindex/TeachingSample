package com.realmax.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.widget.Toast;

/**
 * @author ayuan
 */
public class App extends Application {


    private static App context;
    private static Toast toast;

    @SuppressLint("ShowToast")
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }

    public static App getContext() {
        return context;
    }

    /**
     * 显示Toast
     *
     * @param message 需要显示的文字
     */
    public static void showToast(String message) {
        toast.cancel();
        toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
