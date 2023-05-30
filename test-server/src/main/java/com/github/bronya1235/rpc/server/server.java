package com.github.bronya1235.rpc.server;

import com.github.bronya1235.rpc.api.NameService;
import com.github.bronya1235.rpc.api.RpcAccessPoint;
import com.github.bronya1235.rpc.service.HelloService;
import com.github.bronya1235.rpc.spi.ServiceSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * @Author: Bao
 * @Date: 2023/5/29-05-29-22:04
 * @Description com.github.bronya1235.rpc.server
 * @Function
 */
public class server {
	private static final Logger logger = LoggerFactory.getLogger(server.class);

	public static void main(String[] args) throws Exception {
		//服务名
		String serviceName = HelloService.class.getCanonicalName();
		//临时文件存放目录
		File tmpdirFile = new File(System.getProperty("java.io.tmpdir"));
		//临时文件存放目录生成一个myRpc_name_service.data的文件目录
		File file = new File(tmpdirFile, "myRpc_name_service.data");
		//实现类的一个实例
		HelloService helloService = new HelloServiceImpl();
		logger.info("创建并启动RpcAccessPoint...");
		//采用一个spi的加载方式去获取RpcAccessPoint对象
		try (RpcAccessPoint rpcAccessPoint = ServiceSupport.load(RpcAccessPoint.class);
		     Closeable ignored = rpcAccessPoint.startServer()) {
			//在这个例子里，注册中心是存在本机中临时文件中的一个叫做myRpc_name_service.data的文件来作为注册中心的
			NameService nameService = rpcAccessPoint.getNameService(file.toURI());
			assert nameService!=null;
			logger.info("向RpcAccessPoint注册{}服务",serviceName);
			URI uri = rpcAccessPoint.addServiceProvider(helloService, HelloService.class);
			logger.info("向NameService注册{}服务,服务地址{}",serviceName,uri);
			nameService.registerService(serviceName, uri);
			logger.info("注册完成，开始提供服务，按任何键退出");
			System.in.read();
			logger.info("Bye!");
		}
	}
}
