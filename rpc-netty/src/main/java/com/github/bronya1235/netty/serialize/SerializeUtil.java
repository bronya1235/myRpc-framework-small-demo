package com.github.bronya1235.netty.serialize;

import com.github.bronya1235.rpc.spi.ServiceSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Bao
 * @Date: 2023/6/6-06-06-14:12
 * @Description com.github.bronya1234.netty.serialize
 * @Function 封装了序列化的方法
 */
public class SerializeUtil {
	//日志记录
	public static final Logger logger = LoggerFactory.getLogger(SerializeUtil.class);
	//根据对象类型，找到具体的对象序列化实现类
	private static Map<Class<?>, Serializer<?>> serializerMap = new HashMap<>();
	//根据序列化实现类型，找到序列化对象类型
	private static Map<Byte, Class<?>> typeMap = new HashMap<>();

	//在类加载的时候，把所有的Serializer都加载进来，需要使用spi加载的方式，并把所有的序列化类都存在一个map里面
	static {
		for (Serializer serializer : ServiceSupport.loadAll(Serializer.class)) {
			registerType(serializer.type(), serializer.getSerializeClass(), serializer);
			logger.info("Found serializer, class: {}, type: {}.",
					serializer.getSerializeClass().getCanonicalName(),
					serializer.type());
		}

	}

	private static <E> void registerType(byte type, Class<E> eClass, Serializer<E> serializer) {
		serializerMap.put(eClass, serializer);
		typeMap.put(type, eClass);
	}

	//序列化方法
	public static <E> byte[] serialize(E entry) {
		@SuppressWarnings("unchecked")
		Serializer<E> serializer = (Serializer<E>) serializerMap.get(entry.getClass());
		if (serializer == null) {
			throw new SerializeException(String.format("Unknown entry class type: %s", entry.getClass().toString()));
		}
		//0位置存放type类型
		byte[] bytes = new byte[serializer.size(entry) + 1];
		bytes[0] = serializer.type();
		serializer.serialize(entry, bytes, 1, bytes.length - 1);
		return bytes;
	}

	//反序列化方法，四步走
	//1、对于返回的buffer数组，很自然直接把数组给入
	public static <E> E parse(byte[] buffer) {
		return parse(buffer, 0, buffer.length);
	}
	//2、从数组里面获取到必要的信息，并做一个判断
	private static <E> E parse(byte[] buffer, int offset, int length) {
		byte type = parseEntryType(buffer);
		@SuppressWarnings("unchecked")
		Class<E> eClass = (Class<E>) typeMap.get(type);
		if (null == eClass) {
			throw new SerializeException(String.format("Unknown entry type: %d!", type));
		} else {
			return parse(buffer, offset + 1, length - 1, eClass);
		}

	}
	private static byte parseEntryType(byte[] buffer) {
		return buffer[0];
	}
	//这一步才是实际调用parse方法的地方
	@SuppressWarnings("unchecked")
	private static <E> E parse(byte[] buffer, int offset, int length, Class<E> eClass) {
		//这里才是实际去调用了序列化器的解析方法
		Object entry = serializerMap.get(eClass).parse(buffer, offset, length);
		if (eClass.isAssignableFrom(entry.getClass())) {
			return (E) entry;
		} else {
			throw new SerializeException("Type mismatch!");
		}
	}


}
