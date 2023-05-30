package com.github.bronya1235.rpc;

import com.github.bronya1235.rpc.api.NameService;
import com.github.bronya1235.rpc.api.RpcAccessPoint;
import com.github.bronya1235.rpc.service.HelloService;
import com.github.bronya1235.rpc.spi.ServiceSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * @Author: Bao
 * @Date: 2023/5/30-05-30-10:32
 * @Description com.github.bronya1235.rpc
 * @Function
 */
public class Client {
	private static final Logger logger = LoggerFactory.getLogger(Client.class);

	public static void main(String[] args) throws IOException {
		String serviceName = HelloService.class.getCanonicalName();
		//查询注册中心用的
		File tmpDirFile = new File(System.getProperty("java.io.tmpdir"));
		File file = new File(tmpDirFile, "simple_rpc_name_service.data");
		//调用方法的参数
		String name = "高紫越";
		//同样使用spi调用
		try(RpcAccessPoint rpcAccessPoint = ServiceSupport.load(RpcAccessPoint.class)) {
			NameService nameService = rpcAccessPoint.getNameService(file.toURI());
			//断言注册中心不为空，然后通过服务名，找到远程调用地址
			assert nameService != null;
			URI uri = nameService.lookupService(serviceName);
			//断言远程调用地址不为空，通过地址找到具体的服务
			assert uri != null;
			logger.info("找到服务{}，提供者: {}.", serviceName, uri);
			//返回一个远程方法的实例，实际上是一个代理类，称为桩
			HelloService helloService = rpcAccessPoint.getRemoteService(uri, HelloService.class);
			logger.info("请求服务, name: {}...", name);
			String response = helloService.hello(name);
			logger.info("收到响应: {}.", response);
		}
	}
}
