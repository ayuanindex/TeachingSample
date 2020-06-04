package com.realmax.smarttrafficclient;

/**
 * @author ayuan
 */
public class MainPresent implements MainLogic.MainUiRefresh {

    private final MainView mainView;
    private final MainLogic mainLogic;

    public MainPresent(MainView mainView, MainLogic mainLogic) {
        this.mainView = mainView;
        this.mainLogic = mainLogic;
    }

    /**
     * 初始化
     */
    public void initData() {

    }

    public void setNumberPlate() {

    }
}
