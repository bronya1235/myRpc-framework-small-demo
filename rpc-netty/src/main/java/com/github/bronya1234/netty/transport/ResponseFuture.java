package com.github.bronya1234.netty.transport;

import com.github.bronya1234.netty.transport.pojo.Command;

import java.util.concurrent.CompletableFuture;

/**
 * @Author: Bao
 * @Date: 2023/6/8-06-08-14:36
 * @Description com.github.bronya1234.netty.transport
 * @Function
 */
public class ResponseFuture {
	private final int requestId;
	private final CompletableFuture<Command> future;
	private final long timestamp;

	public ResponseFuture(int requestId, CompletableFuture<Command> future) {
		this.requestId = requestId;
		this.future = future;
		timestamp = System.nanoTime();
	}

	public int getRequestId() {
		return requestId;
	}

	public CompletableFuture<Command> getFuture() {
		return future;
	}

	long getTimestamp() {
		return timestamp;
	}
}
