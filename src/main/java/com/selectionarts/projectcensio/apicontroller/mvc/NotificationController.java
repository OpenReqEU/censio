package com.selectionarts.projectcensio.apicontroller.mvc;


import com.selectionarts.projectcensio.apicontroller.viewmodel.NotificationOverview;
import com.selectionarts.projectcensio.apicontroller.viewmodel.NotificationViewModel;
import com.selectionarts.projectcensio.apicontroller.viewmodel.TaskViewModel;
import com.selectionarts.projectcensio.apicontroller.viewmodel.emails.EmailListElementView;
import com.selectionarts.projectcensio.apicontroller.viewmodel.emails.EmailView;
import com.selectionarts.projectcensio.apicontroller.viewmodel.emails.EmailsViewModel;
import com.selectionarts.projectcensio.model.*;
import com.selectionarts.projectcensio.model.enumeration.NotificationType;
import com.selectionarts.projectcensio.model.mapperViewEntity.NotificationMapper;
import com.selectionarts.projectcensio.model.mapperViewEntity.TaskMapper;
import com.selectionarts.projectcensio.repository.AppRepository;
import com.selectionarts.projectcensio.repository.CustomNotificationRepository;
import com.selectionarts.projectcensio.repository.TaskRepository;
import com.selectionarts.projectcensio.service.dbserviceimpl.DBUserService;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;
import javax.xml.bind.SchemaOutputResolver;
import java.security.Principal;
import java.util.*;

@Controller
@Transactional
public class NotificationController {

    @Autowired
    private DBUserService userService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private CustomNotificationRepository customNotificationRepository;

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private TaskRepository taskRepository;





    @GetMapping("/inbox")
    public String getNotifications(Authentication authentication, Model model, Principal principal)
    {

        long userid = ((UserDetailsDbo)authentication.getPrincipal()).getId();
        User user = userService.findUserById(userid).get();

        Notification notification = user.getNotification();
        NotificationViewModel notificationViewModel = NotificationMapper.convertToNotificationViewModel(notification);

        model.addAttribute("notification", notificationViewModel);


        return "inbox";
    }



    @SubscribeMapping("/user/notification")
    public NotificationOverview getNewNotificiation(Authentication authentication , Principal principal)
    {

        long userid = ((UserDetailsDbo)authentication.getPrincipal()).getId();
        User user = userService.findUserById(userid).get();

        Notification notification = user.getNotification();
        Set<CustomNotification> customNotificationSet = customNotificationRepository.findAllByNotificationAndUnreadEquals(notification,true);
        Set<App> customApplicationSet = appRepository.findAppsBySubscriptionsInAndEnabledIsTrue(user.getSubscriptions());

        return new NotificationOverview(customNotificationSet.size(), customApplicationSet.size());
        /*simpMessagingTemplate.convertAndSendToUser(principal.getName(),
                "/notification", "New Messages" + customNotificationSet.size());*/

    }

    @GetMapping("/inbox/getMails")
    @ResponseBody
    public EmailsViewModel getMails(Authentication authentication , Principal principal)
    {

        long userid = ((UserDetailsDbo)authentication.getPrincipal()).getId();
        User user = userService.findUserById(userid).get();

        Notification notification = user.getNotification();
        Set<CustomNotification> customNotificationSet = customNotificationRepository.findAllByNotification(notification);

        EmailView emailView = new EmailView();
        emailView.setGroup("System");


        for (CustomNotification customNotification :customNotificationSet )
        {
            EmailListElementView emailListElementView = new EmailListElementView();
            emailListElementView.setDatetime(customNotification.getDate().toString());
            emailListElementView.setTime(customNotification.getDate().toString());
            emailListElementView.setFrom(customNotification.getApp().getTitle());
            emailListElementView.setId(customNotification.getId());
            emailListElementView.setSubject(customNotification.getApp().getTitle());

            emailListElementView.setTo(new ArrayList<>());
            emailListElementView.setUnread(customNotification.isUnread());


            Set<Task> openTasksVotes = taskRepository.getAllNotParticipatedTasksByUser(customNotification.getApp(),user);
            Set<Task> openTasksComments = taskRepository.getAllNotCommentedTasksByUser(customNotification.getApp(),user);


            List<TaskViewModel> taskuncommented = new ArrayList<>();
            customNotification.getUncommentedtasks().stream().forEachOrdered(p ->
                    {
                        TaskViewModel taskViewModel = TaskMapper.convertToTaskViewModel(p);
                        if(!openTasksComments.contains(p))
                        {
                            taskViewModel.setCompleted(true);
                        }
                        taskuncommented.add(taskViewModel);

                    });

            //TODO Check against


            List<TaskViewModel> taskunvoted = new ArrayList<>();
            customNotification.getUnvotedtasks().stream().forEachOrdered(p -> {
                TaskViewModel taskViewModel = TaskMapper.convertToTaskViewModel(p);
                if(!openTasksVotes.contains(p))
                {
                    taskViewModel.setCompleted(true);
                }
                taskunvoted.add(taskViewModel);
            });


            emailListElementView.setOpenvotetasks(taskunvoted);
            emailListElementView.setOpencommenttasks(taskuncommented);
            emailListElementView.setBody("Open Votes (" + openTasksVotes.size() +") \n" +
                    "Open Comments (" + openTasksComments.size() +")");
            emailView.getList().add(emailListElementView);

        }

        ArrayList<EmailView> emailViews = new ArrayList<>();
        emailViews.add(emailView);


        return new EmailsViewModel(emailViews);


    }

    @PostMapping("/inbox/getMails/{id}")
    @ResponseBody
    public void visitId(@PathVariable("id") Long id , Authentication authentication , Principal principal, HttpResponse response)
    {
        long userid = ((UserDetailsDbo)authentication.getPrincipal()).getId();
        User user = userService.findUserById(userid).get();

        Notification notification = user.getNotification();
        CustomNotification customNotification = customNotificationRepository.findById(id).get();

        customNotification.setUnread(false);
        customNotificationRepository.save(customNotification);

        response.setStatusCode(200);

    }






}
