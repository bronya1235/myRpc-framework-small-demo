package com.github.bronya1235.netty.transport;

import com.github.bronya1235.netty.transport.pojo.Command;

/**
 * @Author: Bao
 * @Date: 2023/6/8-06-08-10:28
 * @Description com.github.bronya1234.netty.transport
 * @Function 各类请求处理器的主要接口，提供一个handle方法
 */
public interface RequestHandler {
	/**
	 * 处理请求
	 * @param requestCommand 请求命令
	 * @return 响应命令
	 */
	Command handle(Command requestCommand);

	/**
	 * 支持的请求类型
	 */
	int type();
}
