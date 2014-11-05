package com.bardframework.bard.example.crud;

import com.bardframework.bard.core.marker.Model;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

@Model
public class Location {
    @JsonPropertyDescription("The name of the location")
    public String name;
}
