package com.realmax.smarttrafficmanager.bean;

/**
 * @author ayuan
 */
public class InductionLineBean {

    /**
     * id : 17
     * signal_name : south_en_line_in
     * signal_text : 南入口入车压线检测
     * signal_type : bool
     * signal_value : 1
     * commnet : 0无车1有车压线
     */

    private int id;
    private String signalName;
    private String signalText;
    private String signalType;
    private String signalValue;

    public InductionLineBean() {
    }

    public InductionLineBean(int id) {
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
        return "InductionLineBean{" +
                "id=" + id +
                ", signalName='" + signalName + '\'' +
                ", signalText='" + signalText + '\'' +
                ", signalType='" + signalType + '\'' +
                ", signalValue='" + signalValue + '\'' +
                '}';
    }
}
