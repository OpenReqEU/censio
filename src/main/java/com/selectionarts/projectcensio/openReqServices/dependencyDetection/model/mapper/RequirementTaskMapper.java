package com.selectionarts.projectcensio.openReqServices.dependencyDetection.model.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.selectionarts.projectcensio.model.Task;
import com.selectionarts.projectcensio.model.TaskComment;
import com.selectionarts.projectcensio.openReqServices.dependencyDetection.model.dto.RequirementDto;

public class RequirementTaskMapper {
	
	public static List<RequirementDto> getRequirements(List<Task> tasks) {
		
		List<RequirementDto> requirements = new ArrayList<RequirementDto>();
		
		tasks.stream().forEach(t ->  requirements.add(getRequirement(t)));
		
		return requirements;
	}
	
	public static List<RequirementDto> getRequirements(Set<Task> tasks) {
		
		List<RequirementDto> requirements = new ArrayList<RequirementDto>();
		
		tasks.stream().forEach(t ->  requirements.add(getRequirement(t)));
		
		return requirements;
	}

	public static RequirementDto getRequirement(Task task) {
		
		RequirementDto r = new RequirementDto();
		
		r.setId(task.getId());
		r.setTitle(task.getTitle());
		r.setDescription(task.getDescription());
		r.setComments(task.getComments().stream().map(c -> c.getComment()).collect(Collectors.toList()));
		
		return r;
	}
	
	public static List<Task> getTasks(List<RequirementDto> requirements) {
		
		List<Task> tasks = new ArrayList<Task>();
		
		requirements.stream().forEach(r ->  tasks.add(getTask(r)));
		
		return tasks;
	}

	public static Task getTask(RequirementDto requirement) {
		
		Task t = new Task();
		
		t.setId(requirement.getId());
		t.setTitle(requirement.getTitle());
		t.setDescription(requirement.getDescription());
		t.setComments(requirement.getComments().stream().collect(
				Collectors.mapping(c -> new TaskComment(c)
				, Collectors.toSet())
				)
		);
		
		return t;
	}
}
