package com.yunchang.spring.visitor.demo.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.security.MessageDigest;

public abstract class MD5Util {

    private static final Log logger = LogFactory.getLog(MD5Util.class);
	private static final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'A', 'B', 'C', 'D', 'E', 'F' };

    /**
     * 将字符串进行32位MD5编码
     * @param s 原始字符串
     * @return 返回32位MD5码
     */
	public static String MD5(String s) {
		try {
			byte[] btInput = s.getBytes("UTF-8");
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (byte byte0 : md) {
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str).toLowerCase();
		} catch (Exception e) {
            logger.error("",e);
			return "";
		}
	}

    /**
     * 将字符串进行16位MD5编码
     * @param s 原始字符串
     * @return 返回16位MD5码
     */
	public static String MD5_16(String s) {
		return MD5(s).substring(8, 24);
	}

}
