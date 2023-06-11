package com.github.bronya1235.netty.client.stub;

import com.github.bronya1235.netty.client.RequestIdUtil;
import com.github.bronya1235.netty.client.ServiceStub;
import com.github.bronya1235.netty.client.ServiceType;
import com.github.bronya1235.netty.serialize.SerializeUtil;
import com.github.bronya1235.netty.transport.Transport;
import com.github.bronya1235.netty.transport.pojo.Code;
import com.github.bronya1235.netty.transport.pojo.Command;
import com.github.bronya1235.netty.transport.pojo.Header;
import com.github.bronya1235.netty.transport.pojo.ResponseHeader;

import java.util.concurrent.ExecutionException;

/**
 * @Author: Bao
 * @Date: 2023/6/9-06-09-16:17
 * @Description com.github.bronya1234.netty.client.stub
 * @Function
 */
public abstract class  AbstractStub implements ServiceStub {
	protected Transport transport;//因为需要给子类调用的

	@Override
	public void setTransport(Transport transport) {
		this.transport = transport;
	}
	protected byte[] invokeRemote(RpcRequest rpcRequest){
		//主要是构建一个request，然后调用transport把它send出去
		Header header = new Header(RequestIdUtil.next(), 1, ServiceType.TYPE_RPC_REQUEST);
		byte [] payload = SerializeUtil.serialize(rpcRequest);
		Command requestCommand = new Command(header, payload);
		try {
			Command responseCommand = transport.send(requestCommand).get();
			ResponseHeader responseHeader = (ResponseHeader) responseCommand.getHeader();
			if(responseHeader.getCode() == Code.SUCCESS.getCode()) {
				return responseCommand.getPayload();
			} else {
				throw new Exception(responseHeader.getError());
			}

		} catch (ExecutionException e) {
			throw new RuntimeException(e.getCause());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
}
