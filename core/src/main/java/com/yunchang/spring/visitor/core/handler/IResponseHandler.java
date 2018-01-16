package com.yunchang.spring.visitor.core.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Http返回格式处理的接口
 * Created by jasontujun on 2018/1/16.
 */
public interface IResponseHandler {

    /**
     * 自定义http返回.
     * 如果调用成功，result是调用service对应方法的返回值；
     * 如果调用过程中抛出异常，则result为空，exception是抛出的异常。
     * @param request
     * @param response
     * @param service
     * @param method
     * @param paramMap
     * @param costTime
     * @param result
     * @return
     */
    Object handleResponse(HttpServletRequest request,
                          HttpServletResponse response,
                          String service,
                          String method,
                          Map<String, String> paramMap,
                          long costTime,
                          Object result);
}
