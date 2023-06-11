package com.github.bronya1235.netty.transport.impl;

import com.github.bronya1235.netty.transport.InFlightRequests;
import com.github.bronya1235.netty.transport.ResponseFuture;
import com.github.bronya1235.netty.transport.pojo.Command;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Bao
 * @Date: 2023/6/8-06-08-23:42
 * @Description com.github.bronya1234.netty.transport.impl
 * @Function
 */
@ChannelHandler.Sharable
public class ResponseInvocation extends SimpleChannelInboundHandler<Command> {
	private static final Logger logger = LoggerFactory.getLogger(ResponseInvocation.class);
	private final InFlightRequests inFlightRequests;

	ResponseInvocation(InFlightRequests inFlightRequests) {
		this.inFlightRequests = inFlightRequests;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, Command command) throws Exception {
		//把在途请求表里的那个请求给移除了
		ResponseFuture future = inFlightRequests.remove(command.getHeader().getRequestId());
		if(null != future) {
			future.getFuture().complete(command);
		} else {
			logger.warn("Drop response: {}", command);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.warn("Exception: ", cause);
		super.exceptionCaught(ctx, cause);
		Channel channel = ctx.channel();
		if(channel.isActive()){
			ctx.close();
		}
	}
}
