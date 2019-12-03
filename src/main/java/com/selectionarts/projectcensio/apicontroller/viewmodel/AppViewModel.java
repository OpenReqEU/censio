package com.selectionarts.projectcensio.apicontroller.viewmodel;

import com.selectionarts.projectcensio.model.Types;
import lombok.Data;

import java.util.*;

@Data
public class AppViewModel  implements Comparable<AppViewModel>{

    private long id;

    private String title;

    private String description;

    private String tasklabeling;

    private String imgaeLocation;
    
    private int nbTasks;

    private List<TaskViewModel> taskViewModelSet;

    private Set<UserViewModel> userViewModelSet;

    private Date date;

    private List<Types> taskAdditionalTypes;

    private boolean update;

    public AppViewModel()
    {
        this.taskViewModelSet = new ArrayList<>();
        this.userViewModelSet = new TreeSet<>();
        this.taskAdditionalTypes = new ArrayList<>();
    }

    @Override
    public int compareTo(AppViewModel o) {
        return this.getDate().compareTo(o.getDate());
    }
}
