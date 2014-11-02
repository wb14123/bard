package com.bardframework.bard.example.auth;

import com.bardframework.bard.core.marker.Model;

@Model
public class User {
    public String username;
    public String password;
    public String salt;
}
