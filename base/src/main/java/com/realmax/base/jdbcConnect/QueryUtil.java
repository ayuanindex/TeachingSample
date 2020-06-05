package com.realmax.base.jdbcConnect;


import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;

/**
 * @ProjectName: IOAMES
 * @Package: com.realmax.ioames.dao
 * @ClassName: IOAMESQueryDao
 * @CreateDate: 2020/2/19 16:22
 */
public class QueryUtil {
    /**
     * 2020-03-31 00:00:00.0
     */
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 查询全部违章记录
     *
     * @param violateCarBeans 传入集合待存入所有违章数据
     * @param result          接口回调，查询完毕是调用接口
     */
    /*public static void queryForAll(ArrayList<ViolateCarBean> violateCarBeans, Result result) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Connection drivingConn = DBOpenHelper.getDrivingConn();
                    PreparedStatement preparedStatement = drivingConn.prepareStatement("select * from violatetable;");
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        violateCarBeans.add(new ViolateCarBean(
                                resultSet.getString("camera_one"),
                                resultSet.getString("camera_two"),
                                resultSet.getString("numberplate"),
                                resultSet.getInt("violatecount"),
                                resultSet.getString("violatedetail"),
                                resultSet.getString("lastimagepath"),
                                simpleDateFormat.parse(resultSet.getString("create_time"))));
                    }
                    result.success(violateCarBeans);
                    DBOpenHelper.closeAll(preparedStatement, resultSet);
                } catch (SQLException | ParseException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }*/

    /**
     * 添加记录到违章记录表中,同时向违章图片的表添加数据
     *
     * @param violateCarBean 需要添加的对象
     * @param result         接口回调更新行数
     */
    /*public static void addToVio(ViolateCarBean violateCarBean, Result result) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Connection drivingConn = DBOpenHelper.getDrivingConn();
                    String sql = "insert into violatetable(" +
                            "camera_one," +
                            "camera_two," +
                            "numberplate," +
                            "violatecount," +
                            "violatedetail," +
                            "lastimagepath," +
                            "create_time" +
                            ") values (?,?,?,?,?,?,?);";
                    PreparedStatement preparedStatement = drivingConn.prepareStatement(sql);
                    preparedStatement.setString(1, violateCarBean.getCamera_one());
                    preparedStatement.setString(2, violateCarBean.getCamera_two());
                    preparedStatement.setString(3, violateCarBean.getNumberPlate());
                    preparedStatement.setInt(4, violateCarBean.getViolateCount());
                    preparedStatement.setString(5, violateCarBean.getDes());
                    preparedStatement.setString(6, violateCarBean.getLastImagePath());
                    preparedStatement.setString(7, simpleDateFormat.format(violateCarBean.getCreateTime()));
                    int i = preparedStatement.executeUpdate();
                    result.success(i);
                    DBOpenHelper.closeAll(preparedStatement);
                    addToImg(drivingConn, result, violateCarBean);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }*/

    /**
     * 更新指定车辆的违章次数
     *
     * @param violateCarBean 违章次数
     * @param camera
     */
    /*public static void updateViolateCountByNumberPlate(ViolateCarBean violateCarBean, String camera, Result result) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Connection drivingConn = DBOpenHelper.getDrivingConn();
                    String sql = "update violatetable set violatecount=?,camera_two=?,create_time=?,violatedetail=? where numberplate=?";
                    PreparedStatement preparedStatement = drivingConn.prepareStatement(sql);
                    // 更新次数
                    preparedStatement.setInt(1, violateCarBean.getViolateCount());
                    // 更新第二个摄像头·
                    preparedStatement.setString(2, camera);
                    preparedStatement.setString(3, simpleDateFormat.format(violateCarBean.getCreateTime()));
                    preparedStatement.setString(4, violateCarBean.getDes());
                    // 条件
                    preparedStatement.setString(5, violateCarBean.getNumberPlate());
                    int i = preparedStatement.executeUpdate();
                    result.success(i);
                    DBOpenHelper.closeAll(preparedStatement);
                    addToImg(drivingConn, result, violateCarBean);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }*/

    /**
     * 找出传入的对象是否存在于数据库中
     *
     * @param violateCarBean
     * @param result
     */
    /*public static void queryIsFromDatabase(ViolateCarBean violateCarBean, Result result) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Connection drivingConn = DBOpenHelper.getDrivingConn();
                    String sql = "select * from violatetable where numberplate=?";
                    PreparedStatement preparedStatement = drivingConn.prepareStatement(sql);
                    preparedStatement.setString(1, violateCarBean.getNumberPlate());
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        String numberplate = resultSet.getString("numberplate");
                        if (TextUtils.isEmpty(numberplate)) {
                            result.success(false);
                        } else {
                            result.success(true);
                        }
                        return;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }*/

    public interface Result {
        void success(Object object);
    }
}
