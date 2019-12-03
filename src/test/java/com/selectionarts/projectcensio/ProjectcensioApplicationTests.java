package com.selectionarts.projectcensio;

import com.selectionarts.projectcensio.model.App;
import com.selectionarts.projectcensio.model.Task;
import com.selectionarts.projectcensio.model.User;
import com.selectionarts.projectcensio.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectcensioApplicationTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppRepository appRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskCommentRepository taskCommentRepository;
    @Autowired
    private TaskParticipationRepository taskParticipationRepository;

    private User user_test1;
    private User user_test2;
    private App app_test;
    private Task task;

    @Before
    public void buildUp() throws Exception {


    }

    @Test
    public void addTaskToLearningApplication() {

        /*Set<Task> tasks = new HashSet<>();
        tasks.add(new Task("test","test22"));

        app_test.setTasks(tasks);*/

    }

}
