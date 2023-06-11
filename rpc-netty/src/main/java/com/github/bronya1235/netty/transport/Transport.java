package com.github.bronya1235.netty.transport;

import com.github.bronya1235.netty.transport.pojo.Command;

import java.util.concurrent.CompletableFuture;

/**
 * @Author: Bao
 * @Date: 2023/6/7-06-07-21:25
 * @Description com.github.bronya1234.netty.transport
 * @Function
 */
public interface Transport {
	/**
	 * 传输模块就提供一个send发送方法
	 * @param request 请求数据(包括响应数据)
	 * @return 返回值是一个CompletableFuture<Command>，应该是对执行结果的一个封装
	 */
	CompletableFuture<Command> send(Command request);
}
