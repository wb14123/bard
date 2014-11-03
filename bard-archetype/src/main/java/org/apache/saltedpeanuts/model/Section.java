package org.apache.saltedpeanuts.model;

import com.bardframework.bard.core.marker.Model;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Model
@Entity
@NamedQueries({
    @NamedQuery(name = "section.author",
        query = "select c from Section c where c.author = :author order by c.id desc"),
    @NamedQuery(name = "section.storyAccepted",
        query = "select c from Section c where c.id > :start and c.story = :story and c.accepted = true"),
    @NamedQuery(name = "section.current",
        query = "select s from Section s where s.id > :start and s.sectionNumber = s.story.sectionCount")
})
public class Section {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    public String id;

    @ManyToOne
    public User author;

    @ManyToOne
    public Story story;

    @Column
    public boolean accepted = false;

    @Column @JsonProperty("section_number")
    public int sectionNumber = 0;

    @Column @Lob
    public String content;

    public Section() {

    }

    public Section(String id) {
        this.id = id;
    }

}
