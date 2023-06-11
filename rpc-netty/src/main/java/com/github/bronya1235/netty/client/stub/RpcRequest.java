package com.github.bronya1235.netty.client.stub;

/**
 * @Author: Bao
 * @Date: 2023/6/9-06-09-20:20
 * @Description com.github.bronya1234.netty.client.stub
 * @Function
 */
public class RpcRequest {
	private final String interfaceFullName;
	private final String methodName;
	private final byte [] serializedArgs;

	public RpcRequest(String interfaceFullName, String methodName, byte[] serializedArgs) {
		this.interfaceFullName = interfaceFullName;
		this.methodName = methodName;
		this.serializedArgs = serializedArgs;
	}

	public String getInterfaceFullName() {
		return interfaceFullName;
	}

	public String getMethodName() {
		return methodName;
	}

	public byte[] getSerializedArgs() {
		return serializedArgs;
	}
}
