package com.selectionarts.projectcensio.openReqServices.similarityDetection.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.selectionarts.projectcensio.model.Task;
import com.selectionarts.projectcensio.openReqServices.similarityDetection.dal.SimilartiyDetectionProvider;
import com.selectionarts.projectcensio.openReqServices.similarityDetection.model.SimilarityResponseDto;
import com.selectionarts.projectcensio.openReqServices.similarityDetection.model.JsonProjectDto;
import com.selectionarts.projectcensio.openReqServices.similarityDetection.model.ProjectDto;
import com.selectionarts.projectcensio.openReqServices.similarityDetection.model.RequirementsDto;
import com.selectionarts.projectcensio.openReqServices.similarityDetection.model.mapper.RequirementTaskMapper;

@Service
public class SimilarityDetectionService implements ISimilarityDetectionService {

	@Autowired
	private SimilartiyDetectionProvider similartiyDetectionProvider;
	
    @Value( "${censio.email-link}" )
    private String emaillink;
	
	public String addRequirements(Set<Task> tasks, String appId, String taskId, String userId) {
		
		RequirementsDto r = new RequirementsDto(RequirementTaskMapper.getRequirements(tasks));
		
		String url = emaillink + "similarityDetectionCallbacks/AddReqsCallback/" + appId + "/" + taskId + "/" + userId;
		
		SimilarityResponseDto ar = this.similartiyDetectionProvider.addReqs(
				r,
				url
				);

		return ar.getId();
	}
	
	public String getSimilarities(String projectId, Set<Task> tasks, String appId, String taskId, String userId) {
		
		ProjectDto pDto1 = new ProjectDto();
		pDto1.setId(projectId);
		List<String> taskIds = tasks.stream().map( t -> String.valueOf(t.getId())).collect(Collectors.toList());
		if (taskId.equals("0")) {
			taskIds.add("0");
		}
		pDto1.setSpecifiedRequirements(taskIds);
		
		List<ProjectDto> projects = new ArrayList<>();
		projects.add(pDto1);
		
		JsonProjectDto j = new JsonProjectDto();
		j.setProjects(projects);
		
		String url = emaillink + "similarityDetectionCallbacks/GetSimilarityCallback/" + appId + "/" + taskId + "/" + userId;
		
		SimilarityResponseDto ar = this.similartiyDetectionProvider.getSimilarities(
				j, 
				url, 
				projectId, 
				"0.01");
		
		return ar.getId();
	}
}
