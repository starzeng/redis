package com.starzeng.redis.utils;

import java.util.ResourceBundle;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

/**
 * redis pool utils
 * 
 * @author StarZeng
 *
 */
public class JedisPoolUtils {
	// 私有构造方法
	private JedisPoolUtils() {
	}

	// 保证内存中只有一个连接池对象
	public static JedisPool pool = null;

	// 静态代码块
	static {
		// 读取资源文件
		ResourceBundle bundle = ResourceBundle.getBundle("system");

		// 读取相应的值
		JedisPoolConfig config = new JedisPoolConfig();

		config.setMaxIdle(Integer.parseInt(bundle.getString("redis.maxIdle")));//
		// 最大空闲连接数
		config.setMaxTotal(Integer.parseInt(bundle.getString("redis.maxTotal")));//
		// 最大连接数
		config.setMaxWaitMillis(Integer.parseInt(bundle.getString("redis.maxWaitMillis")));//
		// 最大等待超时时间

		pool = new JedisPool(config, bundle.getString("redis.host"), Integer.parseInt(bundle.getString("redis.port")));
	}

	/**
	 * 获取连接
	 * 
	 * @return Jedis
	 */
	public static Jedis get() {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
		} catch (JedisException exception) {
			close(jedis);
			throw exception;
		}
		return jedis;
	}

	/**
	 * 关闭连接
	 * 
	 * @param jedis
	 *            Jedis
	 */
	public static void close(final Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}

}
