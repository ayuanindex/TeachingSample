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

    /**
     * 缴费
     *
     * @param numberPlate 需要缴费的车牌号
     * @param result      回调
     */
    public static void updateParkingRecord(String numberPlate, Result result) {
        CustomerThread.poolExecutor.execute(() -> {
            try {
                Connection drivingConn = DbOpenhelper.getDrivingConn();
                if (drivingConn != null) {
                    PreparedStatement preparedStatement = drivingConn.prepareStatement("update car_information set payment_amount = ? where car_num= ?;");
                    preparedStatement.setString(1, "已缴费");
                    preparedStatement.setString(2, numberPlate);
                    int i = preparedStatement.executeUpdate();
                    if (i > 0) {
                        result.success(true);
                    } else {
                        result.success(false);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 新增车辆停车历史记录
     *
     * @param recordBean 当前车辆出入场情况
     * @param result     添加成功的回调
     */
    public static void addParkingHistory(ParkingRecordBean recordBean, Result result) {
        queryParkingRecord(recordBean.getCarNum(), object -> {
            ParkingRecordBean bean = (ParkingRecordBean) object;
            CustomerThread.poolExecutor.execute(() -> {
                try {
                    Connection drivingConn = DbOpenhelper.getDrivingConn();
                    if (drivingConn != null) {
                        String sql = "INSERT INTO parking_history (" +
                                "car_num," +
                                " begin_time," +
                                " end_time," +
                                " parking_time," +
                                " payment_amount," +
                                " comment," +
                                " startImage," +
                                " endImage" +
                                ") VALUES (?,?,?,?,?,?,?,?);";
                        PreparedStatement preparedStatement = drivingConn.prepareStatement(sql);
                        preparedStatement.setString(1, bean.getCarNum());
                        preparedStatement.setString(2, bean.getBeginTime());
                        preparedStatement.setString(3, bean.getEndTime());
                        preparedStatement.setString(4, bean.getParkingTime());
                        preparedStatement.setString(5, bean.getPaymentAmount());
                        preparedStatement.setString(6, bean.getComment());
                        preparedStatement.setString(7, bean.getStartImage());
                        preparedStatement.setString(8, bean.getEndImage());
                        int i = preparedStatement.executeUpdate();
                        result.success(i > 0);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        });

    }


    public interface Result {
        void success(Object object);
    }
}
