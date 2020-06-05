package com.realmax.smarttrafficmanager.activity.count;

import com.realmax.base.BaseLogic;
import com.realmax.base.BaseUiRefresh;
import com.realmax.smarttrafficmanager.bean.ParkingBean;
import com.realmax.smarttrafficmanager.util.QueryUtil;

import java.util.ArrayList;

/**
 * @author ayuan
 */
public class CountLogic extends BaseLogic {

    private ArrayList<ParkingBean> parkingBeans;

    /**
     * 获取停车位数据
     *
     * @param countUiRefresh 回调
     */
    public void getParking(CountUiRefresh countUiRefresh) {
        parkingBeans = new ArrayList<>();
        QueryUtil.queryParking(parkingBeans, new QueryUtil.Result() {
            @Override
            public void success(Object object) {
                countUiRefresh.setListData(parkingBeans);
            }
        });
    }

    interface CountUiRefresh extends BaseUiRefresh {

        /**
         * 设置列表数据
         *
         * @param parkingBeans 车位集合
         */
        void setListData(ArrayList<ParkingBean> parkingBeans);
    }
}
