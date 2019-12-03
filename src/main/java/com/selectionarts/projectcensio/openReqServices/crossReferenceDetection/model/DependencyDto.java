package com.selectionarts.projectcensio.openReqServices.crossReferenceDetection.model;

import java.util.List;

public class DependencyDto {

    private String fromid;
    private String toid;
    private String dependency_type;
    private String status;
    private List<Component> description;
    
    public DependencyDto() {
		// TODO Auto-generated constructor stub
	}
    
	public DependencyDto(String fromid, String toid, String dependency_type, String status, Float dependency_score,
			List<Component> description) {
		super();
		this.fromid = fromid;
		this.toid = toid;
		this.dependency_type = dependency_type;
		this.status = status;
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

	public List<Component> getDescription() {
		return description;
	}

	public void setDescription(List<Component> description) {
		this.description = description;
	}
    
    
}
