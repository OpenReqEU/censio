package com.selectionarts.projectcensio.service;

import com.selectionarts.projectcensio.model.CustomNotification;
import com.selectionarts.projectcensio.model.Task;

public interface NotificationServiceInterface {

    CustomNotification createNotification(CustomNotification customNotification);

    CustomNotification updateNotification(CustomNotification customNotification);

}
