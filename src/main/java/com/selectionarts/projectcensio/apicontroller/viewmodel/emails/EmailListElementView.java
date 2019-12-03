package com.selectionarts.projectcensio.apicontroller.viewmodel.emails;

import com.selectionarts.projectcensio.apicontroller.viewmodel.TaskViewModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EmailListElementView {
    private long id;
    private String subject;
    private List<String> to;
    private String body;
    private String time;
    private String datetime;
    private String from;
    private String dp;
    private String dpRetina;
    private boolean unread;
    private List<TaskViewModel> openvotetasks;
    private List<TaskViewModel> opencommenttasks;


    public EmailListElementView() {
        this.to = new ArrayList<>();
        this.openvotetasks = new ArrayList<>();
        this.opencommenttasks = new ArrayList<>();
    }
}
