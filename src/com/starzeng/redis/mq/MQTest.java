package com.starzeng.redis.mq;

import com.starzeng.redis.utils.RedisMQ;

public class MQTest {

	public static void main(String[] args) throws InterruptedException {

		int n = 100;
		while (true) {
			Thread.sleep(2000);
			RedisMQ.save("mq:test", "" + (n++));
			String s = RedisMQ.get("mq:test");
			System.out.println(s);
		}

	}

}
