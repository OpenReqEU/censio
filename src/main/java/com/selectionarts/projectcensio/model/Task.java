package com.selectionarts.projectcensio.model;

import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
public class Task implements Comparable<Task>{

    @Id
    @GeneratedValue
    @Column(unique = true, updatable = false, nullable = false)
    private long id;


    @Column(unique = true, nullable = false, length = 100)
    private String title;

    @Column( nullable = false, length = 3000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "app_id")
    private App app;

    @OneToMany(mappedBy = "task")
    private Set<TaskParticipation> participations;

    @OneToMany(mappedBy = "task")
    private Set<TaskComment> comments;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> taskAdditionalTypes;

    private Date creationDate;

    private long nrVotest;

    private boolean enabled;

    @OneToOne
    private User creator;

    public Task() {
        this.participations = new HashSet<>();
        this.comments = new TreeSet<>();
        this.taskAdditionalTypes = new ArrayList<>();
        this.enabled = true;
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.participations = new HashSet<>();
        this.comments = new TreeSet<>();
        this.taskAdditionalTypes = new ArrayList<>();
        this.enabled = true;
    }


    public Task(String title, String description, App app, List<String> taskAdditionalTypes) {
        this.title = title;
        this.description = description;
        this.app = app;
        this.participations = new HashSet<>();
        this.comments = new TreeSet<>();
        this.taskAdditionalTypes = taskAdditionalTypes;
        this.enabled = true;
    }

    public Task(String id, String title, String description, App app) {
        if (id != null) {
            this.id = Long.parseLong(id);
        }

        this.title = title;
        this.description = description;
        this.app = app;
        this.enabled = true;
    }

	public Task(long id, String title, String description, App app, Set<TaskParticipation> participations,
			Set<TaskComment> comments) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.app = app;
		this.participations = participations;
		this.comments = comments;
        this.enabled = true;
	}

    @PrePersist
    protected void onCreate() {
        this.creationDate = new Date();
    }

    @Override
    public int compareTo(Task o) {
        return this.creationDate.compareTo(o.creationDate);
    }

    @Override
    public boolean equals(Object o)
    {
        return this.getId() == ((Task) o).getId();
    }
}
