package com.selectionarts.projectcensio.openReqServices.prsImprovingRequirementsQuality.model.bo;

import lombok.Data;

@Data
public class DescriptionPart {

	private String text;
	private String color;
	
	public DescriptionPart() {
		// TODO Auto-generated constructor stub
	}
	
	public DescriptionPart(String text, String color) {
		super();
		this.text = text;
		this.color = color;
	}
	
}
