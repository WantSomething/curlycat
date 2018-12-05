package com.liujian.custom;

public class AloneMan implements Person {

	@Override
	public void findLove() {
		System.out.println("我叫"+name+",性别"+sex);
		System.out.println("要一个漂亮的妹子");
	}
	
	private String sex = "boy";
	private String name = "aloneMan";
	public String getSex() {
		return sex;
	}
	public String getName() {
		return name;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public void setName(String name) {
		this.name = name;
	}

}
