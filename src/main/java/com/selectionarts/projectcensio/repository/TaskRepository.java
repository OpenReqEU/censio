package com.selectionarts.projectcensio.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.selectionarts.projectcensio.model.App;
import com.selectionarts.projectcensio.model.Task;
import com.selectionarts.projectcensio.model.User;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
	List<Task> findAllByApp(App app);
	List<Task> findByIdIn(List<Long> ids);


	@Query(" SELECT task FROM  Task task  " +
			"WHERE  task.app = :app and task.enabled = true And NOT EXISTS (Select p from task.participations p  Where p.user = :participant)")
	public Set<Task> getAllNotParticipatedTasksByUser(@Param("app") App app, @Param("participant") User participant);

	@Query(" SELECT task FROM  Task task  " +
			"WHERE  task.app = :app  and task.enabled = true and  NOT EXISTS (Select p from task.comments p  Where p.user = :participant)")
	public Set<Task> getAllNotCommentedTasksByUser( @Param("app") App app, @Param("participant") User participant);


	public Set<Task> findTaskByAppAndEnabledIsTrue(App app);
	
	public List<Task> findTaskByAppAndEnabledIsTrue(App app, Pageable pageable);
	
	public long countTaskByAppAndEnabledIsTrue(App app);

	/*@Query(" SELECT count(participation.rating == 1) FROM  Task task Join task.participations participation " +
			"WHERE  task.app = :app And  NOT EXISTS (Select p from task.comments p  Where p.user = :participant)")
	public Set<Task> getAllNotCommentedTasksByUser( @Param("app") App app, @Param("participant") User participant);*/

}
