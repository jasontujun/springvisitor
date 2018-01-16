package com.yunchang.spring.visitor.demo.utils;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.util.StringUtils;

public abstract class ParseJson {
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	 
	 static {
		 objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
	 }

	public static String encodeJson(Object o) {
		try {
			return objectMapper.writeValueAsString(o);
		} catch(Exception e) {
		}
		return null;
	}
	
	/**
	 * 从指定json字符串包装成指定类型T对象返回
	 * @param strInfo 指定json字符串
	 * @param T 转化的对象类型
	 * @return 转化成功，返回指定类型T对象返回；否则返回null
	 */
	public static <T> T  getJsonContentByStr(String strInfo, Class<T> T) {
        if (StringUtils.isEmpty(strInfo)) {
            return null;
        }
		try {
			return objectMapper.readValue(strInfo, T);
		} catch (Exception e) {
		}
		return null;
	}
}
