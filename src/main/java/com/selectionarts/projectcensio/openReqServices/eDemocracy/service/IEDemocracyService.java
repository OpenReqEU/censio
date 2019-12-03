package com.selectionarts.projectcensio.openReqServices.eDemocracy.service;


import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.selectionarts.projectcensio.model.App;
import com.selectionarts.projectcensio.model.Task;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.entity.EdemocracyVoting;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.model.EDemoProjectDto;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.model.EDemoUserDto;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.model.EdemoVotingDto;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.model.VotingResultView;

public interface IEDemocracyService {

	public void vote(String uuid, App app, Boolean thumbsUp);
	
	public EdemocracyVoting registerEdemocracyUser(List<String> users, App app, Task task);
	public EdemocracyVoting getByUserUUID(String uuid);
	
	public JsonNode registerEdemocracyUser(EDemoUserDto user);
	public String loginEdemocracyUser(EDemoUserDto user);
	public boolean tokenTest();
	
	public JsonNode createProject(EDemoProjectDto project);
	public JsonNode retrieveProject(EDemoProjectDto project);
	public JsonNode retrieveProjectReport(EDemoProjectDto project);
	
	public JsonNode createParticipation(EDemoProjectDto project,EDemoUserDto user, String role);
	public JsonNode createVoting(EdemoVotingDto voting,EDemoProjectDto project);
	
	public VotingResultView retrieveProjectReport(int projectId);
	
	public EdemocracyVoting createEmptyVoting(Task task);
	
	public void publicVote(String publicUuid, String nickname, App app, Boolean thumbsUp);
}
