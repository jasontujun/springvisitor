package com.yunchang.spring.visitor.core.invoker;

import com.yunchang.spring.visitor.core.annotation.ReflectiveMethod;
import com.yunchang.spring.visitor.core.annotation.ReflectiveService;
import com.yunchang.spring.visitor.core.cache.IServiceCache;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * 默认的Service反射调用器。
 * Created by jasontujun on 2018/1/10.
 */
@Component
public class DefaultServiceInvoker implements IServiceInvoker, ApplicationContextAware {

    // Spring应用上下文环境
    private ApplicationContext applicationContext;

    private Map<String, Object> serviceMap;
    private Map<String, Map<String, Method>> serviceMethodMap;

    @Autowired(required = false)
    private IServiceCache cache;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        initReflectiveServices();
    }

    protected void initReflectiveServices() {
        // 遍历所有service，将@ReflectiveService注解的Service加入Map
        serviceMap = new HashMap<String, Object>();
        serviceMethodMap = new HashMap<String, Map<String, Method>>();
        String[] allBeanNames = applicationContext.getBeanDefinitionNames();
        for(String beanName : allBeanNames) {
            Object bean = applicationContext.getBean(beanName);
            // 获取类上的@ServiceMethod注解
            Service serviceAnnotation = AnnotationUtils.findAnnotation(bean.getClass(), Service.class);
            ReflectiveService reflectiveServiceAnnotation = AnnotationUtils.findAnnotation(bean.getClass(), ReflectiveService.class);
            if (serviceAnnotation != null && reflectiveServiceAnnotation != null) {
                // @ReflectiveService注解的Service，添加进serviceMap
                serviceMap.put(reflectiveServiceAnnotation.value(), bean);
                Map<String, Method> methodMap = new HashMap<String, Method>();
                // 遍历service所有的方法(包括从接口、父类集成的方法)，将@ReflectiveMethod注解的方法，添加进serviceMethodMap
                Method[] methods = bean.getClass().getMethods();
                for (Method method : methods) {
                    ReflectiveMethod methodAnnotation = method.getAnnotation(ReflectiveMethod.class);
                    if (methodAnnotation != null) {
                        if (methodAnnotation.alias() == null || methodAnnotation.alias().length() == 0) {
                            // 如果别名为空，则使用声明的方法名
                            methodMap.put(method.getName(), method);
                        } else {
                            // 如果别名不为空，则使用别名
                            methodMap.put(methodAnnotation.alias(), method);
                        }
                    }
                }
                serviceMethodMap.put(reflectiveServiceAnnotation.value(), methodMap);
            }
        }
        System.out.println("finish init!");
    }

    @Override
    public Object invoke(String serviceName, String methodName, Map<String, String> params) throws Exception {
        Object service = serviceMap.get(serviceName);
        Map<String, Method> methodMap = serviceMethodMap.get(serviceName);
        if (service == null || methodMap == null) {
            // 根据serviceName找不到service
            throw new IllegalArgumentException("Service [" + serviceName + "] not found");
        }
        Method method = methodMap.get(methodName);
        if (method == null) {
            // 根据methodName找不到method
            throw new IllegalArgumentException("Method [" + methodName + "] not found");
        }
        ReflectiveMethod methodAnnotation = method.getAnnotation(ReflectiveMethod.class);
        if (methodAnnotation == null) {
            // method没有@ReflectiveMethod注解，不允许调用
            throw new IllegalArgumentException("Method [" + methodName + "] cannot invoke without @ReflectiveMethod");
        }

        // 如果方法的@ReflectiveMethod注解的cacheResult配置为true，优先从缓存里查询
        if (methodAnnotation.cacheResult() && cache != null) {
            Object result = cache.getResult(serviceName, methodName, params);
            if (result != null) {
                return result;
            }
        }

        // 反射Service的methodName方法
        String[] paramNames = methodAnnotation.params();
        Parameter[] realParams = method.getParameters();
        Object result;
        // 如果@ReflectiveMethod注解的参数名列表为空，则表示实际方法参数只有一个，且类型为map
        if (paramNames == null || paramNames.length == 0) {
            if (realParams.length == 1 && Map.class.isAssignableFrom(realParams[0].getType())) {
                result = method.invoke(service, params);
            } else {
                throw new IllegalStateException("Method [" + methodName + "] @ReflectiveMethod require only one Map type parameter, but it is not!");
            }
        }
        // 如果@ReflectiveMethod注解的参数名列表不为空，则需要和实际的方法参数列表一致
        else {
            if (realParams.length != paramNames.length) {
                throw new IllegalStateException("Method [" + methodName + "] @ReflectiveMethod parameter length is not the same as method define!");
            } else {
                Object[] paramsValue = new Object[paramNames.length];
                for (int i = 0; i<paramNames.length; i++) {
                    paramsValue[i] = params.get(paramNames[i]);
                }
                result =  method.invoke(service, paramsValue);
            }
        }
        // 如果方法的@ReflectiveMethod注解的cacheResult配置为true，且方法返回result不为null，则缓存返回结果
        if (methodAnnotation.cacheResult() && cache != null && result != null) {
            cache.cacheResult(serviceName, methodName, params, result);
        }
        return result;
    }
}
