package com.selectionarts.projectcensio.apicontroller.viewmodel;

import lombok.Data;

import java.util.*;

@Data
public class TaskViewModel{

    private long id;

    private String title;

    private String description;

    private String appid;

    private long nrvotes;

    private long nrupvotes;

    private long nrdownvotes;

    private long currentbalanceupvotes;

    private long currentbalancedownvotes;

    private long nrcomments;

    private Set<TaskCommentViewModel> commentViewModels;

    private Date date;

    private List<String> taskAdditionalTypes;

    private boolean completed;

    private boolean creator;

    public TaskViewModel()
    {
    	Comparator<TaskCommentViewModel> comp = (TaskCommentViewModel o1, TaskCommentViewModel o2) -> (o2.compareTo(o1));
        this.commentViewModels = new TreeSet<>(comp);
        this.taskAdditionalTypes = new ArrayList<>();
        this.completed = false;
        this.creator = false;
    }

}
