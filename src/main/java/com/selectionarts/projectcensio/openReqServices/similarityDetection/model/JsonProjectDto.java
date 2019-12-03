package com.selectionarts.projectcensio.openReqServices.similarityDetection.model;

import java.util.ArrayList;
import java.util.List;

public class JsonProjectDto {
	
	private List<ProjectDto> projects;

    public JsonProjectDto() {
        this.projects = new ArrayList<>();
    }

    public List<ProjectDto> getProjects() {
        return projects;
    }

	public void setProjects(List<ProjectDto> projects) {
		this.projects = projects;
	}
}
