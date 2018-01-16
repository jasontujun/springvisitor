package com.yunchang.spring.visitor.demo.controller;

import com.yunchang.spring.visitor.demo.utils.ParseJson;
import com.yunchang.spring.visitor.core.controller.AReflectiveController;
import com.yunchang.spring.visitor.core.utils.WebUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试Controller
 * Created by jasontujun on 2018/1/11.
 */
@Controller
public class TestController extends AReflectiveController {

    private static final Log logger = LogFactory.getLog(TestController.class);

    @RequestMapping("/test")
    @ResponseBody
    public Object test(final HttpServletRequest request, final HttpServletResponse response) {
        response.setContentType("text/plain;charset=UTF-8");
        response.setHeader("Connection", "close");
        return "ok";
    }

    @RequestMapping("/v1/{service}/{method}")
    @ResponseBody
    public Object doRequest(@PathVariable final String service, @PathVariable final String method,
                            final HttpServletRequest request, final HttpServletResponse response) {
        return invokeService(request, response, service, method);
    }
}
