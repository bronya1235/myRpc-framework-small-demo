package com.github.bronya1235.rpc.server;

import com.github.bronya1235.rpc.service.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Bao
 * @Date: 2023/5/29-05-29-21:51
 * @Description com.github.bronya1235.rpc.server
 * @Function
 */
public class HelloServiceImpl implements HelloService {
	private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);
	@Override
	public String hello(String name) {
		logger.info("HelloServiceImpl收到：{}.",name);
		String ret ="hello, "+name;
		logger.info("HelloServiceImpl返回：{}.",ret);
		return ret;
	}
}
