package org.binwang.bard.util.user;

public interface UserDao<UserDaoType extends UserDao, UserType> {
    /**
     * Get static UserDao. Used to get UserDao from class type.
     *
     * @return A global static UserDao.
     */
    public UserDaoType getInstance();

    /**
     * Get user from username.
     *
     * @param username The username.
     * @return A user.
     * @throws UserNotFoundException
     */
    public UserType getUser(String username) throws UserNotFoundException;

    /**
     * Save a user.
     *
     * @param user The user.
     */
    public void saveUser(UserType user);

    /**
     * Check if there is a user with specified username and password. It will thrown an
     * exception if no such user found.
     *
     * @param username The username.
     * @param password The encrypted password.
     * @throws UserNotFoundException
     */
    public void checkPassword(String username, String password) throws UserNotFoundException;

    /**
     * Save a user's password
     *
     * @param username The username.
     * @param password The encrypted password.
     */
    public void savePassword(String username, String password);

    /**
     * Save a user's token, with an expire time.
     *
     * @param token    The token.
     * @param username The username.
     * @param expire   Expire time, in millisecond.
     */
    public void saveToken(String token, String username, long expire);

    /**
     * Get user's id from token.
     *
     * @param token The token.
     * @return The found user's id.
     * @throws TokenNotFoundException
     */
    public String getUsernameFromToken(String token) throws TokenNotFoundException;

    /**
     * Save a user's password salt.
     *
     * @param username The username.
     * @param salt     The salt.
     */
    public void saveSalt(String username, String salt);

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
        public UserNotFoundException(String username) {
            super("User not found for id: " + username);
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
