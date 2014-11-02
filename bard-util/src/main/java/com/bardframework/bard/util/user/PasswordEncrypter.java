package com.bardframework.bard.util.user;

import org.apache.commons.codec.digest.DigestUtils;

import java.math.BigInteger;
import java.security.SecureRandom;

public class PasswordEncrypter {
    /**
     * Encrypt a password string with a random generated salt.
     *
     * @param password The origin password need to be encrypt.
     * @return An array with two elements: The first one is the encrypted password and the second one is the salt.
     */
    public static String[] encrypt(String password) {
        SecureRandom random = new SecureRandom();
        String salt = new BigInteger(130, random).toString(32);
        String encryptPassword = encrypt(password, salt);
        return new String[] {encryptPassword, salt};
    }

    /**
     * Encrypt a password string with a given password.
     *
     * @param password The origin password need to be encrypt.
     * @param salt     The salt.
     * @return The encrypted password.
     */
    public static String encrypt(String password, String salt) {
        String saltPassword = password + salt;
        return DigestUtils.sha256Hex(saltPassword.getBytes());
    }
}
