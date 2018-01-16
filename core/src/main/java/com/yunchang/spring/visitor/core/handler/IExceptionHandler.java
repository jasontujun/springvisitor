package com.yunchang.spring.visitor.core.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Http请求出现异常时的返回内容处理的接口。
 * Created by jasontujun on 2018/1/16.
 */
public interface IExceptionHandler {

    /**
     * 自定义http返回.
     * 如果调用成功，result是调用service对应方法的返回值；
     * 如果调用过程中抛出异常，则result为空，exception是抛出的异常。
     * @param request http请求
     * @param response http返回
     * @param paramMap 参数
     * @param exception 异常
     * @return 返回处理的结果
     */
    Object handleError(HttpServletRequest request,
                       HttpServletResponse response,
                       Map<String, String> paramMap,
                       Exception exception);

    /**
     * 将handleError()返回的结果转换为字符串
     * @param response 处理结果
     * @return 返回字符串
     */
    String response2string(Object response);
}
