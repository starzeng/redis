package com.starzeng.redis.utils;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

/**
 * redis CRUD 工具
 * 
 * @author StarZeng
 *
 */
public class RedisCURD2 {

	/**
	 * 添加/修改
	 * 
	 * @param key
	 *            String reidKey
	 * @param object
	 *            Object
	 * @return boolean
	 * 
	 * @throws IntrospectionException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static boolean saveOrUpdate(String key, Object object)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		Jedis jedis = JedisPoolUtils.get();
		jedis.hmset(key, ObjectHashMapper2.toHash(object));
		JedisPoolUtils.close(jedis);
		return true;
	}

	/**
	 * 批量添加/批量修改
	 * 
	 * @param lists
	 *            List<HashObject>
	 * @return boolean
	 * 
	 * @throws IntrospectionException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	public static boolean batchSaveOrUpdate(List<RedisObject> lists) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, IntrospectionException, IOException {
		Jedis jedis = JedisPoolUtils.get();
		Map<String, String> hashMap = null;
		Pipeline pipeline = jedis.pipelined();
		for (RedisObject redisObject : lists) {
			hashMap = ObjectHashMapper2.toHash(redisObject.getObject());
			pipeline.hmset(redisObject.getKey(), hashMap);
		}
		pipeline.syncAndReturnAll();
		pipeline.close();
		JedisPoolUtils.close(jedis);
		return true;
	}

	/**
	 * 查询一个
	 * 
	 * @param key
	 *            RedisKey
	 * 
	 * @return Map<byte[], byte[]>
	 * @throws IntrospectionException
	 * @throws ClassNotFoundException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static Object findOne(String key) throws IllegalAccessException, InstantiationException,
			InvocationTargetException, ClassNotFoundException, IntrospectionException {
		Object object;
		Jedis jedis = JedisPoolUtils.get();
		Map<String, String> map = jedis.hgetAll(key);
		JedisPoolUtils.close(jedis);
		object = ObjectHashMapper2.fromHash(map);
		return object;
	}

	/**
	 * 查询所有
	 * 
	 * @param keys
	 *            前缀+通配符 e: goods:sellerId:*
	 * 
	 * @return List<Object> | null
	 * 
	 * @throws IntrospectionException
	 * @throws ClassNotFoundException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static List<Object> findAll(String keys) throws IllegalAccessException, InstantiationException,
			InvocationTargetException, ClassNotFoundException, IntrospectionException, IOException {

		Jedis jedis = JedisPoolUtils.get();

		Set<String> keySets = jedis.keys(keys);
		Pipeline pipeline = jedis.pipelined();
		for (String key : keySets) {
			pipeline.hgetAll(key);
		}
		List<Object> lists = pipeline.syncAndReturnAll();
		pipeline.close();
		JedisPoolUtils.close(jedis);

		List<Object> ls = new ArrayList<>();
		for (Object o : lists) {
			ls.add(ObjectHashMapper2.fromHash((Map<String, String>) o));
		}

		// Set<String> keySets = jedis.keys(keys);
		// Pipeline pipeline = jedis.pipelined();
		// for (String key : keySets) {
		// pipeline.hgetAll(key);
		// }
		// List<Object> lists = pipeline.syncAndReturnAll();
		// pipeline.close();
		// JedisPoolUtils.close(jedis);
		return ls;
	}

	/**
	 * 删除
	 * 
	 * @param keys
	 *            多个key
	 * @return boolean
	 */
	public static boolean delete(String... keys) {
		Jedis jedis = JedisPoolUtils.get();
		for (String key : keys) {
			jedis.del(key);
		}
		JedisPoolUtils.close(jedis);
		return true;
	}

}
