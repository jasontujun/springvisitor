package com.yunchang.spring.visitor.demo.interceptor;

import com.yunchang.spring.visitor.core.interceptor.ABaseInterceptor;

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
        // TODO 验证签名
        return true;
    }
}
