package com.selectionarts.projectcensio.openReqServices.dependencyDetection.model.bo;

import java.util.List;
import com.selectionarts.projectcensio.model.Task;

import lombok.Data;

@Data
public class DependencyTask extends Task {
	
	private List<Task> predictions;

	public DependencyTask() {
		// TODO Auto-generated constructor stub
	}
	
	public DependencyTask(Task t) {
		super(t.getId(), 
				t.getTitle(), 
				t.getDescription(), 
				t.getApp(), 
				t.getParticipations(), 
				t.getComments());
	}

}
