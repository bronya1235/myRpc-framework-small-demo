package com.github.bronya1235.netty.transport.impl;

import com.github.bronya1235.netty.transport.HandlerRegistryCenter;
import com.github.bronya1235.netty.transport.RequestHandler;
import com.github.bronya1235.netty.transport.pojo.Command;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Bao
 * @Date: 2023/6/8-06-08-23:14
 * @Description com.github.bronya1234.netty.transport.impl
 * @Function
 */
@ChannelHandler.Sharable
public class RequestInvocation extends SimpleChannelInboundHandler<Command> {
	private static final Logger logger = LoggerFactory.getLogger(RequestInvocation.class);
	private HandlerRegistryCenter handlerRegistryCenter;
	//构造器，同一个包里面才能访问
	RequestInvocation(HandlerRegistryCenter handlerRegistryCenter) {
		this.handlerRegistryCenter = handlerRegistryCenter;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, Command command) throws Exception {
		RequestHandler handler = handlerRegistryCenter.getHandler(command.getHeader().getType());
		if(null != handler) {
			//调用处理器的处理方法
			Command response = handler.handle(command);
			if(null != response) {
				//这边写出数据，并创建一个监听器，一旦返回失败，就关闭通道
				channelHandlerContext.writeAndFlush(response).addListener((ChannelFutureListener) channelFuture -> {
					if (!channelFuture.isSuccess()) {
						logger.warn("Write response failed!", channelFuture.cause());
						channelHandlerContext.channel().close();
					}
				});
			} else {
				logger.warn("Response is null!");
			}
		} else {
			throw new Exception(String.format("No handler for request with type: %d!", command.getHeader().getType()));
		}
	}
}
