package com.realmax.smarttrafficmanager.util;

import com.realmax.base.utils.L;
import com.realmax.smarttrafficmanager.activity.tcp.CustomerCallback;
import com.realmax.smarttrafficmanager.activity.tcp.CustomerHandlerBase;
import com.realmax.smarttrafficmanager.activity.tcp.NettyControl;

/**
 * @author ayuan
 */
public class TcpData {
    public static void start() {
        CustomerHandlerBase customerHandlerBase = NettyControl.getHandlerHashMap().get("Camera");
        if (customerHandlerBase != null) {
            customerHandlerBase.setCustomerCallback(new CustomerCallback() {
                @Override
                public void disConnected() {

                }

                @Override
                public void getResultData(String msg) {
                    L.e("拿到数据----------" + msg);
                }
            });
        }
    }
}
