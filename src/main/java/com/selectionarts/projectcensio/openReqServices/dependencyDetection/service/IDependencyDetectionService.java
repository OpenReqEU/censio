package com.selectionarts.projectcensio.openReqServices.dependencyDetection.service;

import java.util.List;
import java.util.Set;

import com.selectionarts.projectcensio.model.Task;
import com.selectionarts.projectcensio.openReqServices.dependencyDetection.model.bo.DependencyTask;

public interface IDependencyDetectionService {
	
	public List<DependencyTask> getDependencies(String appId);
	
	public List<DependencyTask> getDependencies(Set<Task> tasks);
}
