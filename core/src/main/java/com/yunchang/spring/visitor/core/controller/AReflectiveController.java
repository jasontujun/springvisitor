package com.yunchang.spring.visitor.core.controller;

import com.yunchang.spring.visitor.core.handler.IExceptionHandler;
import com.yunchang.spring.visitor.core.handler.IResponseHandler;
import com.yunchang.spring.visitor.core.invoker.IServiceInvoker;
import com.yunchang.spring.visitor.core.utils.WebUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <pre>
 * 基于Url中service和method名进行反射访问对应service方法的基类Controller.
 * 封装了请求处理过程中的异常处理，以及返回格式的统一处理。
 * 请求入参：
 *      1.url中必须带有service和method名字，用于反射调用对应的带ReflectiveService注解的service方法
 *      2.http请求(POST、GET等)的所有参数，默认会转为Map类型，传入对应方法；
 *        如果方法的@ReflectiveMethod注解指定了参数列表params，会将Map中的参数依次按声明顺序传入
 * 请求返回：
 *      1.如果反射的service方法正常返回，则会调用IResponseHandler对应的实现类，统一处理返回内容
 *      2.如果反射的service方法抛出异常，则会调用IExceptionHandler对应的实现类，统一处理返回内容
 * 缓存：
 *      1.如果方法的@ReflectiveMethod注解cacheResult为true，表明该方法支持缓存。
 *        则请求同一个service的同一个method(参数完全相同)的情况下，后续几次请求会优先从缓存中查询方法的返回结果。
 *        如果存在则直接返回，如果缓存查询为空，则反射调用对应service方法，并将结果缓存起来。
 *      2.MapServiceCache是默认的缓存实现，支持设定缓存大小，LRU的淘汰策略。
 *        需要自定义缓存类，可以实现IServiceCache接口，并将对应实现类添加进容器配置即可，并将MapServiceCache从容器配置中去掉。
 * </pre>
 * Created by jasontujun on 2018/1/10.
 * @see com.yunchang.spring.visitor.core.annotation.ReflectiveService
 * @see com.yunchang.spring.visitor.core.annotation.ReflectiveMethod
 * @see com.yunchang.spring.visitor.core.cache.IServiceCache
 * @see com.yunchang.spring.visitor.core.handler.IResponseHandler
 * @see com.yunchang.spring.visitor.core.handler.IExceptionHandler
 */
public abstract class AReflectiveController {

    private static final Log logger = LogFactory.getLog(AReflectiveController.class);

    @Autowired
    private IServiceInvoker invoker;

    @Autowired
    private IExceptionHandler exceptionHandler;

    @Autowired
    private IResponseHandler responseHandler;

    protected Object invokeService(final HttpServletRequest request,
                                   final HttpServletResponse response,
                                   final String service,
                                   final String method) {
        Map<String, String> paramMap = null;
        try {
            final long startTime = System.currentTimeMillis();
            paramMap = WebUtil.getParamMapByRequest(request);
            Object result =  invoker.invoke(service, method, paramMap);
            return responseHandler.handleResponse(request, response,
                    service, method, paramMap, System.currentTimeMillis() - startTime, result);
        } catch (Exception e) {
            logger.error("Error in service:" + service + ",method:" + method, e);
            return exceptionHandler.handleError(request, response, paramMap, e);
        }
    }
}
