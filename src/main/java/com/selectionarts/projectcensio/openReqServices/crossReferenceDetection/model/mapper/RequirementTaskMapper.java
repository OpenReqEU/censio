package com.selectionarts.projectcensio.openReqServices.crossReferenceDetection.model.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.selectionarts.projectcensio.model.Task;
import com.selectionarts.projectcensio.openReqServices.crossReferenceDetection.model.RequirementDto;

public class RequirementTaskMapper {
	
	public static List<RequirementDto> getRequirements(List<Task> tasks) {
		
		List<RequirementDto> requirements = new ArrayList<RequirementDto>();
		
		tasks.stream().forEach(t -> requirements.add(getRequirement(t)));
		
		return requirements;
	}
	
	public static List<RequirementDto> getRequirements(Set<Task> tasks) {
		
		List<RequirementDto> requirements = new ArrayList<RequirementDto>();
		
		tasks.stream().forEach(t -> requirements.add(getRequirement(t)));
		
		return requirements;
	}

	public static RequirementDto getRequirement(Task task) {
		
		RequirementDto r = new RequirementDto();
		
		r.setId(String.valueOf(task.getId()));
		r.setName(task.getTitle());
		r.setText(task.getDescription());
		
		return r;
	}
	
	public static List<Task> getTasks(List<RequirementDto> requirements) {
		
		List<Task> tasks = new ArrayList<Task>();
		
		requirements.stream().forEach(r ->  tasks.add(getTask(r)));
		
		return tasks;
	}

	public static Task getTask(RequirementDto requirement) {
		
		Task t = new Task();
		
		t.setId(Long.parseLong(requirement.getId()));
		t.setTitle(requirement.getName());
		t.setDescription(requirement.getText());
		
		return t;
	}
}
