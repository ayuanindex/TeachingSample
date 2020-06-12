package com.realmax.smarttrafficclient;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.ContentLoadingProgressBar;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.realmax.base.BaseLogic;
import com.realmax.base.BaseUiRefresh;
import com.realmax.base.bean.ParkingRecordBean;
import com.realmax.base.jdbcConnect.QueryUtil;
import com.realmax.base.tcp.CustomerCallback;
import com.realmax.base.tcp.CustomerHandlerBase;
import com.realmax.base.tcp.NettyControl;
import com.realmax.base.tcp.NettyLinkUtil;
import com.realmax.base.utils.CustomerThread;
import com.realmax.base.utils.L;
import com.realmax.base.utils.SpUtil;
import com.realmax.smarttrafficclient.bean.WeatherBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.netty.channel.EventLoopGroup;

/**
 * @author ayuan
 */
public class MainLogic extends BaseLogic {
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat yearDataFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final String NUMBER_PLATE = "numberPlate";
    private static MainUiRefresh mainUiRefresh;
    private WeatherBean weatherBean;
    private String numberPlate;
    private boolean isConnected = false;

    /**
     * 显示设置车牌号的Dialog
     */
    public void showSetNumberPlateDialog() {
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

            this.numberPlate = numberPlate;

            SpUtil.putString(NUMBER_PLATE, numberPlate);
            mainUiRefresh.showToast("设置成功");
            mainUiRefresh.setNumberPlate(numberPlate);
            // 从数据库中查询当前车辆的停车记录
            if (isConnected) {
                queryParkingRecord(numberPlate);
            } else {
                NettyControl.sendWeatherCmd("weather");
            }
            alertDialog.dismiss();
        });

        holder.cardCancel.setOnClickListener(v -> alertDialog.dismiss());
        alertDialog.show();
    }

    public void startPayment() {
        AlertDialog alertDialog = getAlertDialog(mainUiRefresh);
        View inflate = View.inflate(mainUiRefresh.getActivity(), R.layout.dialog_paymen, null);
        alertDialog.setView(inflate);

        PaymentViewHolder holder = new PaymentViewHolder(inflate);

        // TODO: 2020/6/4 待完成
        QueryUtil.deleteParkingRecord(numberPlate, (Object object) -> {
            Handler handler = new Handler(Looper.getMainLooper());
            boolean b = (boolean) object;
            // 模拟缴费过程
            handler.postDelayed(() -> {
                holder.pbPayProgress.setVisibility(View.GONE);
                holder.ivStatus.setVisibility(View.VISIBLE);
                holder.ivStatus.setImageResource(b ? R.drawable.pic_success : R.drawable.pic_error);
                holder.tvPayState.setText(b ? "缴费成功" : "缴费失败");
            }, 1000);
            handler.postDelayed(alertDialog::dismiss, 1800);
            mainUiRefresh.setWidget("暂无缴费单");
        });
        alertDialog.show();
    }

    /**
     * 显示车牌号
     */
    public void showNumberPlate() {
        numberPlate = SpUtil.getString(NUMBER_PLATE, "");
        if (TextUtils.isEmpty(numberPlate)) {
            mainUiRefresh.setNumberPlate("未设置");
        } else {
            mainUiRefresh.setNumberPlate(numberPlate);
            // 从数据库中查询当前车辆的停车记录
            if (isConnected) {
                queryParkingRecord(numberPlate);
            } else {
                NettyControl.sendWeatherCmd("weather");
            }
        }
    }

    /**
     * 停车记录
     *
     * @param numberPlate 车牌号
     */
    private void queryParkingRecord(String numberPlate) {
        QueryUtil.queryParkingRecord(numberPlate, (Object object) -> {
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
                    long pay = (days * 24 * 5) + (hours * 5) + ((minutes >= 30 ? 1 : 0) * 5);
                    String message = "开始时间：" + recordBean.getBeginTime() + "\n" +
                            "结束时间：" + recordBean.getEndTime() + "\n" +
                            "停车时长：" + ((days == 0 ? "" : (days + "天，")) + (hours == 0 ? "" : (hours + "小时，")) + (minutes == 0 ? "" : (minutes + "分钟"))) + "\n" +
                            "需缴费：" + pay + "元," +
                            "缴费状态:" + recordBean.getPaymentAmount();
                    mainUiRefresh.setWidget(message);
                    L.e(message);
                }
            }
        });
    }

    /**
     * 连接虚拟场景
     */
    public void connectVirtualScene() {
        AlertDialog alertDialog = getAlertDialog(mainUiRefresh);
        View inflate = View.inflate(mainUiRefresh.getActivity(), R.layout.dialog_connect_setting, null);
        alertDialog.setView(inflate);
        ConnectedViewHolder connectedViewHolder = new ConnectedViewHolder(inflate);
        connectedViewHolder.cardConnect.setOnClickListener((View v) -> {
            String ipStr = connectedViewHolder.etIp.getText().toString().trim();
            String portStr = connectedViewHolder.etPort.getText().toString().trim();
            boolean flag = true;
            String msg = "请输入";
            if (TextUtils.isEmpty(ipStr)) {
                msg += "IP";
                flag = false;
            }

            if (TextUtils.isEmpty(portStr)) {
                msg += flag ? "端口号" : "和端口号";
                flag = false;
            }

            if (!flag) {
                mainUiRefresh.showToast(msg);
            }

            int port = Integer.parseInt(portStr);

            CustomerThread.poolExecutor.execute(() -> {
                try {
                    CustomerHandlerBase nettyHandler = new CustomerHandlerBase();
                    NettyControl.getHandlerHashMap().put("weather", nettyHandler);

                    NettyLinkUtil nettyLinkUtil = new NettyLinkUtil(ipStr, port);
                    nettyLinkUtil.start(new NettyLinkUtil.Callback() {
                        @Override
                        public void success(EventLoopGroup eventLoopGroup) {
                            // 连接成功
                            isConnected = true;
                            mainUiRefresh.showToast("连接成功");
                            alertDialog.dismiss();
                            getWeather();
                        }

                        @Override
                        public void error() {
                            // 断开连接
                            if (nettyHandler.getCustomerCallback() != null) {
                                nettyHandler.getCustomerCallback().disConnected();
                            }
                        }
                    }, nettyHandler);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
        alertDialog.show();
    }

    /**
     * 获取天气数据
     */
    private void getWeather() {
        CustomerHandlerBase customerHandlerBase = NettyControl.getHandlerHashMap().get("weather");
        if (customerHandlerBase != null) {
            customerHandlerBase.setCustomerCallback(new CustomerCallback() {
                @Override
                public void disConnected() {
                    L.e("连接断开");
                    isConnected = false;
                    connectVirtualScene();
                }

                @Override
                public void getResultData(String msg) {
                    if (!TextUtils.isEmpty(msg)) {
                        try {
                            JSONObject jsonObject = new JSONObject(msg);
                            if (jsonObject.optString("cmd").equals("ans")) {
                                weatherBean = new Gson().fromJson(msg, WeatherBean.class);
                                L.e("获取到的天气数据： " + weatherBean.toString());
                                queryParkingRecord(SpUtil.getString(NUMBER_PLATE, ""));
                                mainUiRefresh.setWeatherToWidget(weatherBean);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        NettyControl.sendWeatherCmd("weather");
    }


    /**
     * 设置天气图标
     *
     * @param weatherBean 天气数据
     * @return 返回Drawable
     */
    public int getWeatherIcon(WeatherBean weatherBean) {
        switch (weatherBean.getWeather()) {
            case "雷雨":
                return R.drawable.pic_weather_leizhenyu;
            case "大雾":
                return R.drawable.pic_weather_dawu;
            case "晴天":
                return R.drawable.pic_weather_tianqing;
            default:
                return R.drawable.pic_weather_na;
        }
    }

    interface MainUiRefresh extends BaseUiRefresh {

        /**
         * 设置车牌号
         *
         * @param numberPlate 车牌号
         */
        void setNumberPlate(String numberPlate);

        /**
         * 将天气信息设置到空间中
         *
         * @param weatherBean 天气信息
         */
        void setWeatherToWidget(WeatherBean weatherBean);

        /**
         * 将分析结果设置到控件中
         *
         * @param message 分析结果
         */
        void setWidget(String message);
    }

    /**
     * 设置回调
     *
     * @param mainUiRefresh 回调
     */
    public void setMainUiRefresh(MainUiRefresh mainUiRefresh) {
        MainLogic.mainUiRefresh = mainUiRefresh;
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

    public static
    class ConnectedViewHolder {
        public View rootView;
        public EditText etIp;
        public EditText etPort;
        public MaterialCardView cardConnect;

        public ConnectedViewHolder(View rootView) {
            this.rootView = rootView;
            this.etIp = (EditText) rootView.findViewById(R.id.etIp);
            this.etPort = (EditText) rootView.findViewById(R.id.etPort);
            this.cardConnect = (MaterialCardView) rootView.findViewById(R.id.cardConnect);
        }
    }
}
