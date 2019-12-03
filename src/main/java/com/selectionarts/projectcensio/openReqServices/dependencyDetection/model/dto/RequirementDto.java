package com.selectionarts.projectcensio.openReqServices.dependencyDetection.model.dto;

import java.util.List;

public class RequirementDto {
	
    private long id;
    private String title;
    private String description;
    private long[] predictions;
    private List<String> comments;
    
    public RequirementDto() {
		// TODO Auto-generated constructor stub
	}

	public RequirementDto(long id, String title, String description, long[] predictions, List<String> comments) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.predictions = predictions;
		this.comments = comments;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long[] getPredictions() {
		return predictions;
	}

	public void setPredictions(long[] predictions) {
		this.predictions = predictions;
	}

	public List<String> getComments() {
		return comments;
	}

	public void setComments(List<String> comments) {
		this.comments = comments;
	}
    
    
}
