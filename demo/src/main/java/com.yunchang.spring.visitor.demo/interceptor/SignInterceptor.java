package com.yunchang.spring.visitor.demo.interceptor;

import com.yunchang.spring.visitor.core.interceptor.ABaseInterceptor;
import com.yunchang.spring.visitor.core.utils.WebUtil;
import com.yunchang.spring.visitor.demo.utils.EncryptUtil;
import com.yunchang.spring.visitor.demo.utils.PropertiesUtil;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 参数签名验证的Interceptor。
 * Created by jasontujun on 2017/10/26.
 */
public class SignInterceptor extends ABaseInterceptor {

    @Override
    public boolean preHandleRequest(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Object handler, Map<String, String> params) throws Exception {
        //如果参数中包含sign字段，则验证签名
        if (params.containsKey("sign")) {
            String key = PropertiesUtil.findValueByKey("demoConfig", "access.key", "");
            params.put("path", request.getRequestURI());
            if (!EncryptUtil.check(params, "sign", key))
                throw new IllegalAccessException("Sorry, sign error!");
        }
        //如果参数中没包含sign字段，则判断ip是否在白名单中
        else {
            String ip = WebUtil.getIpAddr(request);
            String ipRanges = PropertiesUtil.findValueByKey("demoConfig", "ip.white", "");
            if (StringUtils.isEmpty(ipRanges)) {
                throw new IllegalAccessException("Sorry, IP[" + ip + "] not allow!");
            }
            //多个ip段，用逗号分隔。如"192.168.0.0/24,123.207.109.0/24"
            String[] ipRangeList = ipRanges.split(",");
            boolean ipValid = false;
            for (String ipRange : ipRangeList) {
                if (WebUtil.isIpInRange(ip, ipRange)) {
                    ipValid = true;
                    break;
                }
            }
            if (!ipValid) {
                throw new IllegalAccessException("Sorry, IP[" + ip + "] not allow!");
            }
        }
        return true;
    }
}
