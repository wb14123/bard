package org.apache.saltedpeanuts.model;

import com.bardframework.bard.core.marker.Model;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Model
@Entity
public class Article {
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    public String id;

    @Column
    public String title;

    @Column @Lob
    public String content;

    @Column
    public Date createdAt = new Date();

    @ManyToOne
    public User author;

    public Article() {
    }

    public Article(String id) {
        this.id = id;
    }
}
