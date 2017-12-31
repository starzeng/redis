package com.starzeng.redis.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Redis 对象 Map 相互转换工具
 * 
 * @author StarZeng
 *
 */
public class ObjectHashMapper {

	/**
	 * Map转对象
	 * 
	 * @param map
	 *            Map<byte[], byte[]>
	 * @return Object
	 * 
	 * @throws IntrospectionException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws ClassNotFoundException
	 */
	public static Object fromHash(Map<byte[], byte[]> map) throws IntrospectionException, IllegalAccessException,
			InstantiationException, InvocationTargetException, ClassNotFoundException {
		if (map == null || map.isEmpty()) {
			return null;
		}
		String _class = new String(map.get("_class".getBytes(StandardCharsets.UTF_8)));
		if (_class == null || "".equals(_class)) {
			return null;
		}
		Class<?> clazz = Class.forName(_class);
		BeanInfo beanInfo = Introspector.getBeanInfo(clazz); // 获取类属性
		// 给 JavaBean 对象的属性赋值
		Object obj = clazz.newInstance(); // 创建 JavaBean 对象
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			byte[] key = propertyDescriptor.getName().getBytes(StandardCharsets.UTF_8);
			if (map.containsKey(key)) {
				// 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
				Object[] args = new Object[1];
				args[0] = parse(propertyDescriptor.getPropertyType().getSimpleName(), new String(map.get(key)));
				try {
					propertyDescriptor.getWriteMethod().invoke(obj, args);
				} catch (Exception e) {
					obj = null;
					e.printStackTrace();
				}
			}
		}
		return obj;
	}

	/**
	 * 对象转Map
	 * 
	 * @param object
	 *            Object
	 * @return Map<byte[], byte[]>
	 * 
	 * @throws IntrospectionException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static Map<byte[], byte[]> toHash(Object object)
			throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (object == null) {
			return Collections.emptyMap();
		}
		Class<? extends Object> type = object.getClass();
		Map<byte[], byte[]> map = new HashMap<>();
		map.put("_class".getBytes(StandardCharsets.UTF_8), type.getName().getBytes(StandardCharsets.UTF_8));
		BeanInfo beanInfo = Introspector.getBeanInfo(type);
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		Method readMethod;
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			String propertyName = propertyDescriptor.getName();
			if (!propertyName.equals("class")) {
				readMethod = propertyDescriptor.getReadMethod();
				Object result = readMethod.invoke(object, new Object[0]);
				byte[] key = propertyName.getBytes(StandardCharsets.UTF_8);
				// 返回有值得属性
				if (result != null) {
					map.put(key, result.toString().getBytes(StandardCharsets.UTF_8));
				}
			}
		}
		return map;
	}

	/**
	 * 字符串转数类型
	 * 
	 * @param typeName
	 *            数据类型
	 * @param value
	 *            数据值
	 * @return 对应的数据类型值
	 */
	private static Object parse(String typeName, String value) {
		Object object = value;
		switch (typeName.toLowerCase()) {
		case "integer":
			if (value == null || "".equals(value)) {
				object = null;
			} else {
				object = Integer.parseInt(value);
			}
			break;
		case "double":
			if (value == null || "".equals(value)) {
				object = null;
			} else {
				object = Double.parseDouble(value);
			}
			break;
		case "byte":
			if (value == null || "".equals(value)) {
				object = null;
			} else {
				object = Byte.parseByte(value);
			}
			break;
		case "short":
			if (value == null || "".equals(value)) {
				object = null;
			} else {
				object = Short.parseShort(value);
			}
			break;
		case "long":
			if (value == null || "".equals(value)) {
				object = null;
			} else {
				object = Long.parseLong(value);
			}
			break;
		case "float":
			if (value == null || "".equals(value)) {
				object = null;
			} else {
				object = Float.parseFloat(value);
			}
			break;
		case "boolean":
			if ("true".equals(value)) {
				object = Boolean.parseBoolean(value);
			} else {
				object = false;
			}
			break;
		default:
			if (value == null || "".equals(value)) {
				object = null;
			}
			break;
		}
		return object;
	}

}
