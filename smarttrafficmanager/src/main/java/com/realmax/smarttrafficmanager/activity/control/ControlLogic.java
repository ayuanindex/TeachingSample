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

import org.json.JSONException;
import org.json.JSONObject;

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
                    L.e(msg);
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

    interface ControlUiRefresh extends BaseUiRefresh {

        /**
         * 设置图片到控件上
         *
         * @param bitmap 需要设置的图片
         */
        void setImageData(Bitmap bitmap);
    }
}
