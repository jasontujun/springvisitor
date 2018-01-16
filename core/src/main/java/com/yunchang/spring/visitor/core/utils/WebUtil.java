package com.yunchang.spring.visitor.core.utils;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public abstract class WebUtil {

    /**
     * 解析request的所有参数为map，参数的值都进行了UrlDecode
     */
    public static Map<String, String> getParamMapByRequest(HttpServletRequest request) {
        Map<String, String> requestParamsMap = new HashMap<String, String>();
        Enumeration<String> e = request.getParameterNames();
        while (e.hasMoreElements()) {
            String param = e.nextElement();
            String value = request.getParameter(param);
            requestParamsMap.put(param, value);
        }
        return requestParamsMap;
    }

    /**
     * 获取Uri
     */
    public static String getUri(HttpServletRequest request) {
        return request.getScheme() + "://" +
                request.getServerName() +
                ("http".equals(request.getScheme()) && request.getServerPort() == 80 || "https".equals(request.getScheme()) && request.getServerPort() == 443 ? "" : ":" + request.getServerPort() ) +
                request.getRequestURI() +
                (request.getQueryString() != null ? "?" + request.getQueryString() : "");
    }

    /**
     * 获取IP,若获取失败返回null
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = null;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
        }
        return ip;
    }

    /**
     * 判断IP地址是否在IP白名单段内，仅支持IPv4
     * @param ip 待判定的IP地址
     * @param range 以192.168.0.0/22格式定义的IP网段
     * @return true|false
     */
    public static boolean isIpInRange(String ip, String range) {
        if (StringUtils.isEmpty(range) || StringUtils.isEmpty(ip)) {
            return false;
        }
        try {
            int r = Integer.parseInt(range.replaceAll(".*/", ""));
            int mask = 0xFFFFFFFF << (32 - r);

            String cidrIp = range.replaceAll("/.*", "");
            String[] cidrIps = cidrIp.split("\\.");
            int cidrIpAddr = (Integer.parseInt(cidrIps[0]) << 24) | (Integer.parseInt(cidrIps[1]) << 16) |
                    (Integer.parseInt(cidrIps[2]) << 8) | Integer.parseInt(cidrIps[3]);

            String[] ips = ip.split("\\.");
            //简单判断IP格式，不必进行严格检查，后面的算法还会继续判断
            if (ips.length != 4) {
                return false;
            }
            int ipAddr = (Integer.parseInt(ips[0]) << 24) | (Integer.parseInt(ips[1]) << 16) |
                    (Integer.parseInt(ips[2]) << 8) | Integer.parseInt(ips[3]);
            return (ipAddr & mask) == (cidrIpAddr & mask);
        } catch (Exception e) {
            return false;
        }
    }
}
