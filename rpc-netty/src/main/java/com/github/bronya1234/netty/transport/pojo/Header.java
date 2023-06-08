package com.github.bronya1234.netty.transport.pojo;

/**
 * @Author: Bao
 * @Date: 2023/6/7-06-07-21:31
 * @Description com.github.bronya1234.netty.transport.pojo
 * @Function 请求头部
 */
public class Header {
	private int requestId;//请求唯一标识id
	private int version;//版本号
	private int type;//请求类型
	//构造器
	public Header(int requestId, int version, int type) {
		this.requestId = requestId;
		this.version = version;
		this.type = type;
	}
	//get/set方法

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	//获取头部的长度，主要是用来计算整体的请求数据长度的
	//一个int数字，占用4个字节，所以Integer.BYTES==4
	public int length() {
		return Integer.BYTES + Integer.BYTES + Integer.BYTES;
	}
}
