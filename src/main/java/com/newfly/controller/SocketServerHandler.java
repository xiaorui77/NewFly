package com.newfly.controller;

import com.newfly.common.SocketChannelMap;
import com.newfly.pojo.ResultMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class SocketServerHandler extends SimpleChannelInboundHandler<Object>
{
    private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);

    // 初始化时获取
    private ServerController serverController;


    SocketServerHandler() {
        super();
        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
        this.serverController = (ServerController) context.getBean("serverController");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable throwable) {
        logger.debug("异常发生: 捕获于SocketServerHandler:exceptionCaught()", throwable);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object o) {
        ResultMessage msg = (ResultMessage) o;
        logger.info("接受到的消息:");
        logger.info("type:" + msg.getType());
        logger.info("body:" + msg.getBody());
        logger.info("Controller:" + serverController);

        ResultMessage result = serverController.handle(ctx, msg);
        if (result != null)
            ctx.writeAndFlush(result);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("建立连接");
        String clientId = "随机ID";
        SocketChannelMap.add(clientId, (SocketChannel) ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("连接断开");
        SocketChannelMap.remove((SocketChannel) ctx.channel()); // 删除channel
        super.channelInactive(ctx);
    }
}
