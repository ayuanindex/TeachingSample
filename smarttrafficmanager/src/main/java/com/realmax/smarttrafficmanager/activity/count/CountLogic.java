package com.realmax.smarttrafficmanager.activity.count;

import com.realmax.base.BaseLogic;
import com.realmax.base.BaseUiRefresh;
import com.realmax.smarttrafficmanager.bean.ParkingBean;
import com.realmax.smarttrafficmanager.util.QueryUtil;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author ayuan
 */
public class CountLogic extends BaseLogic {

    private ArrayList<ParkingBean> parkingBeans;
    private CountUiRefresh countUiRefresh;
    private Timer timer;
    private TimerTask task;

    /**
     * 获取停车位数据
     */
    public void getParking() {

        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                parkingBeans = new ArrayList<>();
                QueryUtil.queryParking(parkingBeans, (Object object) -> countUiRefresh.setListData(parkingBeans));
                /*parkingBeans.clear();
                QueryUtil.queryParking(parkingBeans, (Object object) -> countUiRefresh.refreshListView(parkingBeans));*/
            }
        };
        timer.schedule(task, 0, 500);
    }

    public void setCountUiRefresh(CountUiRefresh countUiRefresh) {
        this.countUiRefresh = countUiRefresh;
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

    interface CountUiRefresh extends BaseUiRefresh {

        /**
         * 设置列表数据
         *
         * @param parkingBeans 车位集合
         */
        void setListData(ArrayList<ParkingBean> parkingBeans);

        /**
         * 刷新列表
         *
         * @param parkingBeans 停车位集合
         */
        void refreshListView(ArrayList<ParkingBean> parkingBeans);
    }
}
