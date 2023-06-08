package com.github.bronya1234.netty.transport.impl;

import com.github.bronya1234.netty.transport.pojo.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Author: Bao
 * @Date: 2023/6/8-06-08-16:33
 * @Description com.github.bronya1234.netty.transport.impl
 * @Function
 */
public class RequestDecoder extends CommandDecoder{
	//如果是一个请求，那头部直接读取就行了，注意和编码的时候保持一致性
	@Override
	protected Header decodeHeader(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
		return new Header(
				byteBuf.readInt(),
				byteBuf.readInt(),
				byteBuf.readInt()
		);
	}
}
