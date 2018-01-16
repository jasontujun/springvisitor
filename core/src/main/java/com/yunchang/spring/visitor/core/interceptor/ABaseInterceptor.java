package com.yunchang.spring.visitor.core.interceptor;

import com.yunchang.spring.visitor.core.handler.IExceptionHandler;
import com.yunchang.spring.visitor.core.utils.WebUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 拦截器基类
 * Created by jasontujun on 2018/1/10.
 */
public abstract class ABaseInterceptor implements HandlerInterceptor {

    private static final Log logger = LogFactory.getLog(ABaseInterceptor.class);

    @Autowired
    private IExceptionHandler exceptionHandler;

    /**
     * 拦截器前置处理。
     * (该方法将在请求处理之前进行调用，只有该方法返回true，才会继续执行后续的Interceptor和Controller)
     * 默认返回true，子类可以覆盖该方法，自定义前置处理逻辑。
     *
     * @param request
     * @param response
     * @param handler
     * @param params
     * @return
     */
    protected boolean preHandleRequest(HttpServletRequest request,
                                       HttpServletResponse response,
                                       Object handler, Map<String, String> params) throws Exception {
        return true;
    }

    /**
     * 拦截器渲染前处理。
     * (该方法将在请求处理之后，DispatcherServlet进行视图返回渲染之前进行调用)
     * 子类可以覆盖该方法，自定义渲染前处理逻辑。
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @param params
     */
    protected void postHandleRequest(HttpServletRequest request,
                                     HttpServletResponse response,
                                     Object handler, ModelAndView modelAndView,
                                     Map<String, String> params) throws Exception {
    }

    /**
     * 拦截器后置处理。
     * (该方法将在整个请求结束之后，也就是在DispatcherServlet 渲染了对应的视图之后执行)
     * 子类可以覆盖该方法，自定义后置处理逻辑。
     *
     * @param request
     * @param response
     * @param handler
     * @param e
     * @param params
     */
    protected void afterCompleteRequest(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Object handler, Exception e,
                                        Map<String, String> params) throws Exception {
    }


    /**
     * 默认以字符串形式返回给客户端。
     * 子类可以覆盖该方法，以其他形式返回给客户端。
     *
     * @param response HttpServletResponse
     * @param data 返回的内容
     */
    protected void responseError(HttpServletResponse response, Object data) {
        if (data == null) {
            return;
        }
        try {
            String dataStr;
            if (data instanceof String) {
                dataStr = data.toString();
            } else {
                dataStr = exceptionHandler.response2string(data);
            }
            if (dataStr!= null) {
                // 强制要求客户端关闭此http连接
                response.setHeader("Connection", "close");
                // 设置返回内容类型json
                response.setContentType("application/json;charset=UTF-8");
                response.getOutputStream().write(dataStr.getBytes("UTF-8"));//使用OutputStream流向客户端输出字节数组
            }
        } catch (IOException e) {
            logger.error("response error", e);
        }
    }

    @Override
    public final boolean preHandle(HttpServletRequest request,
                                   HttpServletResponse response,
                                   Object handler) throws Exception {
        Map<String, String> paramMap = null;
        try {
            request.setCharacterEncoding("UTF-8");
            paramMap = WebUtil.getParamMapByRequest(request);
            return preHandleRequest(request, response, handler, paramMap);
        } catch (Exception e) {
            logger.error("", e);
            responseError(response, exceptionHandler.handleError(request, response, paramMap, e));
            return false;
        }
    }

    @Override
    public final void postHandle(HttpServletRequest request,
                                 HttpServletResponse response,
                                 Object handler, ModelAndView modelAndView) throws Exception {
        Map<String, String> paramMap = null;
        try {
            request.setCharacterEncoding("UTF-8");
            paramMap = WebUtil.getParamMapByRequest(request);
            postHandleRequest(request, response, handler, modelAndView, paramMap);
        } catch (Exception e) {
            logger.error("", e);
            responseError(response, exceptionHandler.handleError(request, response, paramMap, e));
        }
    }

    @Override
    public final void afterCompletion(HttpServletRequest request,
                                      HttpServletResponse response,
                                      Object handler, Exception e) throws Exception {
        Map<String, String> paramMap = null;
        try {
            request.setCharacterEncoding("UTF-8");
            paramMap = WebUtil.getParamMapByRequest(request);
            afterCompleteRequest(request, response, handler, e, paramMap);
        } finally {
            // 请求最终有异常，才会打印格式化日志
            if (e != null) {
                responseError(response, exceptionHandler.handleError(request, response, paramMap, e));
            }
        }
    }
}
