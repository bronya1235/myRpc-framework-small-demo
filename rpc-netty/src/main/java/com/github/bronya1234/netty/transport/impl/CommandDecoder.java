package com.github.bronya1234.netty.transport.impl;

import com.github.bronya1234.netty.transport.pojo.Command;
import com.github.bronya1234.netty.transport.pojo.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @Author: Bao
 * @Date: 2023/6/8-06-08-16:10
 * @Description com.github.bronya1234.netty.transport.impl
 * @Function
 */
public abstract class CommandDecoder extends ByteToMessageDecoder {
	private static final int LENGTH_FIELD_LENGTH = Integer.BYTES;//4
	@Override
	protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
		if (!byteBuf.isReadable(LENGTH_FIELD_LENGTH)) {
			return;
		}
		byteBuf.markReaderIndex();//标记一下index的初始位置
		int length = byteBuf.readInt() - LENGTH_FIELD_LENGTH;//读一个int的长度，然后减去4，就是整个Command的长度
		//如果缓冲区可读的byte数比length还小，说明一定发生了错误，或者还没写进来，就重置
		if (byteBuf.readableBytes() < length) {
			byteBuf.resetReaderIndex();
			return;
		}
		//到这里说明有数据，就进行解码
		//先对头部进行解码，头部有请求头和响应头，需要实现不同的解码方法
		Header header = decodeHeader(channelHandlerContext, byteBuf);
		//总长度length-头部长度，就是数据的长度了
		int payloadLength  = length - header.length();
		byte [] payload = new byte[payloadLength];
		byteBuf.readBytes(payload);
		list.add(new Command(header, payload));
	}
	protected abstract Header decodeHeader(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) ;
}
