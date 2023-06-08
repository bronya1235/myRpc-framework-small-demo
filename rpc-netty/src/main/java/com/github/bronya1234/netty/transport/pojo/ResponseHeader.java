package com.github.bronya1234.netty.transport.pojo;

import java.nio.charset.StandardCharsets;

/**
 * @Author: Bao
 * @Date: 2023/6/7-06-07-21:40
 * @Description com.github.bronya1234.netty.transport.pojo
 * @Function 响应头部，比请求头多两个参数，一个响应状态码，一个是错误代码
 */
public class ResponseHeader extends Header{
	private int code;
	private String error;

	public ResponseHeader(int requestId, int version, int type) {
		this(requestId,version,type,Code.SUCCESS.getCode(),null);
	}

	public ResponseHeader(int requestId, int version, int type,Throwable throwable) {
		this(requestId,version,type,Code.UNKNOWN_ERROR.getCode(), throwable.getMessage());
	}

	public ResponseHeader(int requestId, int version, int type, int code, String error) {
		super(requestId, version, type);
		this.code = code;
		this.error = error;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	//计算长度，4个位用来表示一个参数，如果有错误，还需要拼接上错误代码
	@Override
	public int length() {
		return Integer.BYTES + Integer.BYTES + Integer.BYTES + Integer.BYTES +
				Integer.BYTES +
				(error == null ? 0 : error.getBytes(StandardCharsets.UTF_8).length);
	}
}
