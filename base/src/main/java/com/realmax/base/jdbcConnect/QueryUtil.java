package com.realmax.base.jdbcConnect;


import com.realmax.base.bean.ParkingRecordBean;
import com.realmax.base.utils.CustomerThread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author ayuan
 */
public class QueryUtil {

    public static void queryParkingRecord(String numberPlate, Result result) {
        CustomerThread.poolExecutor.execute(() -> {
            try {
                Connection drivingConn = DbOpenhelper.getDrivingConn();
                PreparedStatement preparedStatement = drivingConn.prepareStatement("select * from car_information where car_num=?");
                preparedStatement.setString(1, numberPlate);
                ResultSet resultSet = preparedStatement.executeQuery();
                ParkingRecordBean parkingRecordBean = new ParkingRecordBean();
                if (resultSet.first()) {
                    parkingRecordBean.setCarNum(resultSet.getString("car_num"));
                    parkingRecordBean.setBeginTime(resultSet.getString("begin_time"));
                    parkingRecordBean.setEndTime(resultSet.getString("end_time"));
                    parkingRecordBean.setParkingTime(resultSet.getString("parking_time"));
                    parkingRecordBean.setPaymentAmount(resultSet.getString("payment_amount"));
                    parkingRecordBean.setComment(resultSet.getString("comment"));
                    parkingRecordBean.setStartImage(resultSet.getString("startImage"));
                    parkingRecordBean.setEndImage(resultSet.getString("endImage"));
                }
                result.success(parkingRecordBean);
                DbOpenhelper.closeAll(preparedStatement, resultSet);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void deleteParkingRecord(String numberPlate, Result result) {
        CustomerThread.poolExecutor.execute(() -> {
            try {
                Connection drivingConn = DbOpenhelper.getDrivingConn();
                PreparedStatement preparedStatement = drivingConn.prepareStatement("delete from car_information where car_num = ?;");
                preparedStatement.setString(1, numberPlate);
                int i = preparedStatement.executeUpdate();
                if (i > 0) {
                    result.success(true);
                } else {
                    result.success(false);
                }
                DbOpenhelper.closeAll(preparedStatement);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }


    public interface Result {
        void success(Object object);
    }
}
