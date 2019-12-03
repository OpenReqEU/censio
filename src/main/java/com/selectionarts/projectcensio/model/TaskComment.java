package com.selectionarts.projectcensio.model;


import com.selectionarts.projectcensio.model.enumeration.CommentType;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class TaskComment {

    public TaskComment(String comment, CommentType type, Task task, User user, Date date) {
        this.comment = comment;
        this.type = type;
        this.task = task;
        this.user = user;
        this.date = date;
    }


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

    private String comment;

    private CommentType type;

    private Date date;

    public TaskComment() {
	}
    
	public TaskComment(String comment) {
		super();
		this.comment = comment;
	}


    @Override
    public boolean equals(Object o)
    {
        return this.getId() == ((TaskComment) o).getId();
    }

}
