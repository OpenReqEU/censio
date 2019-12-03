package com.selectionarts.projectcensio.apicontroller;


import com.selectionarts.projectcensio.apicontroller.viewmodel.AppViewModel;
import com.selectionarts.projectcensio.apicontroller.viewmodel.TaskViewModel;
import com.selectionarts.projectcensio.model.App;
import com.selectionarts.projectcensio.model.Task;
import com.selectionarts.projectcensio.model.mapperViewEntity.TaskMapper;
import com.selectionarts.projectcensio.service.dbserviceimpl.DBAppService;
import com.selectionarts.projectcensio.service.dbserviceimpl.DBTaskService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.util.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin
public class TaskController {

    @Autowired
    private DBAppService dbAppService;

    @Autowired
    private DBTaskService dbTaskService;

    @Autowired
    private TaskMapper taskMapper;

    @GetMapping("/all")
    public Set<TaskViewModel> all() {
        List<Task> tasks = dbTaskService.findAll();

        // map from entity to view model
        Set<TaskViewModel> tasksViewModel = tasks.stream()
                .map(task -> taskMapper.convertToTaskViewModel(task))
                .collect(Collectors.toSet());

        return tasksViewModel;
    }

    @GetMapping("/{id}")
    public TaskViewModel byId(@PathVariable long id) {
        Task task = dbTaskService.findById(id).orElse(null);

        if (task == null) {
            throw new EntityNotFoundException();
        }

        TaskViewModel taskViewModel = taskMapper.convertToTaskViewModel(task);

        return taskViewModel;
    }


    @GetMapping("/byApps/{appid}")
    public List<TaskViewModel> byApp(@PathVariable String appid) {
        Set<Task> tasks = new TreeSet<>();

        Optional<App> app = dbAppService.findById(appid);
        if (app.isPresent()) {
            tasks = dbTaskService.findAllByApp(app.get());
        }

        // map to note view model
        List<TaskViewModel> taskViewModel = tasks.stream()
                .map(task -> taskMapper.convertToTaskViewModel(task))
                .collect(Collectors.toList());

        return taskViewModel;
    }

    @PostMapping
    public Task save(@RequestBody TaskViewModel taskViewModel,
                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException();
        }

        Task task = taskMapper.convertToTaskEntity(taskViewModel);
        dbTaskService.save(task);

        return task;
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        try{
            Optional<Task> task = dbTaskService.findById(id);
            if(task.isPresent())
            {
                dbTaskService.deleteTask(task.get());
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
