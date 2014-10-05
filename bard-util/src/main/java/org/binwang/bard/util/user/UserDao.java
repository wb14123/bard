package org.binwang.bard.util.user;

public interface UserDao<UserType> {
    public UserType getUser(long userId) throws UserNotFoundException;

    public long getUserIdFromName(String username, String password) throws UserNotFoundException;

    public void saveToken(String token, long userId, long expire);

    public Long getUserIdFromToken(String token) throws TokenNotFoundException;

    public void saveSalt(long userId, String salt);

    public String getSalt(String username) throws SaltNotFoundException;

    public static class UserNotFoundException extends Exception {
        public UserNotFoundException(Long userId) {
            super("User not found for id: " + userId.toString());
        }
    }


    public static class TokenNotFoundException extends Exception {
        public TokenNotFoundException(String token) {
            super("Token not found: " + token);
        }
    }


    public static class SaltNotFoundException extends Exception {
        public SaltNotFoundException(String username) {
            super("Salt not found for user: " + username);
        }
    }
}
