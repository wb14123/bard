package org.binwang.bard.util.user;

public interface UserDao<UserType> {
    /**
     * Get static UserDao. Used to get UserDao from class type.
     *
     * @return A global static UserDao.
     */
    public UserDao getInstance();

    /**
     * Get user from userId.
     *
     * @param userId The id of user.
     * @return A user.
     * @throws UserNotFoundException
     */
    public UserType getUser(long userId) throws UserNotFoundException;

    /**
     * Get user from username and password.
     *
     * @param username The username.
     * @param password The encrypted password.
     * @return A user.
     * @throws UserNotFoundException
     */
    public long getUserIdFromName(String username, String password) throws UserNotFoundException;

    /**
     * Save a user's token, with an expire time.
     *
     * @param token  The token.
     * @param userId The id of user.
     * @param expire Expire time, in millisecond.
     */
    public void saveToken(String token, long userId, long expire);

    /**
     * Get user's id from token.
     *
     * @param token The token.
     * @return The found user's id.
     * @throws TokenNotFoundException
     */
    public Long getUserIdFromToken(String token) throws TokenNotFoundException;

    /**
     * Save a user's password salt.
     *
     * @param userId The id of user.
     * @param salt   The salt.
     */
    public void saveSalt(long userId, String salt);

    /**
     * Get the salt from username.
     *
     * @param username The username.
     * @return The salt.
     * @throws SaltNotFoundException
     */
    public String getSalt(String username) throws SaltNotFoundException;

    /**
     * If a user not found, this exception will be thrown.
     */
    public static class UserNotFoundException extends Exception {
        public UserNotFoundException(Long userId) {
            super("User not found for id: " + userId.toString());
        }
    }


    /**
     * If token not found by userId, this exception will be thrown.
     */
    public static class TokenNotFoundException extends Exception {
        public TokenNotFoundException(String token) {
            super("Token not found: " + token);
        }
    }


    /**
     * If salt not found by username, this exception will be thrown.
     */
    public static class SaltNotFoundException extends Exception {
        public SaltNotFoundException(String username) {
            super("Salt not found for user: " + username);
        }
    }
}
