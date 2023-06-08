package com.github.bronya1234.netty.transport.impl;

import com.github.bronya1234.netty.transport.pojo.Command;
import com.github.bronya1234.netty.transport.pojo.Header;
import com.sun.org.apache.bcel.internal.classfile.Unknown;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author: Bao
 * @Date: 2023/6/8-06-08-16:10
 * @Description com.github.bronya1234.netty.transport.impl
 * @Function
 */
public abstract class CommandEncoder extends MessageToByteEncoder {
	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
		//先做一个判断o是不是Command类型
		if(!(o instanceof Command)){
			//类型不匹配，抛一个异常
			throw  new Exception(String.format("Unknown type:%s!", o.getClass().getCanonicalName()));
		}
		//类型匹配的话就进行编码
		Command command = (Command) o;//类型强转
		//写入一个数，记录总长度+4，这个4其实就是这个长度int数据本身占用了4个字节
		byteBuf.writeInt(Integer.BYTES + command.getHeader().length() + command.getPayload().length);
		//写入头部
		encodeHeader(channelHandlerContext, command.getHeader(), byteBuf);
		//写入实体数据
		byteBuf.writeBytes(command.getPayload());
	}
	//写入头部，request就直接用下面的方法写
	protected void encodeHeader(ChannelHandlerContext channelHandlerContext, Header header, ByteBuf byteBuf) throws Exception {
		byteBuf.writeInt(header.getRequestId());
		byteBuf.writeInt(header.getVersion());
		byteBuf.writeInt(header.getType());
	}
}
