package com.selectionarts.projectcensio.openReqServices.eDemocracy.service;

import com.selectionarts.projectcensio.openReqServices.eDemocracy.entity.EdemocracyVoting;

public interface IEDemocracyVotingService {

	public EdemocracyVoting getByTaskId(long taskId);
	public EdemocracyVoting getPublicVoting(String uuid);
	
}
