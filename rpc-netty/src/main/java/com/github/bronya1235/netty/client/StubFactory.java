package com.github.bronya1235.netty.client;

import com.github.bronya1235.netty.transport.Transport;

/**
 * @Author: Bao
 * @Date: 2023/6/9-06-09-16:15
 * @Description com.github.bronya1234.netty.client
 * @Function
 */
public interface StubFactory {
	<T> T createStub(Transport transport, Class<T> serviceClass);
}
