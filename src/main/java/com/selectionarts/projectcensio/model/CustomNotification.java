package com.selectionarts.projectcensio.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.selectionarts.projectcensio.model.enumeration.NotificationType;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;


@Entity
@Data
public class CustomNotification implements Comparable<CustomNotification>{

    @Id
    @GeneratedValue
    @Column(unique = true, updatable = false, nullable = false)
    private long id;

    @OneToOne
    private App app;

    private NotificationType type;

    @ManyToOne
    @JoinColumn(name = "notification_id")
    private Notification notification;

    private Date date;

    private boolean unread;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Task> unvotedtasks;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Task> uncommentedtasks;

    public CustomNotification(App app, NotificationType type, Notification notification, Date date) {
        this.app = app;
        this.type = type;
        this.notification = notification;
        this.date = date;
        this.unread = true;
    }

    public CustomNotification() {
    }

    @Override
    public boolean equals(Object o)
    {
        return this.getId() == ((CustomNotification) o).getId();
    }

    @Override
    public int compareTo(CustomNotification o) {
        return this.getDate().compareTo(o.getDate());
    }

    /**Can be expanded whenever we need special content for the notification**/

}
