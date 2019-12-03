package com.selectionarts.projectcensio.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Data
public class Notification {

    @Id
    @GeneratedValue
    @Column(unique = true, nullable = false)
    private long id;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "notification", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<CustomNotification> notifications;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;


    public Notification() {
        this.notifications = new TreeSet<>();
    }
}
