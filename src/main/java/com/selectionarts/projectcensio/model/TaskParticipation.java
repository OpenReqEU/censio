package com.selectionarts.projectcensio.model;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class TaskParticipation {

    @Id
    @GeneratedValue
    @Column(unique = true, updatable = false, nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    private int rating;

    private Date ratedat;


    @Override
    public boolean equals(Object o)
    {
        return this.getId() == ((TaskParticipation) o).getId();
    }

}
