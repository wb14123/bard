package org.binwang.bard.util.cache;

public interface Cache {

    /**
     * Set the name of this cache.
     *
     * @param name The name of this cache.
     */
    public void setName(String name);

    /**
     * Get a value by key
     *
     * @param key The key.
     * @return The value.
     */
    public String get(String key);

    /**
     * Set the value of a key.
     *
     * @param key   The key.
     * @param value The value.
     */
    public void set(String key, String value);

    /**
     * Set the value of a key with expire time.
     *
     * @param key     The key.
     * @param seconds The expire time, by second.
     * @param value   The value.
     */
    public void setex(String key, final int seconds, String value);

    /**
     * Delete a key.
     *
     * @param key the Key.
     */
    public void del(String key);

}
