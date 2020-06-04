package com.realmax.base;

import androidx.appcompat.app.AlertDialog;

/**
 * @author ayuan
 */
public abstract class BaseLogic {
    /**
     * 获取一个Dialog的实例
     *
     * @param baseUiRefresh 接口
     * @return 返回alertDialog
     */
    public AlertDialog getAlertDialog(BaseUiRefresh baseUiRefresh) {
        AlertDialog.Builder builder = new AlertDialog.Builder(baseUiRefresh.getActivity());
        AlertDialog alertDialog = builder.create();
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        alertDialog.setCancelable(false);
        return alertDialog;
    }
}
