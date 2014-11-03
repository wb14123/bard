package org.apache.saltedpeanuts.model;

import com.bardframework.bard.core.marker.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.Session;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.criterion.Restrictions;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Model
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    public String id;

    @Column(nullable = false, unique = true)
    public String username;

    @Column(nullable = false)
    @JsonIgnore
    public String password;

    @Column(nullable = false)
    @JsonIgnore
    public String salt;

    @Column
    public String email;

    public User() {

    }

    public User(String id) {
        this.id = id;
    }

    public static User getUserByUsername(Session session, String username) {
        return (User) session.createCriteria(User.class)
            .add(Restrictions.eq("username", username))
            .uniqueResult();
    }
}
