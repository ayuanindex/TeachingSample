package com.realmax.smarttrafficmanager.activity.control;

import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import com.realmax.base.BaseLogic;
import com.realmax.base.BaseUiRefresh;
import com.realmax.base.utils.EncodeAndDecode;
import com.realmax.base.utils.L;
import com.realmax.smarttrafficmanager.activity.tcp.CustomerCallback;
import com.realmax.smarttrafficmanager.activity.tcp.CustomerHandlerBase;
import com.realmax.smarttrafficmanager.activity.tcp.NettyControl;
import com.realmax.smarttrafficmanager.bean.BarrierBean;
import com.realmax.smarttrafficmanager.bean.InductionLineBean;
import com.realmax.smarttrafficmanager.util.QueryUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author ayuan
 */
public class ControlLogic extends BaseLogic {

    private Bitmap bitmap;

    /**
     * 发送查看虚拟摄像头的命令
     *
     * @param deviceType       设备类型
     * @param deviceId         设备编号
     * @param cameraNum        摄像头编号
     * @param controlUiRefresh 回调
     */
    public void startCamera(String deviceType, int deviceId, int cameraNum, ControlUiRefresh controlUiRefresh) {
        CustomerHandlerBase customerHandlerBase = NettyControl.getHandlerHashMap().get("Camera");
        if (customerHandlerBase != null) {
            customerHandlerBase.setCustomerCallback(new CustomerCallback() {
                @Override
                public void disConnected() {
                    L.e("连接断开");
                }

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void getResultData(String msg) {
                    /*L.e(msg);*/
                    getImageData(msg, controlUiRefresh);
                }
            });
        }

        NettyControl.sendCameraCmd(deviceType, deviceId, cameraNum);
    }

    /**
     * 获取照片数据
     *
     * @param msg              json数据
     * @param controlUiRefresh 回调
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getImageData(String msg, ControlUiRefresh controlUiRefresh) {
        try {
            if (!TextUtils.isEmpty(msg)) {
                JSONObject jsonObject = new JSONObject(msg);
                if ("play".equals(jsonObject.optString("cmd"))) {
                    String imageData = jsonObject.optString("cameraImg");
                    bitmap = EncodeAndDecode.base64ToImage(imageData);
                    controlUiRefresh.setImageData(bitmap);
                }
            }
        } catch (JSONException e) {
            String substring = msg.substring(1);
            getImageData(substring, controlUiRefresh);
            L.e("json数据异常");
        }
    }

    /**
     * 查询道闸状态
     *
     * @param barrierId 道闸在数据库中的ID
     */
    public void getBarrierStatus(int barrierId, ControlUiRefresh controlUiRefresh) {
        BarrierBean barrierBean = new BarrierBean();
        barrierBean.setId(barrierId);
        QueryUtil.queryBarrierStatus(barrierBean, (Object object) -> {
            L.e(barrierBean.toString());
            controlUiRefresh.setBarrierStatus(Integer.parseInt(barrierBean.getSignalValue()));
        });
    }

    /**
     * 更新道闸状态
     *
     * @param barrierId 道闸ID
     * @param isChecked true表示开启，false表示关闭
     */
    public void updateBarrier(int barrierId, boolean isChecked) {
        QueryUtil.updateBarrierStatus(barrierId, isChecked ? 1 : 0, (Object object) -> {
            // 查询完成
        });
    }

    public void getInductionLine(int entryId, int outId, ControlUiRefresh controlUiRefresh) {
        ArrayList<InductionLineBean> inductionLineBeans = new ArrayList<>(2);
        inductionLineBeans.add(new InductionLineBean(entryId));
        inductionLineBeans.add(new InductionLineBean(outId));
        QueryUtil.queryInductionLine(inductionLineBeans, (Object object) -> {
            Collections.sort(inductionLineBeans, (InductionLineBean o1, InductionLineBean o2) -> o1.getId() - o2.getId());
            controlUiRefresh.setLineWidgetStatus(inductionLineBeans);
        });
    }

    interface ControlUiRefresh extends BaseUiRefresh {

        /**
         * 设置图片到控件上
         *
         * @param bitmap 需要设置的图片
         */
        void setImageData(Bitmap bitmap);

        /**
         * 设置道闸状态
         *
         * @param signalValue 0表示关，1表示开
         */
        void setBarrierStatus(int signalValue);

        /**
         * 设置界面中感应线控件的状态
         *
         * @param inductionLineBeans 感应下集合
         */
        void setLineWidgetStatus(ArrayList<InductionLineBean> inductionLineBeans);
    }
}
