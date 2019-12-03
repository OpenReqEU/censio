package com.selectionarts.projectcensio.openReqServices.prsImprovingRequirementsQuality.model.dto;

public class RequirementDto {
	
    private String id;
    private String text;

    
    public RequirementDto() {
		// TODO Auto-generated constructor stub
	}


	public RequirementDto(String id, String text) {
		super();
		this.id = id;
		this.text = text;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}
    
}
