package com.selectionarts.projectcensio.openReqServices.eDemocracy.model.mapper;

import com.selectionarts.projectcensio.model.Task;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.model.EDemoProjectDto;

public class TaskToEdemoProject {

	public TaskToEdemoProject() {
		// TODO Auto-generated constructor stub
	}

	public EDemoProjectDto getEDemoProject(Task t)
	{
		return new EDemoProjectDto(0,t.getTitle(),null,null,null);
	}
	
}
