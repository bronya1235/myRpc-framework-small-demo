package com.github.bronya1234.netty.transport.impl;

import com.github.bronya1234.netty.transport.pojo.Header;
import com.github.bronya1234.netty.transport.pojo.ResponseHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;

/**
 * @Author: Bao
 * @Date: 2023/6/8-06-08-23:06
 * @Description com.github.bronya1234.netty.transport.impl
 * @Function
 */
public class ResponseDecoder extends CommandDecoder{
	@Override
	protected Header decodeHeader(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
		int requestId = byteBuf.readInt();
		int version = byteBuf.readInt();
		int type = byteBuf.readInt();
		int code = byteBuf.readInt();
		int errorLength = byteBuf.readInt();
		byte[] errorBytes = new byte[errorLength];
		byteBuf.readBytes(errorBytes);
		return new ResponseHeader(
				requestId, version, type, code, new String(errorBytes, StandardCharsets.UTF_8));
	}
}
