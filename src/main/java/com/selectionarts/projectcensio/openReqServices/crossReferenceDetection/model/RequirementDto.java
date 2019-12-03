package com.selectionarts.projectcensio.openReqServices.crossReferenceDetection.model;

public class RequirementDto {
	
    private String id;
    private String name;
    private String text;

    
    public RequirementDto() {
		// TODO Auto-generated constructor stub
	}


	public RequirementDto(String id, String name, String text) {
		super();
		this.id = id;
		this.name = name;
		this.text = text;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}
    
}
