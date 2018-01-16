package com.yunchang.spring.visitor.demo.utils;

import java.util.*;

/**
 * 加密工具类
 * Created by jasontujun on 2018/1/16.
 */
public abstract class EncryptUtil {

    public static boolean check(Map<String, String> params, String signField, String key) {
        if (!params.containsKey(signField)) {
            return false;
        }
        String sign = params.get(signField);
        String sigString = generateSign(params, signField);
        String checkSign = MD5Util.MD5(key + sigString + key);
        return checkSign.equalsIgnoreCase(sign);
    }

    private static String generateSign(Map<String, String> paramMap, String signField) {
        Set<String> params = paramMap.keySet();
        List<String> sortedParams = new ArrayList<String>(params);
        Collections.sort(sortedParams);
        StringBuilder sb = new StringBuilder();
        for (String paramKey : sortedParams) {
            if (paramKey.equals(signField)) {
                continue;
            }
            String valueStr = paramMap.get(paramKey);
            sb.append(paramKey)
                    .append('=')
                    .append(valueStr == null ? "" : new String(Base64.getEncoder().encode(valueStr.getBytes())));
        }
        return sb.toString();
    }
}
