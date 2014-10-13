package org.binwang.bard.example.db;

import org.binwang.bard.core.marker.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Model
@Entity(name = "user")
public class User {
    @Id
    public long id;
    @Column(name = "name", nullable = false)
    public String name;
}
