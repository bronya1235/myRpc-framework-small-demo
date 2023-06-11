package com.github.bronya1235.netty.transport.pojo;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Bao
 * @Date: 2023/6/8-06-08-9:47
 * @Description com.github.bronya1234.netty.transport.pojo
 * @Function 枚举类，错误代码，对标http的响应状态码
 */
public enum Code {
	SUCCESS(0, "SUCCESS"),
	NO_PROVIDER(-2, "NO_PROVIDER"),
	UNKNOWN_ERROR(-1, "UNKNOWN_ERROR");

	private static Map<Integer, Code> codes = new HashMap<>();
	private int code;
	private String message;

	Code(int code, String message) {
		this.code=code;
		this.message=message;
	}
	public int getCode() {
		return code;
	}

	public String getMessage(Object... args) {
		if(args.length<1){
			return message;
		}
		return String.format(message, args);
	}
}
