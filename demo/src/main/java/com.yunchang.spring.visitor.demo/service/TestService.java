package com.yunchang.spring.visitor.demo.service;

import com.yunchang.spring.visitor.core.annotation.ReflectiveMethod;
import com.yunchang.spring.visitor.core.annotation.ReflectiveService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试Service。
 * 支持反射调用该Service的m1,m2,m3方法。
 * Created by jasontujun on 2018/1/11.
 */
@ReflectiveService("service")
@Service
public class TestService {

    /**
     * 反射调用该方法，参数默认有且只有一个，类型为Map
     * 测试链接 {@link}http://localhost:8080/v1/service/m1?p1=123
     */
    @ReflectiveMethod
    public String m1(Map<String, String> params) {
        return params.get("p1") + "_suffix";
    }

    /**
     * 反射调用该方法，参数自动填充到aa和bb
     * 测试链接 {@link}http://localhost:8080/v1/service/m2?aa=12&bb=23
     */
    @ReflectiveMethod(params = {"aa", "bb"})
    public Map m2(final String aa, final String bb) {
        return new HashMap<String, String>(){{
            put("key", aa + "_" + bb);
        }};
    }

    /**
     * 反射调用该方法，并缓存方法返回结果，下次用同样参数调用该方法，优先查询缓存。
     * 测试链接 {@link}http://localhost:8080/v1/service/cache?cc=111&dd=2222
     * @param cc
     * @param dd
     * @return
     */
    @ReflectiveMethod(params = {"cc", "dd"}, cacheResult = true)
    public Map cache(final String cc, final String dd) {
        return new HashMap<String, String>(){{
            put("key", cc + "#" + dd);
        }};
    }

    /**
     * 无法反射调用该方法，因为注解的参数列表和实际方法声明的参数列表不一致。
     * 测试链接 {@link}http://localhost:8080/v1/service/error?aa=123&bb=789
     */
    @ReflectiveMethod(params = {"aa", "bb"})
    public String error(String aa, String bb, String cc) {
        // 注解参数和实际方法参数不一致，无法阿调用
        return aa + "_" + bb + "cc";
    }

    /**
     * 无法反射调用该方法，因为没有加注解
     * 测试链接 {@link}http://localhost:8080/v1/service/normal?a=123&b=789
     */
    public String normal(Map<String, String> params) {
        return params.get("param2") + "_suffix";
    }
}
