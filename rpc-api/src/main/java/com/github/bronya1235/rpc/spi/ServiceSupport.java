package com.github.bronya1235.rpc.spi;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @Author: Bao
 * @Date: 2023/5/30-05-30-9:22
 * @Description com.github.bronya1235.rpc.spi
 * @Function SPI类加载器的一个工具类
 */
public class ServiceSupport {
	private static final Map<String,Object> singletonServices = new HashMap<>();
	public synchronized static <S> S load(Class<S> service) {
		return StreamSupport.stream(ServiceLoader.load(service).spliterator(), false)
				.map(ServiceSupport::singletonFilter)
				.findFirst().orElseThrow(ServiceLoadException::new);
	}
	public synchronized static <S> Collection<S> loadAll(Class<S> service) {
		return StreamSupport.stream(ServiceLoader.load(service).spliterator(), false)
				.map(ServiceSupport::singletonFilter).collect(Collectors.toList());
	}
	@SuppressWarnings("unchecked")
	private static <S> S singletonFilter(S service){
		if(service.getClass().isAnnotationPresent(Singleton.class)) {
			//如果存在Singleton注解
			String className = service.getClass().getCanonicalName();
			//单例模式的核心，如果已经存在map里面了，返回值就是map里的v，如果没有存进去，那返回值就是null
			Object singletonInstance = singletonServices.putIfAbsent(className, service);
			//如果singletonInstance是null，说明map里面不存在，直接返回service，如果返回值不为null，说明
			//map里已经存在这个类的实例了，那就返回这个实例
			return singletonInstance == null ? service : (S) singletonInstance;
		} else {
			//如果不存在Singleton注解，那就直接返回即可
			return service;
		}
	}
}
