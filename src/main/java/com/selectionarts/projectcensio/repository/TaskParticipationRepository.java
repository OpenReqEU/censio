package com.selectionarts.projectcensio.repository;

import com.selectionarts.projectcensio.model.Task;
import com.selectionarts.projectcensio.model.TaskParticipation;
import com.selectionarts.projectcensio.model.TaskUserPK;
import com.selectionarts.projectcensio.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskParticipationRepository extends JpaRepository<TaskParticipation, TaskUserPK> {

    TaskParticipation findByTaskAndUser(Task task, User user);

}
