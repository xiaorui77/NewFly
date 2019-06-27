package com.newfly.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class LobbyServer
{
    //基础配置信息
    //log日志
    private static final Logger logger = LoggerFactory.getLogger(LobbyServer.class);
    //服务器IP（可配置到配置文件）
    private static final String IP = "127.0.0.1";
    //端口号（可配置到配置文件中）
    private static final int port = 8088;

    //分配用于处理业务的线程组数量  Runtime.getRuntime().availableProcessors()获取jvm可用的线程数
    protected static final int BisGroupSize = Runtime.getRuntime().availableProcessors() * 2;
    //每个线程组中线程的数量
    protected static final int worGroupSize = 4;

    //NioEventLoopGroup进行事件处理，如接收新连接以及数据处理
    private static final EventLoopGroup bossGruop = new NioEventLoopGroup(BisGroupSize);
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup(worGroupSize);

    protected static void run() throws Exception {
        //serverBootstrap 服务端引导
        ServerBootstrap bootStrap = new ServerBootstrap();
        bootStrap.group(bossGruop, workerGroup);
        //指定所使用的 channel 有nio oio linux有epoll（性能比nio强大的异步非阻塞）
        bootStrap.channel(NioServerSocketChannel.class);
        bootStrap.childHandler(new ChannelInitializer<SocketChannel>()
        {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                //ChannelPipeline链 将所有的业务逻辑层连接到一起
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new RtsEncoder());
                pipeline.addLast(new RtsDecoder());
                //pipeline.addLast("logging",new LoggingHandler(LogLevel.WARN));
                //注册HeartBeatReqHandler
                pipeline.addLast(new HeartBeatReqHandler());
                //注册LoginHandler  多个channelHandler执行顺序为注册顺序
                pipeline.addLast(new LoginHandler());
            }
            //ChannelOption设置tcp缓冲区的大小
        }).option(ChannelOption.SO_BACKLOG, 1024)
                //通过NODELY禁用Nagle,使消息立即发出，不用等到一点的数量才发出
                .option(ChannelOption.TCP_NODELAY, true)
                //保持长连接
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        logger.info("LobbySever 启动TCP长连接完成！");
        //sync导致当前Thread 阻塞，一直到绑定操作完成为止 bind方法绑定服务器
        ChannelFuture f = bootStrap.bind(IP, port).sync();
        f.channel().closeFuture().sync();
        logger.info("LobbySever Socket服务器已启动完成！");
        //closeFuture会一直阻塞到channel关闭 然后调用shutdown
        shutdown();
    }

    private static void shutdown() {
        //关闭eventLoopGroup释放所有资源
        bossGruop.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
        logger.info("开始启动LobbySever服务器...");
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        logger.info("装载spring容器成功");

        LobbyManager lobbyManager = LobbyManager.getInstance();
        lobbyManager.init();
        logger.info("初始化管理器成功");

        RedisUtil.getInstance();
        run();
    }
}
