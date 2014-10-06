package org.binwang.bard.util.user;

import java.util.HashMap;
import java.util.Map;

public class TestUserDao implements UserDao<TestUserDao, User> {
    public static Map<String, String> passwordMap = new HashMap<>();
    public static Map<String, String> saltMap = new HashMap<>();
    public static Map<String, String> tokenMap = new HashMap<>();
    public static Map<String, User> userMap = new HashMap<>();

    public static TestUserDao self = new TestUserDao();

    @Override public TestUserDao getInstance() {
        return self;
    }

    @Override public User getUser(String username) throws UserNotFoundException {
        User user = userMap.get(username);
        if (user == null) {
            throw new UserNotFoundException(username);
        }
        return user;
    }

    @Override public void saveUser(User user) {
        userMap.put(user.username, user);
    }

    @Override public void checkPassword(String username, String password)
        throws UserNotFoundException {
        String realPassword = passwordMap.get(username);
        if (realPassword == null || !realPassword.equals(password)) {
            throw new UserNotFoundException(username);
        }
    }

    @Override public void savePassword(String username, String password) {
        passwordMap.put(username, password);
    }

    @Override public void saveToken(String token, String username, long expire) {
        tokenMap.put(token, username);
    }

    @Override public String getUsernameFromToken(String token)
        throws TokenNotFoundException {
        String username = tokenMap.get(token);
        if (username == null) {
            throw new TokenNotFoundException(token);
        }
        return username;
    }

    @Override public void saveSalt(String username, String salt) {
        saltMap.put(username, salt);
    }

    @Override public String getSalt(String username) throws SaltNotFoundException {
        String salt = saltMap.get(username);
        if (salt == null) {
            throw new SaltNotFoundException(username);
        }
        return salt;
    }
}
