package org.apache.saltedpeanuts.model;

import com.bardframework.bard.core.marker.Model;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Model
@Entity
public class Article {
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @JsonPropertyDescription("The id of article")
    public String id;

    @Column
    @JsonPropertyDescription("The title of article")
    public String title;

    @Column @Lob
    @JsonPropertyDescription("Then content of article")
    public String content;

    @Column
    @JsonPropertyDescription("The create time of this article")
    public Date createdAt = new Date();

    @ManyToOne
    @JsonPropertyDescription("The author of this article")
    public User author;

    public Article() {
    }

    public Article(String id) {
        this.id = id;
    }
}
