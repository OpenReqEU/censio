package com.selectionarts.projectcensio.openReqServices.eDemocracy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.selectionarts.projectcensio.openReqServices.eDemocracy.entity.EdemocracyParticipation;

@Repository
public interface EdemocracyParticipationRepository extends JpaRepository<EdemocracyParticipation, Long> {

	public List<EdemocracyParticipation> findByUsername(String username);
	
	public EdemocracyParticipation findByUuid(String uuid);
}
