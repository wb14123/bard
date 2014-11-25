package com.bardframework.bard.util.cache;

import com.bardframework.bard.core.Util;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisCache implements Cache {
    private final JedisPool jedisPool = new JedisPool(
        new JedisPoolConfig(),
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
        return jedisPool.getResource().get(getkey(key));
    }

    @Override public void set(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        jedis.set(getkey(key), value);
        jedis.close();
    }

    @Override public void setex(String key, final int seconds, String value) {
        Jedis jedis = jedisPool.getResource();
        jedis.setex(getkey(key), seconds, value);
        jedis.close();
    }

    @Override public void del(String key) {
        Jedis jedis = jedisPool.getResource();
        jedis.del(getkey(key));
        jedis.close();
    }
}
