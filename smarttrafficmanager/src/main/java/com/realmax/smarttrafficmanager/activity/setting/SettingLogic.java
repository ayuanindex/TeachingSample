package com.realmax.smarttrafficmanager.activity.setting;

import android.text.TextUtils;

import com.realmax.base.App;
import com.realmax.base.BaseLogic;
import com.realmax.base.BaseUiRefresh;
import com.realmax.base.tcp.CustomerCallback;
import com.realmax.base.tcp.CustomerHandlerBase;
import com.realmax.base.tcp.NettyControl;
import com.realmax.base.tcp.NettyLinkUtil;
import com.realmax.base.utils.CustomerThread;
import com.realmax.base.utils.L;
import com.realmax.base.utils.SpUtil;

import io.netty.channel.EventLoopGroup;

/**
 * @author ayuan
 */
public class SettingLogic extends BaseLogic {

    public static final String CAMERA = "Camera";

    /**
     * 获取当前的连接状态
     *
     * @param settingUiRefresh 回调
     */
    public void getCurrentConnectStatus(SettingUiRefresh settingUiRefresh) {
        boolean isConnectHashMap = NettyControl.getIsConnectHashMap(CAMERA);
        settingUiRefresh.setEditAndButton(isConnectHashMap);
    }

    /**
     * 连接到虚拟场景
     *
     * @param ip               需要连接的IP地址
     * @param port             目标服务端的端口号
     * @param settingUiRefresh 回调
     */
    public void connectVirtualScene(String ip, String port, SettingUiRefresh settingUiRefresh) {
        boolean isConnectHashMap = NettyControl.getIsConnectHashMap(CAMERA);
        if (isConnectHashMap) {
            // 停止连接
            NettyControl.closeLink(NettyControl.getEventLoopGroup(CAMERA));
            NettyControl.putConnectState(CAMERA, false);
            settingUiRefresh.setEditAndButton(false);
            settingUiRefresh.showToast("连接已断开");
            return;
        }

        boolean flag = true;
        String tip = "";

        if (TextUtils.isEmpty(ip)) {
            flag = false;
            tip += "IP地址";
        }

        if (TextUtils.isEmpty(port)) {
            flag = false;
            tip += "端口号";
        }

        if (!flag) {
            App.showToast("请设置" + tip);
            return;
        }

        // 存储IP和端口号
        SpUtil.putString("ip", ip);
        SpUtil.putString("port", port);

        CustomerThread.poolExecutor.execute(() -> {
            try {
                // 新建消息接受类
                CustomerHandlerBase nettyHandler = new CustomerHandlerBase();
                // 放入到全局的静态集合中
                NettyControl.putNettyHandler(CAMERA, nettyHandler);
                // 获取回调
                CustomerCallback customerCallback = nettyHandler.getCustomerCallback();

                NettyLinkUtil nettyLinkUtil = new NettyLinkUtil(ip, Integer.parseInt(port));
                nettyLinkUtil.start(new NettyLinkUtil.Callback() {
                    @Override
                    public void success(EventLoopGroup eventLoopGroup) {
                        L.e("连接成功");
                        settingUiRefresh.setEditAndButton(true);
                        NettyControl.putConnectState(CAMERA, true);
                        NettyControl.putEventLoopGroup(CAMERA, eventLoopGroup);
                        settingUiRefresh.showToast("连接成功");
                    }

                    @Override
                    public void error() {
                        L.e("连接失败");
                        settingUiRefresh.setEditAndButton(false);
                        if (customerCallback != null) {
                            customerCallback.disConnected();
                        }
                        NettyControl.putConnectState(CAMERA, false);
                    }
                }, nettyHandler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    interface SettingUiRefresh extends BaseUiRefresh {

        /**
         * 设置页面中控件的状态
         *
         * @param isConnectHashMap ture表示已连接，false表示未连接
         */
        void setEditAndButton(boolean isConnectHashMap);
    }
}
