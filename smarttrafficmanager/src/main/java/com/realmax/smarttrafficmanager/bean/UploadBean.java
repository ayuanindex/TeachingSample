package com.realmax.smarttrafficmanager.bean;

public class UploadBean {

    /**
     * id : generated
     * car_num : 皖B888888
     * begin_time : 2020-06-11 09:51:24
     * end_time : 2020-06-11 09:51:26
     * parking_time : 1
     * payment_amount : 30
     * comment :
     * startImage :
     * endImage :
     */

    private String carNum;
    private String beginTime;
    private String endTime;
    private String parkingTime;
    private String paymentAmount;
    private String comment;
    private String startImage;
    private String endImage;

    public UploadBean() {
    }

    public UploadBean(String carNum, String beginTime, String endTime, String parkingTime, String paymentAmount, String comment, String startImage, String endImage) {
        this.carNum = carNum;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.parkingTime = parkingTime;
        this.paymentAmount = paymentAmount;
        this.comment = comment;
        this.startImage = startImage;
        this.endImage = endImage;
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
        return "UploadBean{" +
                "carNum='" + carNum + '\'' +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", parkingTime='" + parkingTime + '\'' +
                ", paymentAmount='" + paymentAmount + '\'' +
                ", comment='" + comment + '\'' +
                ", startImage='" + startImage + '\'' +
                ", endImage='" + endImage + '\'' +
                '}';
    }
}