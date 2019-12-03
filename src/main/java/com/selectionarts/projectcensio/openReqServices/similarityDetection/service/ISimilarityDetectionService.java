package com.selectionarts.projectcensio.openReqServices.similarityDetection.service;

import java.util.Set;

import com.selectionarts.projectcensio.model.Task;

public interface ISimilarityDetectionService {

	public String addRequirements(Set<Task> tasks, String appId, String taskId, String userId);
	
	public String getSimilarities(String projectId, Set<Task> tasks, String appId, String taskId, String userId);
}
