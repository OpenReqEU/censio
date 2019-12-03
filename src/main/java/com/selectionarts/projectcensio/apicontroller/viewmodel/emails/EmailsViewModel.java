package com.selectionarts.projectcensio.apicontroller.viewmodel.emails;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class EmailsViewModel {
    public List<EmailView> emails;

    public EmailsViewModel() {
        this.emails = new ArrayList<>();
    }

    public EmailsViewModel(List<EmailView> emails) {
        this.emails = emails;
    }
}
