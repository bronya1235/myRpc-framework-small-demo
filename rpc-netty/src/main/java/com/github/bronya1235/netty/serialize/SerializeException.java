package com.github.bronya1235.netty.serialize;

/**
 * @Author: Bao
 * @Date: 2023/6/6-06-06-15:17
 * @Description com.github.bronya1234.netty.serialize
 * @Function 序列化异常
 */

public class SerializeException extends RuntimeException {
	public SerializeException(String msg) {
		super(msg);
	}

	public SerializeException(Throwable throwable) {
		super(throwable);
	}
}
