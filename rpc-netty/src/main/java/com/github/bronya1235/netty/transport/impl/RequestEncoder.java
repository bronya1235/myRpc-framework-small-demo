package com.github.bronya1235.netty.transport.impl;

import com.github.bronya1235.netty.transport.pojo.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Author: Bao
 * @Date: 2023/6/8-06-08-16:33
 * @Description com.github.bronya1234.netty.transport.impl
 * @Function
 */
public class RequestEncoder extends CommandEncoder{
	//request请求直接写
	@Override
	protected void encodeHeader(ChannelHandlerContext channelHandlerContext, Header header, ByteBuf byteBuf) throws Exception {
		super.encodeHeader(channelHandlerContext, header, byteBuf);
	}
}
