package com.selectionarts.projectcensio.openReqServices.crossReferenceDetection.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.selectionarts.projectcensio.model.Task;
import com.selectionarts.projectcensio.openReqServices.crossReferenceDetection.dal.CrossReferenceDetectionProvider;
import com.selectionarts.projectcensio.openReqServices.crossReferenceDetection.model.DependencyDto;
import com.selectionarts.projectcensio.openReqServices.crossReferenceDetection.model.JsonProjectsReqsDepDto;
import com.selectionarts.projectcensio.openReqServices.crossReferenceDetection.model.JsonProjectsReqsDto;
import com.selectionarts.projectcensio.openReqServices.crossReferenceDetection.model.ProjectDto;
import com.selectionarts.projectcensio.openReqServices.crossReferenceDetection.model.RequirementDto;
import com.selectionarts.projectcensio.openReqServices.crossReferenceDetection.model.mapper.RequirementTaskMapper;
import com.selectionarts.projectcensio.openReqServices.dependencyDetection.model.bo.DependencyTask;

@Service
public class CrossReferenceDetectionService implements ICrossReferenceDetectionService {

	@Autowired
	private CrossReferenceDetectionProvider crossReferenceDetectionProvider;
	
	private final static String idPrefix = "QTBUG-";
	
	public List<DependencyTask> getCrossReferences(Set<Task> tasks) {
		
		String projectId = "openCall123456789";
		
		Pattern p = Pattern.compile("\\d+");
		tasks.forEach(t -> {
	        Matcher m1 = p.matcher(t.getDescription());
	        while(m1.find()) {
	        	t.setDescription(t.getDescription().replace(m1.group(), idPrefix + m1.group()));
	        }
	        Matcher m2 = p.matcher(t.getTitle());
	        while(m2.find()) {
	        	t.setTitle(t.getTitle().replace(m2.group(), idPrefix + m2.group()));
	        }
		});
		
		List<String> taskIds = tasks.stream().map( t -> (idPrefix + String.valueOf(t.getId()))).collect(Collectors.toList());
		
		ProjectDto pDto1 = new ProjectDto();
		pDto1.setId(projectId);
		pDto1.setSpecifiedRequirements(taskIds);
		
		List<ProjectDto> projects = new ArrayList<>();
		projects.add(pDto1);
		
		JsonProjectsReqsDto j = new JsonProjectsReqsDto();
		j.setProjects(projects);
		List<RequirementDto> requirements = RequirementTaskMapper.getRequirements(tasks);
		requirements.stream().forEach(t -> t.setId(idPrefix + t.getId()));
		j.setRequirements(requirements);
		
		JsonProjectsReqsDepDto output = this.crossReferenceDetectionProvider.getCrossReferences(j, projectId);
		
		output.getDependencies().forEach(d -> { 
			d.setFromid(d.getFromid().replace(idPrefix, ""));
			d.setToid(d.getToid().replace(idPrefix, ""));
		});
		List<DependencyTask> returnTasks = tasks.stream().map(t -> new DependencyTask(t) ).collect(Collectors.toList());
		
		for (DependencyDto d : output.getDependencies()) {

			Optional<DependencyTask> odt = returnTasks.stream().filter(t -> String.valueOf(t.getId()).equals(d.getFromid())).findFirst();
			if (odt != null && odt.isPresent()) {
				Optional<DependencyTask> odt2 = returnTasks.stream().filter(t -> String.valueOf(t.getId()).equals(d.getToid())).findFirst();
				if (odt2 != null && odt2.isPresent()) {
					if (odt.get().getPredictions() == null) {
						odt.get().setPredictions(new ArrayList<Task>());
					}
					odt.get().getPredictions().add(odt2.get());
				}
			}
		}
		
		returnTasks.forEach(t -> {
			t.setTitle(t.getTitle().replace(idPrefix, ""));
			t.setDescription(t.getDescription().replace(idPrefix, ""));
		});
		
		return returnTasks;
	}
}
