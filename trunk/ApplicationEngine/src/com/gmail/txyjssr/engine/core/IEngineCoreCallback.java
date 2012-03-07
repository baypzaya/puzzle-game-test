package com.gmail.txyjssr.engine.core;

/**
 * 请求数据后的回调接口
 * @author yujsh
 *
 * @param <T>
 */

public interface IEngineCoreCallback<T> {
	public void callback(T data);
}
