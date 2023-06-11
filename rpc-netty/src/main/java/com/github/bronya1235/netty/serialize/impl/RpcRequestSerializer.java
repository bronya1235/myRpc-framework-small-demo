package com.github.bronya1235.netty.serialize.impl;

import com.github.bronya1235.netty.client.stub.RpcRequest;
import com.github.bronya1235.netty.serialize.Serializer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @Author: Bao
 * @Date: 2023/6/9-06-09-23:19
 * @Description com.github.bronya1234.netty.serialize.impl
 * @Function
 */
public class RpcRequestSerializer implements Serializer<RpcRequest> {
	@Override
	public int size(RpcRequest entry) {
		return Integer.BYTES+entry.getInterfaceFullName().getBytes(StandardCharsets.UTF_8).length
				+Integer.BYTES+entry.getMethodName().getBytes(StandardCharsets.UTF_8).length
				+Integer.BYTES+entry.getSerializedArgs().length;
	}

	@Override
	public void serialize(RpcRequest entry, byte[] bytes, int offset, int length) {
		ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
		byte[] tempBytes = entry.getInterfaceFullName().getBytes(StandardCharsets.UTF_8);
		buffer.putInt(tempBytes.length);
		buffer.put(tempBytes);

		tempBytes = entry.getMethodName().getBytes(StandardCharsets.UTF_8);
		buffer.putInt(tempBytes.length);
		buffer.put(tempBytes);

		tempBytes = entry.getSerializedArgs();
		buffer.putInt(tempBytes.length);
		buffer.put(tempBytes);

	}

	@Override
	public RpcRequest parse(byte[] bytes, int offset, int length) {
		ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
		int len = buffer.getInt();
		byte[] tempBytes = new byte[len];
		buffer.get(tempBytes);
		String interfaceFullName = new String(tempBytes, StandardCharsets.UTF_8);

		len = buffer.getInt();
		tempBytes = new byte[len];
		buffer.get(tempBytes);
		String methodName = new String(tempBytes, StandardCharsets.UTF_8);

		len = buffer.getInt();
		tempBytes = new byte[len];
		buffer.get(tempBytes);
		byte[] serializedArgs = tempBytes;
		return  new RpcRequest(interfaceFullName, methodName, serializedArgs);
	}

	@Override
	public byte type() {
		return Types.TYPE_RPC_REQUEST;
	}

	@Override
	public Class<RpcRequest> getSerializeClass() {
		return RpcRequest.class;
	}
}
