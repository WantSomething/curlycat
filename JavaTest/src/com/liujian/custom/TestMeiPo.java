package com.liujian.custom;

public class TestMeiPo {
	public static void main(String[] args) {
		MeiPo meiPo = new MeiPo();
		Person aloneMan = (Person) meiPo.getInstance(new AloneMan());
		System.out.println(aloneMan.getClass());
		aloneMan.findLove();
	}
}
