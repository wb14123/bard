package org.binwang.bard.util.user;

import org.binwang.bard.core.Util;
import redis.clients.jedis.Jedis;

import java.util.UUID;

public class TokenStorage {
    private static final Jedis jedis =
        new Jedis(Util.getConfig().getString("bard.util.redis.token.host"),
            Util.getConfig().getInt("bard.util.redis.token.port"));

    private static final String prefix = "bard_token.";

    /**
     * Store a string and return an UUID key.
     *
     * @param value The value to store.
     * @return The generated key.
     */
    public static String put(String value, int seconds) {
        String key = UUID.randomUUID().toString();
        jedis.setex(prefix + key, seconds, value);
        return key;
    }

    /**
     * Get the value by key.
     *
     * @param key
     * @return The value found by key.
     */
    public static String get(String key) {
        return jedis.get(prefix + key);
    }

}
