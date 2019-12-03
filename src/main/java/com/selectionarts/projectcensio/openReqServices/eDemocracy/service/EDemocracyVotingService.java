package com.selectionarts.projectcensio.openReqServices.eDemocracy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.selectionarts.projectcensio.openReqServices.eDemocracy.entity.EdemocracyVoting;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.repository.EdemocracyVotingRepository;

@Service
public class EDemocracyVotingService implements IEDemocracyVotingService {

	
	@Autowired
	private EdemocracyVotingRepository edemocracyVotingRepository;
	
	public EdemocracyVoting getByTaskId(long taskId) {
		
		EdemocracyVoting ev = this.edemocracyVotingRepository.findByTaskId(taskId);
		
		return ev;
	}
	
	public EdemocracyVoting getPublicVoting(String uuid) {
		
		
		EdemocracyVoting edemocracyVoting = this.edemocracyVotingRepository.findByPublicUuid(uuid);
		
		
		return edemocracyVoting;
	}
}
