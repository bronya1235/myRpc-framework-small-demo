package com.github.bronya1234.netty.serialize.impl;

import com.github.bronya1234.netty.serialize.Serializer;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @Author: Bao
 * @Date: 2023/6/6-06-06-15:34
 * @Description com.github.bronya1234.netty.serialize.impl
 * @Function 字符串序列化器
 */
public class StringSerializer implements Serializer<String>, Types {

	@Override
	public int size(String entry) {
		//注意，一定要指定字符集，避免由于系统不同导致的字符集不同的问题
		return entry.getBytes(StandardCharsets.UTF_8).length;
	}

	@Override
	public void serialize(String entry, byte[] bytes, int offset, int length) {
		byte[] entryBytes = entry.getBytes(StandardCharsets.UTF_8);
		System.arraycopy(entryBytes, 0, bytes, offset, entryBytes.length);
	}

	@Override
	public String parse(byte[] bytes, int offset, int length) {
		return new String(bytes, offset, length, StandardCharsets.UTF_8);
	}

	@Override
	public byte type() {
		return TYPE_STRING;
	}

	@Override
	public Class<String> getSerializeClass() {
		return String.class;
	}
}
