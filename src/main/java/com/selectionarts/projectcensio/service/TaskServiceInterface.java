package com.selectionarts.projectcensio.service;

import com.selectionarts.projectcensio.model.App;
import com.selectionarts.projectcensio.model.Task;
import com.selectionarts.projectcensio.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Pageable;

public interface TaskServiceInterface {

    Task createTask(Task task);

    Task updateTask(Task task);

    void deleteTask(Task task);

    Optional<Task> findById(Long id);

    List<Task> findAll();

    Set<Task> findAllByApp(App app);
    
    public List<Task> findAllByApp(App app, Pageable pageable);

    Task save(Task task);

    void calculateTasksForUser(User user);
    
    public long getNumberOfTasks(App app);
}
