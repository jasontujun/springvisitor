package com.yunchang.spring.visitor.core.invoker;

import java.util.Map;

/**
 * 反射调用Service方法的Invoker接口
 * Created by jasontujun on 2018/1/10.
 */
public interface IServiceInvoker {

    /**
     * 调用@ReflectiveService值为${service}的Service的对应方法。
     * 该方法必须被@ReflectiveMethod标注了，否则无法反射调用该方法，抛出IllegalArgumentException
     * @param serviceName Service名字
     * @param methodName 方法名
     * @param params 参数Map
     * @return 返回调用对应方法的返回值
     * @see com.yunchang.spring.visitor.core.annotation.ReflectiveService
     * @see com.yunchang.spring.visitor.core.annotation.ReflectiveMethod
     * @throws IllegalArgumentException 当Service或者Method名字不存在,或方法没有@ReflectiveMethod注解时抛出
     */
    Object invoke(String serviceName, String methodName, Map<String, String> params) throws Exception;
}
