package org.binwang.bard.util.user;

import org.apache.commons.codec.digest.DigestUtils;
import org.binwang.bard.basic.marker.Required;
import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.Filter;
import org.binwang.bard.core.marker.After;
import org.binwang.bard.util.user.marker.SignUp;

import javax.ws.rs.QueryParam;
import java.math.BigInteger;
import java.security.SecureRandom;

@BindTo(SignUp.class)
public class SignUpFilter extends Filter<SignUp> {
    @After public void signUp(
        @QueryParam("username") @Required String username,
        @QueryParam("password") @Required String password)
        throws IllegalAccessException, InstantiationException {
        UserDao dao = annotation.value().newInstance().getInstance();
        SecureRandom random = new SecureRandom();
        String salt = new BigInteger(130, random).toString(32);
        String saltPassword = password + salt;
        String encryptPassword = DigestUtils.sha256Hex(saltPassword.getBytes());

        dao.savePassword(username, encryptPassword);
        dao.saveSalt(username, salt);
    }

    @Override public void generateDoc() {
    }
}
