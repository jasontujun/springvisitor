package com.yunchang.spring.visitor.demo.handler;

import com.yunchang.spring.visitor.core.handler.IExceptionHandler;
import com.yunchang.spring.visitor.core.utils.WebUtil;
import com.yunchang.spring.visitor.demo.utils.ParseJson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一以JSON格式的http异常处理类
 * Created by jasontujun on 2018/1/16.
 */
@Component
public class JsonExceptionHandler implements IExceptionHandler {


    private static final Log logger = LogFactory.getLog(JsonExceptionHandler.class);

    @Override
    public Object handleError(HttpServletRequest request, HttpServletResponse response,
                              Map<String, String> paramMap, Exception exception) {
        // 统一返回内容为json格式，成功status为ok，失败status为fail
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("status", "fail");
        resMap.put("error", exception.getMessage());
        if (logger.isInfoEnabled()) {
            Map<String, Object> logMap = new HashMap<String, Object>();
            logMap.put("time", System.currentTimeMillis());
            logMap.put("uri", WebUtil.getUri(request));
            logMap.put("ip", WebUtil.getIpAddr(request));
            logMap.put("params", paramMap);
            logMap.put("response", resMap);
            logger.info(ParseJson.encodeJson(logMap));
        }
        response.setHeader("Connection", "close");
        response.setContentType("application/json;charset=UTF-8");
        return resMap;
    }

    @Override
    public String response2string(Object response) {
        return ParseJson.encodeJson(response);
    }
}
