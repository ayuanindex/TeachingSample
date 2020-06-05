package com.realmax.smarttrafficclient;

import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.ContentLoadingProgressBar;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
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
        AlertDialog alertDialog = getAlertDialog(mainUiRefresh);
        View inflate = View.inflate(mainUiRefresh.getActivity(), R.layout.dialog_setnumberplate, null);
        alertDialog.setView(inflate);

        NumberPlateViewHolder holder = new NumberPlateViewHolder(inflate);
        holder.cardDefine.setOnClickListener((View v) -> {
            String numberPlate = holder.etNumberPlate.getText().toString().trim();
            if (TextUtils.isEmpty(numberPlate)) {
                mainUiRefresh.showToast("请输入车牌号");
                return;
            }

            SpUtil.putString(NUMBER_PLATE, numberPlate);
            mainUiRefresh.showToast("设置成功");
            mainUiRefresh.setNumberPlate(numberPlate);
            alertDialog.dismiss();
        });

        holder.cardCancel.setOnClickListener(v -> alertDialog.dismiss());
        alertDialog.show();
    }

    public void startPayment(MainUiRefresh mainUiRefresh) {
        AlertDialog alertDialog = getAlertDialog(mainUiRefresh);
        View inflate = View.inflate(mainUiRefresh.getActivity(), R.layout.dialog_paymen, null);
        alertDialog.setView(inflate);

        PaymentViewHolder holder = new PaymentViewHolder(inflate);

        // TODO: 2020/6/4 待完成

        // 模拟缴费过程
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            holder.pbPayProgress.setVisibility(View.GONE);
            holder.ivStatus.setVisibility(View.VISIBLE);
            holder.tvPayState.setText("缴费成功");
        }, 1000);

        handler.postDelayed(alertDialog::dismiss, 1800);
        alertDialog.show();
    }

    /**
     * 显示车牌号
     */
    public void showNumberPlate(MainUiRefresh mainUiRefresh) {
        String numberPlate = SpUtil.getString(NUMBER_PLATE, "暂未设置车牌号");
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

    public static class PaymentViewHolder {
        public View rootView;
        public ContentLoadingProgressBar pbPayProgress;
        public ImageView ivStatus;
        public MaterialTextView tvPayState;

        public PaymentViewHolder(View rootView) {
            this.rootView = rootView;
            this.pbPayProgress = (ContentLoadingProgressBar) rootView.findViewById(R.id.pbPayProgress);
            this.ivStatus = (ImageView) rootView.findViewById(R.id.ivStatus);
            this.tvPayState = (MaterialTextView) rootView.findViewById(R.id.tvPayState);
        }

    }
}
