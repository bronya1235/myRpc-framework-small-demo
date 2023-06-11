package com.github.bronya1235.netty.client;

import com.github.bronya1235.netty.transport.Transport;
import com.itranswarp.compiler.JavaStringCompiler;

import java.util.Map;

/**
 * @Author: Bao
 * @Date: 2023/6/9-06-09-19:51
 * @Description com.github.bronya1234.netty.client.stub
 * @Function
 */
public class DynamicStubFactory implements StubFactory {
	private final static String STUB_SOURCE_TEMPLATE =
			//包
			"package com.github.bronya1235.netty.client.stub;\n" +
					//导包
					"import com.github.bronya1235.netty.serialize.SerializeUtil;\n" +
					"\n" +
					//类
					"public class %s extends AbstractStub implements %s {\n" +
					"    @Override\n" +
						//方法
					"    public String %s(String arg) {\n" +
					"        return SerializeUtil.parse(\n" +
					"                invokeRemote(\n" +
					"                        new RpcRequest(\n" +
					"                                \"%s\",\n" +
					"                                \"%s\",\n" +
					"                                SerializeUtil.serialize(arg)\n" +
					"                        )\n" +
					"                )\n" +
					"        );\n" +
					"    }\n" +
					"}";

	@Override
	public <T> T createStub(Transport transport, Class<T> serviceClass) {
		try {
			// 填充模板
			//桩名字=接口的简单名字拼接stub
			String stubSimpleName = serviceClass.getSimpleName() + "Stub";
			//接口的全限定名
			String classFullName = serviceClass.getName();
			//桩的全限定名
			String stubFullName = "com.github.bronya1235.netty.client.stub." + stubSimpleName;
			//方法名
			String methodName = serviceClass.getMethods()[0].getName();

			String source = String.format(STUB_SOURCE_TEMPLATE, stubSimpleName, classFullName, methodName, classFullName, methodName);
			// 编译源代码
			JavaStringCompiler compiler = new JavaStringCompiler();
			Map<String, byte[]> results = compiler.compile(stubSimpleName + ".java", source);
			// 加载编译好的类
			Class<?> clazz = compiler.loadClass(stubFullName, results);

			// 把Transport赋值给桩
			ServiceStub stubInstance = (ServiceStub) clazz.newInstance();
			stubInstance.setTransport(transport);
			// 返回这个桩
			return (T) stubInstance;
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}
}
