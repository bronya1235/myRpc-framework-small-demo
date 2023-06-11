package com.github.bronya1235.netty.server;

/**
 * @Author: Bao
 * @Date: 2023/6/9-06-09-21:04
 * @Description com.github.bronya1234.netty.server
 * @Function
 */
public interface ServiceProviderCenter {
	<T> void addServiceProvider(Class<? extends T> serviceClass, T serviceProvider);
}
