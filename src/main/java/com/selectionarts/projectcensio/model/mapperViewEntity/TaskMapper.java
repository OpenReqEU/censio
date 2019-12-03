package com.selectionarts.projectcensio.model.mapperViewEntity;

import com.selectionarts.projectcensio.apicontroller.viewmodel.TaskCommentViewModel;
import com.selectionarts.projectcensio.apicontroller.viewmodel.TaskViewModel;
import com.selectionarts.projectcensio.model.App;
import com.selectionarts.projectcensio.model.Task;
import com.selectionarts.projectcensio.model.TaskComment;
import com.selectionarts.projectcensio.service.dbserviceimpl.DBAppService;
import com.selectionarts.projectcensio.service.dbserviceimpl.DBTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collector;
import java.util.stream.Collectors;

@Component
public class TaskMapper {

    public static Task convertToTaskEntity(TaskViewModel taskViewModel) {
        Task entity = new Task(taskViewModel.getTitle(), taskViewModel.getDescription());
        return entity;
    }

    public static TaskViewModel convertToTaskViewModel(Task task) {
        TaskViewModel viewModel = new TaskViewModel();
        viewModel.setId(task.getId());
        viewModel.setTitle(task.getTitle());
        viewModel.setDescription(task.getDescription());
        viewModel.setAppid(String.valueOf(task.getApp().getId()));

        long totalnr = task.getParticipations().size();
        long totalnrupvotes = task.getParticipations()
                .stream()
                .filter(p->p.getRating() == 1)
                .collect(Collectors.toList()).size();
        long totalnrdownvotes = task.getParticipations()
                .stream()
                .filter(p->p.getRating() == 0)
                .collect(Collectors.toList()).size();

        viewModel.setNrvotes(totalnr);
        viewModel.setNrupvotes(totalnrupvotes);
        viewModel.setNrdownvotes(totalnrdownvotes);
        viewModel.setNrcomments(task.getComments().size());
        viewModel.setTaskAdditionalTypes(task.getTaskAdditionalTypes());

        
        int upBalance = 50;
        if (totalnr > 0) {
        	upBalance = (int)(((float)totalnrupvotes/(float)totalnr) * 100);
        }
        viewModel.setCurrentbalanceupvotes(upBalance);
        
        int downBalance = 50;
        if (totalnr > 0) {
        	downBalance = (int)(((float)totalnrdownvotes/(float)totalnr) * 100);
        }
        if (upBalance + downBalance != 100) {
        	downBalance = 100 - upBalance;
        }
        
        viewModel.setCurrentbalancedownvotes(downBalance);

        for(TaskComment comment: task.getComments())
        {
            TaskCommentViewModel taskCommentViewModel = TaskCommentMapper.convertToTaskViewModel(comment);
            viewModel.getCommentViewModels().add(taskCommentViewModel);
        }

        return viewModel;
    }
}
