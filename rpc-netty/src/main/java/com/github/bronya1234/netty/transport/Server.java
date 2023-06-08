package com.github.bronya1234.netty.transport;

/**
 * @Author: Bao
 * @Date: 2023/6/8-06-08-10:19
 * @Description com.github.bronya1234.netty.transport
 * @Function
 */
public interface Server {
	void start(HandlerRegistryCenter handlerRegistryCenter, int port) throws Exception;
	void stop();
}
