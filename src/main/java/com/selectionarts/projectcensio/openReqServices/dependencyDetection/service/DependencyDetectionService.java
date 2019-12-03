package com.selectionarts.projectcensio.openReqServices.dependencyDetection.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.selectionarts.projectcensio.model.App;
import com.selectionarts.projectcensio.model.Task;
import com.selectionarts.projectcensio.openReqServices.dependencyDetection.dal.DependencyDetectionProvider;
import com.selectionarts.projectcensio.openReqServices.dependencyDetection.model.bo.DependencyTask;
import com.selectionarts.projectcensio.openReqServices.dependencyDetection.model.dto.RequirementDto;
import com.selectionarts.projectcensio.openReqServices.dependencyDetection.model.mapper.RequirementTaskMapper;
import com.selectionarts.projectcensio.repository.AppRepository;
import com.selectionarts.projectcensio.repository.TaskRepository;

@Service
public class DependencyDetectionService implements IDependencyDetectionService {

	@Autowired
	private DependencyDetectionProvider dependencyDetectionProvider;
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private AppRepository appRepository;
	
	public List<DependencyTask> getDependencies(String appId) {
		
		List<DependencyTask> returnTasks = new ArrayList<DependencyTask>();
		
		Optional<App> optApp = this.appRepository.findById(Long.parseLong(appId));
		if (optApp != null && optApp.isPresent()) {
			
			returnTasks = getDependenciesByTasksSet(optApp.get().getTasks());
		}
		
		return returnTasks;
	}
	
	private List<DependencyTask> getDependenciesByTasksSet(Set<Task> tasks) {
		
		List<RequirementDto> requirements = RequirementTaskMapper.getRequirements(tasks);
		
		List<DependencyTask> returnTasks = this.getDependenciesByTasks(requirements);
		
		return returnTasks;
		
	}
	
	private List<DependencyTask> getDependenciesByTasks(List<RequirementDto> requirements) {
	
		List<DependencyTask> returnTasks = new ArrayList<DependencyTask>();
		
		RequirementDto[] reqs = this.dependencyDetectionProvider.getDependencies(requirements.toArray(new RequirementDto[0]));
		
		for (RequirementDto r : reqs) {

			List<Long> ps = Arrays.stream(r.getPredictions()).boxed().collect(Collectors.toList());
			
			if (r.getId() == 0) {
				
				DependencyTask dt = new DependencyTask(RequirementTaskMapper.getTask(r));
				
				List<Task> predictedTask = this.taskRepository.findByIdIn(ps);
				dt.setPredictions(predictedTask);
				
				returnTasks.add(dt);
			} else {
				Optional<Task> optTask = this.taskRepository.findById(r.getId());
				if (optTask != null && optTask.isPresent()) {
					
					DependencyTask dt = new DependencyTask(optTask.get());
					
					List<Task> predictedTask = this.taskRepository.findByIdIn(ps);
					dt.setPredictions(predictedTask);
					
					returnTasks.add(dt);
				}
			}
		}

		return returnTasks;
	}	

	@Override
	public List<DependencyTask> getDependencies(Set<Task> tasks) {
		
		List<DependencyTask> returnTasks = getDependenciesByTasksSet(tasks);
		
		return returnTasks;
	}
	
}
