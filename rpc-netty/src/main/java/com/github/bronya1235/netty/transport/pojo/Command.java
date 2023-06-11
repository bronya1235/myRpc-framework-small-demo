package com.github.bronya1235.netty.transport.pojo;

/**
 * @Author: Bao
 * @Date: 2023/6/7-06-07-21:26
 * @Description com.github.bronya1234.netty.transport.pojo
 * @Function 一个数据结构，封装了一个请求的请求头和请求数据
 */
public class Command {
	protected Header header;
	private byte[] payload;

	public Command(Header header, byte[] payload) {
		this.header = header;
		this.payload = payload;
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public byte[] getPayload() {
		return payload;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}
}
