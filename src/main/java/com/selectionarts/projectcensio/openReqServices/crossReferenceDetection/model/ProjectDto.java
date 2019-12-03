package com.selectionarts.projectcensio.openReqServices.crossReferenceDetection.model;

import java.util.List;

public class ProjectDto {

    private String id;
    private List<String> specifiedRequirements;
    
    public ProjectDto() {
		// TODO Auto-generated constructor stub
	}

	public ProjectDto(String id, List<String> specifiedRequirements) {
		super();
		this.id = id;
		this.specifiedRequirements = specifiedRequirements;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getSpecifiedRequirements() {
		return specifiedRequirements;
	}

	public void setSpecifiedRequirements(List<String> specifiedRequirements) {
		this.specifiedRequirements = specifiedRequirements;
	}
}
