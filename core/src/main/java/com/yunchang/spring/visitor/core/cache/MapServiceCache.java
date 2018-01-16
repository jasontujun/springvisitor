package com.yunchang.spring.visitor.core.cache;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 基于LinkedHashMap实现的简单IServiceCache缓存实现类。
 * 该缓存大小固定，可配置；支持LRU的淘汰策略。
 * Created by jasontujun on 2018/1/12.
 */
@Component
public class MapServiceCache implements IServiceCache {

    // 缓存sql解析结果
    private CacheMap<String, Object> cacheMap;

    public MapServiceCache() {
        // TODO 缓存大小最好可配置。
        cacheMap = new CacheMap<String, Object>(4096);
    }

    @Override
    public String generateKey(String service, String method, Map<String, String> param) {
        String result = "[cache]" + service + "." + method;
        TreeMap<String, String> sortMap = new TreeMap<String, String>(param);
        for (String key : sortMap.keySet()) {
            result = result + "." + key + "=" + sortMap.get(key);
        }
        return result;
    }

    @Override
    public boolean cacheResult(String service, String method, Map<String, String> param, Object result) {
        cacheMap.put(generateKey(service, method, param), result);
        return true;
    }

    @Override
    public Object getResult(String service, String method, Map<String, String> param) {
        return cacheMap.get(generateKey(service, method, param));
    }

    /**
     * 大小为maxSize的支持LRU的Map
     */
    private static class CacheMap<K, V> extends LinkedHashMap<K, V> {

        private final int maxSize;

        public CacheMap(int maxSize) {
            super(maxSize > 32 ? 32 : maxSize, 0.75f, true);
            this.maxSize = maxSize;
        }

        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > maxSize;
        }
    }
}
