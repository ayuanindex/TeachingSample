package com.realmax.smarttrafficmanager.util;

import android.annotation.SuppressLint;

import com.realmax.base.jdbcConnect.DbOpenhelper;
import com.realmax.base.utils.CustomerThread;
import com.realmax.smarttrafficmanager.bean.ParkingBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * @author ayuan
 */
public class QueryUtil {
    /**
     * 2020-03-31 00:00:00.0
     */
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void queryParking(ArrayList<ParkingBean> parkingBeans, Result result) {
        CustomerThread.poolExecutor.execute(() -> {
            try {
                Connection drivingConn = DbOpenhelper.getDrivingConn();
                String sql = "select * from signal_info where id in (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);";
                PreparedStatement preparedStatement = drivingConn.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    ParkingBean e = new ParkingBean();
                    e.setId(resultSet.getInt("id"));
                    e.setSignalName(resultSet.getString("signal_name"));
                    e.setSignalText(resultSet.getString("signal_text"));
                    e.setSignalType(resultSet.getString("signal_type"));
                    e.setSignalValue(resultSet.getString("signal_value"));
                    parkingBeans.add(e);
                }
                result.success(parkingBeans);
                DbOpenhelper.closeAll(preparedStatement, resultSet);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public interface Result {
        /**
         * 查询成功回调
         *
         * @param object 回调数据
         */
        void success(Object object);
    }
}
