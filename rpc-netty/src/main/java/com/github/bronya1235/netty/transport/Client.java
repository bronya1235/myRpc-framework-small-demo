package com.github.bronya1235.netty.transport;

import java.io.Closeable;
import java.net.SocketAddress;
import java.util.concurrent.TimeoutException;

/**
 * @Author: Bao
 * @Date: 2023/6/8-06-08-10:19
 * @Description com.github.bronya1234.netty.transport
 * @Function 这个其实就是NettyClient，客户端启动类
 */
public interface Client extends Closeable {
	Transport createTransport(SocketAddress address, long connectionTimeout) throws InterruptedException, TimeoutException;
	@Override
	void close();
}
