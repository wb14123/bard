package com.bardframework.bard.example.crud;

import com.bardframework.bard.core.marker.Model;

@Model
public class User {
    public int id;
    public String name;
    public Location location = new Location();
}
