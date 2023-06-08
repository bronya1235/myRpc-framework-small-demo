package com.github.bronya1234.netty.transport.impl;

import com.github.bronya1234.netty.transport.HandlerRegistryCenter;
import com.github.bronya1234.netty.transport.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Bao
 * @Date: 2023/6/8-06-08-15:09
 * @Description com.github.bronya1234.netty.transport.impl
 * @Function 服务器启动类，进行一些配置用的
 */
public class NettyServer implements Server {
	//日志
	private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);
	//端口号
	private int port;
	//两个线程池，一个boss，负责接受accept时间，从线程池负责分配具体的io
	private EventLoopGroup mainGroup;
	private EventLoopGroup subGroup;
	//通道，具体意义待补充
	private Channel channel;
	//处理器注册中心，可以不用一个一个导入处理器了
	private HandlerRegistryCenter handlerRegistryCenter;
	@Override
	public void start(HandlerRegistryCenter handlerRegistryCenter, int port) throws Exception {
		this.port=port;
		this.handlerRegistryCenter = handlerRegistryCenter;

		EventLoopGroup mainGroup = newEventLoopGroup();
		EventLoopGroup subGroup = newEventLoopGroup();
		ChannelHandler channelHandlerPipeline = newChannelHandlerPipeline();
		//采用了创建好线程池，再创建启动类的方式去做
		ServerBootstrap serverBootstrap = newBootstrap(channelHandlerPipeline, mainGroup, subGroup);
		Channel channel = doBind(serverBootstrap);

		this.mainGroup = mainGroup;
		this.subGroup = subGroup;
		this.channel = channel;
	}
	//创建一个EventLoopGroup
	private EventLoopGroup newEventLoopGroup() {
		if (Epoll.isAvailable()) {
			//这个地方还需要再查询一下
			return new EpollEventLoopGroup();
		} else {
			//这个是netty推荐使用的
			return new NioEventLoopGroup();
		}
	}

	@Override
	public void stop() {
		if (mainGroup != null) {
			mainGroup.shutdownGracefully();
		}
		if (subGroup != null) {
			subGroup.shutdownGracefully();
		}
		if (channel != null) {
			channel.close();
		}
	}
	//这个是把创建好的框架真正启动起来，同时把这个channel返回，这个主要是用来做服务器关机的
	private Channel doBind(ServerBootstrap serverBootstrap) throws Exception {
		return serverBootstrap.bind(port)
				.sync()
				.channel();
	}


	//这个是创建handler管道的方法
	private ChannelHandler newChannelHandlerPipeline() {
		return new ChannelInitializer<Channel>() {
			@Override
			protected void initChannel(Channel channel) {
				channel.pipeline()
						//请求解码器
						.addLast(new RequestDecoder())
						//请求编码器
						.addLast(new ResponseEncoder())
						//客户端请求handler
						.addLast(new RequestInvocation(handlerRegistryCenter));
			}
		};
	}
	//这个是创建启动类的方法，和之前的demo基本一致，其实就是使用netty的常见过程
	private ServerBootstrap newBootstrap(ChannelHandler channelHandler, EventLoopGroup mainGroup, EventLoopGroup subGroup) {
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.channel(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
				.group(mainGroup, subGroup)
				.childHandler(channelHandler)
				.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
		return serverBootstrap;
	}
}
