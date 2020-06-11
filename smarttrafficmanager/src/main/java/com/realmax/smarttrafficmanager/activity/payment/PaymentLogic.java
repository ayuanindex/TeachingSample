package com.realmax.smarttrafficmanager.activity.payment;

import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import com.realmax.base.BaseLogic;
import com.realmax.base.BaseUiRefresh;
import com.realmax.base.utils.EncodeAndDecode;
import com.realmax.base.utils.L;
import com.realmax.base.utils.NumberPlateORC;
import com.realmax.smarttrafficmanager.activity.tcp.CustomerCallback;
import com.realmax.smarttrafficmanager.activity.tcp.CustomerHandlerBase;
import com.realmax.smarttrafficmanager.activity.tcp.NettyControl;
import com.realmax.smarttrafficmanager.bean.InductionLineBean;
import com.realmax.smarttrafficmanager.util.QueryUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author ayuan
 */
public class PaymentLogic extends BaseLogic {

    private Bitmap bitmap;
    private Timer timer;
    private String ETC = "ETC收费站";
    private int deviceId = 2;
    private int cameraNum = 1;
    private boolean orc = true;
    private boolean flag = false;
    private Bitmap numberPlateBitmap;
    private String jsonStr;


    /**
     * 开启摄像头
     */
    public void startCamera(PaymentUiRefresh paymentUiRefresh) {
        CustomerHandlerBase customerHandlerBase = NettyControl.getHandlerHashMap().get("Camera");
        if (customerHandlerBase != null) {
            customerHandlerBase.setCustomerCallback(new CustomerCallback() {
                @Override
                public void disConnected() {
                    L.e("TCP断开");
                }

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void getResultData(String msg) {
                    /*L.e(msg);*/
                    getImageData(msg, paymentUiRefresh);
                }
            });
        }

        NettyControl.sendCameraCmd(ETC, deviceId, cameraNum);
    }

    /**
     * 获取照片数据
     *
     * @param msg              json数据
     * @param paymentUiRefresh 回调
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getImageData(String msg, PaymentUiRefresh paymentUiRefresh) {
        try {
            if (!TextUtils.isEmpty(msg)) {
                jsonStr = msg;
                JSONObject jsonObject = new JSONObject(msg);
                String cameraImg = jsonObject.optString("cameraImg");
                bitmap = EncodeAndDecode.base64ToImage(cameraImg);
                paymentUiRefresh.setImage(bitmap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            String substring = msg.substring(1);
            getImageData(substring, paymentUiRefresh);
        }
    }

    /**
     * 获取出入口的状态
     */
    public void getEntranceStatus(PaymentUiRefresh paymentUiRefresh) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                ArrayList<InductionLineBean> inductionLineBeans = new ArrayList<>();
                QueryUtil.queryAllInductionLine(inductionLineBeans, (Object object) -> {
                    for (InductionLineBean inductionLineBean : inductionLineBeans) {
                        if (inductionLineBean.getSignalValue().equals("1") && inductionLineBean.getId() % 2 == 1) {
                            // 出入口入车压线
                            switchCamera(inductionLineBean.getId());
                            flag = true;
                            L.e("检测到压线");
                            break;
                        } else if (inductionLineBean.getSignalValue().equals("1") && inductionLineBean.getId() % 2 == 0) {
                            // 出入口出车压线
                            orc = true;
                            flag = false;
                            break;
                        }
                    }

                    LicensePlateRecognition(paymentUiRefresh);
                });
            }
        }, 0, 100);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void LicensePlateRecognition(PaymentUiRefresh paymentUiRefresh) {
        try {
            if (!TextUtils.isEmpty(jsonStr)) {
                JSONObject jsonObject = new JSONObject(jsonStr);
                int deviceId = jsonObject.optInt("deviceId");
                int cameraNum = jsonObject.optInt("cameraNum");
                if (this.deviceId == deviceId && this.cameraNum == cameraNum) {
                    if (flag && orc) {
                        orc = false;
                        Bitmap bitmap = EncodeAndDecode.base64ToImage(jsonObject.optString("cameraImg"));
                        NumberPlateORC.getNumberPlate(bitmap, (String numberPlate) -> {
                            if (numberPlate != null) {
                                L.e("请求成功------" + numberPlate);
                                L.e("结束识别");
                                paymentUiRefresh.setNumberPlate(numberPlate);
                                orc = false;
                            } else {
                                orc = true;
                            }
                        });
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据拿到的压线状态切换摄像头
     *
     * @param id 在数据库中的ID
     */
    private void switchCamera(int id) {
        switch (id) {
            case 17:
                deviceId = 2;
                cameraNum = 1;
                break;
            case 19:
                deviceId = 2;
                cameraNum = 2;
                break;
            case 21:
                deviceId = 1;
                cameraNum = 2;
                break;
            case 23:
                deviceId = 1;
                cameraNum = 1;
                break;
        }

        NettyControl.sendCameraCmd(ETC, deviceId, cameraNum);
    }

    interface PaymentUiRefresh extends BaseUiRefresh {

        /**
         * 设置下显示图片
         *
         * @param bitmap 图片
         */
        void setImage(Bitmap bitmap);

        /**
         * 设置车牌号
         *
         * @param numberPlate 车牌号
         */
        void setNumberPlate(String numberPlate);
    }
}
