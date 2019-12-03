package com.selectionarts.projectcensio.openReqServices.crossReferenceDetection.service;

import java.util.List;
import java.util.Set;

import com.selectionarts.projectcensio.model.Task;
import com.selectionarts.projectcensio.openReqServices.dependencyDetection.model.bo.DependencyTask;

public interface ICrossReferenceDetectionService {

	public List<DependencyTask> getCrossReferences(Set<Task> tasks);
}
