package org.binwang.bard.util.user;

import org.apache.commons.codec.digest.DigestUtils;
import org.binwang.bard.basic.marker.ErrorCase;
import org.binwang.bard.basic.marker.HandleErrors;
import org.binwang.bard.core.Filter;
import org.binwang.bard.core.marker.Before;
import org.binwang.bard.util.user.marker.Login;

import javax.ws.rs.QueryParam;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class LoginFilter extends Filter<Login> {
    @HandleErrors({
        @ErrorCase(code = 403, logLevel = "DEBUG", description = "Username or password error",
            exception = UserDao.UserNotFoundException.class),
        @ErrorCase(code = 500, logLevel = "DEBUG", description = "Salt not found for user",
            exception = UserDao.SaltNotFoundException.class),
        @ErrorCase(code = 500, logLevel = "DEBUG", description = "Encrypt password error",
            exception = NoSuchAlgorithmException.class)
    })
    @Before public void login(
        @QueryParam("username") String username,
        @QueryParam("password") String password)
        throws UserDao.UserNotFoundException, UserDao.SaltNotFoundException,
        NoSuchAlgorithmException, InstantiationException, IllegalAccessException {
        String salt = getUserDao().getSalt(username);
        String saltPassword = password + salt;
        String encryptPassword = DigestUtils.sha256Hex(saltPassword.getBytes());
        Long userId = getUserDao().getUserIdFromName(username, encryptPassword);
        String token = UUID.randomUUID().toString();
        getUserDao().saveToken(token, userId, 1000 * 60 * 24 * 7);
    }

    @Override public void generateDoc() {
    }

    private UserDao getUserDao() throws IllegalAccessException, InstantiationException {
        UserDao dao = annotation.value().newInstance();
        return dao.getInstance();
    }
}
