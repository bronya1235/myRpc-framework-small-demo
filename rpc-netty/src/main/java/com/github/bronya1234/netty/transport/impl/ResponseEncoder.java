package com.github.bronya1234.netty.transport.impl;

import com.github.bronya1234.netty.transport.pojo.Header;
import com.github.bronya1234.netty.transport.pojo.ResponseHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;

/**
 * @Author: Bao
 * @Date: 2023/6/8-06-08-22:41
 * @Description com.github.bronya1234.netty.transport.impl
 * @Function
 */
public class ResponseEncoder extends CommandEncoder{
	@Override
	protected void encodeHeader(ChannelHandlerContext channelHandlerContext, Header header, ByteBuf byteBuf) throws Exception {
		//调一下父类的方法，把type，version，requestId写了
		super.encodeHeader(channelHandlerContext, header, byteBuf);
		//然后还要处理Response响应特有的数据
		//先判断是不是ResponseHeader，因为到时候是使用父类的，不会直接使用具体的子类
		if(header instanceof ResponseHeader){
			//强转
			ResponseHeader responseHeader = (ResponseHeader) header;
			//code写入
			byteBuf.writeInt(responseHeader.getCode());
			//写入error，两步走，先写入error的长度，再写入数据
			//计算error的长度
			int errorLength = responseHeader.length()-(Integer.BYTES + Integer.BYTES + Integer.BYTES + Integer.BYTES +
					Integer.BYTES);
			//写入
			byteBuf.writeInt(errorLength);
			//写入数据，字符传转byte即可
			byteBuf.writeBytes(errorLength==0?new byte[0]:responseHeader.getError().getBytes(StandardCharsets.UTF_8));
		}else {
			throw new Exception(String.format("Invalid header type: %s!", header.getClass().getCanonicalName()));
		}
	}
}
