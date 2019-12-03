package com.selectionarts.projectcensio.openReqServices.prsImprovingRequirementsQuality.model.bo;

import java.util.List;
import com.selectionarts.projectcensio.model.Task;
import com.selectionarts.projectcensio.openReqServices.prsImprovingRequirementsQuality.model.dto.Improvement;

import lombok.Data;

@Data
public class ImprovementTask extends Task {
	
	private List<Improvement> improvements;
	
	private List<DescriptionPart> descriptionParts;
	
	public ImprovementTask(Task t) {
		super(t.getId(), 
				t.getTitle(), 
				t.getDescription(), 
				t.getApp(), 
				t.getParticipations(), 
				t.getComments());
	}

}
