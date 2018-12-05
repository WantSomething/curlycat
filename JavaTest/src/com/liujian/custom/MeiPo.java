package com.liujian.custom;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MeiPo implements InvocationHandler{
	
	private Object target;
	
	public Object getInstance(Object target){
		this.target = target;
		Class clazz = target.getClass();
		return Proxy.newProxyInstance(clazz.getClassLoader(),clazz.getInterfaces(),this);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("我是媒婆，我要开始查找。。。");
		method.invoke(target, args);
		System.out.println("找到啦！！！");
		return null;
	}

}
