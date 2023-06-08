package com.github.bronya1234.netty.transport;

import java.io.Closeable;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @Author: Bao
 * @Date: 2023/6/8-06-08-14:34
 * @Description com.github.bronya1234.netty.transport
 * @Function 在途请求容器，把许多多线程请求用一个容器进行封装，考虑了连接超时以及控制连接数量的问题
 */
public class InFlightRequests implements Closeable {
	private final static long TIMEOUT_SEC = 10L;//超时时间
	private final Semaphore semaphore = new Semaphore(10);//信号量，控制请求数量的
	//map，key是请求的id，value是一个responseFuture
	private final Map<Integer, ResponseFuture> futureMap = new ConcurrentHashMap<>();
	//周期性任务线程池
	private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
	//周期性任务返回结果
	private final ScheduledFuture scheduledFuture;
	//构造器初始化了一个周期性任务
	public InFlightRequests() {
		scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(this::removeTimeoutFutures, TIMEOUT_SEC, TIMEOUT_SEC, TimeUnit.SECONDS);
	}
	//周期性任务的具体任务细节
	private void removeTimeoutFutures() {
		futureMap.entrySet().removeIf(entry -> {
			//当前时间戳减去之间创建连接请求的时候的时间戳，是否大于超时时间
			if( System.nanoTime() - entry.getValue().getTimestamp() > TIMEOUT_SEC * 1000000000L) {
				semaphore.release();
				return true;
			} else {
				return false;
			}
		});
	}
	//把responseFuture放入在途请求的方法，需要去获取一个信号量，如果没有获取到就会提示超时
	public void put(ResponseFuture responseFuture) throws InterruptedException, TimeoutException {
		if(semaphore.tryAcquire(TIMEOUT_SEC, TimeUnit.SECONDS)) {
			futureMap.put(responseFuture.getRequestId(), responseFuture);
		} else {
			throw new TimeoutException();
		}
	}

	//把一个ResponseFuture移除的方法，需要释放这个信号量
	public ResponseFuture remove(int requestId) {
		//移除这个future，并且获取到这个被删除的future
		ResponseFuture future = futureMap.remove(requestId);
		//如果确实被删除了，需要释放信号量
		if(null != future) {
			semaphore.release();
		}
		return future;
	}

	@Override
	public void close() {
		//关闭任务以及线程池
		scheduledFuture.cancel(true);
		scheduledExecutorService.shutdown();
	}
}
