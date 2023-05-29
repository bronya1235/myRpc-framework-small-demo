package com.github.bronya1235.rpc.api;

import java.io.Closeable;
import java.net.URI;


/**
 * @Author: Bao
 * @Date: 2023/5/29-05-29-20:52
 * @Description com.github.bronya1235.rpc
 * @Function rpc框架对外提供的服务接口
 */
public interface RpcAccessPoint extends Closeable{
	/*
	 * 分析：一个rpc框架需要提供哪些服务？
	 * 1、对于服务端，需要提供一个注册服务的接口，一个提供服务的接口
	 * 2、对于客户端，需要提供一个获取远程服务的引用，也就是桩，用来调用服务
	 * */
	/**
	 * 客户端获取远程服务的引用
	 *
	 * @param uri          远程服务地址
	 * @param serviceClass 服务的接口类的class
	 * @param <T>          服务接口的类型
	 * @return 远程服务引用
	 */
	<T> T getRemoteService(URI uri, Class<T> serviceClass);

	/**
	 * 服务端注册服务的实现实例
	 *
	 * @param service      实现实例
	 * @param serviceClass 服务的接口类的Class
	 * @param <T>          服务接口的类型
	 * @return 远程服务地址
	 */
	<T> URI addServiceProvider(T service, Class<T> serviceClass);

	/**
	 * 启动rpc框架，监听接口，开始提供远程服务
	 * @return 返回一个服务实例，用于程序停止的时候安全的关闭服务
	 * @throws Exception
	 */
	Closeable startServer() throws Exception;
}
