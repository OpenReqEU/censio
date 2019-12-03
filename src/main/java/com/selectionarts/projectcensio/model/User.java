package com.selectionarts.projectcensio.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
public class User {


    @Id
    @GeneratedValue
    @Column(unique = true, updatable = false, nullable = false)
    private long id;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(unique = false, nullable = false, length = 100)
    private String password;

    @Column( unique = false, nullable = false, length = 100)
    private String firstName;

    @Column(unique = false, nullable = false, length = 100)
    private String lastName;

    @Column(unique = false, nullable = false, length = 100)
    private String role;

    @Column(unique = false, nullable = false, length = 100)
    private boolean firstlogin;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<AppRegistration> subscriptions;

    @OneToOne(fetch = FetchType.LAZY,
            cascade =  CascadeType.ALL,
            mappedBy = "user")
    private Notification notification;

    public User()
    {
        this.subscriptions = new HashSet<>();
    }

    public User(String email, String firstName, String lastName, String role) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.subscriptions = new HashSet<>();
        this.role = role;
    }

    @Override
    public boolean equals(Object o)
    {
        return this.getId() == ((User) o).getId();
    }
}
