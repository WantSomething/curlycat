package com.liujian.custom;

public class Singleton {
	private Singleton(){}
	private static Singleton singleton = null;
	
	public static Singleton getInstance(){
		if(singleton == null){
			synchronized(singleton.getClass()){
				if(singleton == null){
					singleton = new Singleton();
				}
			}
		}
		return singleton;
	}
	
	
}
