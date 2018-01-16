package com.yunchang.spring.visitor.demo.utils;

import java.util.ResourceBundle;

public abstract class PropertiesUtil {

    /**
     * 查询value根据key
     *
     * @param fileName 名称
     * @param keyName  查询key
     * @return 有 返回对应value 无 返回空字符串
     */
    public static String findValueByKey(String fileName, String keyName) {
        ResourceBundle rb = ResourceBundle.getBundle(fileName);
        if (null != rb) {
            return rb.getString(keyName);
        }
        return "";
    }

    /**
     * 查询value根据key(带默认值)
     *
     * @param fileName     名称
     * @param keyName      查询key
     * @param defaultValue 默认值
     * @return 有 返回对应value 无 返回空字符串
     */
    public static String findValueByKey(String fileName, String keyName, String defaultValue) {
        ResourceBundle rb = ResourceBundle.getBundle(fileName);
        if (null != rb) {
            try {
                return rb.getString(keyName);
            } catch (Exception e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
}
