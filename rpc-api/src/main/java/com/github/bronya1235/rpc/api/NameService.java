package com.github.bronya1235.rpc.api;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Collection;

/**
 * @Author: Bao
 * @Date: 2023/5/29-05-29-21:13
 * @Description com.github.bronya1235.rpc
 * @Function 注册中心。对于客户端而言，他应该只知道服务的名称，然后通过服务的名称去查询到远程服务的地址
 */
public interface NameService {
	/*分析：接口应该主要提供两个方法
	 * 1、用于客户端查询服务的URI
	 * 2、用于服务器注册URI
	 * */

	/**
	 * 客户端查询服务地址
	 *
	 * @param serviceName 服务名称
	 * @return 服务地址
	 */
	URI lookupService(String serviceName) throws IOException;

	/**
	 * 服务器注册服务
	 *
	 * @param serviceName 服务名
	 * @param uri         服务地址
	 * @throws IOException
	 */
	void registerService(String serviceName, URI uri) throws IOException;

	/**
	 * 获取支持的协议
	 *
	 * @return 返回一个所支持协议的列表
	 */
	Collection<String> supportedSchemes();

	/**
	 * 连接注册中心
	 *
	 * @param nameServiceUri 注册中心的地址
	 */
	void connect(URI nameServiceUri);
}
