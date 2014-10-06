package org.binwang.bard.util.user.marker;

import org.binwang.bard.util.user.UserDao;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {
    public Class<? extends UserDao> value();
}
