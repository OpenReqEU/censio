package com.selectionarts.projectcensio.apicontroller.viewmodel;

import com.selectionarts.projectcensio.model.enumeration.CommentType;
import lombok.Data;

import java.util.Date;

@Data
public class TaskCommentViewModel implements Comparable<TaskCommentViewModel>{

    private long id;

    private String comment;

    private CommentType type;

    private String userauthor;

    private Date date;

    public TaskCommentViewModel()
    {
        this.comment="";
        this.type = CommentType.NEUTRAL;
        this.userauthor = "";
        this.date = new Date();
    }

    public TaskCommentViewModel(long id, String comment, CommentType type, String userauthor, Date date) {
        this.id = id;
        this.comment = comment;
        this.type = type;
        this.userauthor = userauthor;
        this.date = date;
    }

    @Override
    public int compareTo(TaskCommentViewModel o) {
        return this.getDate().compareTo(o.getDate());
    }
}
