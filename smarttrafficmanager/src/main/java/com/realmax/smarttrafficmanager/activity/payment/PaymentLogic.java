package com.realmax.smarttrafficmanager.activity.payment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.realmax.base.BaseLogic;
import com.realmax.base.BaseUiRefresh;
import com.realmax.base.tcp.CustomerCallback;
import com.realmax.base.tcp.CustomerHandlerBase;
import com.realmax.base.tcp.NettyControl;
import com.realmax.base.utils.EncodeAndDecode;
import com.realmax.base.utils.L;
import com.realmax.base.utils.NumberPlateORC;
import com.realmax.smarttrafficmanager.bean.InductionLineBean;
import com.realmax.smarttrafficmanager.bean.UploadBean;
import com.realmax.smarttrafficmanager.bean.WeatherBean;
import com.realmax.smarttrafficmanager.util.QueryUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author ayuan
 */
public class PaymentLogic extends BaseLogic {
    private PaymentUiRefresh paymentUiRefresh;
    private Timer timer;
    private String ETC = "ETC收费站";
    private int deviceId = 2;
    private int cameraNum = 1;
    private boolean orc = true;
    private boolean flag = false;
    private String jsonStr;
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat yearDataFormat = new SimpleDateFormat("yyyy-MM-dd");
    private UploadBean bean;
    private TimerTask task;

    /**
     * 开启摄像头
     */
    public void startCamera() {
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
                    getImageData(msg);
                }
            });
        }

        NettyControl.sendCameraCmd(ETC, deviceId, cameraNum);
    }

    /**
     * 获取照片数据
     *
     * @param msg json数据
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getImageData(String msg) {
        try {
            if (!TextUtils.isEmpty(msg)) {
                JSONObject jsonObject = new JSONObject(msg);
                if (jsonObject.optString("cmd").equals("play")) {
                    jsonStr = msg;
                    String cameraImg = jsonObject.optString("cameraImg");
                    Bitmap bitmap = EncodeAndDecode.base64ToImage(cameraImg);
                    paymentUiRefresh.setImage(bitmap);
                } else if (jsonObject.optString("cmd").equals("ans")) {
                    WeatherBean weatherBean = new Gson().fromJson(msg, WeatherBean.class);
                    String year = yearDataFormat.format(new Date());
                    setWidget(bean, year + " " + weatherBean.getTime());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            String substring = msg.substring(1);
            getImageData(substring);
        }
    }

    /**
     * 获取出入口的状态
     */
    public void getEntranceStatus() {
        timer = new Timer();
        task = new TimerTask() {
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

                    LicensePlateRecognition();
                });
            }
        };
        timer.schedule(task, 0, 100);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void LicensePlateRecognition() {
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
                                // 车牌号识别成功，从数据库中查询数据
                                QueryUtil.queryParkingRecording(numberPlate, (Object object) -> {
                                    if (object != null) {
                                        bean = (UploadBean) object;
                                        NettyControl.sendWeatherCmd("Camera");
                                    }
                                });
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
     * 将数据处理后设置到控件上
     * 停车费每小时5元
     *
     * @param bean        需要展示的数据
     * @param currentTime 当前时间
     */
    private void setWidget(UploadBean bean, String currentTime) {
        try {
            if (!TextUtils.isEmpty(bean.getBeginTime())) {
                Date start = simpleDateFormat.parse(bean.getBeginTime(), new ParsePosition(0));
                Date end;
                if (TextUtils.isEmpty(bean.getEndTime())) {
                    end = simpleDateFormat.parse(currentTime, new ParsePosition(0));
                } else {
                    end = simpleDateFormat.parse(bean.getEndTime(), new ParsePosition(0));
                }

                if (start != null && end != null) {
                    long timeDifference = end.getTime() - start.getTime();
                    long days = timeDifference / (1000 * 60 * 60 * 24);
                    long hours = (timeDifference - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                    long minutes = (timeDifference - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
                    long pay = (days * 24 * 5) + (hours * 5) + ((minutes / 60) * 5);
                    paymentUiRefresh.setWidget(
                            simpleDateFormat.format(start),
                            simpleDateFormat.format(end),
                            pay,
                            bean.getPaymentAmount()
                    );
                }
            } else {
                paymentUiRefresh.showToast("暂无停车记录");
                paymentUiRefresh.setWidget("暂无", "暂无", 0, "暂无");
            }
        } catch (Exception e) {
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

    public void setPaymentUiRefresh(PaymentUiRefresh paymentUiRefresh) {
        this.paymentUiRefresh = paymentUiRefresh;
    }

    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        if (task != null) {
            task.cancel();
            task = null;
        }
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

        /**
         * 将对应的停车数据展示出来
         *
         * @param start         开始时间
         * @param end           结束时间
         * @param pay           缴费金额
         * @param paymentAmount 缴费状态
         */
        void setWidget(String start, String end, long pay, String paymentAmount);
    }
}
