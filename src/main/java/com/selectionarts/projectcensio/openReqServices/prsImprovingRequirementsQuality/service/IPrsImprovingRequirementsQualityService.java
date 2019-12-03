package com.selectionarts.projectcensio.openReqServices.prsImprovingRequirementsQuality.service;

import java.util.List;
import java.util.Set;

import com.selectionarts.projectcensio.model.Task;
import com.selectionarts.projectcensio.openReqServices.prsImprovingRequirementsQuality.model.bo.ImprovementTask;

public interface IPrsImprovingRequirementsQualityService {

	public List<ImprovementTask> getImprovedRequirements(Set<Task> tasks);
}
