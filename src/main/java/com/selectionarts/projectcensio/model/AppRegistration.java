package com.selectionarts.projectcensio.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class AppRegistration {

    @Id
    @GeneratedValue
    @Column(unique = true, updatable = false, nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "app_id")
    private App app;

    @Column(updatable = false, nullable = false)
    private Date registeredAt;

    @Override
    public boolean equals(Object o)
    {
        return this.getId() == ((AppRegistration) o).getId();
    }

}
