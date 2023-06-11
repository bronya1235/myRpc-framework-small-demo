package com.github.bronya1235.netty.client;

import com.github.bronya1235.netty.transport.Transport;

/**
 * @Author: Bao
 * @Date: 2023/6/9-06-09-20:15
 * @Description com.github.bronya1234.netty.client
 * @Function
 */
public interface ServiceStub {
	void setTransport(Transport transport);
}
