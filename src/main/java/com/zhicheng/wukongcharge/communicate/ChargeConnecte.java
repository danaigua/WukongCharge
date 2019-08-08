package com.zhicheng.wukongcharge.communicate;

import org.apache.log4j.Logger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ChargeConnecte {

	public void starService() {
		final Logger log = Logger.getLogger(TestCommunication.class);
		//服务类
		ServerBootstrap bootstrap = new ServerBootstrap();
		
		//boss线程监听端口， work线程负责读写
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		
		//定义工作组
		bootstrap.group(boss, worker);
		
		
		//设置管道channel
		bootstrap.channel(NioServerSocketChannel.class);
		
		//添加handler，管道中的处理器，通过ChannelInitializer来构造
		bootstrap.childHandler(new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel ch) throws Exception {
				//此方法每次客户端连接都会调用，是为通道初始化的方法
				
				//获得通道channel中的管道链（执行链、handler链）
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast(new TomdaChargerDecoder());
				pipeline.addLast("tomdaChargerServerHandler", new TomdaChargerServerHandler());
				pipeline.addLast(new TomdaChargerEncoder());
				log.info("成功连接");
			}
		});
        //6.设置参数
        //设置参数，TCP参数
		bootstrap.option(ChannelOption.SO_BACKLOG, 2048);//连接缓冲池的大小
		bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);//维持链接的活跃，清除死链接
		bootstrap.childOption(ChannelOption.TCP_NODELAY, true);//关闭延迟发送
		
		//绑定ip和port
		try {
			ChannelFuture channelFuture = bootstrap.bind("0.0.0.0", 6068).sync();//监听关闭
			channelFuture.channel().closeFuture().sync();  //等待服务关闭，关闭后应该释放资源
		} catch (InterruptedException e) {
			log.info("连接失败");
			System.out.println("连接失败");
			e.printStackTrace();
		}finally {
			//优雅的关闭资源
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
}
