package com.selectionarts.projectcensio.openReqServices.eDemocracy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.selectionarts.projectcensio.openReqServices.eDemocracy.entity.EdemocracyVoting;

@Repository
public interface EdemocracyVotingRepository extends JpaRepository<EdemocracyVoting, Long> {

	public EdemocracyVoting findByParticipationsUuid(String uuid);
	public List<EdemocracyVoting>findByParticipationsUsername(String username);
	public EdemocracyVoting findByTaskId(long uuid);
	public EdemocracyVoting findByEDemoProjectId(int uuid);
	
	public EdemocracyVoting findByPublicUuid(String uuid);
}
