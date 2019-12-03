package com.selectionarts.projectcensio.model.mapperViewEntity;

import com.selectionarts.projectcensio.apicontroller.viewmodel.TaskCommentViewModel;
import com.selectionarts.projectcensio.apicontroller.viewmodel.TaskViewModel;
import com.selectionarts.projectcensio.model.Task;
import com.selectionarts.projectcensio.model.TaskComment;
import com.selectionarts.projectcensio.model.User;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TaskCommentMapper {

    public static TaskComment convertToTaskEntity(TaskCommentViewModel taskCommentViewModel, Task task, User user) {

        TaskComment taskComment = new TaskComment(
                taskCommentViewModel.getComment(),
                taskCommentViewModel.getType(),
                task,user,taskCommentViewModel.getDate());

        return taskComment;
    }

    public static TaskCommentViewModel convertToTaskViewModel(TaskComment taskComment) {

        TaskCommentViewModel taskCommentViewModel =
                new TaskCommentViewModel(taskComment.getId(),
                        taskComment.getComment(),
                        taskComment.getType(),
                        taskComment.getUser().getEmail(),
                        taskComment.getDate());

        return taskCommentViewModel;
    }
}
