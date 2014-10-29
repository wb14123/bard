package org.binwang.bard.util.user;

import org.binwang.bard.core.Util;
import org.binwang.bard.util.cache.Cache;
import org.binwang.bard.util.cache.CacheManager;

import java.util.UUID;

public class TokenStorage {
    private static Cache cache = CacheManager.getCache(
        Util.getConfig().getString("bard.util.user.cache.name"),
        Util.getConfig().getString("bard.util.user.cache.class"));

    /**
     * Put username into the token storage with an expire time.
     * @param username The username.
     * @param seconds The expire time in second.
     * @return Then generated token.
     */
    public static String put(String username, final int seconds) {
        String key = UUID.randomUUID().toString();
        cache.setex(key, seconds, username);
        return key;
    }

    /**
     * Get the username by token from token storage.
     * @param token The token.
     * @return The username got by token.
     */
    public static String get(String token) {
        return cache.get(token);
    }

}
