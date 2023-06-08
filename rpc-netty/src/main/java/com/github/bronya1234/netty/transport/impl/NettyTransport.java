package com.github.bronya1234.netty.transport.impl;

import com.github.bronya1234.netty.transport.InFlightRequests;
import com.github.bronya1234.netty.transport.ResponseFuture;
import com.github.bronya1234.netty.transport.Transport;
import com.github.bronya1234.netty.transport.pojo.Command;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

import java.util.concurrent.CompletableFuture;

/**
 * @Author: Bao
 * @Date: 2023/6/8-06-08-10:58
 * @Description com.github.bronya1234.netty.transport.impl
 * @Function transport类的具体实现
 */
public class NettyTransport implements Transport {
	private final Channel channel;
	private final InFlightRequests inFlightRequests;

	public NettyTransport(Channel channel, InFlightRequests inFlightRequests) {
		this.channel = channel;
		this.inFlightRequests = inFlightRequests;
	}

	@Override
	public CompletableFuture<Command> send(Command request) {
		// 构建返回值
		CompletableFuture<Command> completableFuture = new CompletableFuture<>();
		try {
			// 将在途请求放到inFlightRequests中
			inFlightRequests.put(new ResponseFuture(request.getHeader().getRequestId(), completableFuture));
			// 发送命令，并添加了一个监听器，这个监听器的任务是如果不成功，就关闭completableFuture，以及channel
			channel.writeAndFlush(request).addListener((ChannelFutureListener) channelFuture -> {
				// 处理发送失败的情况
				if (!channelFuture.isSuccess()) {
					completableFuture.completeExceptionally(channelFuture.cause());
					channel.close();
				}
			});
		} catch (Throwable t) {
			// 处理发送异常
			inFlightRequests.remove(request.getHeader().getRequestId());
			completableFuture.completeExceptionally(t);
		}
		return completableFuture;
	}
}
