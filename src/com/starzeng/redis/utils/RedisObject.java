package com.starzeng.redis.utils;

/**
 * Hash 存储对象
 * 
 * @author StarZeng
 *
 */
public class RedisObject {

	/**
	 * 存储的 key 值
	 * <p>
	 * e: key => "goods:sellerId:goodsId"
	 * </p>
	 */
	private String key;

	/**
	 * 存储对象
	 */
	private Object object;

	public RedisObject(String key, Object object) {
		super();
		this.key = key;
		this.object = object;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the object
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * @param object
	 *            the object to set
	 */
	public void setObject(Object object) {
		this.object = object;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RedisObject [key=" + key + ", object=" + object + "]";
	}

}
