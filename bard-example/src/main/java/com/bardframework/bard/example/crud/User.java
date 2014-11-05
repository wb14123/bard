package com.bardframework.bard.example.crud;

import com.bardframework.bard.core.marker.Model;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

@Model
public class User {
    @JsonPropertyDescription("Id of the user")
    public int id;

    @JsonPropertyDescription("Name of the user")
    public String name;

    @JsonPropertyDescription("Location of the user")
    public Location location = new Location();
}
