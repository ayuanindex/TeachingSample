package com.realmax.smarttrafficmanager.activity.control;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.realmax.base.BaseLogic;
import com.realmax.base.BaseUiRefresh;
import com.realmax.base.bean.ParkingRecordBean;
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
import java.text.ParsePosition;
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
    private int deviceId;
    private int cameraNum;
    private String jsonStr;
    private boolean isOpen = true;
    private boolean flag = false;
    private Bitmap numberPlateBitmap;
    private String numberPlate;
    private String deviceType = "ETC收费站";
    private boolean isEnter = false;
    private Line inAndOut;

    /**
     * 2020-03-31 00:00:00.0
     */
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat yearDataFormat = new SimpleDateFormat("yyyy-MM-dd");
    private WeatherBean weatherBean;
    private Timer timerParking;
    private TimerTask taskParking;


    public enum Line {
        ENTER, OUT, CONVENTION,
        SOUTHENTRY, SOUTHOUT, NORTHENTRY, NORTHOUT;
    }

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
                    weatherBean = new Gson().fromJson(msg, WeatherBean.class);
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
     * 开启出入口循环检测
     */
    public void getEntranceStatus() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                // 查询道闸状态
                QueryUtil.queryBarrierStatus(new BarrierBean(barrierId), (Object object) -> {
                    controlUiRefresh.setBarrierStatus(Integer.parseInt(((BarrierBean) object).getSignalValue()));
                });

                getAllInductionLine();
            }
        };
        timer.schedule(task, 0, 100);
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
            String currentTime = yearDataFormat.format(date);
            currentTime = currentTime + " " + weatherBean.getTime();
            // 上传图片
            FTPUtil ftpUtil = new FTPUtil();
            ftpUtil.openConnect();
            String finalCurrentTime = currentTime;
            ftpUtil.compressImage(numberPlateBitmap, String.valueOf(date.getTime()), (File file) -> {
                // 图片转File成功，并存储到了手机上
                boolean uploading = ftpUtil.uploading(file, String.valueOf(date.getTime()));
                L.e("上传状态----------" + uploading);
                if (uploading) {
                    if (file != null) {
                        if (file.delete()) {
                            L.e("上传照片临时文件已删除");
                        }
                    }
                    // 拼接上传后的图片地址
                    String imageUrl = "http://driving.zuto360.com/upload/" + date.getTime() + ".png";
                    // 查询当前车牌是否已经有停车记录
                    QueryUtil.queryNumberPlate(numberPlate, (Object object) -> {
                        boolean doesItExist = (boolean) object;
                        if (doesItExist) {
                            // 更新停车记录
                            // 根据inAndOut的状态来判断数据库中存储数据的方式
                            switch (inAndOut) {
                                case ENTER:
                                    // 入口记录
                                    QueryUtil.updateStartTime(numberPlate, finalCurrentTime, imageUrl, (Object success) -> {
                                        // 查询当前车辆的停车记录
                                        queryParkingRecord(numberPlate);
                                    });
                                    break;
                                case OUT:
                                    // 出口记录
                                    QueryUtil.updateEndTime(numberPlate, finalCurrentTime, imageUrl, (Object success) -> {
                                        // 查询当前车辆的停车记录
                                        queryParkingRecord(numberPlate);
                                    });
                                    break;
                            }
                        } else {
                            // 新增停车记录
                            QueryUtil.insertParkingRecords(numberPlate, finalCurrentTime, imageUrl, (Object success) -> {
                                // 查询当前车辆的停车记录
                                queryParkingRecord(numberPlate);
                            });
                        }
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有感应线的状态
     */
    public void getAllInductionLine() {
        ArrayList<InductionLineBean> inductionLineBeans = new ArrayList<>();
        // 南入  入车
        inductionLineBeans.add(new InductionLineBean(17));
        // 南入  出车
        inductionLineBeans.add(new InductionLineBean(18));
        // 南出  入车
        inductionLineBeans.add(new InductionLineBean(19));
        // 南出  出车
        inductionLineBeans.add(new InductionLineBean(20));

        // 北入  入车
        inductionLineBeans.add(new InductionLineBean(21));
        // 北入  出车
        inductionLineBeans.add(new InductionLineBean(22));
        // 北出  入车
        inductionLineBeans.add(new InductionLineBean(23));
        // 北出  出车
        inductionLineBeans.add(new InductionLineBean(24));
        QueryUtil.queryInductionLine(inductionLineBeans, (Object object) -> {
            /*L.e(inductionLineBeans.toString());*/
            for (InductionLineBean inductionLineBean : inductionLineBeans) {
                if (!TextUtils.isEmpty(inductionLineBean.getSignalValue())) {
                    int value = Integer.parseInt(inductionLineBean.getSignalValue());
                    if (value == 1) {
                        setWidgetStatus(inductionLineBean.getId(), value);
                        cancelBilling();
                        break;
                    }

                    if (inductionLineBean.getId() == inductionLineBeans.get(inductionLineBeans.size() - 1).getId()) {
                        controlUiRefresh.setLineStatus(Line.ENTER, 0);
                        controlUiRefresh.setLineStatus(Line.OUT, 0);
                    }

                    if (flag) {
                        startRecognition();
                    }
                } else {
                    L.e("没有读取到状态");
                }
            }
        });
    }

    /**
     * 取消计费
     */
    private void cancelBilling() {
        if (timerParking != null) {
            timerParking.cancel();
            timerParking = null;
        }

        if (taskParking != null) {
            taskParking.cancel();
            taskParking = null;
        }
    }

    /**
     * 检测到压线的线，并切换到对应的摄像头
     *
     * @param id    感应线的ID
     * @param value 感应线的状态值
     */
    private void setWidgetStatus(int id, int value) {
        L.e("压线的是：-----------" + id);
        // 是否是出口压线的标示符
        isEnter = id == 19 || id == 23;
        // true 为开始识别，false为停止识别
        flag = id % 2 != 0;

        switch (id) {
            // 南入
            case 17:
                barrierId = 25;
                inAndOut = Line.ENTER;
                startCamera(deviceType, 2, 1);
                controlUiRefresh.setLineStatus(Line.ENTER, value);
                controlUiRefresh.selectRadiuButton(Line.SOUTHENTRY);
                break;
            case 18:
                barrierId = 25;
                inAndOut = Line.CONVENTION;
                startCamera(deviceType, 2, 1);
                controlUiRefresh.setLineStatus(Line.OUT, value);
                controlUiRefresh.selectRadiuButton(Line.SOUTHENTRY);
                break;
            // 南出
            case 19:
                barrierId = 26;
                inAndOut = Line.OUT;
                startCamera(deviceType, 2, 2);
                controlUiRefresh.setLineStatus(Line.ENTER, value);
                controlUiRefresh.selectRadiuButton(Line.SOUTHOUT);
                break;
            case 20:
                barrierId = 26;
                inAndOut = Line.CONVENTION;
                startCamera(deviceType, 2, 2);
                controlUiRefresh.setLineStatus(Line.OUT, value);
                controlUiRefresh.selectRadiuButton(Line.SOUTHOUT);
                break;
            // 北入
            case 21:
                barrierId = 27;
                inAndOut = Line.ENTER;
                startCamera(deviceType, 1, 2);
                controlUiRefresh.setLineStatus(Line.ENTER, value);
                controlUiRefresh.selectRadiuButton(Line.NORTHENTRY);
                break;
            case 22:
                barrierId = 27;
                inAndOut = Line.CONVENTION;
                startCamera(deviceType, 1, 2);
                controlUiRefresh.setLineStatus(Line.OUT, value);
                controlUiRefresh.selectRadiuButton(Line.NORTHENTRY);
                break;
            // 北出
            case 23:
                barrierId = 28;
                inAndOut = Line.OUT;
                startCamera(deviceType, 1, 1);
                controlUiRefresh.setLineStatus(Line.ENTER, value);
                controlUiRefresh.selectRadiuButton(Line.NORTHOUT);
                break;
            case 24:
                barrierId = 28;
                inAndOut = Line.CONVENTION;
                startCamera(deviceType, 1, 1);
                controlUiRefresh.setLineStatus(Line.OUT, value);
                controlUiRefresh.selectRadiuButton(Line.NORTHOUT);
                break;
        }
        // 获取切换到的入口的道闸状态
        getBarrierStatus(barrierId);
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
                NumberPlateORC.getNumberPlate(bitmap, (String numberPlate, int confidence) -> {
                    isOpen = true;
                    // 对识别出的车牌号进行可信度的判断
                    if (confidence > 98) {
                        if (!TextUtils.isEmpty(numberPlate)) {
                            flag = false;
                            numberPlateBitmap = bitmap;
                            this.numberPlate = numberPlate;
                            // 将车牌刷新到界面上
                            controlUiRefresh.setNumberPlate(numberPlate + "\n入场时间:无\n出场时间:无" +
                                    "\n停车时长:无" +
                                    "\n需缴费:—缴费状态:");
                            // 获取当前虚拟场景的时间
                            NettyControl.sendWeatherCmd("Camera");
                        } else {
                            controlUiRefresh.setNumberPlate("未检测出车牌号" + "\n入场时间:无\n出场时间:无" +
                                    "\n停车时长:无" +
                                    "\n需缴费:无—缴费状态:无");
                        }
                    } else {
                        // 车牌识别不准确
                        controlUiRefresh.showToast("车牌识别不准确，请调整车位");
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停车记录
     *
     * @param numberPlate 车牌号
     */
    private void queryParkingRecord(String numberPlate) {
        if (isEnter) {
            isEnter = false;
            // 取消计费
            cancelBilling();

            timerParking = new Timer();
            taskParking = new TimerTask() {
                @Override
                public void run() {
                    L.e("正在计算停车费用");
                    calculateCost(numberPlate);
                }
            };
            timerParking.schedule(taskParking, 0, 1000);
        } else {
            calculateCost(numberPlate);
        }

    }

    private void calculateCost(String numberPlate) {
        com.realmax.base.jdbcConnect.QueryUtil.queryParkingRecord(numberPlate, (Object object) -> {
            ParkingRecordBean recordBean = (ParkingRecordBean) object;
            if (recordBean.getBeginTime() != null) {
                String year = yearDataFormat.format(new Date()) + " " + weatherBean.getTime();
                Date start = simpleDateFormat.parse(recordBean.getBeginTime(), new ParsePosition(0));
                Date end;
                if (TextUtils.isEmpty(recordBean.getEndTime())) {
                    end = simpleDateFormat.parse(year, new ParsePosition(0));
                } else {
                    end = simpleDateFormat.parse(recordBean.getEndTime(), new ParsePosition(0));
                }

                if (start != null && end != null) {
                    long timeDifference = end.getTime() - start.getTime();
                    long days = timeDifference / (1000 * 60 * 60 * 24);
                    long hours = (timeDifference - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                    long minutes = (timeDifference - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
                    long pay = ((days <= 0 ? 0 : days) * 24 * 5) + ((hours <= 0 ? 0 : hours) * 5) + ((minutes >= 30 ? 1 : 0) * 5);
                    String parkingTime = (days <= 0 ? "" : (days + "天，")) + (hours <= 0 ? "" : (hours + "小时，")) + (minutes <= 0 ? "" : (minutes + "分钟"));
                    if (TextUtils.isEmpty(parkingTime)) {
                        parkingTime = "无";
                    }
                    controlUiRefresh.setNumberPlate(numberPlate + "\n入场时间:" + recordBean.getBeginTime() + "\n出场时间:" + (recordBean.getEndTime() == null ? "无" : recordBean.getEndTime()) +
                            "\n停车时长:" + parkingTime +
                            "\n需缴费:" + pay + "元—缴费状态:" + recordBean.getPaymentAmount());
                    QueryUtil.updateParkingTime(numberPlate, parkingTime, pay, object1 -> {
                        // 更新成功
                    });

                    if (pay == 0 && (!isEnter && recordBean.getPaymentAmount().equals("未缴费"))) {
                        // 开启道闸，删除正在停车的记录，以为在管理段已经添加到了停车历史记录表中
                        updateBarrier(barrierId, true);
                        // 取消计费
                        cancelBilling();
                    }
                }

                if (recordBean.getPaymentAmount().equals("已缴费")) {
                    // 开启道闸，删除正在停车的记录，以为在管理段已经添加到了停车历史记录表中
                    updateBarrier(barrierId, true);
                    L.e("取消计算");
                    // 取消计费
                    cancelBilling();
                }
            }
        });
    }


    public void onDestroy() {
        // 取消计费
        cancelBilling();

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

    /**
     * 开始识别车牌号
     */
    private void startRecognition() {
        if (isOpen) {
            L.e("开始进行车牌识别");
            identifyTheLicensePlate();
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

        /**
         * 设置感应线的状态
         *
         * @param PassInAndOut 进出的选择
         * @param value        状态设置
         */
        void setLineStatus(Line PassInAndOut, int value);

        /**
         * 设置单选按钮的状态
         */
        void selectRadiuButton(Line line);
    }
}
