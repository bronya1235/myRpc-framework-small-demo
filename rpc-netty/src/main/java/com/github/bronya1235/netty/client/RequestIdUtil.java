package com.github.bronya1235.netty.client;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: Bao
 * @Date: 2023/6/9-06-09-20:30
 * @Description com.github.bronya1234.netty.client
 * @Function 原子类，返回一个请求id，是并发安全的，并且id可以自增长
 */
public class RequestIdUtil {
	private final static AtomicInteger nextRequestId = new AtomicInteger(0);
	public static int next() {
		return nextRequestId.getAndIncrement();
	}
}
