package com.bardframework.bard.util.cache;

import com.bardframework.bard.core.Util;

import java.util.HashMap;
import java.util.Map;

public class CacheManager {
    private static Map<String, Cache> caches = new HashMap<>();

    public static Cache getCache(String name) {
        return caches.get(name);
    }

    public static Cache getCache(String name, String cacheClassName) {
        Cache cache = caches.get(name);
        if (cache != null) {
            return cache;
        }
        try {
            Class<? extends Cache> cacheClass =
                (Class<? extends Cache>) Class.forName(cacheClassName);
            cache = cacheClass.newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            Util.getLogger()
                .error("Cannot get cache, name: {}, class: {}, error: {}", name, cacheClassName, e);
            return null;
        }
        cache.setName(name);
        caches.put(name, cache);
        return cache;
    }
}
