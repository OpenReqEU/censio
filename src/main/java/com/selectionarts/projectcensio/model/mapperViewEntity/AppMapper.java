package com.selectionarts.projectcensio.model.mapperViewEntity;

import com.selectionarts.projectcensio.apicontroller.viewmodel.AppViewModel;
import com.selectionarts.projectcensio.apicontroller.viewmodel.TaskViewModel;
import com.selectionarts.projectcensio.apicontroller.viewmodel.UserViewModel;
import com.selectionarts.projectcensio.model.App;
import com.selectionarts.projectcensio.model.AppRegistration;
import com.selectionarts.projectcensio.model.Task;
import com.selectionarts.projectcensio.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class AppMapper {

    public static App convertToAppEntity(AppViewModel appViewModel) {
        App entity = new App(appViewModel.getId(), appViewModel.getTitle(), appViewModel.getDescription(), appViewModel.getTasklabeling());
        return entity;
    }

    public static AppViewModel convertToAppViewModel(App entity, Set<Task> taskSet) {
        AppViewModel viewModel = new AppViewModel();
        viewModel.setId(entity.getId());
        viewModel.setTitle(entity.getTitle());
        viewModel.setDescription(entity.getDescription());
        viewModel.setNbTasks(entity.getTasks().size());
        viewModel.setTaskAdditionalTypes(entity.getTasktypes());
        viewModel.setTasklabeling(entity.getTasklabeling());
        viewModel.setImgaeLocation(entity.getImgaeLocation());

        for(Task task: taskSet)
        {
            TaskViewModel taskViewModel = TaskMapper.convertToTaskViewModel(task);
            viewModel.getTaskViewModelSet().add(taskViewModel);
        }

        for(AppRegistration registration: entity.getSubscriptions())
        {
            UserViewModel userViewModel = UserMapper.convertToUserViewModel(registration.getUser());
            viewModel.getUserViewModelSet().add(userViewModel);
        }


        return viewModel;
    }


    public static AppViewModel convertToAppViewModel(App entity, Set<Task> taskSet, User user) {
        AppViewModel viewModel = new AppViewModel();
        viewModel.setId(entity.getId());
        viewModel.setTitle(entity.getTitle());
        viewModel.setDescription(entity.getDescription());
        viewModel.setNbTasks(entity.getTasks().size());
        viewModel.setTaskAdditionalTypes(entity.getTasktypes());
        viewModel.setTasklabeling(entity.getTasklabeling());
        viewModel.setImgaeLocation(entity.getImgaeLocation());

        for(Task task: taskSet)
        {
            TaskViewModel taskViewModel = TaskMapper.convertToTaskViewModel(task);

            if(user.equals(entity.getCreator()) || user.equals(task.getCreator()))
            {
                taskViewModel.setCreator(true);
            }

            viewModel.getTaskViewModelSet().add(taskViewModel);
        }

        for(AppRegistration registration: entity.getSubscriptions())
        {
            UserViewModel userViewModel = UserMapper.convertToUserViewModel(registration.getUser());
            viewModel.getUserViewModelSet().add(userViewModel);
        }


        return viewModel;
    }

    public static AppViewModel convertToAppViewModel(App entity, List<Task> taskSet, User user) {
    	
        AppViewModel viewModel = new AppViewModel();
        viewModel.setId(entity.getId());
        viewModel.setTitle(entity.getTitle());
        viewModel.setDescription(entity.getDescription());
        viewModel.setNbTasks(entity.getTasks().size());
        viewModel.setTaskAdditionalTypes(entity.getTasktypes());
        viewModel.setTasklabeling(entity.getTasklabeling());
        viewModel.setImgaeLocation(entity.getImgaeLocation());

        for(Task task: taskSet)
        {
            TaskViewModel taskViewModel = TaskMapper.convertToTaskViewModel(task);

            if(user.equals(entity.getCreator()) || user.equals(task.getCreator()))
            {
                taskViewModel.setCreator(true);
            }

            viewModel.getTaskViewModelSet().add(taskViewModel);
        }

        for(AppRegistration registration: entity.getSubscriptions())
        {
            UserViewModel userViewModel = UserMapper.convertToUserViewModel(registration.getUser());
            viewModel.getUserViewModelSet().add(userViewModel);
        }


        return viewModel;
    }

}
