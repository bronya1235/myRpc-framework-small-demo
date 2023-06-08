package com.github.bronya1234.netty.serialize;

/**
 * @Author: Bao
 * @Date: 2023/6/6-06-06-13:56
 * @Description com.github.bronya1234.netty.serialize
 * @Function 提供一个序列化接口
 */
public interface Serializer<T> {
	/**
	 * 计算对象序列化以后的长度
	 *
	 * @param entry 待序列化的对象
	 * @return 序列化以后的长度
	 */
	int size(T entry);

	/**
	 * 序列化方法。将对象序列化为一个byte类型的数组
	 *
	 * @param entry  待序列化对象
	 * @param bytes  存放序列化结果的字节数组
	 * @param offset 偏移量，也就是数组的起始位置
	 * @param length 序列化以后的长度，也就是{@link Serializer#size(java.lang.Object)}方法的返回值
	 */
	void serialize(T entry, byte[] bytes, int offset, int length);

	/**
	 * 对象反序列化方法
	 *
	 * @param bytes  对象序列化后的字节数组
	 * @param offset 偏移量，也就是数组的起始位置
	 * @param length 序列化以后的长度，也就是{@link Serializer#size(java.lang.Object)}方法的返回值
	 * @return 反序列化后生成的对象
	 */
	T parse(byte[] bytes, int offset, int length);

	/**
	 * 用一个字节对象标注对象的类型
	 */
	byte type();

	/**
	 * 返回带序列化对象的Class对象。
	 */
	Class<T> getSerializeClass();
}
