package com.selectionarts.projectcensio.apicontroller.viewmodel;

import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Data
public class NotificationViewModel {

    private Set<CustomNotificationViewModel> notifications;

    public NotificationViewModel() {
        this.notifications = new TreeSet<>();
    }


}
