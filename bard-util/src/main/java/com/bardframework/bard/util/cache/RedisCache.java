package com.bardframework.bard.util.cache;

import com.bardframework.bard.core.Util;
import redis.clients.jedis.Jedis;

public class RedisCache implements Cache {
    private final Jedis jedis = new Jedis(
        Util.getConfig().getString("bard.util.redis.host", "localhost"),
        Util.getConfig().getInt("bard.util.redis.port", 6379));

    private String name;

    public RedisCache() {
    }

    private String getkey(String key) {
        return name + "." + key;
    }

    @Override public void setName(String name) {
        this.name = name;
    }

    @Override public String get(String key) {
        return jedis.get(getkey(key));
    }

    @Override public void set(String key, String value) {
        jedis.set(getkey(key), value);
    }

    @Override public void setex(String key, final int seconds, String value) {
        jedis.setex(getkey(key), seconds, value);
    }

    @Override public void del(String key) {
        jedis.del(getkey(key));
    }
}
