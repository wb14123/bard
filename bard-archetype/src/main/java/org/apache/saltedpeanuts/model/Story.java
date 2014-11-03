package org.apache.saltedpeanuts.model;

import com.bardframework.bard.core.marker.Model;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Model
@Entity
public class Story {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    public String id;

    @Column(nullable = false)
    public String title;

    @Column
    public String description;

    @Column @JsonProperty("section_count")
    public int sectionCount;

    @ManyToOne
    public User author;

    public Story() {
    }

    public Story(String id) {
        this.id = id;
    }
}
