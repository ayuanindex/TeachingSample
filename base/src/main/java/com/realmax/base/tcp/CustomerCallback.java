package com.realmax.base.tcp;

public interface CustomerCallback {
   /* void success(EventLoopGroup eventLoopGroup);*/

    void disConnected();

    /*void sendMessage(ChannelHandlerContext handlerContext);*/

    void getResultData(String msg);
}