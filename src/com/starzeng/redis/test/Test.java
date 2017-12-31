package com.starzeng.redis.test;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.starzeng.redis.test.object.User;
import com.starzeng.redis.utils.RedisCURD;
import com.starzeng.redis.utils.RedisObject;

public class Test {

	public static void main(String[] args) {
		// saveOrUpdate();
//		batchSaveOrUpdate();
		
	}

	public static void saveOrUpdate() {
		User user = new User();
		user.setId(1);
		user.setName("where 我是谁");
		try {
			RedisCURD.saveOrUpdate("User:001", user);
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
			user.setMoney(1.00 + i);
			;
			lists.add(new RedisObject("User:" + i, user));
		}
		try {
			RedisCURD.batchSaveOrUpdate(lists);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | IntrospectionException
				| IOException e) {
			e.printStackTrace();
		}
	}
	
	

}
