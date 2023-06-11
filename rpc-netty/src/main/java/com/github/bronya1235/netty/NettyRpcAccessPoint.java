package com.github.bronya1235.netty;

import com.github.bronya1235.netty.client.StubFactory;
import com.github.bronya1235.netty.server.ServiceProviderCenter;
import com.github.bronya1235.netty.transport.Client;
import com.github.bronya1235.netty.transport.HandlerRegistryCenter;
import com.github.bronya1235.netty.transport.Server;
import com.github.bronya1235.netty.transport.Transport;
import com.github.bronya1235.rpc.api.RpcAccessPoint;
import com.github.bronya1235.rpc.spi.ServiceSupport;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

/**
 * @Author: Bao
 * @Date: 2023/6/9-06-09-21:42
 * @Description com.github.bronya1234.netty
 * @Function
 */
public class NettyRpcAccessPoint implements RpcAccessPoint {
	private final String host = "localhost";//ip地址
	private final int port = 9999;//端口号
	private final URI uri = URI.create("rpc://" + host + ":" + port);
	private Server server = null;
	private Client client = ServiceSupport.load(Client.class);//随着rpc对象启动直接加载
	//把uri与数据传输服务transport进行绑定，在调用桩的方法的时候，是靠transport进行数据传输的
	private final Map<URI, Transport> clientMap = new ConcurrentHashMap<>();
	//这个是创建桩的方法，具体的实现在client模块进行
	private final StubFactory stubFactory = ServiceSupport.load(StubFactory.class);
	//ServiceProviderCenter接口提供了一个添加服务提供者的方法，本质上是一个HashMap来存储的
	private final ServiceProviderCenter ServiceProviderCenter = ServiceSupport.load(ServiceProviderCenter.class);

	/**
	 *
	 * @param uri          远程服务地址,用户从nameService可以查到
	 * @param serviceClass 服务的接口类的class
	 * @param <T>
	 * @return
	 */
	@Override
	public <T> T getRemoteService(URI uri, Class<T> serviceClass) {
		Transport transport = clientMap.computeIfAbsent(uri, this::createTransport);
		return stubFactory.createStub(transport, serviceClass);
	}
	private Transport createTransport(URI uri) {
		try {
			return client.createTransport(new InetSocketAddress(uri.getHost(), uri.getPort()),30000L);
		} catch (InterruptedException | TimeoutException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public <T> URI addServiceProvider(T service, Class<T> serviceClass) {
		ServiceProviderCenter.addServiceProvider(serviceClass, service);
		return uri;
	}

	@Override
	public Closeable startServer() throws Exception {
		if (null == server) {
			server = ServiceSupport.load(Server.class);
			server.start(HandlerRegistryCenter.getInstance(), port);

		}
		return () -> {
			if(null != server) {
				server.stop();
			}
		};
	}

	@Override
	public void close() throws IOException {
		if(null != server) {
			server.stop();
		}
		client.close();
	}
}
