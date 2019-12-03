package com.selectionarts.projectcensio.service.dbserviceimpl;

import com.selectionarts.projectcensio.model.*;
import com.selectionarts.projectcensio.model.enumeration.NotificationType;
import com.selectionarts.projectcensio.repository.AppRepository;
import com.selectionarts.projectcensio.repository.CustomNotificationRepository;
import com.selectionarts.projectcensio.repository.TaskRepository;
import com.selectionarts.projectcensio.service.TaskServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DBTaskService implements TaskServiceInterface {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private CustomNotificationRepository customNotificationRepository;

    @Override
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }

    @Override
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public List<Task> findAll() {
        return (List<Task>) taskRepository.findAll();
    }

    @Override
    public Set<Task> findAllByApp(App app) {
        return taskRepository.findTaskByAppAndEnabledIsTrue(app);
    }
    
    @Override
    public List<Task> findAllByApp(App app, Pageable pageable) {
        return taskRepository.findTaskByAppAndEnabledIsTrue(app, pageable);
    }
    
    @Override
    public long getNumberOfTasks(App app) {
        return taskRepository.countTaskByAppAndEnabledIsTrue(app);
    }

    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public void calculateTasksForUser(User user)
    {

        Set<App> appIterable = appRepository.findAppsBySubscriptionsInAndEnabledIsTrue(user.getSubscriptions());

        for(App  app: appIterable)
        {
            System.out.println("App Title: " + app.getTitle());
            Set<Task> openTasksVotes = taskRepository.getAllNotParticipatedTasksByUser(app,user);
            Set<Task> openTasksComments = taskRepository.getAllNotCommentedTasksByUser(app,user);

             CustomNotification customNotification = customNotificationRepository.findByNotificationAndApp(user.getNotification(),app);

            if(customNotification == null)
            {
                customNotification = new CustomNotification();
                customNotification.setNotification(user.getNotification());
                customNotification.setApp(app);
                customNotification.setDate(new Date());
                customNotification.setUnread(true);
                customNotification.setType(NotificationType.CREATE);
                customNotification.setUnvotedtasks(new TreeSet<>());
                customNotification.setUncommentedtasks(new TreeSet<>());

            }


            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(customNotification.getDate());
            cal2.setTime(new Date());
            boolean sameDay = cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                    cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);

            if(!sameDay)
            {
                customNotification.setUnvotedtasks(new TreeSet<>());
                customNotification.setUncommentedtasks(new TreeSet<>());
                customNotification.setDate(new Date());
                customNotification.setUnread(true);
            }



            for(Task opentask : openTasksVotes)
            {
                if(!customNotification.getUnvotedtasks().contains(opentask))
                {
                    customNotification.getUnvotedtasks().add(opentask);
                    customNotification.setUnread(true);
                }
            }

            for(Task opencomment : openTasksComments)
            {
                if(!customNotification.getUncommentedtasks().contains(opencomment))
                {
                    customNotification.getUncommentedtasks().add(opencomment);
                    customNotification.setUnread(true);
                }
            }




            customNotificationRepository.save(customNotification);
        }

    }


}
