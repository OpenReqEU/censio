package com.selectionarts.projectcensio.apicontroller.viewmodel;

import com.selectionarts.projectcensio.model.App;
import com.selectionarts.projectcensio.model.CustomNotification;
import com.selectionarts.projectcensio.model.Notification;
import com.selectionarts.projectcensio.model.enumeration.NotificationType;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
public class CustomNotificationViewModel implements Comparable<CustomNotificationViewModel>{

    private long id;

    private AppViewModel app;

    private NotificationType type;

    private Date date;


    @Override
    public boolean equals(Object o)
    {
        return this.getId() == ((CustomNotificationViewModel) o).getId();
    }

    @Override
    public int compareTo(CustomNotificationViewModel o) {
        return this.getDate().compareTo(o.getDate());
    }

    public CustomNotificationViewModel() {
    }

    /**Can be expanded whenever we need special content for the notification**/

}
