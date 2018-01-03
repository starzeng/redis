package com.starzeng.redis.test;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.starzeng.redis.test.object.User;
import com.starzeng.redis.utils.RedisCURD2;
import com.starzeng.redis.utils.RedisObject;

public class Test2 {

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		// saveOrUpdate();// Pipelined SET: 0.263 seconds
		// batchSaveOrUpdate();// Pipelined SET: 8.808 seconds
		// findOne();// Pipelined SET: 0.323 seconds
		findAll();// Pipelined SET: 16.381 seconds
		long end = System.currentTimeMillis();
		System.out.println("Pipelined SET: " + ((end - start) / 1000.0) + " seconds");
	}

	public static void saveOrUpdate() {
		User user = new User();
		user.setId(1);
		user.setName("where 我是谁");
		try {
			RedisCURD2.saveOrUpdate("U:001", user);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| IntrospectionException e) {
			e.printStackTrace();
		}
	}

	public static void batchSaveOrUpdate() {
		List<RedisObject> lists = new ArrayList<>();
		User user;
		for (int i = 0; i < 1000000; i++) {
			user = new User();
			user.setId(i);
			user.setAge(i + 10);
			user.setName("StarZeng" + i);
			user.setTel("186***" + i);
			user.setMoney(1.00 + i / 10);
			lists.add(new RedisObject("U:" + i, user));
		}
		try {
			RedisCURD2.batchSaveOrUpdate(lists);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | IntrospectionException
				| IOException e) {
			e.printStackTrace();
		}
	}

	public static void findOne() {
		try {
			User user = (User) RedisCURD2.findOne("U:120293");
			System.out.println(user);
		} catch (IllegalAccessException | InstantiationException | InvocationTargetException | ClassNotFoundException
				| IntrospectionException e) {
			e.printStackTrace();
		}
	}

	public static void findAll() {
		try {
			List<?> lists = RedisCURD2.findAll("U:*");
			System.out.println(lists.size());
			// User user;
			// for (Object o : lists) {
			// System.out.println(o);
			// user = (User) o;
			// System.out.println(user);
			// }
			// for (int i = 80; i < 90; i++) {
			// user = (User) lists.get(i);
			// System.out.println(user);
			//
			// }
		} catch (IllegalAccessException | InstantiationException | InvocationTargetException | ClassNotFoundException
				| IntrospectionException | IOException e) {
			e.printStackTrace();
		}
	}
}
