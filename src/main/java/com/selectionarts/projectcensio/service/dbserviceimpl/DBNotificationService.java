package com.selectionarts.projectcensio.service.dbserviceimpl;

import com.selectionarts.projectcensio.model.CustomNotification;
import com.selectionarts.projectcensio.model.Task;
import com.selectionarts.projectcensio.repository.CustomNotificationRepository;
import com.selectionarts.projectcensio.service.NotificationServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;

public class DBNotificationService implements NotificationServiceInterface {

    @Autowired
    private CustomNotificationRepository customNotificationRepository;

    @Override
    public CustomNotification createNotification(CustomNotification customNotification) {
        return this.customNotificationRepository.save(customNotification);
    }

    @Override
    public CustomNotification updateNotification(CustomNotification customNotification) {
        return this.customNotificationRepository.save(customNotification);
    }

}
