package com.liujian.custom;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Test1 {
	public static void main(String[] args) {
		Class clazz = MeiPo.class;
		System.out.println(clazz.getName());
		System.out.println(clazz.getInterfaces());
		Method[] methods = clazz.getMethods();
		try {
			for (Method method : methods) {
				System.out.println(method.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 

	}
}
