package com.selectionarts.projectcensio.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
public class App {

    @Id
    @GeneratedValue
    @Column(unique = true, nullable = false)
    private long id;


    @Column(unique = true, nullable = false, length = 100)
    private String title;

    @Column( nullable = false, length = 3000)
    private String description;

    @Column(length = 3000)
    private String tasklabeling;

    private String imgaeLocation;

    @OneToMany(mappedBy = "app")
    private Set<AppRegistration> subscriptions;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "app", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Task> tasks;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "app", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Types> tasktypes;


    @OneToOne
    private User creator;

    private boolean enabled;

    public App()
    {
        this.subscriptions = new HashSet<>();
        this.tasks = new HashSet<>();
        this.tasktypes = new ArrayList<>();
        this.enabled = true;
        this.tasklabeling = "";
    }

    public App(String title, String description) {
        this.title = title;
        this.description = description;
        this.subscriptions = new HashSet<>();
        this.tasks = new TreeSet<>();
        this.tasktypes =  new ArrayList<>();
        this.enabled = true;
        this.tasklabeling = "";
    }

    public App(long id, String title, String description, String tasklabeling) {
        if (Long.valueOf(id) != null) {
            this.id = id;
        }

        this.title = title;
        this.description = description;
        this.enabled = true;
        this.tasklabeling = tasklabeling;

    }

    @Override
    public boolean equals(Object o)
    {
        return this.getId() == ((App) o).getId();
    }

}

