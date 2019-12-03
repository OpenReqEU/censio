package com.selectionarts.projectcensio.model.mapperViewEntity;

import com.selectionarts.projectcensio.apicontroller.viewmodel.AppViewModel;
import com.selectionarts.projectcensio.apicontroller.viewmodel.CustomNotificationViewModel;
import com.selectionarts.projectcensio.apicontroller.viewmodel.NotificationViewModel;
import com.selectionarts.projectcensio.apicontroller.viewmodel.TaskCommentViewModel;
import com.selectionarts.projectcensio.model.*;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {


    public static NotificationViewModel convertToNotificationViewModel(Notification notification) {

        NotificationViewModel notificationViewModel = new NotificationViewModel();
        for(CustomNotification customnotification: notification.getNotifications())
        {
            CustomNotificationViewModel customNotificationViewModel = new CustomNotificationViewModel();
            AppViewModel viewModel = new AppViewModel();
            viewModel.setTitle(customnotification.getApp().getTitle());
            customNotificationViewModel.setApp(viewModel);
            customNotificationViewModel.setType(customnotification.getType());
            customNotificationViewModel.setDate(customnotification.getDate());
            notificationViewModel.getNotifications().add(customNotificationViewModel);
        }

        return notificationViewModel;
    }
}
