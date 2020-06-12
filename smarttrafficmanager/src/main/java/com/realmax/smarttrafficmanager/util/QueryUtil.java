package com.realmax.smarttrafficmanager.util;

import android.annotation.SuppressLint;

import com.realmax.base.jdbcConnect.DbOpenhelper;
import com.realmax.base.utils.CustomerThread;
import com.realmax.base.utils.L;
import com.realmax.smarttrafficmanager.bean.BarrierBean;
import com.realmax.smarttrafficmanager.bean.InductionLineBean;
import com.realmax.smarttrafficmanager.bean.ParkingBean;
import com.realmax.smarttrafficmanager.bean.UploadBean;

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

    /**
     * 查询道闸状态
     *
     * @param barrierBean 道闸
     * @param result      回调
     */
    public static void queryBarrierStatus(BarrierBean barrierBean, Result result) {
        try {
            Connection drivingConn = DbOpenhelper.getDrivingConn();
            String sql = "select * from signal_info where id=?;";
            PreparedStatement preparedStatement = drivingConn.prepareStatement(sql);
            preparedStatement.setInt(1, barrierBean.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                barrierBean.setSignalName(resultSet.getString("signal_name"));
                barrierBean.setSignalText(resultSet.getString("signal_text"));
                barrierBean.setSignalType(resultSet.getString("signal_type"));
                barrierBean.setSignalValue(resultSet.getString("signal_value"));
            }
            result.success(barrierBean);
            DbOpenhelper.closeAll(preparedStatement, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /*CustomerThread.poolExecutor.execute(() -> {

        });*/
    }

    /**
     * 更新道闸的状态
     *
     * @param barrierId 道闸ID
     * @param i         开关状态
     * @param result    回调
     */
    public static void updateBarrierStatus(int barrierId, int i, Result result) {
        CustomerThread.poolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection drivingConn = DbOpenhelper.getDrivingConn();
                    String sql = "update signal_info set signal_value=? where id = ?";
                    PreparedStatement preparedStatement = drivingConn.prepareStatement(sql);
                    preparedStatement.setString(1, String.valueOf(i));
                    preparedStatement.setInt(2, barrierId);
                    int update = preparedStatement.executeUpdate();
                    L.e(update + "哈哈");
                    result.success(1);
                    DbOpenhelper.closeAll(preparedStatement);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 查询感应线的状态
     *
     * @param inductionLineBeans 感应线集合
     * @param result             查询你结果回调
     */
    public static void queryInductionLine(ArrayList<InductionLineBean> inductionLineBeans, Result result) {
        try {
            Connection drivingConn = DbOpenhelper.getDrivingConn();
            PreparedStatement preparedStatement = drivingConn.prepareStatement("select * from signal_info where id in (?, ?);");
            preparedStatement.setInt(1, inductionLineBeans.get(0).getId());
            preparedStatement.setInt(2, inductionLineBeans.get(1).getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                for (InductionLineBean inductionLineBean : inductionLineBeans) {
                    if (resultSet.getInt("id") == inductionLineBean.getId()) {
                        inductionLineBean.setSignalName(resultSet.getString("signal_name"));
                        inductionLineBean.setSignalText(resultSet.getString("signal_text"));
                        inductionLineBean.setSignalType(resultSet.getString("signal_type"));
                        inductionLineBean.setSignalValue(resultSet.getString("signal_value"));
                        break;
                    }
                }
            }
            result.success(inductionLineBeans);
            DbOpenhelper.closeAll(preparedStatement, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询所有感应线的状态
     *
     * @param inductionLineBeans 感应线集合
     * @param result             回调
     */
    public static void queryAllInductionLine(ArrayList<InductionLineBean> inductionLineBeans, Result result) {
        try {
            Connection drivingConn = DbOpenhelper.getDrivingConn();
            PreparedStatement preparedStatement = drivingConn.prepareStatement("select * from signal_info where id in (17,18,19,20,21,22,23,24);");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                InductionLineBean inductionLineBean = new InductionLineBean();
                inductionLineBean.setId(resultSet.getInt("id"));
                inductionLineBean.setSignalName(resultSet.getString("signal_name"));
                inductionLineBean.setSignalText(resultSet.getString("signal_text"));
                inductionLineBean.setSignalType(resultSet.getString("signal_type"));
                inductionLineBean.setSignalValue(resultSet.getString("signal_value"));
                inductionLineBeans.add(inductionLineBean);
            }
            result.success(inductionLineBeans);
            DbOpenhelper.closeAll(preparedStatement, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 插入一条停车数据
     *
     * @param numberPlate      车牌号
     * @param finalCurrentTime 当前时间
     * @param imageUrl         上传后的图片地址
     */
    public static void insertParkingRecords(String numberPlate, String finalCurrentTime, String imageUrl) {
        CustomerThread.poolExecutor.execute(() -> {
            try {
                Connection drivingConn = DbOpenhelper.getDrivingConn();
                String sql = "INSERT INTO driving.car_information (" +
                        "car_num," +
                        " begin_time," +
                        " comment," +
                        " payment_amount," +
                        " startImage" +
                        ") VALUES (?,?,?,?,?);";
                PreparedStatement preparedStatement = drivingConn.prepareStatement(sql);
                preparedStatement.setString(1, numberPlate);
                preparedStatement.setString(2, finalCurrentTime);
                preparedStatement.setString(3, "1");
                preparedStatement.setString(4, "未缴费");
                preparedStatement.setString(5, imageUrl);
                int i = preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 查询你对应车牌号的停车记录
     *
     * @param numberPlate 车牌号
     */
    public static void queryNumberPlate(String numberPlate, Result result) {
        CustomerThread.poolExecutor.execute(() -> {
            try {
                Connection drivingConn = DbOpenhelper.getDrivingConn();
                PreparedStatement preparedStatement = drivingConn.prepareStatement("select * from car_information where car_num = ?");
                preparedStatement.setString(1, numberPlate);
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.last();
                int count = resultSet.getRow();
                if (count > 0) {
                    // 查询到了当前车牌号的数据
                    result.success(true);
                } else {
                    // 没有查询到当前车牌号的数据，则插入一行数据
                    result.success(false);
                }
                DbOpenhelper.closeAll(preparedStatement, resultSet);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 更新停车记录
     *
     * @param numberPlate      车牌号
     * @param finalCurrentTime 当前说时间
     * @param imageUrl         上传后图片的URL地址
     * @param result           回调
     */
    public static void updateParkingRecords(String numberPlate, String finalCurrentTime, String imageUrl, Result result) {
        queryParkingRecord(numberPlate, (Object object) -> {
            try {
                String flag = (String) object;
                // 创建sql语句
                String sql = "";
                if ("0".equals(flag)) {
                    // 记录开始状态
                    sql = "update car_information set begin_time=?,startImage=?,comment='1' where car_num=?";
                } else if ("1".equals(flag)) {
                    // 记录结束状态
                    sql = "update car_information set end_time=?,endImage=?,comment='0' where car_num=?";
                }
                Connection drivingConn = DbOpenhelper.getDrivingConn();
                PreparedStatement preparedStatement = drivingConn.prepareStatement(sql);
                preparedStatement.setString(1, finalCurrentTime);
                preparedStatement.setString(2, imageUrl);
                preparedStatement.setString(3, numberPlate);
                int i = preparedStatement.executeUpdate();
                result.success(i > 0);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private static void queryParkingRecord(String numberPlate, Result result) {
        CustomerThread.poolExecutor.execute(() -> {
            try {
                Connection drivingConn = DbOpenhelper.getDrivingConn();
                PreparedStatement preparedStatement = drivingConn.prepareStatement("select * from car_information where car_num = ?");
                preparedStatement.setString(1, numberPlate);
                ResultSet resultSet = preparedStatement.executeQuery();
                String comment = "";
                while (resultSet.next()) {
                    comment = resultSet.getString("comment");
                }
                result.success(comment);
                DbOpenhelper.closeAll(preparedStatement, resultSet);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 根据车牌号查询当前车辆的停车记录
     *
     * @param numberPlate 车牌号
     * @param result
     */
    public static void queryParkingRecording(String numberPlate, Result result) {
        CustomerThread.poolExecutor.execute(() -> {
            try {
                Connection drivingConn = DbOpenhelper.getDrivingConn();
                PreparedStatement preparedStatement = drivingConn.prepareStatement("select * from car_information where car_num = ?;");
                preparedStatement.setString(1, numberPlate);
                ResultSet resultSet = preparedStatement.executeQuery();
                UploadBean uploadBean = new UploadBean();
                if (resultSet.first()) {
                    uploadBean.setCarNum(resultSet.getString("car_num"));
                    uploadBean.setBeginTime(resultSet.getString("begin_time"));
                    uploadBean.setEndTime(resultSet.getString("end_time"));
                    uploadBean.setParkingTime(resultSet.getString("parking_time"));
                    uploadBean.setPaymentAmount(resultSet.getString("payment_amount"));
                    uploadBean.setComment(resultSet.getString("comment"));
                    uploadBean.setStartImage(resultSet.getString("startImage"));
                    uploadBean.setEndImage(resultSet.getString("endImage"));
                }
                result.success(uploadBean);
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
