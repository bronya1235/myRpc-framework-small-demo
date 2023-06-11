package com.github.bronya1235.netty.server;

import com.github.bronya1235.netty.client.ServiceType;
import com.github.bronya1235.netty.client.stub.RpcRequest;
import com.github.bronya1235.netty.serialize.SerializeUtil;
import com.github.bronya1235.netty.transport.RequestHandler;
import com.github.bronya1235.netty.transport.pojo.Code;
import com.github.bronya1235.netty.transport.pojo.Command;
import com.github.bronya1235.netty.transport.pojo.Header;
import com.github.bronya1235.netty.transport.pojo.ResponseHeader;
import com.github.bronya1235.rpc.spi.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Bao
 * @Date: 2023/6/9-06-09-21:07
 * @Description com.github.bronya1234.netty.server
 * @Function
 */
@Singleton
public class ServerRequestHandler implements RequestHandler,ServiceProviderCenter {
	private static final Logger logger = LoggerFactory.getLogger(ServerRequestHandler.class);//日志
	private Map<String,Object> serviceProviderMap = new HashMap<>();
	@Override
	public Command handle(Command requestCommand) {
		Header header = requestCommand.getHeader();
		// 从payload中反序列化RpcRequest
		RpcRequest rpcRequest = SerializeUtil.parse(requestCommand.getPayload());
		try {
			// 查找所有已注册的服务提供方，寻找rpcRequest中需要的服务
			Object serviceProvider = serviceProviderMap.get(rpcRequest.getInterfaceFullName());
			if(serviceProvider != null) {
				// 找到服务提供者，利用Java反射机制调用服务的对应方法
				String arg = SerializeUtil.parse(rpcRequest.getSerializedArgs());
				Method method = serviceProvider.getClass().getMethod(rpcRequest.getMethodName(), String.class);
				String result = (String ) method.invoke(serviceProvider, arg);
				// 把结果封装成响应命令并返回
				return new Command(new ResponseHeader(header.getRequestId(), header.getVersion(), type()), SerializeUtil.serialize(result));
			}
			// 如果没找到，返回NO_PROVIDER错误响应。
			logger.warn("No service Provider of {}#{}(String)!", rpcRequest.getInterfaceFullName(), rpcRequest.getMethodName());
			return new Command(new ResponseHeader(header.getRequestId(), header.getVersion(), type(), Code.NO_PROVIDER.getCode(), "No provider!"), new byte[0]);
		} catch (Throwable t) {
			// 发生异常，返回UNKNOWN_ERROR错误响应。
			logger.warn("Exception: ", t);
			return new Command(new ResponseHeader(header.getRequestId(), header.getVersion(), type(), Code.UNKNOWN_ERROR.getCode(), t.getMessage()), new byte[0]);
		}
	}

	@Override
	public int type() {
		return ServiceType.TYPE_RPC_REQUEST;
	}

	@Override
	public synchronized <T> void addServiceProvider(Class<? extends T> serviceClass, T serviceProvider) {
		serviceProviderMap.put(serviceClass.getCanonicalName(), serviceProvider);
		logger.info("Add service: {}, provider: {}.",
				serviceClass.getCanonicalName(),
				serviceProvider.getClass().getCanonicalName());
	}
}
