package com.realmax.base.tcp;


import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author ayuan
 */
public abstract class BaseNettyHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private static final String TAG = "NettyHandler";
    /**
     * 回调接口
     */
    private CustomerCallback callback;
    private boolean flag = false;
    private StringBuffer strings = new StringBuffer();
    private int leftCount = 0;
    private int rightCount = 0;
    private static final char left = '{';
    private static final char right = '}';
    public String currentCommand = "";


    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, ByteBuf msg) {
        /*Log.i(TAG, "channelRead0: client channelRead..哈哈哈");*/
        ByteBuf buf = msg.readBytes(msg.readableBytes());
        String s = buf.toString(StandardCharsets.UTF_8);
        /*String s = buf.toString(StandardCharsets.UTF_8);*/
        currentCommand = s;
        callbackFunction(s);
    }

    /**
     * 返回Json字符串
     *
     * @param jsonStr json格式的字符串
     */
    public abstract void callbackFunction(String jsonStr);

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause) {
        cause.printStackTrace();
        channelHandlerContext.close();
    }
}
