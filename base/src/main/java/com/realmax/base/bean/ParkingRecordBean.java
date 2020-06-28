package com.realmax.base.bean;

public class ParkingRecordBean {

    /**
     * id : 21
     * car_num : 闽D66666
     * begin_time : 2020-06-12 09:05:25
     * end_time : 2020-06-12 13:20:25
     * parking_time : null
     * payment_amount : 未缴费
     * comment : 0
     * startImage : http://driving.zuto360.com/upload/1591931492776.png
     * endImage : http://driving.zuto360.com/upload/1591931544989.png
     */

    private int id;
    private String carNum = "";
    private String beginTime = "";
    private String endTime = "";
    private String parkingTime = "";
    private String paymentAmount = "";
    private String comment = "";
    private String startImage = "";
    private String endImage = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getParkingTime() {
        return parkingTime;
    }

    public void setParkingTime(String parkingTime) {
        this.parkingTime = parkingTime;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStartImage() {
        return startImage;
    }

    public void setStartImage(String startImage) {
        this.startImage = startImage;
    }

    public String getEndImage() {
        return endImage;
    }

    public void setEndImage(String endImage) {
        this.endImage = endImage;
    }

    @Override
    public String toString() {
        return "ParkingRecordBean{" +
                "id=" + id +
                ", car_num='" + carNum + '\'' +
                ", begin_time='" + beginTime + '\'' +
                ", end_time='" + endTime + '\'' +
                ", parking_time=" + parkingTime +
                ", payment_amount='" + paymentAmount + '\'' +
                ", comment='" + comment + '\'' +
                ", startImage='" + startImage + '\'' +
                ", endImage='" + endImage + '\'' +
                '}';
    }
}
