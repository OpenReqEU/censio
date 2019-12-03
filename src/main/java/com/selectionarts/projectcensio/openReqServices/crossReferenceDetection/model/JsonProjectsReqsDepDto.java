package com.selectionarts.projectcensio.openReqServices.crossReferenceDetection.model;

import java.util.ArrayList;
import java.util.List;

public class JsonProjectsReqsDepDto {
	
    private List<ProjectDto> projects;
    private List<RequirementDto> requirements;
    private List<DependencyDto> dependencies;

    public JsonProjectsReqsDepDto() {
        this.projects = new ArrayList<>();
    }

	public List<ProjectDto> getProjects() {
		return projects;
	}

	public void setProjects(List<ProjectDto> projects) {
		this.projects = projects;
	}

	public List<RequirementDto> getRequirements() {
		return requirements;
	}

	public void setRequirements(List<RequirementDto> requirements) {
		this.requirements = requirements;
	}

	public List<DependencyDto> getDependencies() {
		return dependencies;
	}

	public void setDependencies(List<DependencyDto> dependencies) {
		this.dependencies = dependencies;
	}
	
}
