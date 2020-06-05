package com.realmax.smarttrafficmanager.bean;

/**
 * @author ayuan
 */
public class ParkingBean {

    /**
     * id : 1
     * signal_name : parking01
     * signal_text : 停车位01
     * signal_type : bool
     * signal_value : 0
     * commnet : 0无车1有车
     */

    private int id;
    private String signalName;
    private String signalText;
    private String signalType;
    private String signalValue;

    public ParkingBean() {
    }

    public ParkingBean(int id, String signalName, String signalText, String signalType, String signalValue) {
        this.id = id;
        this.signalName = signalName;
        this.signalText = signalText;
        this.signalType = signalType;
        this.signalValue = signalValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSignalName() {
        return signalName;
    }

    public void setSignalName(String signalName) {
        this.signalName = signalName;
    }

    public String getSignalText() {
        return signalText;
    }

    public void setSignalText(String signalText) {
        this.signalText = signalText;
    }

    public String getSignalType() {
        return signalType;
    }

    public void setSignalType(String signalType) {
        this.signalType = signalType;
    }

    public String getSignalValue() {
        return signalValue;
    }

    public void setSignalValue(String signalValue) {
        this.signalValue = signalValue;
    }

    @Override
    public String toString() {
        return "ParkingBean{" +
                "id=" + id +
                ", signalName='" + signalName + '\'' +
                ", signalText='" + signalText + '\'' +
                ", signalType='" + signalType + '\'' +
                ", signalValue='" + signalValue + '\'' +
                '}';
    }
}
