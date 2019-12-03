package com.selectionarts.projectcensio.apicontroller.viewmodel.emails;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EmailView {
    private String group;
    private List<EmailListElementView> list;

    public EmailView() {
        this.list = new ArrayList<>();
    }
}
