package com.realmax.smarttrafficmanager.util;

import android.annotation.SuppressLint;

import com.realmax.base.jdbcConnect.DbOpenhelper;
import com.realmax.base.utils.CustomerThread;
import com.realmax.base.utils.L;
import com.realmax.smarttrafficmanager.bean.BarrierBean;
import com.realmax.smarttrafficmanager.bean.InductionLineBean;
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


        /*CustomerThread.poolExecutor.execute(new Runnable() {
            @Override
            public void run() {
            }
        });*/
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
