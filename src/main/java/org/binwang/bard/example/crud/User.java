package org.binwang.bard.example.crud;

import org.binwang.bard.core.marker.Model;

@Model
public class User {
    public int id;
    public String name;
    public Location location = new Location();
}