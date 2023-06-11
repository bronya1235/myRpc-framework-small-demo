package com.github.bronya1235.netty.transport.impl;

import com.github.bronya1235.netty.transport.Client;
import com.github.bronya1235.netty.transport.InFlightRequests;
import com.github.bronya1235.netty.transport.Transport;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.SocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * @Author: Bao
 * @Date: 2023/6/8-06-08-23:30
 * @Description com.github.bronya1234.netty.transport.impl
 * @Function
 */
public class NettyClient implements Client {
	//一个线程池，负责发送请求即可
	private volatile EventLoopGroup group;
	//一个客户端启动器
	private volatile Bootstrap bootstrap;
	//一个在途请求
	private final InFlightRequests inFlightRequests;
	//一个通道的容器
	private List<Channel> channels = new LinkedList<>();
	//构造器，初始化在途请求容器
	public NettyClient() {
		inFlightRequests = new InFlightRequests();
	}
	@Override
	public Transport createTransport(SocketAddress address, long connectionTimeout) throws InterruptedException, TimeoutException {
		return new NettyTransport(createChannel(address, connectionTimeout),inFlightRequests);
	}
	private synchronized Channel createChannel(SocketAddress address, long connectionTimeout) throws InterruptedException, TimeoutException{
		//如果地址为空就就不合法了
		if(address==null){
			throw new IllegalArgumentException("address must not be null");
		}
		//线程池必须是单例模式，
		if(group==null){
			group= newGroup();
		}
		if(bootstrap==null){
			ChannelHandler channelHandler = newChannelHandlerPipeline();
			bootstrap= newBootstrap(channelHandler,group);
		}
		ChannelFuture channelFuture;
		Channel channel;
		channelFuture = bootstrap.connect(address);
		if (!channelFuture.await(connectionTimeout)) {
			throw new TimeoutException();
		}
		channel = channelFuture.channel();
		if (channel == null || !channel.isActive()) {
			throw new IllegalStateException();
		}
		channels.add(channel);
		return channel;

	}



	@Override
	public void close() {
		for (Channel channel : channels) {
			if(null != channel) {
				channel.close();
				channels.remove(channel);
			}
		}
		if (group != null) {
			group.shutdownGracefully();
		}
		inFlightRequests.close();
	}
	//这个是创建handler管道的方法
	private ChannelHandler newChannelHandlerPipeline() {
		return new ChannelInitializer<Channel>() {
			@Override
			protected void initChannel(Channel channel) {
				channel.pipeline()
						//响应解码器
						.addLast(new ResponseDecoder())
						//请求编码器
						.addLast(new RequestEncoder())
						//客户端请求handler
						.addLast(new ResponseInvocation(inFlightRequests));
			}
		};
	}
	//创建EventLoopGroup的方法
	private EventLoopGroup newGroup() {
		if (Epoll.isAvailable()) {
			return new EpollEventLoopGroup();
		} else {
			return new NioEventLoopGroup();
		}
	}
	//这个是创建启动类的方法，和之前的demo基本一致，其实就是使用netty的常见过程
	private Bootstrap newBootstrap(ChannelHandler channelHandler, EventLoopGroup group) {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class)
				.group(group)
				.handler(channelHandler)
				.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
		return bootstrap;
	}
}
