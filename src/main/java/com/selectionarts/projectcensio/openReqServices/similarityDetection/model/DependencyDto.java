package com.selectionarts.projectcensio.openReqServices.similarityDetection.model;

import java.util.List;

public class DependencyDto {

    private String fromid;
    private String toid;
    private String dependency_type;
    private String status;
    private Float dependency_score;
    private List<String> description;
    
    public DependencyDto() {
		// TODO Auto-generated constructor stub
	}
    
	public DependencyDto(String fromid, String toid, String dependency_type, String status, Float dependency_score,
			List<String> description) {
		super();
		this.fromid = fromid;
		this.toid = toid;
		this.dependency_type = dependency_type;
		this.status = status;
		this.dependency_score = dependency_score;
		this.description = description;
	}

	public String getFromid() {
		return fromid;
	}

	public void setFromid(String fromid) {
		this.fromid = fromid;
	}

	public String getToid() {
		return toid;
	}

	public void setToid(String toid) {
		this.toid = toid;
	}

	public String getDependency_type() {
		return dependency_type;
	}

	public void setDependency_type(String dependency_type) {
		this.dependency_type = dependency_type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Float getDependency_score() {
		return dependency_score;
	}

	public void setDependency_score(Float dependency_score) {
		this.dependency_score = dependency_score;
	}

	public List<String> getDescription() {
		return description;
	}

	public void setDescription(List<String> description) {
		this.description = description;
	}
    
    
}
