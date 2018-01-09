package com.starzeng.redis.utils;

import java.util.List;

import redis.clients.jedis.Jedis;

/**
 * redis 消息队列
 * 
 * @author zengweixiong
 *
 */
public class RedisMQ {

	/**
	 * 添加消息到队列最后
	 * 
	 * @param key
	 *            String 消息频道
	 * @param strings
	 *            String[] 消息内容
	 * @return 添加多个
	 */
	public static Long save(String key, String... strings) {
		Jedis jedis = JedisPoolUtils.get();
		Long num = jedis.lpush(key, strings);
		JedisPoolUtils.close(jedis);
		return num;
	}

	/**
	 * 移除并获取一条消息
	 * 
	 * @param key
	 *            String 消息频道
	 * @return 一条消息
	 */
	public static String get(String key) {
		Jedis jedis = JedisPoolUtils.get();
		List<String> lists = jedis.brpop(30, key);
		JedisPoolUtils.close(jedis);
		if (lists.size() > 0) {
			return lists.get(1);
		}
		return null;
	}

}
