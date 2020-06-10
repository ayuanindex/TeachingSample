package com.realmax.smarttrafficmanager.activity.control;

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
import com.realmax.smarttrafficmanager.bean.BarrierBean;
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
public class ControlLogic extends BaseLogic {

    private Bitmap bitmap;
    private Timer timer;
    private TimerTask task;
    private int barrierId = 25;
    private int entryId = 17;
    private int outId = 18;
    private BarrierBean barrierBean;
    private ArrayList<InductionLineBean> inductionLineBeans;
    private int deviceId;
    private int cameraNum;
    private String jsonStr;
    private boolean isOpen = true;

    /**
     * 发送查看虚拟摄像头的命令
     *
     * @param deviceType       设备类型
     * @param deviceId         设备编号
     * @param cameraNum        摄像头编号
     * @param controlUiRefresh 回调
     */
    public void startCamera(String deviceType, int deviceId, int cameraNum, ControlUiRefresh controlUiRefresh) {
        this.deviceId = deviceId;
        this.cameraNum = cameraNum;

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
                    jsonStr = msg;
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
    public void getBarrierStatus(int barrierId) {
        this.barrierId = barrierId;
    }

    /**
     * 更新道闸状态
     *
     * @param barrierId 道闸ID
     * @param isChecked true表示开启，false表示关闭
     */
    public void updateBarrier(int barrierId, boolean isChecked) {
        this.barrierId = barrierId;
        QueryUtil.updateBarrierStatus(barrierId, isChecked ? 1 : 0, (Object object) -> {
            // 查询完成
        });
    }

    /**
     * 查询感应线状态
     *
     * @param entryId 入车
     * @param outId   出车
     */
    public void getInductionLine(int entryId, int outId) {
        this.entryId = entryId;
        this.outId = outId;
    }

    /**
     * 开启出入口循环检测
     */
    public void getEntranceStatus(ControlUiRefresh controlUiRefresh) {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                // 道闸
                barrierBean = new BarrierBean();
                barrierBean.setId(barrierId);

                // 感应线
                inductionLineBeans = new ArrayList<>(2);
                inductionLineBeans.add(new InductionLineBean(entryId));
                inductionLineBeans.add(new InductionLineBean(outId));

                // 查询道闸状态
                QueryUtil.queryBarrierStatus(barrierBean, (Object object) -> {
                    /*L.e(barrierBean.toString());*/
                    controlUiRefresh.setBarrierStatus(Integer.parseInt(barrierBean.getSignalValue()));
                });

                QueryUtil.queryInductionLine(inductionLineBeans, (Object object) -> {
                    L.e(inductionLineBeans.toString());
                    controlUiRefresh.setLineWidgetStatus(inductionLineBeans);

                    // 根据感应线状态拍摄图片
                    String signalValue = inductionLineBeans.get(1).getSignalValue();
                    int i = Integer.parseInt(signalValue);
                    if (i == 1 && isOpen) {
                        // 有车压线
                        identifyTheLicensePlate();
                    }
                });
            }
        };
        timer.schedule(task, 0, 100);
    }

    /**
     * 识别车牌号
     */
    private void identifyTheLicensePlate() {
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            int deviceId = jsonObject.optInt("deviceId");
            int cameraNum = jsonObject.optInt("cameraNum");
            if (deviceId == this.deviceId && cameraNum == this.cameraNum) {
                // 判断当当前摄像头的数据是否是切花后摄像头的数据
                // 开始获取摄像头中的数据
                isOpen = false;
                NumberPlateORC.getNumberPlate(bitmap, (String numberPlate) -> {
                    if (!TextUtils.isEmpty(numberPlate)) {
                        updateBarrier(barrierId, true);
                    } else {
                        // 如果没有识别成功则重新识别
                        identifyTheLicensePlate();
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
