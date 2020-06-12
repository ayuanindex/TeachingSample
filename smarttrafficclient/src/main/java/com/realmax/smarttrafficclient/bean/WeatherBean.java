package com.realmax.smarttrafficclient.bean;

public class WeatherBean {

    /**
     * cmd : ans
     * time : 09:01:52
     * weather : 晴天
     * temp : 23.5
     * windSpeed : 5.0
     * humi : 25.3
     */

    private String cmd;
    private String time;
    private String weather;
    private double temp;
    private double windSpeed;
    private double humi;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getHumi() {
        return humi;
    }

    public void setHumi(double humi) {
        this.humi = humi;
    }

    @Override
    public String toString() {
        return "WeatherBean{" +
                "cmd='" + cmd + '\'' +
                ", time='" + time + '\'' +
                ", weather='" + weather + '\'' +
                ", temp=" + temp +
                ", windSpeed=" + windSpeed +
                ", humi=" + humi +
                '}';
    }
}
