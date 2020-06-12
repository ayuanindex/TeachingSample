package com.realmax.smarttrafficmanager.activity.control;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.realmax.base.BaseLogic;
import com.realmax.base.BaseUiRefresh;
import com.realmax.base.ftp.FTPUtil;
import com.realmax.base.tcp.CustomerCallback;
import com.realmax.base.tcp.CustomerHandlerBase;
import com.realmax.base.tcp.NettyControl;
import com.realmax.base.utils.EncodeAndDecode;
import com.realmax.base.utils.L;
import com.realmax.base.utils.NumberPlateORC;
import com.realmax.smarttrafficmanager.bean.BarrierBean;
import com.realmax.smarttrafficmanager.bean.InductionLineBean;
import com.realmax.smarttrafficmanager.bean.WeatherBean;
import com.realmax.smarttrafficmanager.util.QueryUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author ayuan
 */
public class ControlLogic extends BaseLogic {
    private ControlUiRefresh controlUiRefresh;
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
    private boolean flag = false;
    private Bitmap numberPlateBitmap;
    private String numberPlate;
    /**
     * 2020-03-31 00:00:00.0
     */
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 发送查看虚拟摄像头的命令
     *
     * @param deviceType 设备类型
     * @param deviceId   设备编号
     * @param cameraNum  摄像头编号
     */
    public void startCamera(String deviceType, int deviceId, int cameraNum) {
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
                    getImageData(msg);
                }
            });
        }

        NettyControl.sendCameraCmd(deviceType, deviceId, cameraNum);
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
                if ("play".equals(jsonObject.optString("cmd"))) {
                    String imageData = jsonObject.optString("cameraImg");
                    bitmap = EncodeAndDecode.base64ToImage(imageData);
                    jsonStr = msg;
                    controlUiRefresh.setImageData(bitmap);
                } else if ("ans".equals(jsonObject.optString("cmd"))) {
                    WeatherBean weatherBean = new Gson().fromJson(msg, WeatherBean.class);
                    L.e("拿到虚拟场景的环境信息-------------" + weatherBean.toString());
                    upload(weatherBean);
                }
            }
        } catch (JSONException e) {
            String substring = msg.substring(1);
            getImageData(substring);
            L.e("json数据异常");
        }
    }

    /**
     * 上传
     *
     * @param weatherBean 环境数据
     */
    private void upload(WeatherBean weatherBean) {
        try {
            // 获取当前系统的年月日
            Date date = new Date();
            String currentTime = simpleDateFormat.format(date);
            currentTime = currentTime + " " + weatherBean.getTime();
            // 上传图片
            FTPUtil ftpUtil = new FTPUtil();
            ftpUtil.openConnect();
            String finalCurrentTime = currentTime;
            ftpUtil.compressImage(numberPlateBitmap, String.valueOf(date.getTime()), (File file) -> {
                // 转换成功
                boolean uploading = ftpUtil.uploading(file, String.valueOf(date.getTime()));
                L.e("上传状态----------" + uploading);
                if (uploading) {
                    if (file != null) {
                        if (file.delete()) {
                            L.e("上传照片临时文件已删除");
                        }
                    }
                    // 拼接地址
                    String imageUrl = "http://driving.zuto360.com/upload/" + date.getTime() + ".png";
                    // 查询当前车牌是否已经有停车记录
                    QueryUtil.queryNumberPlate(numberPlate, (Object object) -> {
                        boolean doesItExist = (boolean) object;
                        if (doesItExist) {
                            // 更新
                            QueryUtil.updateParkingRecords(numberPlate, finalCurrentTime, imageUrl, (Object success) -> {
                            });
                        } else {
                            // 新增停车记录
                            QueryUtil.insertParkingRecords(numberPlate, finalCurrentTime, imageUrl);
                        }
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
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
    public void getEntranceStatus() {
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
                    /*L.e(inductionLineBeans.toString());*/
                    controlUiRefresh.setLineWidgetStatus(inductionLineBeans);

                    // 根据感应线状态拍摄图片
                    String signalValue = inductionLineBeans.get(0).getSignalValue();
                    String signalValue1 = inductionLineBeans.get(1).getSignalValue();
                    int i = Integer.parseInt(signalValue);
                    int i1 = Integer.parseInt(signalValue1);
                    if (i == 1) {
                        L.e("开始进行车牌识别");
                        flag = true;
                    } else if (i1 == 1) {
                        L.e("车牌识别结束");
                        flag = false;
                        isOpen = true;
                    }

                    if (isOpen && flag) {
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
                        isOpen = false;
                        numberPlateBitmap = bitmap;
                        this.numberPlate = numberPlate;
                        // 将车牌刷新到界面上
                        controlUiRefresh.setNumberPlate(numberPlate);
                        // 修改道闸状态
                        updateBarrier(barrierId, true);
                        // 获取当前虚拟场景的时间
                        NettyControl.sendWeatherCmd();
                    } else {
                        isOpen = true;
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        // 取消定时
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    public void setControlUiRefresh(ControlUiRefresh controlUiRefresh) {
        this.controlUiRefresh = controlUiRefresh;
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

        /**
         * 设置车牌奥
         *
         * @param numberPlate 识别出的车牌号
         */
        void setNumberPlate(String numberPlate);

        /**
         * 结束时调用
         */
        void onDestroy();
    }
}
