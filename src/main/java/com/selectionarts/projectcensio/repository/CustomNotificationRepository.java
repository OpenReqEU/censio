package com.selectionarts.projectcensio.repository;

import com.selectionarts.projectcensio.model.App;
import com.selectionarts.projectcensio.model.CustomNotification;
import com.selectionarts.projectcensio.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CustomNotificationRepository extends JpaRepository<CustomNotification, Long> {

    Set<CustomNotification> findAllByNotificationAndUnreadEquals(Notification notification, boolean unread);
    Set<CustomNotification> findAllByNotification(Notification notification);
    CustomNotification findByNotificationAndApp(Notification notification, App app);

}
