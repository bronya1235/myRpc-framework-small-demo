package com.github.bronya1234.netty.transport;

import com.github.bronya1235.rpc.spi.ServiceSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Bao
 * @Date: 2023/6/8-06-08-10:43
 * @Description com.github.bronya1234.netty.transport
 * @Function
 */
public class HandlerRegistryCenter {
	private static final Logger logger = LoggerFactory.getLogger(HandlerRegistryCenter.class);
	//handler表
	private Map<Integer,RequestHandler> handlerMap = new HashMap<>();
	//单例模式，每个类保存一个实例
	private static volatile HandlerRegistryCenter instance = null;
	//双重判定的懒汉式单例模式加载，解决并发同步问题
	public static HandlerRegistryCenter getInstance() {
		if (null == instance) {
			synchronized (HandlerRegistryCenter.class){
				if (instance==null){
					instance = new HandlerRegistryCenter();
				}
			}
		}
		return instance;
	}
	//构造器，私有
	private HandlerRegistryCenter(){
		Collection<RequestHandler> requestHandlers = ServiceSupport.loadAll(RequestHandler.class);
		for (RequestHandler requestHandler : requestHandlers) {
			handlerMap.put(requestHandler.type(), requestHandler);
			logger.info("Load request handler, type: {}, class: {}.", requestHandler.type(), requestHandler.getClass().getCanonicalName());
		}
	}
	//根据类型返回处理器
	public RequestHandler getHandler(int type){
		return handlerMap.get(type);
	}

}
