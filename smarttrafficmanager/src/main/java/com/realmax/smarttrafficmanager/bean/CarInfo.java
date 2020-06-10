package com.realmax.smarttrafficmanager.bean;

public class CarInfo {

    /**
     * id : 1
     * car_num : asj
     * begin_time : 2020-06-10 10:29:35
     * end_time : 2020-06-10 10:29:37
     * parking_time : 12
     * payment_amount : 1
     * comment : lkasd
     * startImage : sdfkasdj
     * endImage : laskdjflaw
     */

    private int id;
    private String carNum;
    private String beginTime;
    private String endTime;
    private String parkingTime;
    private String paymentAmount;
    private String comment;
    private String startImage;
    private String endImage;

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
        return "CarInfo{" +
                "id=" + id +
                ", carNum='" + carNum + '\'' +
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
