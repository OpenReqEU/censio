package com.selectionarts.projectcensio.openReqServices.prsImprovingRequirementsQuality.model.dto;

import java.util.ArrayList;
import java.util.List;

public class RequirementsDto {
	
    private List<RequirementDto> requirements = null;
    
    public RequirementsDto() {
		// TODO Auto-generated constructor stub
    	requirements = new ArrayList<RequirementDto>();
	}

	public RequirementsDto(List<RequirementDto> requirements) {
		super();
		this.requirements = requirements;
	}

	public List<RequirementDto> getRequirements() {
		return requirements;
	}

	public void setRequirements(List<RequirementDto> requirements) {
		this.requirements = requirements;
	}
}
