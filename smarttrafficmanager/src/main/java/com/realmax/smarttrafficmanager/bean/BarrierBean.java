package com.realmax.smarttrafficmanager.bean;

public class BarrierBean {

    /**
     * id : 25
     * signal_name : south_en_gate
     * signal_text : 南入口道闸
     * signal_type : bool
     * signal_value : 0
     * commnet : 0落下1抬起
     */

    private int id;
    private String signalName;
    private String signalText;
    private String signalType;
    private String signalValue;

    public BarrierBean() {
    }

    public BarrierBean(int id) {
        this.id = id;
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
        return "BarrierBean{" +
                "id=" + id +
                ", signalName='" + signalName + '\'' +
                ", signalText='" + signalText + '\'' +
                ", signalType='" + signalType + '\'' +
                ", signalValue='" + signalValue + '\'' +
                '}';
    }
}