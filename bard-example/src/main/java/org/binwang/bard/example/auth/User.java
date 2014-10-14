package org.binwang.bard.example.auth;

import org.binwang.bard.core.marker.Model;

@Model
public class User {
    public String username;
    public String password;
    public String salt;
}
