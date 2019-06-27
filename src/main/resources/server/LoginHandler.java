package com.newfly.server;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class LoginHandler extends ChannelHandlerAdapter
{

    public static final Logger logger = LoggerFactory.getLogger(LoginHandler.class);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error(ctx.channel().remoteAddress()+" 错误关闭");
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 用于获取客户端发送的消息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //用于获取客户端发送的消息
        RtsProtocal body = (RtsProtocal)msg;
        logger.info("LobbyServer接受的客户端的信息:"+body.toString());
        //如果是登录相关协议
        if(body.getType() == 1){
            logger.info("是登录相关的协议!");
            LobbyManager lobbyManager = LobbyManager.getInstance();
            //根据sessionId 获取gameSession
            LobbyGameSession gameSession = lobbyManager.sessionManager.get(ctx.channel().id().asLongText());
            if(gameSession == null){
                gameSession = new LobbyGameSession();
                //设置ip
                gameSession.setIP(((InetSocketAddress)ctx.channel().remoteAddress()).getAddress()
                        .getHostAddress());
                //设置sessionId
                gameSession.setSessionId(ctx.channel().id().asLongText());
                lobbyManager.addSession(ctx.channel().id().asLongText(), gameSession);
            }
            //根据协议号调用业务
            Processors processors = lobbyManager.protocolManager.get(body.getProtocloNumber());
            processors.setGameSession(gameSession);
            processors.setJson(new String(body.getContent()));
            //执行具体的业务逻辑
            processors.process();
        }else{
            //通知下一个channelHandler执行
            ctx.fireChannelRead(msg);
        }
    }
}