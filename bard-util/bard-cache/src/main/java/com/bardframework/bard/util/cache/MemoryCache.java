package com.bardframework.bard.util.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a very simple cache implementation. Never use it in production.
 */
public class MemoryCache implements Cache {
    private Map<String, String> map = new HashMap<>();
    private String name;

    @Override public void setName(String name) {
        this.name = name;
    }

    @Override public String get(String key) {
        return map.get(name + key);
    }

    @Override public void set(String key, String value) {
        map.put(name + key, value);
    }

    /**
     * This method not has a expire time.
     *
     * @param key     The key.
     * @param seconds Not support for now.
     * @param value   The value.
     */
    @Override public void setex(String key, int seconds, String value) {
        set(key, value);
    }

    @Override public void del(String key) {
        map.remove(name + key);
    }
}
