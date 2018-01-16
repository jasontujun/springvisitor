package com.yunchang.spring.visitor.core.cache;

import java.util.Map;

/**
 * 缓存Service方法返回结果的接口
 * Created by jasontujun on 2018/1/12.
 */
public interface IServiceCache {

    /**
     * 根据Service名字+方法名+参数Map，生成代表该次请求的唯一的key
     * @param service Service名字
     * @param method 方法名
     * @param param 参数Map
     * @return 返回生成的唯一的key
     */
    String generateKey(String service, String method, Map<String, String> param);

    /**
     * 缓存service方法的返回结果
     * @param service Service名字
     * @param method 方法名
     * @param param 参数Map
     * @param result service方法的返回结果
     * @return 返回true，表示缓存成功；false表示缓存失败。
     */
    boolean cacheResult(String service, String method, Map<String, String> param, Object result);

    /**
     * 根据Service名字+方法名+参数Map，查询缓存的方法调用结果
     * @param service Service名字
     * @param method 方法名
     * @param param 参数Map
     * @return 返回对应key的缓存；返回null，表示没有缓存
     */
    Object getResult(String service, String method, Map<String, String> param);
}
