package com.realmax.smarttrafficclient;

import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.realmax.base.BaseLogic;
import com.realmax.base.BaseUiRefresh;
import com.realmax.base.utils.SpUtil;

/**
 * @author ayuan
 */
public class MainLogic extends BaseLogic {

    public static final String NUMBER_PLATE = "numberPlate";

    /**
     * 显示设置车牌号的Dialog
     */
    public void showSetNumberPlateDialog(MainUiRefresh mainUiRefresh) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainUiRefresh.getActivity());
        AlertDialog alertDialog = builder.create();
        View inflate = View.inflate(mainUiRefresh.getActivity(), R.layout.dialog_setnumberplate, null);
        alertDialog.setView(inflate);
        NumberPlateViewHolder holder = new NumberPlateViewHolder(inflate);
        holder.cardDefine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numberPlate = holder.etNumberPlate.getText().toString().trim();
                if (TextUtils.isEmpty(numberPlate)) {
                    mainUiRefresh.showToast("请输入车牌号");
                    return;
                }

                SpUtil.putString(NUMBER_PLATE, numberPlate);
                mainUiRefresh.showToast("设置成功");
                mainUiRefresh.setNumberPlate(numberPlate);
                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public static class NumberPlateViewHolder {

        public View rootView;
        public TextInputEditText etNumberPlate;
        public MaterialCardView cardCancel;
        public MaterialCardView cardDefine;

        public NumberPlateViewHolder(View rootView) {
            this.rootView = rootView;
            this.etNumberPlate = (TextInputEditText) rootView.findViewById(R.id.etNumberPlate);
            this.cardCancel = (MaterialCardView) rootView.findViewById(R.id.cardCancel);
            this.cardDefine = (MaterialCardView) rootView.findViewById(R.id.cardDefine);
        }


    }

    /**
     * 显示车牌号
     */
    public void showNumberPlate(MainUiRefresh mainUiRefresh) {
        String numberPlate = SpUtil.getString(NUMBER_PLATE, "");
        mainUiRefresh.setNumberPlate(numberPlate);
    }

    interface MainUiRefresh extends BaseUiRefresh {

        /**
         * 设置车牌号
         *
         * @param numberPlate 车牌号
         */
        void setNumberPlate(String numberPlate);
    }
}
